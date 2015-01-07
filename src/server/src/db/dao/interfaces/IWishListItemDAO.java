package db.dao.interfaces;

import db.dao.DAOException;

public interface IWishListItemDAO
{
	public int getId();
	public void setId(int id) throws DAOException;
	public String getTitle();
	public void setTitle(String title) throws DAOException;
	public String getArtist();
	public void setArtist(String artist) throws DAOException;
	public String getAlbum();
	public void setAlbum(String album) throws DAOException;
	public int getAdded();
	public void setAdded(int added) throws DAOException;
}
