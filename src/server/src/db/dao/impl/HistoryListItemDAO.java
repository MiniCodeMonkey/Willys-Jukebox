package db.dao.impl;

import db.dal.DALException;
import db.dal.impl.AudioTracks;
import db.dao.DAOException;
import db.dao.interfaces.IAudioTrackDAO;
import db.dao.interfaces.IHistoryListItemDAO;

public class HistoryListItemDAO implements IHistoryListItemDAO
{
	private int played = -1;
	private int audioTrackId = -1;
	
	public HistoryListItemDAO(int played, int audioTrackId) throws DAOException
	{
		this.played = played;
	}
	
	public int getPlayed()
	{
		return this.played;
	}
	
	public void setPlayed(int played) throws DAOException
	{
		this.played = played;
	}
	
	public IAudioTrackDAO getAudioTrack() throws DAOException
	{
		try
		{
			return new AudioTracks().getAudioTrack(this.audioTrackId);
		}
		catch (DALException e)
		{
			throw new DAOException(e);
		}
	}
	
	public void setAudioTrackId(int id) throws DAOException
	{
		this.audioTrackId = id;
	}
	
	public int getAudioTrackId()
	{
		return this.audioTrackId;
	}
}
