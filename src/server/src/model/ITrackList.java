package model;

import java.util.ArrayList;

/**
 * 
 * @author jeppe_kronborg
 */
public interface ITrackList
{
	
	public void addTrack(AudioTrack track) throws DataException, DuplicatedTrackException;
	
	public AudioTrack getTrack(int id) throws DataException;
	
	public void clearList() throws DataException;
	
	public ArrayList<AudioTrack> search(String query) throws DataException;
	
	public int count() throws DataException;
	
	public ArrayList<AudioTrack> getAlbumTracksForArtistWithLetter(String format, int index) throws DataException;
}
