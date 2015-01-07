package dk.wdk.shared;

import java.io.Serializable;

public class PlaylistUpdateTrack implements Serializable {
	private static final long serialVersionUID = -6277155269849612862L;
	private String name;
	private String artist;
	private int position;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtist() {
		return artist;
	}
}