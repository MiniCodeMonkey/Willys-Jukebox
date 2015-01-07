package db.dao.interfaces;

import db.dao.DAOException;

public interface IHistoryListItemDAO
{
	public int getPlayed();
	public void setPlayed(int played) throws DAOException;
	public IAudioTrackDAO getAudioTrack() throws DAOException;
	public void setAudioTrackId(int id) throws DAOException;
	public int getAudioTrackId();
}
