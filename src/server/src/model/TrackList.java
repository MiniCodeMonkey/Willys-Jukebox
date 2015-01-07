package model;

import java.util.ArrayList;

import db.daf.DAFException;
import db.daf.impl.AudioTracksDAF;
import db.daf.interfaces.IAudioTracksDAF;
import db.dao.interfaces.IAudioTrackDAO;

/**
 * 
 * @author jeppe_kronborg
 */
public class TrackList implements ITrackList
{
	
	private static TrackList instance = null;
	private IAudioTracksDAF audioTracks = new AudioTracksDAF();
	
	protected TrackList()
	{
		// Prevent TrackList from being initiated normally (Singleton pattern)
	}
	
	public static TrackList getInstance()
	{
		if (instance == null)
		{
			instance = new TrackList();
		}
		
		return instance;
	}
	
	public void addTrack(AudioTrack track) throws DataException, DuplicatedTrackException
	{
		try
		{
			audioTracks.createAudioTrack(track);
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public AudioTrack getTrack(int id) throws DataException
	{
		try
		{
			return audioTracks.getAudioTrack(id);
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public void clearList() throws DataException
	{
		try
		{
			audioTracks.deleteAudioTracks();
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public ArrayList<String> getAlbumsForArtistWithLetter(String letter) throws DataException
	{
		try
		{
			return (ArrayList<String>) audioTracks.getAlbumsForArtistWithLetter(letter);
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public ArrayList<AudioTrack> search(String query) throws DataException
	{
		try
		{
			return (ArrayList<AudioTrack>) audioTracks.getAudioTracksByWildcardSearch(query);
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public int count() throws DataException
	{
		try
		{
			return audioTracks.getAudioTrackCount();
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}

	public ArrayList<AudioTrack> getAlbumTracksForArtistWithLetter(String letter, int index) throws DataException
	{
		try
		{
			return (ArrayList<AudioTrack>) audioTracks.getAlbumTracksForArtistWithLetter(letter, index);
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public IAudioTrackDAO getRandomTrack() throws DataException
	{
		try
		{
			return audioTracks.getRandomTrack();
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
}
