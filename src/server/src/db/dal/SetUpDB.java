package db.dal;

import java.sql.SQLException;
import java.sql.Statement;

import db.Connector;

public class SetUpDB
{
	private Statement statement = null;
	
	/**
	 * Up to date schema is found in db/schema.sql
	 */
	public SetUpDB()
	{
		try
		{
			statement = Connector.getConn().createStatement();
			
			setUpAudioTracks();
			setUpQueueList();
			setUpHistory();
			setUpSettings();
			setUpWishList();
			setUpAlbumCovers();
			
			statement.close();
		}
		catch (SQLException e)
		{
			System.out.println("Exception when trying to set up db");
			e.printStackTrace();
		}
	}

	public void setUpAudioTracks() throws SQLException
	{
		statement.executeUpdate("DROP TABLE IF EXISTS `audiotracks`");
		statement.executeUpdate("CREATE TABLE `audiotracks` (`id` INTEGER AUTO_INCREMENT, `name` varchar(255) NOT NULL, `artist` varchar(255) DEFAULT NULL,"
				+ "`album` varchar(255) DEFAULT NULL, `filename` TEXT NOT NULL, `last_played` int(11) DEFAULT NULL, PRIMARY KEY (id), INDEX indx(name, artist, album), UNIQUE (name, artist))");
	}
	
	public void setUpQueueList() throws SQLException
	{
		statement.executeUpdate("DROP TABLE IF EXISTS `queuelist`");
		statement.executeUpdate("CREATE TABLE `queuelist` (" + "`added` int(11) NOT NULL," + "`trackid` int(11) NOT NULL)");
	}
	
	public void setUpHistory() throws SQLException
	{
		statement.executeUpdate("DROP TABLE IF EXISTS `history`");
		statement.executeUpdate("CREATE TABLE `history` (" + "`played` int(11) NOT NULL," + "`trackid` int(11) NOT NULL)");
	}
	
	public void setUpSettings() throws SQLException
	{
		statement.executeUpdate("DROP TABLE IF EXISTS `settings`");
		statement.executeUpdate("CREATE TABLE `settings` (" + "`name` varchar(255) NOT NULL," + "`value` varchar(255) DEFAULT NULL)");
	}
	
	public void setUpWishList() throws SQLException
	{
		statement.executeUpdate("DROP TABLE IF EXISTS `wishlist`");
		statement.executeUpdate("CREATE TABLE `wishlist` (" + "`id` INTEGER AUTO_INCREMENT," + "`title` varchar(255) NOT NULL," + "`artist` varchar(255) DEFAULT NULL," + "`album` varchar(255) DEFAULT NULL,"
				+ "`added` int(11) DEFAULT NULL, PRIMARY KEY (id))");
	}
	
	private void setUpAlbumCovers() throws SQLException
	{
		statement.executeUpdate("DROP TABLE IF EXISTS `album_covers`");
		statement.executeUpdate("CREATE TABLE `album_covers` (" +
				"`id` int(11) NOT NULL AUTO_INCREMENT," +
				"`album` varchar(255) NOT NULL," +
				"`image` longblob," +
				"PRIMARY KEY (`id`)" +
				") ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;");
	}
}
