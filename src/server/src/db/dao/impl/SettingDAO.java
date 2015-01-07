package db.dao.impl;

import db.dao.DAOException;
import db.dao.interfaces.ISettingDAO;

public class SettingDAO implements ISettingDAO
{
	private String name = null;
	private String value = null;
	
	public SettingDAO(String name, String value) throws DAOException
	{
		this.name = name;
		this.value = value;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getValue()
	{
		return this.value;
	}
	
	public void setName(String name) throws DAOException
	{
		this.name = name;
	}
	
	public void setValue(String value) throws DAOException
	{
		this.value = value;
	}	
}
