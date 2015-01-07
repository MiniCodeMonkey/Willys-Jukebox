package model;

import java.util.ArrayList;

/**
 * 
 * @author jeppe_kronborg
 */
public class WishList implements IWishList
{
	
	ArrayList<Wish> wishlist = new ArrayList<Wish>();
	
	public void addWish(String newWish)
	{
		Wish wish = new Wish(newWish);
		
		wishlist.add(wish);
	}
	
	public void removeWish(int index)
	{
		wishlist.remove(index);
	}
}
