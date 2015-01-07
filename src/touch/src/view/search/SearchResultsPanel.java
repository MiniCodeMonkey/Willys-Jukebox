package view.search;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JLayeredPane;

import view.browse.BrowseAlbumsView;
import view.shared.JLoadingIndicator;
import dk.wdk.shared.SearchResultUpdateTrack;

public class SearchResultsPanel extends JLayeredPane {
	private static final long serialVersionUID = 7787465861538299509L;
	private javax.swing.JLabel jBackgroundLabel = new javax.swing.JLabel();
	private javax.swing.JLabel jResultsLabel = new javax.swing.JLabel();
	private ArrayList<javax.swing.JButton> jResultsButtons = new ArrayList<javax.swing.JButton>();
	private JLoadingIndicator jLoadingIndicator = new JLoadingIndicator();
	private BrowseAlbumsView browseAlbumsView;
	private javax.swing.JLayeredPane resultsPane = new javax.swing.JLayeredPane();
	//private javax.swing.JScrollPane scrollPane = null;
	
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
	.getInstance(view.TouchApp.class).getContext().getResourceMap(
			SearchResultsPanel.class);
	
	public SearchResultsPanel(BrowseAlbumsView browseAlbumsView)
	{
		this.browseAlbumsView = browseAlbumsView;
		
		this.setBounds(0, 0, 1218, 536 + 37);
		this.setMaximumSize(new java.awt.Dimension(1218, 536 + 37));
		this.setName("SearchResultsPanel");
		this.setVisible(false);
		
		resultsPane.setBounds(10, 10, this.getWidth() - 20, 440);
		resultsPane.setMaximumSize(new java.awt.Dimension(this.getWidth() - 20, 440));
		
		//scrollPane = new javax.swing.JScrollPane(resultsPane);
		//scrollPane.setBounds(50, 50, 500, 500);
		this.add(resultsPane, javax.swing.JLayeredPane.MODAL_LAYER);
		
		jLoadingIndicator.setBounds(this.getWidth() - jLoadingIndicator.getWidth() - 10,
				this.getHeight() - jLoadingIndicator.getHeight() - 10 - 37,
				jLoadingIndicator.getWidth(), jLoadingIndicator.getHeight());
		jLoadingIndicator.setVisible(false);
		this.add(jLoadingIndicator, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jResultsLabel.setText("");
		jResultsLabel.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
		jResultsLabel.setForeground(Color.WHITE);
		jResultsLabel.setName("jResultsLabel");
		jResultsLabel.setSize(new java.awt.Dimension(200, 30));
		jResultsLabel.setFont(new Font("Thonburi", Font.PLAIN, 28));
		jResultsLabel.setBounds(this.getWidth() - 200, this.getHeight() - 30 - 5, 200 - 7, 30);
		this.add(jResultsLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jBackgroundLabel.setText("");
		jBackgroundLabel.setName("jBackgroundLabel");
		jBackgroundLabel.setSize(new java.awt.Dimension(1218, 536));
		jBackgroundLabel.setBounds(0, 0, 1218, 536);
		jBackgroundLabel.setIcon(resourceMap.getIcon("jBackgroundLabel.icon"));
		this.add(jBackgroundLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}

	public void setTracks(ArrayList<SearchResultUpdateTrack> tracks) {
		// Remove old track buttons
		for (javax.swing.JButton button : jResultsButtons)
		{
			resultsPane.remove(button);
		}
		
		// Add new tracks if any
		if (tracks == null)
		{
			jResultsLabel.setText("");
			resultsPane.repaint();
			return;
		}
		
		int x = 10;
		int y = 10;
		int totalWidth = this.getWidth() - (2 * x);
		int artistWidth = 350;
		int albumWidth = 350;
		int titleWidth = totalWidth - artistWidth - albumWidth;
		int height = 40;
		
		int trackCount = tracks.size();
		if (trackCount == 1)
		{
			jResultsLabel.setText(trackCount + " result");
		}
		else
		{
			jResultsLabel.setText(trackCount + " results");
		}
		
		int i = 0;
		for (final SearchResultUpdateTrack track : tracks)
		{
			if (i > 12)
				break;
			
			// Title
			javax.swing.JButton titleButton = new javax.swing.JButton(track.getName());
			titleButton.setHorizontalAlignment(javax.swing.JButton.LEFT);
			titleButton.setFocusPainted(false);
			titleButton.setMargin(new Insets(0, 0, 0, 0));
			titleButton.setContentAreaFilled(false);
			titleButton.setBorderPainted(false);
			titleButton.setOpaque(false);
			titleButton.setName("titleButton");		
			titleButton.setBounds(x, y, titleWidth, height);
			titleButton.setForeground(Color.WHITE);
			titleButton.setFont(new Font("Thonburi", Font.PLAIN, 22));
			titleButton
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(
						java.awt.event.ActionEvent evt) {
					browseAlbumsView.playTrack(track.getTrackId(), track.getName());
				}
			});
			resultsPane.add(titleButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
			jResultsButtons.add(titleButton);
			
			// Artist
			javax.swing.JButton artistButton = new javax.swing.JButton(track.getArtist());
			artistButton.setHorizontalAlignment(javax.swing.JButton.LEFT);
			artistButton.setFocusPainted(false);
			artistButton.setMargin(new Insets(0, 0, 0, 0));
			artistButton.setContentAreaFilled(false);
			artistButton.setBorderPainted(false);
			artistButton.setOpaque(false);
			artistButton.setName("artistButton");	
			artistButton.setBounds(x + titleWidth, y, artistWidth, height);
			artistButton.setForeground(titleButton.getForeground());
			artistButton.setFont(titleButton.getFont());
			artistButton
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(
						java.awt.event.ActionEvent evt) {
					browseAlbumsView.playTrack(track.getTrackId(), track.getName());
				}
			});
			resultsPane.add(artistButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
			jResultsButtons.add(artistButton);
			
			// Album
			javax.swing.JButton albumButton = new javax.swing.JButton(track.getAlbum());
			albumButton.setHorizontalAlignment(javax.swing.JButton.LEFT);
			albumButton.setFocusPainted(false);
			albumButton.setMargin(new Insets(0, 0, 0, 0));
			albumButton.setContentAreaFilled(false);
			albumButton.setBorderPainted(false);
			albumButton.setOpaque(false);
			albumButton.setName("albumButton");	
			albumButton.setBounds(x + titleWidth + artistWidth, y, albumWidth, height);
			albumButton.setForeground(titleButton.getForeground());
			albumButton.setFont(titleButton.getFont());
			albumButton
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(
						java.awt.event.ActionEvent evt) {
					browseAlbumsView.playTrack(track.getTrackId(), track.getName());
				}
			});
			resultsPane.add(albumButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
			jResultsButtons.add(albumButton);
			
			y += height;
			i++;
		}
		
		resultsPane.repaint();
		//scrollPane.repaint();
	}
	
	public void setSearching(boolean isSearching)
	{
		jLoadingIndicator.setVisible(isSearching);
	}
}
