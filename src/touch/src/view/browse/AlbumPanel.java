package view.browse;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

import view.JImageButton;

public class AlbumPanel extends JLayeredPane {
	private static final long serialVersionUID = 5609752563546901578L;
	private javax.swing.JLabel jAlbumCoverLabel = new javax.swing.JLabel();
	private javax.swing.JLabel jArtistLabel = new javax.swing.JLabel();
	private javax.swing.JLabel jAlbumTitleLabel = new javax.swing.JLabel();
	private ArrayList<JImageButton> jTrackButtons = new ArrayList<JImageButton>();
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
			.getInstance(view.TouchApp.class).getContext().getResourceMap(
					AlbumPanel.class);

	public AlbumPanel(final BrowseAlbumsView browseAlbumsView, ImageIcon albumImage, String artist, String albumTitle,
			TreeMap<Integer, String> trackList) {
		jAlbumCoverLabel.setText("");
		jAlbumCoverLabel.setName("jAlbumCoverLabel");
		jAlbumCoverLabel.setSize(new java.awt.Dimension(400, 400));
		jAlbumCoverLabel.setBounds(0, 0, 400, 400);

		// Resize album cover image
		if (albumImage != null) {
			albumImage.setImage(albumImage.getImage().getScaledInstance(400,
					400, java.awt.Image.SCALE_SMOOTH));
			jAlbumCoverLabel.setIcon(albumImage);
		} else {
			jAlbumCoverLabel.setIcon(resourceMap
					.getIcon("jAlbumCoverLabel.icon"));
		}

		this.add(jAlbumCoverLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

		jArtistLabel.setText(artist);
		jArtistLabel.setName("jArtistLabel");
		jArtistLabel.setSize(new java.awt.Dimension(400, 50));
		jArtistLabel.setBounds(0, 400, 400, 50);
		jArtistLabel.setForeground(Color.WHITE);
		jArtistLabel.setFont(new Font("Thonburi", Font.BOLD, 30));
		this.add(jArtistLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

		jAlbumTitleLabel.setText(albumTitle);
		jAlbumTitleLabel.setName("jAlbumTitleLabel");
		jAlbumTitleLabel.setSize(new java.awt.Dimension(400, 50));
		jAlbumTitleLabel.setBounds(0, 435, 400, 50);
		jAlbumTitleLabel.setForeground(Color.WHITE);
		jAlbumTitleLabel.setFont(new Font("Thonburi", Font.PLAIN, 30));
		this.add(jAlbumTitleLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

		int y = 0;
		int n = 0;
		Set<Integer> keys = trackList.keySet();
		for (Iterator<Integer> i = keys.iterator(); i.hasNext();) {
			if (n > 20)
			{
				// Can not display more than 20 tracks
				break;
			}
			
			final Integer trackId = (Integer) i.next();
			final String trackTitle = (String) trackList.get(trackId);

			// jTrackButtonsBackground.icon
			JImageButton jButtonAddTrack = new JImageButton();
			jButtonAddTrack.setBackgroundImage(resourceMap.getImageIcon("jTrackButtonsBackground.icon").getImage()); // NOI18N
			jButtonAddTrack.setText(trackTitle); // NOI18N
			jButtonAddTrack.setForeground(Color.BLACK);
			jButtonAddTrack.setHorizontalAlignment(javax.swing.JButton.LEFT);
			jButtonAddTrack.setFocusPainted(false);
			jButtonAddTrack.setMargin(new Insets(0, 0, 0, 0));
			jButtonAddTrack.setContentAreaFilled(false);
			jButtonAddTrack.setBorderPainted(false);
			jButtonAddTrack.setOpaque(false);
			jButtonAddTrack.setName("jButtonAddTrack"); // NOI18N
			jButtonAddTrack.setBounds(420, y, 426, 27);
			jButtonAddTrack
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							browseAlbumsView.playTrack(trackId, trackTitle);
						}
					});
			this.add(jButtonAddTrack, javax.swing.JLayeredPane.DEFAULT_LAYER);
			y += 34;

			jTrackButtons.add(jButtonAddTrack);
			n++;
		}
	}
}
