package db.dal.interfaces;

import java.util.List;

import db.dal.DALException;
import db.dao.interfaces.IWishListItemDAO;

public interface IWishList
{
	public IWishListItemDAO getWishListItem(int id) throws DALException;
	public void createWishListItem(IWishListItemDAO wishListItem) throws DALException;
	public void editWishListItem(IWishListItemDAO wishListItem) throws DALException;
	public void deleteWishListItem(int id) throws DALException;
	public List<IWishListItemDAO> getWishListItems() throws DALException;
}
