package dk.wdk.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaylistUpdate implements Serializable {
	private static final long serialVersionUID = -8462893587500160509L;

	private ArrayList<PlaylistUpdateTrack> playlistUpdateTracks = new ArrayList<PlaylistUpdateTrack>();
	
	public PlaylistUpdate()
	{
		
	}
	
	public ArrayList<PlaylistUpdateTrack> getPlaylistTracks() {
		return playlistUpdateTracks;
	}

	public void addTrack(String name, String artist, int position) {
		PlaylistUpdateTrack playlistUpdateTrack = new PlaylistUpdateTrack();
		playlistUpdateTrack.setName(name);
		playlistUpdateTrack.setArtist(artist);
		playlistUpdateTrack.setPosition(position);

		playlistUpdateTracks.add(playlistUpdateTrack);
	}
}
