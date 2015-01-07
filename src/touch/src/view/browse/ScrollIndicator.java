package view.browse;

import java.util.ArrayList;

import javax.swing.JLayeredPane;

public class ScrollIndicator extends JLayeredPane
{
	private static final long serialVersionUID = -8367492162027543198L;
	private ArrayList<javax.swing.JLabel> dots = new ArrayList<javax.swing.JLabel>();
	private BrowseAlbumsView browseAlbumsView;
	
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
	.getInstance(view.TouchApp.class).getContext().getResourceMap(
			ScrollIndicator.class);
	
	public ScrollIndicator(BrowseAlbumsView browseAlbumsView)
	{
		this.browseAlbumsView = browseAlbumsView;
	}
	
	public void setDots(int dotCount, int selected)
	{
		// Remove old dots
		for (javax.swing.JLabel dot : dots)
		{
			this.remove(dot);
		}
		dots.clear();
		
		if (dotCount > 1)
		{
			// Add new dots
			int x = 0;
			for (int i = 0; i < dotCount; i++)
			{
				javax.swing.JLabel dotLabel = new javax.swing.JLabel();
				dotLabel.setBounds(x, 0, 20, 20);
				
				if (i == selected)
					dotLabel.setIcon(resourceMap.getIcon("jBulletLabelSelected.icon"));
				else
					dotLabel.setIcon(resourceMap.getIcon("jBulletLabelUnSelected.icon"));
				
				this.add(dotLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
				x += 22;
				
				dots.add(dotLabel);
			}
			
			int newWidth = x - 2;
			int newHeight = 20;
			this.setBounds(browseAlbumsView.getWidth() / 2 - newWidth / 2, browseAlbumsView.getHeight() - newHeight - 20, newWidth, newHeight);
		}
		
		this.repaint();
	}
}
