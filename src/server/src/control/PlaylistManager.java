package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.Connector;
import model.AudioTrack;
import model.DataException;
import model.QueueList;

public class PlaylistManager
{
	private SettingsController settingsController = new SettingsController();
	private AudioTrack currentTrack;
	
	public void canEnqueue(AudioTrack track) throws PlaylistManagerException{
		
		currentTrack = track;
		
		if(Boolean.parseBoolean(settingsController.getSetting("playlistManagerEnabled"))){
			checkQueueSize();
			checkArtistThrottling();
			checkSongFrequency();
			checkArtistFrequency();
		}
	}
	
	private void checkQueueSize() throws PlaylistManagerException{
		int currentQueueSize;
		try
		{
			currentQueueSize = (QueueList.getInstance().size() + 1);
		}
		catch (DataException e)
		{
			e.printStackTrace();
			return;
		}
		
		int maxQueueSize = Integer.parseInt(settingsController.getSetting("queueSize"));
		
		if(maxQueueSize < currentQueueSize){
			throw new PlaylistManagerException("The queue is full. Please try again later");
		}
		
	}
	
	private void checkArtistThrottling() throws PlaylistManagerException{
		
		int numberOfArtistInQueue = 0;
		int maxNumberOfArtistInQueue = Integer.parseInt(settingsController.getSetting("artistThrottling"));
		
		try
		{
			for(AudioTrack track: QueueList.getInstance().getTracks())
			{
				if(track.getArtist().equalsIgnoreCase(currentTrack.getArtist()))
				{
					numberOfArtistInQueue++;
				}
			}
		}
		catch (DataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(maxNumberOfArtistInQueue <= numberOfArtistInQueue){
			String s = " ";
			if(numberOfArtistInQueue != 1)
				s = "s ";
			
			throw new PlaylistManagerException("You cannot enqueue that song. The queue already \n" +
					"contain" + s + numberOfArtistInQueue + " song" + s + " by " +  currentTrack.getArtist() + ".\n" +
			"Please try again later.");
		}
	}
	
	private void checkSongFrequency() throws PlaylistManagerException{
		if (currentTrack == null)
			return;
		
		int numberOfSecondsDefined = Integer.parseInt(settingsController.getSetting("songFrequency"))*60;
		int secondsSinceLastPlayed = currentTrack.getLastPlayed();
		int currentTime = (int)System.currentTimeMillis()/1000;
		int diff = currentTime - secondsSinceLastPlayed;
		
		
		if(diff > numberOfSecondsDefined){
			throw new PlaylistManagerException("You cannot enqueue the song because it has been \n" +
					"played within the last " + (numberOfSecondsDefined/60) + " min.\n" +
			"Please try again later.");
		}
		
	}
	
	private void checkArtistFrequency() throws PlaylistManagerException{
		if(currentTrack == null)
			return;
		
		PreparedStatement statement1, statement2;
		ResultSet rs1, rs2;
		
		try
		{
			statement1 = Connector.getConn().prepareStatement("SELECT COUNT(*) FROM history");
			rs1 = statement1.executeQuery();
			rs1.next();
			
			statement2 = Connector.getConn().prepareStatement("SELECT COUNT(*) FROM history WHERE trackid=?");
			statement2.setInt(1, currentTrack.getId());
			rs2 = statement2.executeQuery();
			rs2.next();
			
			double percentageDefined = Integer.parseInt(settingsController.getSetting("artistFrequency"));
			double historySize = rs1.getInt(1);
			double numberOfTimesInHistory = rs2.getInt(1);
			double percentageOfHistory = 0;
			
			rs1.close();
			rs2.close();
			
			if(historySize != 0){
				percentageOfHistory = (numberOfTimesInHistory/(historySize)*100.0);
				
				if(percentageOfHistory > percentageDefined)
				{
					throw new PlaylistManagerException("You cannot enqueue the song because the artist have \n" +
							"already been played " + percentageDefined + "% of the time.\n " +
					"Please try again later.");
				}
				
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}
}