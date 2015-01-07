package view.shared;

import javax.swing.JLabel;

public class JLoadingIndicator extends JLabel {
	private static final long serialVersionUID = -7766490767199038058L;
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
	.getInstance(view.TouchApp.class).getContext().getResourceMap(
			JLoadingIndicator.class);
	
	public JLoadingIndicator()
	{
		this.setIcon(resourceMap.getIcon("jLoadingIndicator.icon"));
		this.setText("");
		this.setBounds(0, 0, 54, 55);
	}
}
