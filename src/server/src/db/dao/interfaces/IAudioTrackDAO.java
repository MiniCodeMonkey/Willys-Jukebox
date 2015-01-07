package db.dao.interfaces;

import javax.swing.ImageIcon;

import db.dao.DAOException;

public interface IAudioTrackDAO
{
	/**
	 * @return Returns the Audio Track id
	 */
	public int getId();
	
	/**
	 * @param id Sets the AudioTrack id
	 */
	public void setId(int id) throws DAOException;
	
	/**
	 * @return Returns the name / title of the track or null
	 */
	public String getName();
	
	/**
	 * Sets the name / title of the track
	 * @param name name / title of the track
	 */
	public void setName(String name) throws DAOException;
	
	/**
	 * @return Returns the artist of the track or null
	 */
	public String getArtist();
	
	/**
	 * Sets the artist of the track
	 * @param artist artist of the track
	 */
	public void setArtist(String artist) throws DAOException;
	
	/**
	 * @return The album of the track og null
	 */
	public String getAlbum();
	
	/**
	 * Sets the album of the track
	 * @param album album of the track
	 */
	public void setAlbum(String album) throws DAOException;
	
	/**
	 * @return The absolute filename of the track
	 */
	public String getFilename();
	
	/**
	 * Sets the absolute filename of the track
	 * @param filename absolute filename of the track
	 */
	public void setFilename(String filename) throws DAOException;
	
	/**
	 * Returns the last played time as a UNIX timestamp
	 * @return the last played time as a UNIX timestamp
	 */
	public int getLastPlayed();
	
	/**
	 * Sets the last played time as a UNIX timestamp
	 * @param lastPlayed last played time as a UNIX timestamp
	 */
	public void setLastPlayed(int lastPlayed) throws DAOException;
	
	/**
	 * Returns the album cover image for the album or NULL if no Album Cover was found
	 * @return
	 */
	public ImageIcon getAlbumCover();
	
	/**
	 * Sets the album cover for the image
	 * @param albumCover
	 * @throws DAOException
	 */
	public void setAlbumCover(ImageIcon albumCover) throws DAOException;
}
