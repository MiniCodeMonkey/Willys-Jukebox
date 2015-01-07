package db.dal.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.Connector;
import db.dal.DALException;
import db.dal.interfaces.IHistoryList;
import db.dao.DAOException;
import db.dao.impl.HistoryListItemDAO;
import db.dao.interfaces.IHistoryListItemDAO;

public class HistoryList implements IHistoryList
{
	public void createHistoryListItem(IHistoryListItemDAO historyListItem) throws DALException
	{
		try
		{
			PreparedStatement ps = Connector.getConn().prepareStatement("INSERT INTO history (played, trackid) VALUES (?,?)");
			
			ps.setInt(1, historyListItem.getPlayed());
			ps.setInt(2, historyListItem.getAudioTrackId());
			
			int affectedRows = ps.executeUpdate();
			ps.close();
			
			if (affectedRows <= 0)
				throw new DALException("Could not create history list item");
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
	}
	
	public List<IHistoryListItemDAO> getHistoryListItems() throws DALException
	{
		List<IHistoryListItemDAO> results = new ArrayList<IHistoryListItemDAO>();
		
		try
		{
			Statement statement = Connector.getConn().createStatement();
			ResultSet rs = statement.executeQuery("SELECT played, trackid FROM historylist");
			
			while (rs.next())
			{
				try
				{
					results.add(
							new HistoryListItemDAO(rs.getInt(1), rs.getInt(2))
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
}
