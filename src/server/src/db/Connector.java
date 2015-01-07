package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.dal.SetUpDB;

/**
 * Connector to the SQLite database
 */
public class Connector
{
	private static Connection conn = null;
	
	public static Connection connectToDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox", "jukebox", "passjukewdk");
	}
	
	/**
	 * @return The active connection to the database
	 * @throws SQLException 
	 */
	public static Connection getConn() throws SQLException
	{
		if (conn == null)
		{
			try
			{
				conn = Connector.connectToDatabase();
				
				// If there is not at least five tables in database then SetUpDB
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery("SHOW TABLES");
				int rows = 0;
				
				while (rs.next())
				{
					rows++;
				}
				
				rs.close();
				statement.close();
				
				if (rows < 5)
				{
					// Set up the database
					new SetUpDB();
				}
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		
		return conn;
	}
}