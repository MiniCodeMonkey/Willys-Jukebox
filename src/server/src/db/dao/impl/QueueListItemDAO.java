package db.dao.impl;

import db.dal.DALException;
import db.dal.impl.AudioTracks;
import db.dao.DAOException;
import db.dao.interfaces.IAudioTrackDAO;
import db.dao.interfaces.IQueueListItemDAO;

public class QueueListItemDAO implements IQueueListItemDAO
{
	private int added = -1;
	private int audioTrackId = -1;
	
	public QueueListItemDAO(int added, int audioTrackId) throws DAOException
	{
		this.added = added;
		this.audioTrackId = audioTrackId;
	}
	
	public int getAdded()
	{
		return this.added;
	}
	
	public void setAdded(int added) throws DAOException
	{
		this.added = added;
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
