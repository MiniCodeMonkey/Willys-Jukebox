package control.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import view.ServerView;

import control.AudioManager;
import control.PlaylistManagerException;

import model.AudioTrack;
import model.DataException;
import model.QueueList;
import model.TrackList;

import dk.wdk.shared.AlbumBrowseUpdate;
import dk.wdk.shared.BrowseAlbumAvailability;
import dk.wdk.shared.NowPlayingUpdate;
import dk.wdk.shared.PlaylistUpdate;
import dk.wdk.shared.Protocol;
import dk.wdk.shared.SearchResultUpdate;

public class Connection extends Thread
{
	private Socket client = null;
	private ObjectInputStream objectInputStream = null;
	private ObjectOutputStream objectOutputStream = null;
	private AudioManager audioManager = null;
	private ServerView serverView = null;
	
	public Connection()
	{
	}
	
	public Connection(Socket clientSocket, AudioManager audioManager, ServerView serverView)
	{
		this.audioManager = audioManager;
		this.serverView = serverView;
		
		client = clientSocket;
		try
		{
			objectInputStream = new ObjectInputStream(client.getInputStream());
			objectOutputStream = new ObjectOutputStream(client.getOutputStream());
		}
		catch (Exception e1)
		{
			try
			{
				client.close();
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
			return;
		}
		this.start();
	}
	
	public void run()
	{
		while (true)
		{
			try
			{
				while (objectInputStream.available() <= 0)
				{
					try
					{
						Thread.sleep(500);
					}
					catch (InterruptedException e)
					{
						// Do nothing special if interrupted
					}
				}
				
				int recv = objectInputStream.readInt();
				
				switch (recv)
				{
				case Protocol.HELLO:
					sendNowPlayingUpdate();
					sendPlaylistUpdate();
					sendBrowseAlbumAvailability();
					break;
				
				case Protocol.BROWSE_ALBUM:
					char letter = objectInputStream.readChar();
					int index = objectInputStream.readInt();
					sendAlbumBrowseUpdate(letter, index);
					break;
				
				case Protocol.PING:
					break;
				
				case Protocol.REQUEST_AUDIOTRACK:
					int audioTrack = objectInputStream.readInt();
					QueueList queueList = QueueList.getInstance();
					try
					{
						queueList.addTrackToQueue(TrackList.getInstance().getTrack(audioTrack));
						serverView.updateQueueList();
						
						// Send AUDIO_TRACK_REQUEST_ACCEPTED message
						objectOutputStream.writeInt(Protocol.AUDIO_TRACK_REQUEST_ACCEPTED);
						objectOutputStream.writeInt(audioTrack);
						objectOutputStream.flush();
					}
					catch (PlaylistManagerException e)
					{
						sendErrorMessage(e.getMessage());
					}
					catch (DataException e)
					{
						sendErrorMessage(e.getMessage());
					}
					break;
				
				case Protocol.SEARCH_QUERY:
					String query = (String) objectInputStream.readObject();
					sendSearchResult(query);
					break;
				
				default:
					break;
				}
				
			}
			catch (ClassNotFoundException e2)
			{
				e2.printStackTrace();
			}
			catch (EOFException e1)
			{
				e1.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				break;
			}
			catch (DataException e)
			{
				System.out.println("**** DATA EXCEPTION ****");
				e.printStackTrace();
			}
		}
		
		// Close streams and connections
		try
		{
			objectInputStream.close();
			objectOutputStream.close();
			client.close();
		}
		catch (IOException e)
		{
			// Do nothing if already closed
		}
		
		System.out.println("Client disconnected from server.");
	}
	
	private void sendSearchResult(String query) throws IOException
	{
		// Create SearchResultUpdate object
		SearchResultUpdate searchResultUpdate = new SearchResultUpdate(query);
		ArrayList<AudioTrack> searchResultTracks;
		try
		{
			searchResultTracks = TrackList.getInstance().search(query);
		}
		catch (DataException e)
		{
			e.printStackTrace();
			searchResultTracks = new ArrayList<AudioTrack>(); // Send empty list
		}
		
		Iterator<AudioTrack> track = searchResultTracks.iterator();
		while (track.hasNext())
		{
			AudioTrack currentTrack = track.next();
			searchResultUpdate.addTrack(currentTrack.getName(), currentTrack.getArtist(), currentTrack.getAlbum(), (int) currentTrack.getId());
		}
		
		// Write command
		objectOutputStream.writeInt(Protocol.SEARCH_RESULT);
		
		// Write SearchResultUpdate object
		objectOutputStream.writeObject(searchResultUpdate);
		objectOutputStream.flush();
	}
	
	private void sendErrorMessage(String message) throws IOException
	{
		objectOutputStream.writeInt(Protocol.ERROR);
		objectOutputStream.writeObject(message);
		objectOutputStream.flush();
	}
	
	private void sendAlbumBrowseUpdate(char letter, int index) throws IOException, SocketException, DataException
	{
		// Get album name
		ArrayList<AudioTrack> albumTracks = TrackList.getInstance().getAlbumTracksForArtistWithLetter(String.format("%c", letter), index);
		
		AlbumBrowseUpdate albumBrowseUpdate = null;
		
		if (albumTracks.size() <= 0)
			return;
		
		String artist = null;
		for (AudioTrack track : albumTracks)
		{
			if (track.getArtist() == null)
				continue;
			
			if (artist == null)
			{
				artist = track.getArtist();
			}
			else
			{
				if (artist.length() < track.getArtist().length())
				{
					artist = track.getArtist();
				}
			}
		}
		
		if (artist == null)
			return;
		
		try
		{
			albumBrowseUpdate = new AlbumBrowseUpdate(artist, albumTracks.get(0).getAlbum(), albumTracks.get(0).getAlbumCoverImage(), index, letter);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			return;
		}
		
		for (AudioTrack track : albumTracks)
		{
			track.setArtist(artist);
			albumBrowseUpdate.addTrack((int) track.getId(), track.getName());
		}
		
		objectOutputStream.writeInt(Protocol.ALBUM_BROWSE_UPDATE); // Command
		objectOutputStream.writeObject(albumBrowseUpdate); // Object
		objectOutputStream.flush();
	}
	
	public void sendBrowseAlbumAvailability() throws IOException, SocketException, DataException
	{
		// Send command
		objectOutputStream.writeInt(Protocol.BROWSE_ALBUM_AVAILABILITY);
		
		BrowseAlbumAvailability browseAlbumAvailability = new BrowseAlbumAvailability();
		
		for (char c = 'A'; c <= 'Z'; c++)
		{
			browseAlbumAvailability.addLetter(String.format("%c", c), TrackList.getInstance().getAlbumsForArtistWithLetter(String.format("%c", c)).size());
		}
		browseAlbumAvailability.addLetter("®", TrackList.getInstance().getAlbumsForArtistWithLetter("®").size());
		browseAlbumAvailability.addLetter("¯", TrackList.getInstance().getAlbumsForArtistWithLetter("¯").size());
		browseAlbumAvailability.addLetter("", TrackList.getInstance().getAlbumsForArtistWithLetter("").size());
		browseAlbumAvailability.addLetter("#", TrackList.getInstance().getAlbumsForArtistWithLetter("#").size());
		
		objectOutputStream.writeObject(browseAlbumAvailability);
		objectOutputStream.flush();
	}
	
	public void sendPlaylistUpdate() throws IOException, SocketException
	{
		// Send command
		objectOutputStream.writeInt(Protocol.PLAYLIST_UPDATE);
		
		// Send PlaylistUpdate object
		PlaylistUpdate playlistUpdate = new PlaylistUpdate();
		
		int position = 1;
		try
		{
			for (AudioTrack track : QueueList.getInstance().getTracks())
			{
				playlistUpdate.addTrack(track.getName(), track.getArtist(), position);
				position++;
			}
		}
		catch (DataException e)
		{
			e.printStackTrace();
		}
		
		objectOutputStream.writeObject(playlistUpdate);
		objectOutputStream.flush();
	}
	
	public void sendNowPlayingUpdate() throws IOException, SocketException
	{
		
		// Send command
		objectOutputStream.writeInt(Protocol.NOW_PLAYING_UPDATE);
		
		// Send NowPlayingUpdate object
		AudioTrack nowPlayingTrack = audioManager.getCurrentTrack();
		
		try
		{
			if (nowPlayingTrack != null)
			{
				NowPlayingUpdate nowPlayingUpdate = new NowPlayingUpdate(nowPlayingTrack.getName(), nowPlayingTrack.getArtist(), nowPlayingTrack.getAlbum(), nowPlayingTrack.getAlbumCoverImage(),
						audioManager.getDuration(true), audioManager.getElapsedTime(true), audioManager.isPaused());
				
				objectOutputStream.writeObject(nowPlayingUpdate);
			}
			else
			{
				NowPlayingUpdate nowPlayingUpdate = new NowPlayingUpdate("", "", "", null, 0, 0, true);
				objectOutputStream.writeObject(nowPlayingUpdate);
			}
		}
		catch (Exception e)
		{
			// There could be issues regarding album cover image
		}
		
		objectOutputStream.flush();
	}
}