package db.daf.impl;

import db.daf.DAFException;
import db.daf.interfaces.ISettingsDAF;
import db.dal.DALException;
import db.dal.impl.Settings;
import db.dal.interfaces.ISettings;
import db.dao.DAOException;
import db.dao.impl.SettingDAO;

public class SettingsDAF implements ISettingsDAF
{
	private ISettings settingsDAL;
	
	public SettingsDAF()
	{
		this.settingsDAL = new Settings();
	}
	
	public void deleteSetting(String name) throws DAFException
	{
		try
		{
			settingsDAL.deleteSetting(name);
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}
	
	public String getSetting(String name) throws DAFException
	{
		try
		{
			return settingsDAL.getSetting(name).getValue();
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}
	
	public void setSetting(String name, String value) throws DAFException
	{
		// If setting exists, delete it first
		try
		{
			settingsDAL.deleteSetting(name);
		}
		catch (DALException e)
		{
			// A DAL exception is cast if the setting was not found, just ignore it
		}
		
		// Create setting
		try
		{
			settingsDAL.createSetting(new SettingDAO(name, value));
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
	
	public int getSettingsCount() throws DAFException
	{
		try
		{
			return settingsDAL.getSettingsCount();
		}
		catch (DALException e)
		{
			throw new DAFException(e);
		}
	}
	
}
