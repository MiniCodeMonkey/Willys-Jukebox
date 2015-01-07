/*
 * GuiView.java
 */

package view.front;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;

import view.browse.BrowseAlbumsView;
import view.shared.JErrorPanel;
import view.shared.JMessagePanel;
import control.network.Connection;
import dk.wdk.shared.PlaylistUpdateTrack;

/**
 * The application's main frame.
 */
public class TouchView extends FrameView {

	private final int MAX_PLAYLIST_SIZE = 300;
	private final boolean fullscreenEnabled = false;
	private org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
	.getInstance(view.TouchApp.class).getContext().getResourceMap(
			TouchView.class);
	private long duration = 0;
	private long elapsed = 0;
	private long startedPlayingTime = 0;
	private boolean isPaused = false;
	public BrowseAlbumsView browseAlbumsView = null;
	public Connection connection = null;

	public TouchView(SingleFrameApplication app) {
		super(app);
		
		initComponents();

		connection = new Connection(this); // Connect to server computer

		// Go fullscreen
		if (fullscreenEnabled) {
			this.getFrame().setUndecorated(true);
			this.getFrame().setResizable(false);
			this.getFrame().setSize(this.getFrame().getToolkit().getScreenSize());
			
			GraphicsDevice d = app.getMainFrame().getGraphicsConfiguration()
					.getDevice();

			if (!d.isFullScreenSupported())
				System.out.println("Fullscreen not supported");

			d.setFullScreenWindow(this.getFrame());
			
			DisplayMode displayMode = new DisplayMode(1920, 1080, d.getDisplayMode().getBitDepth(), d.getDisplayMode().getRefreshRate());
			d.setDisplayMode(displayMode);
			
			int[] pixels = new int[16 * 16];
			Image image = Toolkit.getDefaultToolkit().createImage(
			        new MemoryImageSource(16, 16, pixels, 0, 16));
			Cursor transparentCursor =
			        Toolkit.getDefaultToolkit().createCustomCursor
			             (image, new Point(0, 0), "invisibleCursor");
			app.getMainFrame().setCursor(transparentCursor);
		}
	}

