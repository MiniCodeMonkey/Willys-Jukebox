package view.search;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.Timer;

import view.browse.BrowseAlbumsView;

public class SearchBoxPanel extends JLayeredPane
{
	private static final long serialVersionUID = -4884157299169230838L;
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
	.getInstance(view.TouchApp.class).getContext().getResourceMap(
			SearchBoxPanel.class);
	private javax.swing.JLabel backgroundLabel = new javax.swing.JLabel();
	private javax.swing.JLabel cursorLabel = new javax.swing.JLabel();
	private javax.swing.JLabel textLabel = new javax.swing.JLabel();
	private javax.swing.JButton clearButton = new javax.swing.JButton();
	private BrowseAlbumsView browseAlbumsView = null;
	private Timer cursorTimer;
	
	public SearchBoxPanel(final BrowseAlbumsView browseAlbumsView)
	{
		cursorTimer = new Timer(600, cursorTimerTick);
		
		this.browseAlbumsView = browseAlbumsView;
		
		this.setBounds(1920 / 2 - 1218 / 2, 20, 1218, 80);
		this.setName("SearchBoxPanel");
		
		clearButton.setIcon(resourceMap.getIcon("clearButton.icon"));
		clearButton.setFocusPainted(false);
		clearButton.setMargin(new Insets(0, 0, 0, 0));
		clearButton.setContentAreaFilled(false);
		clearButton.setBorderPainted(false);
		clearButton.setOpaque(false);
		clearButton.setName("clearButton");		
		clearButton.setBounds(1155, 80 / 2 - 50 / 2, 50, 50);
		clearButton.setVisible(false);
		clearButton
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(
					java.awt.event.ActionEvent evt) {
					clearSearch();
			}
		});
		this.add(clearButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		cursorLabel.setBounds(5 + 80, 19, 1, 43);
		cursorLabel.setIcon(resourceMap.getIcon("cursorLabel.icon"));
		cursorLabel.setVisible(false);
		this.add(cursorLabel, javax.swing.JLayeredPane.MODAL_LAYER);	
		
		textLabel.setBounds(5 + 80, 5, 1218 - 5 - 69 - 5 - 80, 70);
		textLabel.setFont(new Font("Thonburi", Font.PLAIN, 40));
		textLabel.setName("textLabel");
		
		this.setInactive(true);
		this.add(textLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		backgroundLabel.setBounds(0, 0, 1218, 80);
		backgroundLabel.setIcon(resourceMap.getIcon("backgroundLabel.icon"));
		backgroundLabel.addMouseListener(new MouseAdapter()
		{
		    public void mouseReleased(MouseEvent e)
		    {
	        	if (!browseAlbumsView.confirmPanel.isVisible())
				{
					if (!browseAlbumsView.keyboardPanel.isVisible())
					{
						browseAlbumsView.setBrowseViewVisible(false);
					
						textLabel.setText("");
						textLabel.setForeground(Color.BLACK);
						cursorLabel.setVisible(true);
						cursorTimer.start();
						
						browseAlbumsView.searchResultsPanel.setVisible(true);
						browseAlbumsView.keyboardPanel.setVisible(true);
					}
				}
		    }
		});
		this.add(backgroundLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}

	public void setInactive(boolean inactive) {
		if (inactive)
		{
			textLabel.setText("Search title/artist/album...");
			textLabel.setForeground(Color.GRAY);
			cursorTimer.stop();
			cursorLabel.setVisible(false);
			
			int minX = 85;
			cursorLabel.setBounds(minX, (int)cursorLabel.getBounds().getY(), (int)cursorLabel.getBounds().getWidth(), (int)cursorLabel.getBounds().getHeight());

		}
	}
	
	public void addText(String text)
	{
		
		if (browseAlbumsView.confirmPanel.isVisible())
			return;
		
		textLabel.setText(textLabel.getText() + text);
		
		this.textChanged();
	}
	
	public void removeLastLetter()
	{
		
		if (browseAlbumsView.confirmPanel.isVisible())
			return;
		
		String text = textLabel.getText();
		
		if (text.length() > 0)
		{
			textLabel.setText(text.substring(0, text.length() - 1));
		}
		
		this.textChanged();
	}
	
	public void clearSearch()
	{

		
		if (browseAlbumsView.confirmPanel.isVisible())
			return;
		
		textLabel.setText("");
		this.textChanged();
	}
	
	private void textChanged()
	{
		String query = textLabel.getText();
		browseAlbumsView.searchQuery(query);
		
		if (query.length() > 0)
			clearButton.setVisible(true);
		else
			clearButton.setVisible(false);
		
		int minX = 85;
		
		FontMetrics fm = textLabel.getFontMetrics(textLabel.getFont());
		cursorLabel.setBounds(minX + fm.stringWidth(query), (int)cursorLabel.getBounds().getY(), (int)cursorLabel.getBounds().getWidth(), (int)cursorLabel.getBounds().getHeight());

	}
	
	ActionListener cursorTimerTick = new ActionListener() {
	      public void actionPerformed(ActionEvent actionEvent) {
	        cursorLabel.setVisible(!cursorLabel.isVisible());
	      }
	    };
}
