package db.dao.impl;

import db.dao.DAOException;
import db.dao.interfaces.IWishListItemDAO;

public class WishListItemDAO implements IWishListItemDAO
{
	private int added = -1;
	private String album = null;
	private String artist = null;
	private int id = -1;
	private String title = null;
	
	public WishListItemDAO(int added, String album, String artist, int id, String title) throws DAOException
	{
		setAdded(added);
		setAlbum(album);
		setArtist(artist);
		setId(id);
		setTitle(title);
	}
	
	public int getAdded()
	{
		return this.added;
	}
	
	public String getAlbum()
	{
		return this.album;
	}
	
	public String getArtist()
	{
		return this.artist;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setId(int id) throws DAOException
	{
		this.id = id;
	}
	
	public void setAdded(int added) throws DAOException
	{
		this.added = added;
	}
	
	public void setAlbum(String album) throws DAOException
	{
		this.album = album;
	}
	
	public void setArtist(String artist) throws DAOException
	{
		this.artist = artist;
	}
	
	public void setTitle(String title) throws DAOException
	{
		this.title = title;
	}
}
