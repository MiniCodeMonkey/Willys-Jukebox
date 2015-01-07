package db.dao.interfaces;

import db.dao.DAOException;

public interface ISettingDAO
{
	public String getName();
	public void setName(String name) throws DAOException;
	
	public String getValue();
	public void setValue(String value) throws DAOException;
}
