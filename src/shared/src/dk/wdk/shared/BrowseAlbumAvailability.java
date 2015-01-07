package dk.wdk.shared;

import java.io.Serializable;
import java.util.TreeMap;

public class BrowseAlbumAvailability implements Serializable {

	private static final long serialVersionUID = 843977548664959873L;
	
	private TreeMap<String,Integer> albumAvailability = new TreeMap<String,Integer>();

	public BrowseAlbumAvailability() {
	}
	
	public int getAlbumsForLetter(String letter)
	{
		Integer albums = albumAvailability.get(letter);
		return (albums == null ? 0 : albums);
	}
	
	public void addLetter(String letter, int value)
	{
		albumAvailability.put(letter, value);
	}
}
