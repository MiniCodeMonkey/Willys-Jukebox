package dk.wdk.shared;

import java.io.Serializable;

public class SearchResultUpdateTrack implements Serializable {
	private static final long serialVersionUID = 6989728968519804827L;
	
	private String name;
	private String artist;
	private String album;
	private int trackId;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtist() {
		return artist;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}

	public String getAlbum() {
		return album;
	}
	
	public void setTrackId(int trackId)
	{
		this.trackId = trackId;
	}
	
	public int getTrackId()
	{
		return trackId;
	}
}