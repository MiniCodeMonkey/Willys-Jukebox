package db.dal.interfaces;

import java.util.List;

import db.dal.DALException;
import db.dao.interfaces.IHistoryListItemDAO;

public interface IHistoryList
{
	public void createHistoryListItem(IHistoryListItemDAO queueListItem) throws DALException;
	public List<IHistoryListItemDAO> getHistoryListItems() throws DALException;
}
