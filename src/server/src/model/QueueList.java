package model;

import java.util.ArrayList;

import control.PlaylistManager;
import control.PlaylistManagerException;
import db.daf.DAFException;
import db.daf.impl.HistoryListDAF;
import db.daf.impl.QueueListDAF;
import db.daf.interfaces.IHistoryListDAF;
import db.daf.interfaces.IQueueListDAF;

/**
 * 
 * @author jeppe_kronborg
 */
public class QueueList implements IQueueList
{
	
	private static QueueList instance = null;
	private IQueueListDAF queueListDAF;
	private IHistoryListDAF historyListDAF;
	PlaylistManager playlistManager;
	
	// Prevent QueueList from being initiated normally
	protected QueueList()
	{
		playlistManager = new PlaylistManager();
		queueListDAF = new QueueListDAF();
		historyListDAF = new HistoryListDAF();
	}
	
	public static QueueList getInstance()
	{
		if (instance == null)
		{
			instance = new QueueList();
		}
		
		return instance;
	}
	
	public void addToHistory(AudioTrack audioTrack) throws DataException
	{
		try
		{
			historyListDAF.addTrackToHistory(audioTrack);
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public void addTrackToQueue(AudioTrack track) throws PlaylistManagerException, DataException
	{
		playlistManager.canEnqueue(track);
		try
		{
			queueListDAF.addTrackToQueue(track);
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public void removeTrack(AudioTrack track) throws DataException
	{
		try
		{
			queueListDAF.deleteTrackFromQueue(track);
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public ArrayList<AudioTrack> getTracks() throws DataException
	{
		try
		{
			return (ArrayList<AudioTrack>) queueListDAF.getQueue();
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public int size() throws DataException
	{
		try
		{
			return queueListDAF.getQueueCount();
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
	
	public AudioTrack popTrack() throws DataException
	{
		try
		{
			AudioTrack track = queueListDAF.getQueue().get(0);
			queueListDAF.deleteTrackFromQueue(track);
			
			return track;
		}
		catch (DAFException e)
		{
			throw new DataException(e);
		}
	}
}
