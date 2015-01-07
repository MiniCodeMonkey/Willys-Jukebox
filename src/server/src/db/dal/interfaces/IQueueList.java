package db.dal.interfaces;

import java.util.List;

import db.dal.DALException;
import db.dao.interfaces.IQueueListItemDAO;

public interface IQueueList
{
	public void createQueueListItem(IQueueListItemDAO queueListItem) throws DALException;
	public void deleteQueueListItem(int id) throws DALException;
	public List<IQueueListItemDAO> getQueueListItems() throws DALException;
	public int getQueueCount() throws DALException;
}
