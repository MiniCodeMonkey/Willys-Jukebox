package control.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import model.DataException;

import view.ServerView;

import control.AudioManager;

public class Server extends Thread
{
	private ServerSocket serverSocket;
	private final int PORT = 3200;
	private AudioManager audioManager;
	private ArrayList<Connection> clientConnections = new ArrayList<Connection>();
	private ServerView serverView = null;
	
	public Server(AudioManager audioManager, ServerView serverView) throws Exception
	{
		this.audioManager = audioManager;
		this.serverView = serverView;
		
		serverSocket = new ServerSocket(PORT);
		System.out.println("Server listening on port " + PORT);
		this.start();
	}
	
	public void run()
	{
		while (true)
		{
			try
			{
				System.out.println("Waiting for connections.");
				Socket client = serverSocket.accept();
				System.out.println("Accepted a connection from: " + client.getInetAddress());
				Connection connection = new Connection(client, audioManager, serverView);
				
				synchronized (clientConnections)
				{
					clientConnections.add(connection);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
	public void sendBrowseAlbumAvailability()
	{
		class UpdateThread extends Thread
		{
			public void run()
			{
				synchronized (clientConnections)
				{
					for (Iterator<Connection> it = clientConnections.iterator(); it.hasNext();)
					{
						Connection connection = (Connection) it.next();
						try
						{
							connection.sendBrowseAlbumAvailability();
						}
						catch (SocketException e)
						{
							//clientConnections.remove(connection);
						}
						catch (IOException e)
						{
							//clientConnections.remove(connection);
						}
						catch (DataException e)
						{
							// Do nothing in case of a data exception
						}
					}
				}
			}
		}
		
		UpdateThread updateThread = new UpdateThread();
		updateThread.start();
	}
	
	public void sendNowPlayingUpdate()
	{
		class UpdateThread extends Thread
		{
			public void run()
			{
				synchronized (clientConnections)
				{
					for (Iterator<Connection> it = clientConnections.iterator(); it.hasNext();)
					{
						Connection connection = (Connection) it.next();
						try
						{
							connection.sendNowPlayingUpdate();
						}
						catch (SocketException e)
						{
							//clientConnections.remove(connection);
						}
						catch (IOException e)
						{
							//clientConnections.remove(connection);
						}
					}
				}
			}
		}
		
		UpdateThread updateThread = new UpdateThread();
		updateThread.start();
	}
	
	public void sendPlaylistUpdate()
	{
		class UpdateThread extends Thread
		{
			public void run()
			{
				try
				{
					synchronized (clientConnections)
					{
						for (Iterator<Connection> it = clientConnections.iterator(); it.hasNext();)
						{
							Connection connection = (Connection) it.next();
							try
							{
								connection.sendPlaylistUpdate();
							}
							catch (SocketException e)
							{
								//clientConnections.remove(connection);
							}
							catch (IOException e)
							{
								//clientConnections.remove(connection);
							}
						}
					}
				}
				catch (java.util.ConcurrentModificationException e)
				{
					sendPlaylistUpdate();
					return;
				}
			}
		}
		
		UpdateThread updateThread = new UpdateThread();
		updateThread.start();
	}
}
