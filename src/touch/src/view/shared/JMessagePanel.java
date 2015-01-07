package view.shared;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLayeredPane;

public class JMessagePanel extends JLayeredPane {
	private static final long serialVersionUID = 102058412170340618L;
	private javax.swing.JLabel jBackgroundLabel = new javax.swing.JLabel();
	private javax.swing.JLabel jLabelMessage = new javax.swing.JLabel();
	
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
	.getInstance(view.TouchApp.class).getContext().getResourceMap(
			JMessagePanel.class);
	
	public JMessagePanel()
	{
		this.setBounds(0, 0, 447, 91);
		this.setMaximumSize(new java.awt.Dimension(447, 91));
		this.setName("messagePanel");
		this.setVisible(false);
		
		jLabelMessage.setBounds(10, 10, 447 - 20, 91 - 20);
		jLabelMessage.setForeground(Color.BLACK);
		jLabelMessage.setFont(new Font("Thonburi", Font.PLAIN, 30));
		jLabelMessage.setText("");
		jLabelMessage.setForeground(Color.WHITE);
		jLabelMessage.setHorizontalAlignment(javax.swing.JLabel.CENTER);
		jLabelMessage.setVerticalAlignment(javax.swing.JLabel.CENTER);
		this.add(jLabelMessage, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jBackgroundLabel.setText("");
		jBackgroundLabel.setName("jBackgroundLabel");
		jBackgroundLabel.setSize(new java.awt.Dimension(447, 91));
		jBackgroundLabel.setBounds(0, 0, 447, 91);
		jBackgroundLabel.setIcon(resourceMap.getIcon("jBackgroundLabel.icon"));
		this.add(jBackgroundLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}
	
	public void setMessage(String message)
	{
		jLabelMessage.setText("<html>"+ message +"</html>");
	}

	public void showDialog() {
		this.setVisible(true);
		javax.swing.Timer timer = new javax.swing.Timer(1500, new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	JMessagePanel.this.setVisible(false);
		    }    
		});
		timer.start();
	}
}
