package db.daf.impl;

import java.util.ArrayList;
import java.util.List;

import model.AudioTrack;
import model.DuplicatedTrackException;
import db.daf.DAFException;
import db.daf.interfaces.IAudioTracksDAF;
import db.dal.DALException;
import db.dal.interfaces.IAudioTracks;
import db.dal.impl.AudioTracks;
import db.dao.DAOException;
import db.dao.impl.AudioTrackDAO;
import db.dao.interfaces.IAudioTrackDAO;

public class AudioTracksDAF implements IAudioTracksDAF
{
	IAudioTracks audioTracksDAL;
	
	public AudioTracksDAF()
	{
		audioTracksDAL = new AudioTracks();
	}
	
	public void createAudioTrack(AudioTrack audioTrack) throws DAFException, DuplicatedTrackException
	{
		try
		{
			int insertId = audioTracksDAL.createAudioTrack(new AudioTrackDAO(audioTrack.getId(), audioTrack.getName(), audioTrack.getArtist(), audioTrack.getAlbum(), audioTrack.getFilename(), audioTrack.getLastPlayed()));
			audioTrack.setId(insertId);
		}
		catch (DAOException e)
		{
			throw new DAFException(e);
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}
	
	public void deleteAudioTrack(int id) throws DAFException
	{
		try
		{
			audioTracksDAL.deleteAudioTrack(id);
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}
	
	public void editAudioTrack(AudioTrack audioTrack) throws DAFException
	{
		try
		{
			audioTracksDAL.editAudioTrack(new AudioTrackDAO(audioTrack.getId(), audioTrack.getName(), audioTrack.getArtist(), audioTrack.getAlbum(), audioTrack.getFilename(), audioTrack.getLastPlayed()));
		}
		catch (DAOException e)
		{
			throw new DAFException(e);
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}
	
	public AudioTrack getAudioTrack(int id) throws DAFException
	{
		AudioTrackDAO audioTrackDAO;
		try
		{
			audioTrackDAO = (AudioTrackDAO) audioTracksDAL.getAudioTrack(id);
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
		
		AudioTrack audioTrack = new AudioTrack(audioTrackDAO.getFilename(), false);
		audioTrack.setId(audioTrackDAO.getId());
		audioTrack.setName(audioTrackDAO.getName());
		audioTrack.setArtist(audioTrackDAO.getArtist());
		audioTrack.setAlbum(audioTrackDAO.getAlbum());
		audioTrack.setLastPlayed(audioTrackDAO.getLastPlayed());
		
		return audioTrack;
	}
	
	public List<AudioTrack> getAudioTracks() throws DAFException
	{
		List<AudioTrack> results = new ArrayList<AudioTrack>();
		
		List<IAudioTrackDAO> tracks;
		try
		{
			tracks = audioTracksDAL.getAudioTracks();
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
		
		for (IAudioTrackDAO audioTrackDAO : tracks)
		{
			AudioTrack audioTrack = new AudioTrack(audioTrackDAO.getFilename(), false);
			audioTrack.setId(audioTrackDAO.getId());
			audioTrack.setName(audioTrackDAO.getName());
			audioTrack.setArtist(audioTrackDAO.getArtist());
			audioTrack.setAlbum(audioTrackDAO.getAlbum());
			audioTrack.setLastPlayed(audioTrackDAO.getLastPlayed());
			
			results.add(audioTrack);
		}
		
		return results;
	}
	
	public void deleteAudioTracks() throws DAFException
	{
		try
		{
			audioTracksDAL.deleteAudioTracks();
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}
	
	public List<AudioTrack> getAudioTracksByWildcardSearch(String query) throws DAFException
	{
		List<AudioTrack> results = new ArrayList<AudioTrack>();
		
		List<IAudioTrackDAO> tracks;
		try
		{
			tracks = audioTracksDAL.getTracksByWildcardSearch(query);
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
		
		for (IAudioTrackDAO audioTrackDAO : tracks)
		{
			AudioTrack audioTrack = new AudioTrack(audioTrackDAO.getFilename(), false);
			audioTrack.setId(audioTrackDAO.getId());
			audioTrack.setName(audioTrackDAO.getName());
			audioTrack.setArtist(audioTrackDAO.getArtist());
			audioTrack.setAlbum(audioTrackDAO.getAlbum());
			audioTrack.setLastPlayed(audioTrackDAO.getLastPlayed());
			
			results.add(audioTrack);
		}
		
		return results;
	}
	
	public List<AudioTrack> getAudioTracksByNameSearch(String query) throws DAFException
	{
		List<AudioTrack> results = new ArrayList<AudioTrack>();
		
		List<IAudioTrackDAO> tracks;
		try
		{
			tracks = audioTracksDAL.getTracksByNameSearch(query, false);
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
		
		for (IAudioTrackDAO audioTrackDAO : tracks)
		{
			AudioTrack audioTrack = new AudioTrack(audioTrackDAO.getFilename(), false);
			audioTrack.setId(audioTrackDAO.getId());
			audioTrack.setName(audioTrackDAO.getName());
			audioTrack.setArtist(audioTrackDAO.getArtist());
			audioTrack.setAlbum(audioTrackDAO.getAlbum());
			audioTrack.setLastPlayed(audioTrackDAO.getLastPlayed());
			
			results.add(audioTrack);
		}
		
		return results;
	}
	
	public List<AudioTrack> getAudioTracksByArtistSearch(String query) throws DAFException
	{
		List<AudioTrack> results = new ArrayList<AudioTrack>();
		
		List<IAudioTrackDAO> tracks;
		try
		{
			tracks = audioTracksDAL.getTracksByArtistSearch(query, false);
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
		
		for (IAudioTrackDAO audioTrackDAO : tracks)
		{
			AudioTrack audioTrack = new AudioTrack(audioTrackDAO.getFilename(), false);
			audioTrack.setId(audioTrackDAO.getId());
			audioTrack.setName(audioTrackDAO.getName());
			audioTrack.setArtist(audioTrackDAO.getArtist());
			audioTrack.setAlbum(audioTrackDAO.getAlbum());
			audioTrack.setLastPlayed(audioTrackDAO.getLastPlayed());
			
			results.add(audioTrack);
		}
		
		return results;
	}
	
	public List<AudioTrack> getAudioTracksByAlbumSearch(String query) throws DAFException
	{
		List<AudioTrack> results = new ArrayList<AudioTrack>();
		
		List<IAudioTrackDAO> tracks;
		try
		{
			tracks = audioTracksDAL.getTracksByAlbumSearch(query, false);
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
		
		for (IAudioTrackDAO audioTrackDAO : tracks)
		{
			AudioTrack audioTrack = new AudioTrack(audioTrackDAO.getFilename(), false);
			audioTrack.setId(audioTrackDAO.getId());
			audioTrack.setName(audioTrackDAO.getName());
			audioTrack.setArtist(audioTrackDAO.getArtist());
			audioTrack.setAlbum(audioTrackDAO.getAlbum());
			audioTrack.setLastPlayed(audioTrackDAO.getLastPlayed());
			
			results.add(audioTrack);
		}
		
		return results;
	}

	public int getAudioTrackCount() throws DAFException
	{
		try
		{
			return audioTracksDAL.getAudioTrackCount();
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}

	public List<String> getAlbumsForArtistWithLetter(String letter) throws DAFException
	{
		try
		{
			return audioTracksDAL.getAlbumsForArtistWithLetter(letter);
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}

	public List<AudioTrack> getAlbumTracksForArtistWithLetter(String letter, int index) throws DAFException
	{
		List<AudioTrack> results = new ArrayList<AudioTrack>();
		
		try
		{
			String album = audioTracksDAL.getAlbumForArtistWithLetter(letter, index);
			
			List<IAudioTrackDAO> tracks = audioTracksDAL.getTracksByAlbumSearch(album, true);
			
			for (IAudioTrackDAO audioTrackDAO : tracks)
			{
				AudioTrack audioTrack = new AudioTrack(audioTrackDAO.getFilename(), false);
				audioTrack.setId(audioTrackDAO.getId());
				audioTrack.setName(audioTrackDAO.getName());
				audioTrack.setArtist(audioTrackDAO.getArtist());
				audioTrack.setAlbum(audioTrackDAO.getAlbum());
				audioTrack.setLastPlayed(audioTrackDAO.getLastPlayed());
				
				results.add(audioTrack);
			}
			
			return results;
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}

	public IAudioTrackDAO getRandomTrack() throws DAFException
	{
		try
		{
			return audioTracksDAL.getRandomTrack();
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}
}
