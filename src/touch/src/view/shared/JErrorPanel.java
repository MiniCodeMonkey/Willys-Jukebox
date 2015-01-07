package view.shared;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLayeredPane;

public class JErrorPanel extends JLayeredPane {
	private static final long serialVersionUID = 3044813266801017599L;
	private javax.swing.JLabel jBackgroundLabel = new javax.swing.JLabel();
	private javax.swing.JButton jButtonOk = new javax.swing.JButton();
	private javax.swing.JLabel jLabelMessage = new javax.swing.JLabel();
	
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
	.getInstance(view.TouchApp.class).getContext().getResourceMap(
			JErrorPanel.class);
	
	public JErrorPanel()
	{
		this.setBounds(0, 0, 630, 287);
		this.setMaximumSize(new java.awt.Dimension(630, 287));
		this.setName("confirmPanel");
		this.setVisible(false);
		
		jLabelMessage.setBounds(95, 40, 480, 120);
		jLabelMessage.setForeground(Color.BLACK);
		jLabelMessage.setFont(new Font("Thonburi", Font.PLAIN, 30));
		jLabelMessage.setText("");
		this.add(jLabelMessage, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jButtonOk.setIcon(resourceMap.getIcon("jButtonOk.icon"));
		jButtonOk.setPressedIcon(resourceMap.getIcon("jButtonOkDown.icon"));
		jButtonOk.setName("jButtonOk");
		jButtonOk.setSize(135, 55);
		jButtonOk.setBounds(400, 180, 135, 55);
		jButtonOk
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(
					java.awt.event.ActionEvent evt) {
				JErrorPanel.this.setVisible(false);
				JErrorPanel.this.setMessage("");
			}
		});
		this.add(jButtonOk, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
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
