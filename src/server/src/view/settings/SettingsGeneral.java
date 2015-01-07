package view.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSlider;

public class SettingsGeneral {

	public JCheckBox checkboxCrossfader;
	public JCheckBox checkboxPassword;
	public JPasswordField oldPasswordField;
	public JPasswordField newPasswordField;
	public JPasswordField confirmPasswordField;
	public JSlider slider;
	public JLabel labelSeconds;
	public JLabel labelNewPassword;
	public JLabel labelConfirmPassword;
	
	protected JComponent SettingsGeneral() {
		
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		//Crossfading checkbox
		checkboxCrossfader = new JCheckBox("Enable crossfading");
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		panel.add(checkboxCrossfader, c);
		c.gridwidth = 1;//reset the gridwith
		
		
		//Crossfading slider
		slider = new JSlider(1,12,2);
		slider.setMajorTickSpacing(11);
	    slider.setMinorTickSpacing(1);
	    slider.setPaintTicks(true);
	    slider.setPaintLabels(true);
	    slider.setEnabled(false);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		panel.add(slider, c);
		c.gridwidth = 1;//reset the gridwith
		labelSeconds = new JLabel("Seconds");
		labelSeconds.setEnabled(false);
		c.gridx = 3;
		c.gridy = 1;
		panel.add(labelSeconds,c);
		
		//Password checkbox
		checkboxPassword = new JCheckBox("Protect playlist with password");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		panel.add(checkboxPassword, c);
		c.gridwidth = 1;//reset the gridwith
		
		//New password textfield
		labelNewPassword = new JLabel("New password:");
		labelNewPassword.setEnabled(false);
		c.gridx = 0;
		c.gridy = 4;
		panel.add(labelNewPassword, c);
		newPasswordField = new JPasswordField();
		newPasswordField.setEnabled(false);
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 3;
		panel.add(newPasswordField, c);    
		c.gridwidth = 1;
		
		//Confirm password textfield
		labelConfirmPassword = new JLabel("Confirm password:");
		labelConfirmPassword.setEnabled(false);
		c.gridx = 0;
		c.gridy = 5;
		panel.add(labelConfirmPassword, c);
		confirmPasswordField = new JPasswordField();
		confirmPasswordField.setEnabled(false);
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 3;
		panel.add(confirmPasswordField, c);    
		c.gridwidth = 1;
		
		checkboxCrossfader.addActionListener(checkboxCrossfaderActionListener);
		checkboxPassword.addActionListener(checkboxPasswordActionListener);
		
        return panel;
    }
	
	ActionListener checkboxCrossfaderActionListener = new ActionListener() {
	      public void actionPerformed(ActionEvent actionEvent) {
	        boolean selected = checkboxCrossfader.isSelected();
	        
	        if(selected){
	        	slider.setEnabled(true);
	        	labelSeconds.setEnabled(true);
	        }
	        else{
	        	slider.setEnabled(false);
	        	labelSeconds.setEnabled(false);
	        }
	      }
	 };
	 
	 public void setEnabled(boolean selected){
		 labelNewPassword.setEnabled(selected);
		 labelConfirmPassword.setEnabled(selected);
		 newPasswordField.setEnabled(selected);
		 confirmPasswordField.setEnabled(selected);
	 }
	 
	 ActionListener checkboxPasswordActionListener = new ActionListener(){

		 public void actionPerformed(ActionEvent e)
		 {
			 boolean selected = checkboxPassword.isSelected();
			 
			 setEnabled(selected);
			 
		 }
		 
	 };	
}