	private void initComponents() {
		mainPanel = new javax.swing.JPanel();
		jLayeredPane = new javax.swing.JLayeredPane();
		jButtonAddTrack = new javax.swing.JButton();
		jLabelBackground = new javax.swing.JLabel();
		jLabelTrackTitle = new javax.swing.JLabel();
		jLabelTrackArtist = new javax.swing.JLabel();
		jLabelTrackAlbum = new javax.swing.JLabel();
		jLabelTrackAlbumImage = new javax.swing.JLabel();
		jLabelEmptyPlaylist = new javax.swing.JLabel();
		jLabelPlaylistPositions = new javax.swing.JLabel[MAX_PLAYLIST_SIZE];
		jLabelPlaylistTitles = new javax.swing.JLabel[MAX_PLAYLIST_SIZE];
		jLabelPlaylistArtist = new javax.swing.JLabel[MAX_PLAYLIST_SIZE];
		jLabelConnectionError = new javax.swing.JLabel();
		jProgressBarConnecting = new javax.swing.JProgressBar();
		jProgressBackgroundLabel = new javax.swing.JLabel();
		jProgressForegroundLabel = new javax.swing.JLabel();
		jProgressElapsedLabel = new javax.swing.JLabel();
		jProgressRemainingLabel = new javax.swing.JLabel();
		jExitButton = new javax.swing.JButton();

		mainPanel.setMaximumSize(new java.awt.Dimension(1920, 1080));
		mainPanel.setMinimumSize(new java.awt.Dimension(1920, 1080));
		mainPanel.setName("mainPanel");
		mainPanel.setPreferredSize(new java.awt.Dimension(1920, 1080));
		mainPanel.setSize(new java.awt.Dimension(1920, 1080));
		
		jLayeredPane.setBounds(new java.awt.Rectangle(0, 0, 1920, 1080));
		jLayeredPane.setMaximumSize(new java.awt.Dimension(1920, 1080));
		jLayeredPane.setName("jLayeredPane");
		
		browseAlbumsView = new BrowseAlbumsView(this);
		jLayeredPane.add(browseAlbumsView);
		browseAlbumsView.setVisible(false);
		
		jExitButton.setName("jExitButton");
		jExitButton.setText("");
		jExitButton.setFocusPainted(false);
		jExitButton.setMargin(new Insets(0, 0, 0, 0));
		jExitButton.setContentAreaFilled(false);
		jExitButton.setBorderPainted(false);
		jExitButton.setOpaque(false);
		jExitButton.setBounds(1870, 0, 50, 50);
		jExitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitButtonClicked(evt);
			}
		});
		jLayeredPane.add(jExitButton);
		
		jLabelEmptyPlaylist
				.setBounds(new Rectangle(880 + 10, 120 + 10, 550, 50));
		jLabelEmptyPlaylist.setText("Queue is empty.");
		jLabelEmptyPlaylist.setForeground(new Color(204, 204, 204)); // #CCC
		jLabelEmptyPlaylist.setFont(new Font("Thonburi", Font.PLAIN, 40));
		jLayeredPane.add(jLabelEmptyPlaylist);

		int y = 10;
		for (int i = 0; i < MAX_PLAYLIST_SIZE; i++) {
			jLabelPlaylistPositions[i] = new javax.swing.JLabel();
			jLabelPlaylistPositions[i].setBounds(880 + 10, 120 + y, 140, 80);
			jLabelPlaylistPositions[i].setText(String.format("%02d", i + 1));
			jLabelPlaylistPositions[i].setForeground(new Color(160, 160, 160)); // #A0A0A0
			jLabelPlaylistPositions[i].setFont(new Font("Harabara", Font.BOLD,
					100));
			jLabelPlaylistPositions[i].setVisible(false);
			jLayeredPane.add(jLabelPlaylistPositions[i]);

			y += 85;
		}

		y = 10;
		for (int i = 0; i < MAX_PLAYLIST_SIZE; i++) {
			jLabelPlaylistTitles[i] = new javax.swing.JLabel();
			jLabelPlaylistTitles[i].setBounds(880 + 150, 120 + y, 815, 40);
			jLabelPlaylistTitles[i].setText("");
			jLabelPlaylistTitles[i].setForeground(Color.WHITE);
			jLabelPlaylistTitles[i]
					.setFont(new Font("Thonburi", Font.BOLD, 28));
			jLayeredPane.add(jLabelPlaylistTitles[i]);

			y += 85;
		}

		y = 10;
		for (int i = 0; i < MAX_PLAYLIST_SIZE; i++) {
			jLabelPlaylistArtist[i] = new javax.swing.JLabel();
			jLabelPlaylistArtist[i].setBounds(880 + 150, 120 + y + 35, 815, 40);
			jLabelPlaylistArtist[i].setText("");
			jLabelPlaylistArtist[i].setForeground(Color.WHITE);
			jLabelPlaylistArtist[i]
					.setFont(new Font("Thonburi", Font.PLAIN, 28));
			jLayeredPane.add(jLabelPlaylistArtist[i]);

			y += 85;
		}

		jButtonAddTrack.setIcon(resourceMap.getIcon("jButtonAddTrack.icon")); // NOI18N
		jButtonAddTrack.setText(resourceMap.getString("jButtonAddTrack.text")); // NOI18N
		jButtonAddTrack.setFocusPainted(false);
		jButtonAddTrack.setMargin(new Insets(0, 0, 0, 0));
		jButtonAddTrack.setContentAreaFilled(false);
		jButtonAddTrack.setBorderPainted(false);
		jButtonAddTrack.setOpaque(false);
		jButtonAddTrack.setName("jButtonAddTrack"); // NOI18N
		jButtonAddTrack.setBounds(27, 282, 881, 188);
		jButtonAddTrack.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addTrackClicked(evt);
			}
		});
		jLayeredPane.add(jButtonAddTrack,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jProgressForegroundLabel.setName("jProgressForegroundLabel");
		jProgressForegroundLabel.setBounds(116, 920, 0, 21);
		jProgressForegroundLabel.setIcon(resourceMap.getIcon("jProgressForegroundLabel.icon"));
		jLayeredPane.add(jProgressForegroundLabel,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jProgressBackgroundLabel.setName("jProgressBackgroundLabel");
		jProgressBackgroundLabel.setBounds(116, 920, 534, 21);
		jProgressBackgroundLabel.setIcon(resourceMap.getIcon("jProgressBackgroundLabel.icon"));
		jLayeredPane.add(jProgressBackgroundLabel,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jProgressElapsedLabel.setName("jProgressElapsedLabel");
		jProgressElapsedLabel.setBounds(64, 915, 50, 30);
		jProgressElapsedLabel.setFont(new Font("Thonburi", Font.PLAIN, 17));
		jProgressElapsedLabel.setForeground(Color.WHITE);
		jLayeredPane.add(jProgressElapsedLabel,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jProgressRemainingLabel.setName("jProgressRemainingLabel");
		jProgressRemainingLabel.setBounds(656, 915, 60, 30);
		jProgressRemainingLabel.setFont(new Font("Thonburi", Font.PLAIN, 17));
		jProgressRemainingLabel.setForeground(Color.WHITE);
		jLayeredPane.add(jProgressRemainingLabel,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jLabelTrackTitle.setText("");
		jLabelTrackTitle.setName("jLabelTrackTitle");
		jLabelTrackTitle.setSize(new java.awt.Dimension(500, 50));
		jLabelTrackTitle.setBounds(396, 638, 500, 50);
		jLabelTrackTitle.setForeground(Color.WHITE);
		jLabelTrackTitle.setFont(new Font("Thonburi", Font.PLAIN, 35));
		jLayeredPane.add(jLabelTrackTitle,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabelTrackArtist.setText("");
		jLabelTrackArtist.setName("jLabelTrackArtist");
		jLabelTrackArtist.setSize(new java.awt.Dimension(500, 50));
		jLabelTrackArtist.setBounds(396, 638 + 60, 500, 40);
		jLabelTrackArtist.setForeground(Color.WHITE);
		jLabelTrackArtist.setFont(new Font("Thonburi", Font.PLAIN, 30));
		jLayeredPane.add(jLabelTrackArtist,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabelTrackAlbum.setText("");
		jLabelTrackAlbum.setName("jLabelTrackAlbum");
		jLabelTrackAlbum.setSize(new java.awt.Dimension(500, 50));
		jLabelTrackAlbum.setBounds(396, 638 + 60 + 50, 500, 40);
		jLabelTrackAlbum.setForeground(Color.WHITE);
		jLabelTrackAlbum.setFont(new Font("Thonburi", Font.PLAIN, 30));
		jLayeredPane.add(jLabelTrackAlbum,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabelTrackAlbumImage.setText("");
		jLabelTrackAlbumImage.setName("jLabelTrackAlbumImage");
		jLabelTrackAlbumImage.setSize(new java.awt.Dimension(500, 50));
		jLabelTrackAlbumImage.setBounds(71, 603, 300, 300);
		jLayeredPane.add(jLabelTrackAlbumImage,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jProgressBarConnecting.setName("jProgressBarConnecting");
		jProgressBarConnecting.setBounds(1920 / 2 - 250 / 2, 1000, 250, 20);
		jProgressBarConnecting.setIndeterminate(true);
		jProgressBarConnecting.setVisible(false);
		jLayeredPane.add(jProgressBarConnecting, javax.swing.JLayeredPane.MODAL_LAYER);
		
		jLabelConnectionError.setName("jLabelConnectionError");
		jLabelConnectionError.setBounds(0, 0, 1920, 1080);
		jLabelConnectionError.setSize(new java.awt.Dimension(1920, 1080));
		jLabelConnectionError.setOpaque(true);
		jLabelConnectionError.setIcon(resourceMap.getIcon("jLabelConnectionError.icon"));
		jLabelConnectionError.setVisible(false);
		jLayeredPane.add(jLabelConnectionError, javax.swing.JLayeredPane.MODAL_LAYER);
		
		jLabelBackground.setIcon(resourceMap.getIcon("jLabelBackground.icon")); // NOI18N
		jLabelBackground
				.setText(resourceMap.getString("jLabelBackground.text")); // NOI18N
		jLabelBackground.setName("jLabelBackground"); // NOI18N
		jLabelBackground.setSize(new java.awt.Dimension(1920, 1080));
		jLabelBackground.setBounds(0, 0, 1920, 1080);
		jLayeredPane.add(jLabelBackground,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(
				mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				mainPanelLayout.createSequentialGroup().add(jLayeredPane,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1920,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(0, Short.MAX_VALUE)));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				mainPanelLayout.createSequentialGroup().add(jLayeredPane,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1080,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		setComponent(mainPanel);
		
		jUpdateTimeTimer = new javax.swing.Timer(250, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateElapsedTime();
			}
		});
		
		jUpdateTimeTimer.start();
	}

	protected void exitButtonClicked(ActionEvent evt) {
		System.exit(0);
	}

	protected void addTrackClicked(ActionEvent evt) {
		browseAlbumsView.showView();
	}

	public void updateNowPlaying(String title, String artist, String album,
			ImageIcon albumImage, long duration, long elapsed, boolean isPaused) {
		jLabelTrackTitle.setText(title);
		jLabelTrackArtist.setText(artist);
		jLabelTrackAlbum.setText(album);
		
		startedPlayingTime = System.currentTimeMillis();
		this.isPaused = isPaused;
		this.duration = duration;
		this.elapsed = elapsed;
		updateElapsedTime();

		// Resize album cover image
		if (albumImage != null) {
			albumImage.setImage(albumImage.getImage().getScaledInstance(300,
					300, java.awt.Image.SCALE_SMOOTH));
			jLabelTrackAlbumImage.setIcon(albumImage);
		}
		else if (title.length() > 0)
		{
			jLabelTrackAlbumImage.setIcon(resourceMap.getIcon("jLabelTrackAlbumImage.icon"));
		}
		else
		{
			jLabelTrackAlbumImage.setIcon(null);
		}
	}
	
	private void updateElapsedTime()
	{
		if (duration <= 0 || isPaused)
		{
			jProgressForegroundLabel.setVisible(false);
			jProgressBackgroundLabel.setVisible(false);
			jProgressElapsedLabel.setVisible(false);
			jProgressRemainingLabel.setVisible(false);
			return;
		}
		
		long now = System.currentTimeMillis();
		long elapsedAbs = now - startedPlayingTime + elapsed;
		double percentElapsed = (double)elapsedAbs / (double)duration;
		long remaining = duration - elapsedAbs;
		
		if (remaining <= 0)
			return;
		
		jProgressForegroundLabel.setVisible(true);
		jProgressBackgroundLabel.setVisible(true);
		jProgressElapsedLabel.setVisible(true);
		jProgressRemainingLabel.setVisible(true);
		
		Rectangle oldBounds = jProgressForegroundLabel.getBounds();
		jProgressForegroundLabel.setBounds((int)oldBounds.getX(), (int)oldBounds.getY(), (int)(percentElapsed * (double)jProgressBackgroundLabel.getBounds().getWidth()), (int)oldBounds.getHeight());
		
		int elapsedSec = (int)Math.ceil(elapsedAbs / 1000);
		int remainingSec = (int)Math.ceil(remaining / 1000);
		int durationSec = (int)Math.ceil(duration / 1000);
		int sumSec = 0;
		
		do
		{
			sumSec = (elapsedSec + remainingSec);
			remainingSec--;
		} while (sumSec > durationSec);
		
		do
		{
			sumSec = (elapsedSec + remainingSec);
			remainingSec++;
		} while (sumSec < durationSec - 1);
		
		int elapsedSeconds = (int)(elapsedSec % 60);
		int elapsedMinutes = (int)((elapsedSec - elapsedSeconds) / 60);
		jProgressElapsedLabel.setText(String.format("%02d:%02d", elapsedMinutes, elapsedSeconds));
		
		int remainingSeconds = (int)(remainingSec % 60);
		int remainingMinutes = (int)((remainingSec - remainingSeconds) / 60);
		jProgressRemainingLabel.setText(String.format("-%02d:%02d", remainingMinutes, remainingSeconds));
	}
	
	public void updatePlaylist(ArrayList<PlaylistUpdateTrack> playlistTracks) {
		// Show empty playlist label?
		if (playlistTracks.size() <= 0) {
			jLabelEmptyPlaylist.setVisible(true);
		} else {
			jLabelEmptyPlaylist.setVisible(false);
		}

		// Remove all track infos
		for (int i = 0; i < MAX_PLAYLIST_SIZE; i++) {
			jLabelPlaylistTitles[i].setText("");
			jLabelPlaylistArtist[i].setText("");
			jLabelPlaylistPositions[i].setVisible(false);
		}

		// Set new track info
		for (PlaylistUpdateTrack track : playlistTracks) {
			jLabelPlaylistTitles[track.getPosition() - 1].setText(track
					.getName());
			jLabelPlaylistArtist[track.getPosition() - 1].setText(track
					.getArtist());
			jLabelPlaylistPositions[track.getPosition() - 1].setVisible(true);
		}
	}
	
	public void setConnectionError(boolean error)
	{
		if (error)
		{
			updatePlaylist(new ArrayList<PlaylistUpdateTrack>()); // Remove all songs from playlist
			updateNowPlaying("", "", "", null, 0, 0, true); // Remove now playing
			jLabelConnectionError.setVisible(true);
			jProgressBarConnecting.setVisible(true);
		}
		else
		{
			jLabelConnectionError.setVisible(false);
			jProgressBarConnecting.setVisible(false);
		}
	}
	
	public void showError(String errorMessage)
	{
		JErrorPanel errorPanel = new JErrorPanel();
		errorPanel.setMessage(errorMessage);
		
		errorPanel.setBounds(mainPanel.getWidth() / 2 - errorPanel.getWidth() / 2,
				mainPanel.getHeight() / 2 - errorPanel.getHeight() / 2,
				errorPanel.getWidth(), errorPanel.getHeight());
		
		if (browseAlbumsView.isVisible())
		{
			browseAlbumsView.jLayeredPane.add(errorPanel, javax.swing.JLayeredPane.POPUP_LAYER);
		}
		else
		{
			jLayeredPane.add(errorPanel, javax.swing.JLayeredPane.POPUP_LAYER);
		}
		
		errorPanel.showDialog();
	}
	
	public void showMessage(String message) {
		JMessagePanel messagePanel = new JMessagePanel();
		messagePanel.setMessage(message);
		
		messagePanel.setBounds(mainPanel.getWidth() / 2 - messagePanel.getWidth() / 2,
				mainPanel.getHeight() / 2 - messagePanel.getHeight() / 2,
				messagePanel.getWidth(), messagePanel.getHeight());
		
		if (browseAlbumsView.isVisible())
		{
			browseAlbumsView.jLayeredPane.add(messagePanel, javax.swing.JLayeredPane.POPUP_LAYER);
		}
		else
		{
			jLayeredPane.add(messagePanel, javax.swing.JLayeredPane.POPUP_LAYER);
		}
		
		messagePanel.showDialog();
	}

	private javax.swing.JButton jButtonAddTrack;
	private javax.swing.JLabel jLabelBackground;
	private javax.swing.JLayeredPane jLayeredPane;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JLabel jLabelTrackTitle;
	private javax.swing.JLabel jLabelTrackArtist;
	private javax.swing.JLabel jLabelTrackAlbum;
	private javax.swing.JLabel jLabelTrackAlbumImage;
	private javax.swing.JLabel jLabelEmptyPlaylist;
	private javax.swing.JLabel[] jLabelPlaylistPositions;
	private javax.swing.JLabel[] jLabelPlaylistTitles;
	private javax.swing.JLabel[] jLabelPlaylistArtist;
	private javax.swing.JLabel jLabelConnectionError;
	private javax.swing.JProgressBar jProgressBarConnecting;
	private javax.swing.JLabel jProgressBackgroundLabel;
	private javax.swing.JLabel jProgressForegroundLabel;
	private javax.swing.JLabel jProgressElapsedLabel;
	private javax.swing.JLabel jProgressRemainingLabel;
	private javax.swing.Timer jUpdateTimeTimer;
	private javax.swing.JButton jExitButton;
}
