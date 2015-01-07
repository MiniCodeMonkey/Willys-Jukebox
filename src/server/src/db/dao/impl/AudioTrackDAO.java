package db.dao.impl;

import javax.swing.ImageIcon;

import db.dao.DAOException;
import db.dao.interfaces.IAudioTrackDAO;

public class AudioTrackDAO implements IAudioTrackDAO
{
	private int id = -1;
	private String name = null;
	private String artist = null;
	private String album = null;
	private String filename = null;
	private int lastPlayed = -1;
	private ImageIcon albumCover = null;
	
	/**
	 * Create a new audio track data access object
	 * @param id Track id or -1 for auto generated id
	 * @param name Track name/title
	 * @param artist Artist name
	 * @param album Album name
	 * @param filename Absolute file name
	 * @param lastPlayed Last played time as unix time stamp or -1 for none
	 * @throws DAOException
	 */
	public AudioTrackDAO(int id, String name, String artist, String album, String filename, int lastPlayed) throws DAOException
	{
		setId(id);
		setName(name);
		setArtist(artist);
		setAlbum(album);
		setFilename(filename);
		setLastPlayed(lastPlayed);
	}
	
	public String getAlbum()
	{
		return this.album;
	}
	
	public String getArtist()
	{
		return this.artist;
	}
	
	public String getFilename()
	{
		return this.filename;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public int getLastPlayed()
	{
		return this.lastPlayed;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setAlbum(String album) throws DAOException
	{
		this.album = album;
	}
	
	public void setArtist(String artist) throws DAOException
	{
		this.artist = artist;
	}
	
	public void setFilename(String filename) throws DAOException
	{
		this.filename = filename;
	}
	
	public void setLastPlayed(int lastPlayed) throws DAOException
	{
		this.lastPlayed = lastPlayed;
	}
	
	public void setName(String name) throws DAOException
	{
		this.name = name;
	}
	
	public void setId(int id) throws DAOException
	{
		this.id = id;
	}

	public ImageIcon getAlbumCover()
	{
		return this.albumCover;
	}

	public void setAlbumCover(ImageIcon albumCover) throws DAOException
	{
		this.albumCover = albumCover;
	}
}
