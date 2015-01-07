package dk.wdk.shared;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class NowPlayingUpdate implements Serializable {
	private static final long serialVersionUID = -2169772685794228427L;
	private String name;
	private String artist;
	private String album;
	private ImageIcon albumImage;
	private long duration;
	private long elapsed;
	private boolean isPaused;

	public NowPlayingUpdate(String name, String artist, String album,
			ImageIcon albumImage, long duration, long elapsed, boolean isPaused) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.albumImage = albumImage;
		this.duration = duration;
		this.elapsed = elapsed;
		this.isPaused = isPaused;
	}

	public String getName() {
		return name;
	}

	public String getArtist() {
		return artist;
	}

	public String getAlbum() {
		return album;
	}

	public ImageIcon getAlbumImage() {
		return albumImage;
	}

	public long getDuration() {
		return duration;
	}

	public long getElapsed() {
		return elapsed;
	}
	
	public boolean getIsPaused()
	{
		return isPaused;
	}
}
