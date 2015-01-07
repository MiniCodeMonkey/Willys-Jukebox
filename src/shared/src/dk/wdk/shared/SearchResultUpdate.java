package dk.wdk.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResultUpdate implements Serializable {
	private static final long serialVersionUID = -7836568042185835893L;
	
	private ArrayList<SearchResultUpdateTrack> searchResultUpdateTracks = new ArrayList<SearchResultUpdateTrack>();
	private String query;
	
	public SearchResultUpdate(String query)
	{
		this.query = query;
	}
	
	public ArrayList<SearchResultUpdateTrack> getTracks() {
		return searchResultUpdateTracks;
	}
	
	public String getQuery()
	{
		return query;
	}
	
	public void setQuery(String query)
	{
		this.query = query;
	}

	public void addTrack(String name, String artist, String album, int trackId) {
		SearchResultUpdateTrack searchResultUpdateTrack = new SearchResultUpdateTrack();
		searchResultUpdateTrack.setName(name);
		searchResultUpdateTrack.setArtist(artist);
		searchResultUpdateTrack.setAlbum(album);
		searchResultUpdateTrack.setTrackId(trackId);

		searchResultUpdateTracks.add(searchResultUpdateTrack);
	}
}
