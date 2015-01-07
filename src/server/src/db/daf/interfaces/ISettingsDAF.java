package db.daf.interfaces;

import db.daf.DAFException;

public interface ISettingsDAF
{
	public String getSetting(String name) throws DAFException;
	public void setSetting(String name, String value) throws DAFException;
	public void deleteSetting(String name) throws DAFException;
	public int getSettingsCount() throws DAFException;
}
