package db.dal.interfaces;

import java.util.List;

import db.dal.DALException;
import db.dao.interfaces.ISettingDAO;

public interface ISettings
{
	public ISettingDAO getSetting(String name) throws DALException;
	public void createSetting(ISettingDAO setting) throws DALException;
	public void editSetting(ISettingDAO setting) throws DALException;
	public void deleteSetting(String name) throws DALException;
	public List<ISettingDAO> getSettings() throws DALException;
	public int getSettingsCount() throws DALException;
}
