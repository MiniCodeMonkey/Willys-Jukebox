package db.dal.impl;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import model.DuplicatedTrackException;

import org.cmc.music.metadata.ImageData;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;
import org.cmc.music.myid3.MyID3Listener;

import db.Connector;
import db.dal.DALException;
import db.dal.interfaces.IAudioTracks;
import db.dao.DAOException;
import db.dao.impl.AudioTrackDAO;
import db.dao.interfaces.IAudioTrackDAO;

public class AudioTracks implements IAudioTracks
{
	
	public int createAudioTrack(IAudioTrackDAO audioTrack) throws DALException, DuplicatedTrackException
	{
		try
		{
			PreparedStatement ps = Connector.getConn().prepareStatement("INSERT INTO audiotracks (id, name, artist, album, filename, last_played) VALUES (?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			
			if (audioTrack.getId() == -1)
				ps.setNull(1, java.sql.Types.INTEGER);
			else
				ps.setInt(1, audioTrack.getId());
			
			ps.setString(2, audioTrack.getName());
			ps.setString(3, audioTrack.getArtist());
			ps.setString(4, audioTrack.getAlbum());
			ps.setString(5, audioTrack.getFilename());
			ps.setInt(6, audioTrack.getLastPlayed());
			
			int affectedRows;
			
			try
			{
				affectedRows = ps.executeUpdate();
			}
			catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e)
			{
				throw new DuplicatedTrackException(audioTrack);
			}
			
			if (affectedRows <= 0)
				throw new DALException("Could not create audio track");
			
			ResultSet keys = ps.getGeneratedKeys();
			
			int n = -1;
			if (keys != null && keys.next())
			{
				n = keys.getInt(1);
			}
			
			keys.close();
			ps.close();
			
			return n;
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
	}
	
	public void deleteAudioTrack(int id) throws DALException
	{
		Statement statement = null;
		try
		{
			statement = Connector.getConn().createStatement();
			
			int affectedRows = statement.executeUpdate("DELETE FROM audiotracks WHERE id='" + id + "'");
			
			if (affectedRows <= 0)
				throw new DALException("Could not delete audiotrack with id " + id);
			
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		finally
		{
			try
			{
				statement.close();
			}
			catch (SQLException e)
			{
				
			}
		}
	}
	
	public void editAudioTrack(IAudioTrackDAO audioTrack) throws DALException
	{
		try
		{
			PreparedStatement ps = Connector.getConn().prepareStatement("UPDATE audiotracks set name=?, artist=?, album=?, filename=?, last_played=? WHERE id=?");
			ps.setString(1, audioTrack.getName());
			ps.setString(2, audioTrack.getArtist());
			ps.setString(3, audioTrack.getAlbum());
			ps.setString(4, audioTrack.getFilename());
			ps.setInt(5, audioTrack.getLastPlayed());
			
			int affectedRows = ps.executeUpdate();
			
			ps.close();
			
			if (affectedRows <= 0)
				throw new DALException("Could not update audio track with id " + audioTrack.getId());
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
	}
	
	public IAudioTrackDAO getAudioTrack(int id) throws DALException
	{
		AudioTrackDAO audioTrack = null;
		
		Statement statement = null;
		
		try
		{
			statement = Connector.getConn().createStatement();
			ResultSet rs = statement.executeQuery("SELECT id, name, artist, album, filename, last_played FROM audiotracks WHERE id=" + id);
			
			if (!rs.next())
				throw new DALException("AudioTrack with id " + id + " does not exist");
			
			try
			{
				audioTrack = new AudioTrackDAO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
			}
			catch (DAOException e)
			{
				throw new DALException(e);
			}
			
			rs.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		finally
		{
			try
			{
				statement.close();
			}
			catch (SQLException e)
			{
				
			}
		}
		
		return audioTrack;
	}
	
	public List<IAudioTrackDAO> getAudioTracks() throws DALException
	{
		List<IAudioTrackDAO> results = new ArrayList<IAudioTrackDAO>();
		
		Statement statement = null;
		
		try
		{
			statement = Connector.getConn().createStatement();
			
			ResultSet rs = statement.executeQuery("SELECT id, name, artist, album, filename, last_played FROM audiotracks ORDER BY artist DESC");
			
			while (rs.next())
			{
				try
				{
					results.add(new AudioTrackDAO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				}
				catch (DAOException e)
				{
					throw new DALException(e);
				}
			}
			
			rs.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		finally
		{
			try
			{
				statement.close();
			}
			catch (SQLException e)
			{
				
			}
		}
		
		return results;
	}
	
	public void deleteAudioTracks() throws DALException
	{
		Statement statement = null;
		
		try
		{
			statement = Connector.getConn().createStatement();
			statement.executeUpdate("DELETE FROM audiotracks");
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		finally
		{
			try
			{
				statement.close();
			}
			catch (SQLException e)
			{
				
			}
		}
	}
	
	public List<String> getAlbumsForArtistWithLetter(String letter) throws DALException
	{
		if (letter.length() != 1)
			throw new DALException("Letter length should be 1, letter length is " + letter.length() + " (" + letter + ")");
		
		List<String> results = new ArrayList<String>();
		
		// Retreive album names from audiotracks where the album contains the
		// same artist for
		// all tracks and more than 5 tracks exists for the album
		
		String query;
		
		if (letter.contentEquals("#"))
		{
			query = "SELECT a1.album " + "FROM audiotracks a1 WHERE ";
			
			String letters = "ABCDEFGHIJKLMNOPQRSTVWXYZÆØÅ";
			
			for (int i = 0; i < letters.length(); i++)
			{
				char l = letters.charAt(i);
				
				query += "UPPER(a1.artist) NOT LIKE '" + l + "%' AND ";
			}
			
			query += " a1.album != '' " + "GROUP BY a1.album " + "HAVING COUNT(DISTINCT a1.artist) = 1 AND COUNT(a1.artist) > 5 " + "ORDER BY a1.artist ASC";
		}
		else
		{
			query = "SELECT a1.album " + "FROM audiotracks a1 " + "WHERE UPPER(a1.artist) LIKE '" + letter + "%' " + "AND a1.album != '' " + "GROUP BY a1.album "
					+ "HAVING COUNT(DISTINCT a1.artist) = 1 AND COUNT(a1.artist) > 5 " + "ORDER BY a1.artist ASC";
		}
		
		Statement statement = null;
		try
		{
			statement = Connector.getConn().createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			while (rs.next())
			{
				results.add(rs.getString(1));
			}
			rs.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		finally
		{
			try
			{
				statement.close();
			}
			catch (SQLException e)
			{
				
			}
		}
		
		return results;
	}
	
	public List<IAudioTrackDAO> getTracksByWildcardSearch(String query) throws DALException
	{
		boolean wordSearch = true;
		List<IAudioTrackDAO> results = new ArrayList<IAudioTrackDAO>();
		
		try
		{
			PreparedStatement ps = null;
			
			String[] words = query.split(" ");
			
			if (query.length() <= 0)
			{
				String queryStr = "SELECT id, name, artist, album, filename, last_played FROM audiotracks ORDER BY artist DESC";
				ps = Connector.getConn().prepareStatement(queryStr);
			}
			else if (wordSearch && words.length >= 1)
			{
				String queryStr = "SELECT id, name, artist, album, filename, last_played FROM audiotracks WHERE ";
				
				for (int n = 0; n < words.length; n++)
				{
					queryStr += "(name LIKE ? OR artist LIKE ? OR album LIKE ?) AND ";
				}
				
				queryStr = queryStr.substring(0, queryStr.length() - 5);
				queryStr += " ORDER BY artist DESC";
				
				ps = Connector.getConn().prepareStatement(queryStr);
				
				int psNo = 1;
				for (String word : words)
				{
					ps.setString(psNo, "%" + word + "%");
					psNo++;
					
					ps.setString(psNo, "%" + word + "%");
					psNo++;
					
					ps.setString(psNo, "%" + word + "%");
					psNo++;
				}
			}
			else
			{
				ps = Connector.getConn().prepareStatement(
						"SELECT id, name, artist, album, filename, last_played FROM audiotracks WHERE name LIKE ? OR artist LIKE ? OR album LIKE ? ORDER BY artist DESC");
				ps.setString(1, "%" + query + "%");
				ps.setString(2, "%" + query + "%");
				ps.setString(3, "%" + query + "%");
			}
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				try
				{
					results.add(new AudioTrackDAO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				}
				catch (DAOException e)
				{
					throw new DALException(e);
				}
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		
		return results;
	}
	
	public List<IAudioTrackDAO> getTracksByNameSearch(String query, boolean exact) throws DALException
	{
		List<IAudioTrackDAO> results = new ArrayList<IAudioTrackDAO>();
		
		try
		{
			PreparedStatement ps;
			if (exact)
			{
				ps = Connector.getConn().prepareStatement("SELECT id, name, artist, album, filename, last_played FROM audiotracks WHERE name = ? ORDER BY artist ASC");
				ps.setString(1, query);
			}
			else
			{
				ps = Connector.getConn().prepareStatement("SELECT id, name, artist, album, filename, last_played FROM audiotracks WHERE name LIKE ? ORDER BY artist ASC");
				ps.setString(1, "%" + query + "%");
			}
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				try
				{
					results.add(new AudioTrackDAO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				}
				catch (DAOException e)
				{
					throw new DALException(e);
				}
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		
		return results;
	}
	
	public List<IAudioTrackDAO> getTracksByArtistSearch(String query, boolean exact) throws DALException
	{
		List<IAudioTrackDAO> results = new ArrayList<IAudioTrackDAO>();
		
		try
		{
			PreparedStatement ps;
			if (exact)
			{
				ps = Connector.getConn().prepareStatement("SELECT id, name, artist, album, filename, last_played FROM audiotracks WHERE artist = ? ORDER BY artist ASC");
				ps.setString(1, query);
			}
			else
			{
				ps = Connector.getConn().prepareStatement("SELECT id, name, artist, album, filename, last_played FROM audiotracks WHERE artist LIKE ? ORDER BY artist ASC");
				ps.setString(1, "%" + query + "%");
			}
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				try
				{
					results.add(new AudioTrackDAO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				}
				catch (DAOException e)
				{
					throw new DALException(e);
				}
			}
			
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		
		return results;
	}
	
	public List<IAudioTrackDAO> getTracksByAlbumSearch(String query, boolean exact) throws DALException
	{
		List<IAudioTrackDAO> results = new ArrayList<IAudioTrackDAO>();
		
		try
		{
			PreparedStatement ps;
			if (exact)
			{
				ps = Connector.getConn().prepareStatement("SELECT id, name, artist, album, filename, last_played FROM audiotracks WHERE album = ? ORDER BY artist ASC");
				ps.setString(1, query);
			}
			else
			{
				ps = Connector.getConn().prepareStatement("SELECT id, name, artist, album, filename, last_played FROM audiotracks WHERE album LIKE ? ORDER BY artist ASC");
				ps.setString(1, "%" + query + "%");
			}
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				try
				{
					results.add(new AudioTrackDAO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				}
				catch (DAOException e)
				{
					throw new DALException(e);
				}
			}
			
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		
		return results;
	}
	
	public int getAudioTrackCount() throws DALException
	{
		int count = -1;
		try
		{
			Statement statement = Connector.getConn().createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM audiotracks");
			
			if (!rs.next())
				throw new DALException("Could not get audio track count");
			
			count = rs.getInt(1);
			
			rs.close();
			statement.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		
		return count;
	}
	
	public String getAlbumForArtistWithLetter(String letter, int index) throws DALException
	{
		if (letter.length() != 1)
			throw new DALException("Letter length should be 1, letter length is " + letter.length() + " (" + letter + ")");
		
		String album = null;
		
		String query;
		
		if (letter.contentEquals("#"))
		{
			query = "SELECT a1.album " + "FROM audiotracks a1 WHERE ";
			
			String letters = "ABCDEFGHIJKLMNOPQRSTVWXYZÆØÅ";
			
			for (int i = 0; i < letters.length(); i++)
			{
				char l = letters.charAt(i);
				
				query += "UPPER(a1.artist) NOT LIKE '" + l + "%' AND ";
			}
			
			query += " a1.album != '' " + "GROUP BY a1.album " + "HAVING COUNT(DISTINCT a1.artist) = 1 AND COUNT(a1.artist) > 5 " + "ORDER BY a1.artist ASC LIMIT " + index + ",1";
		}
		else
		{
			query = "SELECT a1.album " + "FROM audiotracks a1 " + "WHERE UPPER(a1.artist) LIKE '" + letter + "%' " + "AND a1.album != '' " + "GROUP BY a1.album "
					+ "HAVING COUNT(DISTINCT a1.artist) = 1 AND COUNT(a1.artist) > 5 " + "ORDER BY a1.artist ASC LIMIT " + index + ",1";
		}
		
		try
		{
			Statement statement = Connector.getConn().createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			if (!rs.next())
			{
				throw new DALException("No album for artist with letter " + letter + " and index " + index);
			}
			
			album = rs.getString(1);
			
			rs.close();
			statement.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		
		return album;
	}
	
	public ImageIcon getAlbumCover(String album, String filename) throws DALException
	{
		if (album == null)
			return null;
		
		ImageIcon albumCover = null;
		PreparedStatement statement = null;
		
		try
		{
			statement = Connector.getConn().prepareStatement("SELECT image FROM album_covers WHERE album=?");
			statement.setString(1, album);
			
			ResultSet rs = statement.executeQuery();
			
			if (!rs.next())
			{
				// Album is not registered in album_cover table.
				// Fetch album cover and save it to the database.
				
				if (filename == null)
					return null;
				
				try
				{
					File src = new File(filename);
					MusicMetadataSet src_set = new MyID3().read(src, new MyID3Listener()
					{
						public void log()
						{
						}
						
						public void log(String s)
						{
						}
					});
					MusicMetadata metadata = (MusicMetadata) src_set.getSimplified();
					
					Vector<ImageData> pictures = metadata.getPictures();
					if (pictures.size() > 0)
					{
						ImageData picture = pictures.get(0);
						
						InputStream in = new ByteArrayInputStream(picture.imageData);
						BufferedImage bufferedImage = ImageIO.read(in);
						
						// This should throw an exception if the image format is
						// damaged in some way
						bufferedImage.getColorModel().getAlpha(null);
						
						this.setAlbumCover(album, new ImageIcon(bufferedImage));
						
						return new ImageIcon(bufferedImage);
					}
				}
				catch (Exception e)
				{
					// Invalid image data or alike
					return null;
				}
			}
			else
			{
				InputStream stream = rs.getBinaryStream(1);
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				
				try
				{
					int a1 = stream.read();
					while (a1 >= 0)
					{
						output.write((char) a1);
						a1 = stream.read();
					}
					
					Image image = Toolkit.getDefaultToolkit().createImage(output.toByteArray());
					ImageIcon imageIcon = new ImageIcon(image);
					
					return imageIcon;
				}
				catch (IOException e)
				{
					return null;
				}
				finally
				{
					try
					{
						output.close();
					}
					catch (IOException e)
					{
					}
				}
			}
			
			rs.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		finally
		{
			try
			{
				statement.close();
			}
			catch (SQLException e)
			{
				
			}
		}
		
		return albumCover;
	}
	
	public void setAlbumCover(String album, ImageIcon albumCover) throws DALException
	{
		if (album == null || album.length() <= 0)
			return;
		
		if (albumCover != null)
		{
			Image image = albumCover.getImage();
			//Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
			albumCover = new ImageIcon(image);
		}
			
		InputStream is = null;
		PreparedStatement ps = null;
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write((RenderedImage) albumCover.getImage(), "png", out);
			byte[] imgData = out.toByteArray();
			is = new ByteArrayInputStream(imgData);
			
			ps = Connector.getConn().prepareStatement("INSERT INTO album_covers (album, image) VALUES (?, ?)");
			ps.setString(1, album);
			ps.setBinaryStream(2, is, out.size());
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		catch (IOException e)
		{
			throw new DALException(e);
		}
		finally
		{
			try
			{
				ps.close();
			}
			catch (SQLException e)
			{
			}
			
			try
			{
				is.close();
			}
			catch (IOException e)
			{
			}
		}
	}
	
	public IAudioTrackDAO getRandomTrack() throws DALException
	{
		AudioTrackDAO audioTrack = null;
		
		Statement statement = null;
		
		try
		{
			statement = Connector.getConn().createStatement();
			ResultSet rs = statement.executeQuery("SELECT id, name, artist, album, filename, last_played FROM audiotracks ORDER BY RAND() LIMIT 1");
			
			if (!rs.next())
				throw new DALException("No audio tracks exists");
			
			try
			{
				audioTrack = new AudioTrackDAO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
			}
			catch (DAOException e)
			{
				e.printStackTrace();
				// throw new DALException(e);
			}
			
			rs.close();
		}
		catch (SQLException e)
		{
			throw new DALException(e);
		}
		finally
		{
			try
			{
				statement.close();
			}
			catch (SQLException e)
			{
				
			}
		}
		
		return audioTrack;
	}
}
