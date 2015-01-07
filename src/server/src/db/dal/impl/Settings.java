package db.dal.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.Connector;
import db.dal.DALException;
import db.dal.interfaces.ISettings;
import db.dao.DAOException;
import db.dao.impl.SettingDAO;
import db.dao.interfaces.ISettingDAO;

public class Settings implements ISettings
{
	
	public void createSetting(ISettingDAO setting) throws DALException
	{
		try
		{
			PreparedStatement ps = Connector.getConn().prepareStatement("INSERT INTO settings (name, value) VALUES (?,?)");
			ps.setString(1, setting.getName());
			ps.setString(2, setting.getValue());
			
			int affectedRows = ps.executeUpdate();
			ps.close();
			
			if (affectedRows <= 0)
				throw new DALException("Could not create setting with name " + setting.getName());
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
	}
	
	public void deleteSetting(String name) throws DALException
	{
		try
		{
			PreparedStatement ps = Connector.getConn().prepareStatement("DELETE FROM settings WHERE name=?");
			ps.setString(1, name);
			
			int affectedRows = ps.executeUpdate();
			ps.close();
			
			if (affectedRows <= 0)
				throw new DALException("Could not delete setting with name " + name);
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
	}
	
	public void editSetting(ISettingDAO setting) throws DALException
	{
		try
		{
			PreparedStatement ps = Connector.getConn().prepareStatement("UPDATE settings set value=? WHERE name=?");
			ps.setString(1, setting.getValue());
			ps.setString(2, setting.getName());
			
			int affectedRows = ps.executeUpdate();
			ps.close();
			
			if (affectedRows <= 0)
				throw new DALException("Could not update setting with name " + setting.getName());
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
	}
	
	public ISettingDAO getSetting(String name) throws DALException
	{
		SettingDAO result = null;
		try
		{
			PreparedStatement ps = Connector.getConn().prepareStatement("SELECT name, value FROM settings WHERE name=?");
			ps.setString(1, name);
			
			ResultSet rs = ps.executeQuery();
			
			if (!rs.next())
				throw new DALException("Setting: " + name + " does not exist");
			
			try
			{
				result = new SettingDAO(rs.getString(1), rs.getString(2));
			}
			catch (DAOException e)
			{
				throw new DALException(e);
			}
			
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		
		return result;
	}
	
	public List<ISettingDAO> getSettings() throws DALException
	{
		List<ISettingDAO> results = new ArrayList<ISettingDAO>();
		
		try
		{
			Statement statement = Connector.getConn().createStatement();
			ResultSet rs = statement.executeQuery("SELECT name, value FROM settings");
			
			while (rs.next())
			{
				try
				{
					results.add(new SettingDAO(rs.getString(1), rs.getString(2)));
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
	
	public int getSettingsCount() throws DALException
	{
		int result = -1;
		
		try
		{
			Statement statement = Connector.getConn().createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM settings");
			
			if (!rs.next())
				throw new DALException("Could not get settings count");
			
			result = rs.getInt(1);
			
			rs.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		
		return result;
	}
	
}
