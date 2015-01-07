package view.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import control.SettingsController;



public class SettingsView extends JDialog{
	private static final long serialVersionUID = 5744483319920723816L;
	SettingsGeneral setgen = new SettingsGeneral();
	SettingsPlaylistManager setplay = new SettingsPlaylistManager();
	SettingsLastfm setlast	= new SettingsLastfm();
	SettingsController controller = new SettingsController();
	JComponent panel1;
	JComponent panel2;
	JComponent panel3;
	
	JButton ok;
	JButton cancel;
	
		public SettingsView(Frame parent, boolean modal) {
			super(parent, modal);
			
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setName("Settings");
	        
			//Layout of tabs
			BorderLayout borderLayout = new BorderLayout();
			
			getContentPane().setLayout(borderLayout);
			
			JTabbedPane tabs = new JTabbedPane();
			
			panel1 = setgen.SettingsGeneral();
			tabs.addTab("General",panel1);
			
			panel2 = setplay.SettingsPlaylist();
			tabs.addTab("Playlist Manager",panel2);
			
			panel3 = setlast.SettingsLastfm();
			tabs.addTab("Last.fm", panel3);
			
			add(tabs,BorderLayout.PAGE_START);
		
			//Ok and Cancel Buttons
			JPanel panel = new JPanel(new FlowLayout());
			ok = new JButton("Ok");
			cancel = new JButton("Cancel");
			
			panel.add(cancel);
			panel.add(ok);
			
			ok.addActionListener(okButtonActionListener);
			cancel.addActionListener(cancelButtonActionListener);
			
			add(panel,BorderLayout.PAGE_END);
			
			Dimension size = new Dimension(400, 250);
			this.setMaximumSize(size);
			this.setMinimumSize(size);
			this.setResizable(false);
			
			// Load settings
			setgen.checkboxCrossfader.setSelected(Boolean.parseBoolean(controller.getSetting("crossFadingEnabled")));
			setgen.slider.setEnabled(Boolean.parseBoolean(controller.getSetting("crossFadingEnabled")));
			setgen.labelSeconds.setEnabled(Boolean.parseBoolean(controller.getSetting("crossFadingEnabled")));
			setgen.slider.setValue(Integer.parseInt(controller.getSetting("crossFadeSeconds")));
			
			setgen.setEnabled(Boolean.parseBoolean(controller.getSetting("password")));
			setgen.checkboxPassword.setSelected(Boolean.parseBoolean(controller.getSetting("protectWithPasswordEnabled")));
			
			setplay.setEnabled(Boolean.parseBoolean(controller.getSetting("playlistManagerEnabled")));
			setplay.checkBoxPlaylistManager.setSelected(Boolean.parseBoolean(controller.getSetting("playlistManagerEnabled")));
			setplay.queueSizeSpinner.setValue(Integer.parseInt(controller.getSetting("queueSize")));
			setplay.artistThrottlingSpinner.setValue(Integer.parseInt(controller.getSetting("artistThrottling")));
			setplay.songFrequencySpinner.setValue(Integer.parseInt(controller.getSetting("songFrequency")));
			setplay.artistFrequencySpinner.setValue(Integer.parseInt(controller.getSetting("artistFrequency")));
	}	
		
		ActionListener okButtonActionListener = new ActionListener(){

			 public void actionPerformed(ActionEvent e)
			 {
				  controller.setSetting("crossFadingEnabled", setgen.checkboxCrossfader.isSelected() ? "true" : "false");
				  controller.setSetting("crossFadeSeconds", String.format("%d", setgen.slider.getValue()));
				  controller.setSetting("protectWithPasswordEnabled", setgen.checkboxPassword.isSelected() ? "true" : "false");
				  //controller.setSetting("password", value)
				  controller.setSetting("playlistManagerEnabled", setplay.checkBoxPlaylistManager.isSelected() ? "true" : "false");
				  controller.setSetting("queueSize", String.format("%d", setplay.queueSizeSpinner.getValue()));
				  controller.setSetting("artistThrottling", String.format("%d", setplay.artistThrottlingSpinner.getValue()));
				  controller.setSetting("songFrequency", String.format("%d", setplay.songFrequencySpinner.getValue()));
				  controller.setSetting("artistFrequency", String.format("%d", setplay.artistFrequencySpinner.getValue()));
				  
				  SettingsView.this.dispose();
			 }
			 
		 };
		 
		 ActionListener cancelButtonActionListener = new ActionListener(){

			 public void actionPerformed(ActionEvent e)
			 {
				SettingsView.this.dispose();	
			 }
			 
		 };
}
