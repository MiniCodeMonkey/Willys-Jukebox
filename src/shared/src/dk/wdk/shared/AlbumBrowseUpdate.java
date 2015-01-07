package dk.wdk.shared;

import java.io.Serializable;
import java.util.TreeMap;

import javax.swing.ImageIcon;

public class AlbumBrowseUpdate implements Serializable {

	private static final long serialVersionUID = 843977548664959873L;
	
	private int index;
	private char letter;
	private String artist;
	private String album;
	private ImageIcon albumImage;
	private TreeMap<Integer,String> trackList = new TreeMap<Integer,String>();

	public AlbumBrowseUpdate(String artist, String album, ImageIcon albumImage, int index, char letter) {
		this.artist = artist;
		this.album = album;
		this.albumImage = albumImage;
		this.index = index;
		this.letter = letter;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public char getLetter()
	{
		return letter;
	}

	public String getArtist() {
		return artist;
	}

	public String getAlbum() {
		return album;
	}

	public ImageIcon getAlbumImage() {
		return albumImage;
	}

	public TreeMap<Integer,String> getTrackList()
	{
		return trackList;
	}
	
	public void addTrack(int trackId, String trackName)
	{
		trackList.put(trackId, trackName);
	}
}
