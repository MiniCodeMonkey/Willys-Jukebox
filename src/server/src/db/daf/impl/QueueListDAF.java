package db.daf.impl;

import java.util.ArrayList;
import java.util.List;

import model.AudioTrack;
import db.daf.DAFException;
import db.daf.interfaces.IAudioTracksDAF;
import db.daf.interfaces.IQueueListDAF;
import db.dal.DALException;
import db.dal.impl.QueueList;
import db.dao.DAOException;
import db.dao.impl.QueueListItemDAO;
import db.dao.interfaces.IQueueListItemDAO;

public class QueueListDAF implements IQueueListDAF
{
	private QueueList queueListDAL;
	
	public QueueListDAF()
	{
		this.queueListDAL = new QueueList();
	}
	
	public void addTrackToQueue(AudioTrack audioTrack) throws DAFException
	{
		try
		{
			queueListDAL.createQueueListItem(new QueueListItemDAO((int) (System.currentTimeMillis() / 1000), audioTrack.getId()));
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
	
	public void deleteTrackFromQueue(AudioTrack audioTrack) throws DAFException
	{
		try
		{
			queueListDAL.deleteQueueListItem(audioTrack.getId());
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}
	
	public List<AudioTrack> getQueue() throws DAFException
	{
		List<AudioTrack> results = new ArrayList<AudioTrack>();
		
		List<IQueueListItemDAO> queueListItems;
		try
		{
			queueListItems = queueListDAL.getQueueListItems();
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
		IAudioTracksDAF audioTracks = new AudioTracksDAF();
		
		for (IQueueListItemDAO queueListItemDAO : queueListItems)
		{
			AudioTrack audioTrack = audioTracks.getAudioTrack(queueListItemDAO.getAudioTrackId());
			results.add(audioTrack);
		}
		
		return results;
	}

	public int getQueueCount() throws DAFException
	{
		try
		{
			return queueListDAL.getQueueCount();
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}
	
}
