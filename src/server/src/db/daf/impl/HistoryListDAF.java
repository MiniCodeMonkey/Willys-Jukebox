package db.daf.impl;

import java.util.ArrayList;
import java.util.List;

import model.AudioTrack;
import db.daf.DAFException;
import db.daf.interfaces.IAudioTracksDAF;
import db.daf.interfaces.IHistoryListDAF;
import db.dal.DALException;
import db.dal.impl.HistoryList;
import db.dao.DAOException;
import db.dao.impl.HistoryListItemDAO;
import db.dao.interfaces.IHistoryListItemDAO;

public class HistoryListDAF implements IHistoryListDAF
{
	private HistoryList historyListDAL;
	
	public HistoryListDAF()
	{
		this.historyListDAL = new HistoryList();
	}
	
	public void addTrackToHistory(AudioTrack audioTrack) throws DAFException
	{
		try
		{
			historyListDAL.createHistoryListItem(new HistoryListItemDAO((int) (System.currentTimeMillis() / 1000), audioTrack.getId()));
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
	
	public List<AudioTrack> getHistory() throws DAFException
	{
		List<AudioTrack> results = new ArrayList<AudioTrack>();
		
		List<IHistoryListItemDAO> historyListItems;
		try
		{
			historyListItems = historyListDAL.getHistoryListItems();
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
		IAudioTracksDAF audioTracks = new AudioTracksDAF();
		
		for (IHistoryListItemDAO historyListItemDAO : historyListItems)
		{
			AudioTrack audioTrack = audioTracks.getAudioTrack(historyListItemDAO.getAudioTrackId());
			results.add(audioTrack);
		}
		
		return results;
	}
}
