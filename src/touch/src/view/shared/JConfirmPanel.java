package view.shared;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLayeredPane;

import view.browse.BrowseAlbumsView;

public class JConfirmPanel extends JLayeredPane {
	private static final long serialVersionUID = 3044813866801017599L;
	private javax.swing.JLabel jBackgroundLabel = new javax.swing.JLabel();
	private javax.swing.JButton jButtonYes = new javax.swing.JButton();
	private javax.swing.JButton jButtonNo = new javax.swing.JButton();
	private javax.swing.JLabel jLabelMessage = new javax.swing.JLabel();
	private Font messageFont = new Font("Thonburi", Font.PLAIN, 30);
	
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
	.getInstance(view.TouchApp.class).getContext().getResourceMap(
			JConfirmPanel.class);
	
	public JConfirmPanel(final BrowseAlbumsView browseAlbumsView)
	{
		this.setBounds(0, 0, 630, 287);
		this.setMaximumSize(new java.awt.Dimension(630, 287));
		this.setName("confirmPanel");
		this.setVisible(false);
		
		jLabelMessage.setBounds(95, 40, 480, 120);
		jLabelMessage.setForeground(Color.BLACK);
		jLabelMessage.setFont(messageFont);
		jLabelMessage.setText("");
		this.add(jLabelMessage, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jButtonYes.setIcon(resourceMap.getIcon("jButtonYes.icon"));
		jButtonYes.setPressedIcon(resourceMap.getIcon("jButtonYesDown.icon"));
		jButtonYes.setName("jButtonYes");
		jButtonYes.setSize(135, 55);
		jButtonYes.setBounds(95, 180, 135, 55);
		jButtonYes
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(
					java.awt.event.ActionEvent evt) {
				JConfirmPanel.this.setVisible(false);
				JConfirmPanel.this.setMessage("");
				browseAlbumsView.requestSong();
			}
		});
		this.add(jButtonYes, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jButtonNo.setIcon(resourceMap.getIcon("jButtonNo.icon"));
		jButtonNo.setPressedIcon(resourceMap.getIcon("jButtonNoDown.icon"));
		jButtonNo.setName("jButtonNo");
		jButtonNo.setSize(135, 55);
		jButtonNo.setBounds(400, 180, 135, 55);
		jButtonNo
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(
					java.awt.event.ActionEvent evt) {
				JConfirmPanel.this.setVisible(false);
				JConfirmPanel.this.setMessage("");
			}
		});
		this.add(jButtonNo, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jBackgroundLabel.setText("");
		jBackgroundLabel.setName("jBackgroundLabel");
		jBackgroundLabel.setSize(new java.awt.Dimension(630, 287));
		jBackgroundLabel.setBounds(0, 0, 630, 287);
		jBackgroundLabel.setIcon(resourceMap.getIcon("jBackgroundLabel.icon"));
		this.add(jBackgroundLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}
	
	public void setMessage(String message)
	{		
		jLabelMessage.setText("<html>"+ message +"</html>");
	}

	public void showDialog() {
		this.setVisible(true);
	}
}
