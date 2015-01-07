package db.dal.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.Connector;
import db.dal.DALException;
import db.dal.interfaces.IQueueList;
import db.dao.DAOException;
import db.dao.impl.QueueListItemDAO;
import db.dao.interfaces.IQueueListItemDAO;

public class QueueList implements IQueueList
{
	
	public void createQueueListItem(IQueueListItemDAO queueListItem) throws DALException
	{
		try
		{
			PreparedStatement ps = Connector.getConn().prepareStatement("SELECT COUNT(*) FROM queuelist WHERE trackid=?");
			ps.setInt(1, queueListItem.getAudioTrackId());
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				if (rs.getInt(1) > 0)
				{
					throw new DALException("The track is already enqueued!");
				}
			}
			rs.close();
			ps.close();
			
			ps = Connector.getConn().prepareStatement("INSERT INTO queuelist (added, trackid) VALUES (?,?)");
			
			ps.setInt(1, queueListItem.getAdded());
			ps.setInt(2, queueListItem.getAudioTrackId());
			
			int affectedRows = ps.executeUpdate();
			ps.close();
			
			if (affectedRows <= 0)
				throw new DALException("Could not create queue list item");
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
	}
	
	public void deleteQueueListItem(int id) throws DALException
	{
		try
		{
			PreparedStatement ps = Connector.getConn().prepareStatement("DELETE FROM queuelist WHERE trackid=?");
			
			ps.setInt(1, id);
			
			int affectedRows = ps.executeUpdate();
			ps.close();
			
			if (affectedRows <= 0)
				throw new DALException("Could not delete queue list item");
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
	}
	
	public List<IQueueListItemDAO> getQueueListItems() throws DALException
	{
		List<IQueueListItemDAO> results = new ArrayList<IQueueListItemDAO>();
		
		try
		{
			Statement statement = Connector.getConn().createStatement();
			ResultSet rs = statement.executeQuery("SELECT added, trackid FROM queuelist ORDER BY added ASC");
			while (rs.next())
			{
				try
				{
					results.add(
							new QueueListItemDAO(rs.getInt(1), rs.getInt(2))
					);
				}
				catch (DAOException e)
				{
					throw new DALException(e);
				}
			}
			rs.close();
			statement.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
	
		return results;
	}

	public int getQueueCount() throws DALException
	{
		int res = -1;
		
		try
		{
			Statement statement = Connector.getConn().createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM queuelist");
			
			if (!rs.next())
				throw new DALException("Could not get queue count");
			
			res = rs.getInt(1);
			
			rs.close();
			statement.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		
		return res;
	}
	
}
