package db.dao.interfaces;

import db.dao.DAOException;

public interface IQueueListItemDAO
{
	public int getAdded();
	public void setAdded(int added) throws DAOException;
	public IAudioTrackDAO getAudioTrack() throws DAOException;
	public void setAudioTrackId(int id) throws DAOException;
	public int getAudioTrackId();
}
