package control.network;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import view.front.TouchView;
import dk.wdk.shared.AlbumBrowseUpdate;
import dk.wdk.shared.BrowseAlbumAvailability;
import dk.wdk.shared.NowPlayingUpdate;
import dk.wdk.shared.PlaylistUpdate;
import dk.wdk.shared.Protocol;
import dk.wdk.shared.SearchResultUpdate;

public class Connection extends Thread {
	private Socket socket;
	private final int PORT = 3200;
	private ObjectInputStream objectInputStream = null;
	private ObjectOutputStream objectOutputStream = null;
	private TouchView touchView = null;

	public Connection(TouchView touchView2) {

		this.touchView = touchView2;

		this.start();
	}

	private String getIpAddress() {
		String fallBackIp = "127.0.0.1";
		//fallBackIp = "192.168.1.7";
		//fallBackIp = "82.211.204.183";
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("config.cfg"));
			String str;
			while ((str = in.readLine()) != null) {
				if (str.length() > 0)
					return str;
			}
			in.close();
		} catch (IOException e) {
		}

		return fallBackIp;
	}

	public void run() {
		try {
			System.out.println("Connecting to server");
			try {
				socket = new Socket(this.getIpAddress(), PORT);
			} catch (ConnectException e) {
				touchView.setConnectionError(true);
				try {
					Thread.sleep(5000); // Wait and retry
				} catch (InterruptedException e1) {

				}

				// Call method again (recursive)
				run();
				return;
			}

			touchView.setConnectionError(false);

			objectOutputStream = new ObjectOutputStream(socket
					.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());

			// Send HELLO
			objectOutputStream.writeInt(Protocol.HELLO);
			objectOutputStream.flush();

			// Receive commands
			while (true) {

				try {
					while (objectInputStream.available() <= 0) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// Do nothing special in case of interrupt
						}

						// Check if socket is still alive
						try {
							objectOutputStream.writeInt(Protocol.PING);
							objectOutputStream.flush();
						} catch (SocketException e) {
							touchView.setConnectionError(true);
							run();
							return;
						}
					}
					int recv = objectInputStream.readInt();
					handleCommand(recv);

				} catch (EOFException e) {
					// Try to reconnect in case of EOF
					try {
						objectInputStream.close();
						objectOutputStream.close();
						socket.close();
					} catch (IOException e2) {

					}

					run();
					return;
				}
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				objectInputStream.close();
				objectOutputStream.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		run();
	}

	public void browseAlbum(String letter, int index) throws IOException {
		// Send command
		objectOutputStream.writeInt(Protocol.BROWSE_ALBUM);
		objectOutputStream.writeChar(letter.charAt(0)); // Letter
		objectOutputStream.writeInt(index); // Index
		objectOutputStream.flush();
	}

	private void handleCommand(int command) throws IOException,
			ClassNotFoundException, EOFException {

		switch (command) {
		case Protocol.ALBUM_BROWSE_UPDATE: {
			AlbumBrowseUpdate albumBrowseUpdate = (AlbumBrowseUpdate) objectInputStream
					.readObject();
			touchView.browseAlbumsView.albumBrowseUpdate(albumBrowseUpdate);
			break;
		}

		case Protocol.BROWSE_ALBUM_AVAILABILITY: {
			BrowseAlbumAvailability browseAlbumAvailability = (BrowseAlbumAvailability) objectInputStream
					.readObject();
			touchView.browseAlbumsView
					.setAlbumAvailability(browseAlbumAvailability);
			break;
		}

		case Protocol.NOW_PLAYING_UPDATE: {
			try {
				NowPlayingUpdate nowPlayingUpdate = (NowPlayingUpdate) objectInputStream
						.readObject();
				touchView.updateNowPlaying(nowPlayingUpdate.getName(),
						nowPlayingUpdate.getArtist(), nowPlayingUpdate
								.getAlbum(), nowPlayingUpdate.getAlbumImage(),
						nowPlayingUpdate.getDuration(), nowPlayingUpdate
								.getElapsed(), nowPlayingUpdate.getIsPaused());
			} catch (OptionalDataException e) {
				touchView.updateNowPlaying("", "", "", null, 0, 0, true);
				reconnect();
			}

			break;
		}

		case Protocol.PLAYLIST_UPDATE: {
			try {
				PlaylistUpdate playlistUpdate = (PlaylistUpdate) objectInputStream
						.readObject();

				touchView.updatePlaylist(playlistUpdate.getPlaylistTracks());
			} catch (OptionalDataException e) {
				reconnect();
				e.printStackTrace();
			}
			break;
		}

		case Protocol.ERROR: {
			try {
				String errorMessage = (String) objectInputStream.readObject();
				touchView.showError(errorMessage);
			} catch (OptionalDataException e) {
				reconnect();
				e.printStackTrace();
			}
			break;
		}

		case Protocol.SEARCH_RESULT: {
			SearchResultUpdate searchResultUpdate = (SearchResultUpdate) objectInputStream
					.readObject();
			touchView.browseAlbumsView.searchResultUpdate(searchResultUpdate);
			break;
		}

		case Protocol.AUDIO_TRACK_REQUEST_ACCEPTED: {
			// The track id is not currently being used but MUST be read from
			// the buffer
			/* int audioTrackId = */objectInputStream.readInt();
			//touchView.showMessage("Track enqueued");
			
			touchView.browseAlbumsView.searchResultsPanel.setTracks(null);
			touchView.browseAlbumsView.searchBoxPanel.setInactive(true);
			touchView.browseAlbumsView.searchResultsPanel.setVisible(false);
			touchView.browseAlbumsView.keyboardPanel.setVisible(false);
			touchView.browseAlbumsView.setBrowseViewVisible(false);
			touchView.browseAlbumsView.setVisible(false);
			
			break;
		}
		}
	}

	public void requestAudioTrack(int currentTrackId) {
		// Send command
		try {
			objectOutputStream.writeInt(Protocol.REQUEST_AUDIOTRACK);
			objectOutputStream.writeInt(currentTrackId);
			objectOutputStream.flush();
		} catch (IOException e) {

		}
	}

	public void searchQuery(String query) {
		// Send command
		try {
			objectOutputStream.writeInt(Protocol.SEARCH_QUERY);
			objectOutputStream.writeObject(query);
			objectOutputStream.flush();
		} catch (IOException e) {

		}
	}

	private void reconnect() {
		System.out.println("Reconnecting!");
		try {
			objectInputStream.close();
			objectOutputStream.close();
			socket.close();
		} catch (IOException e2) {

		}
	}
}
