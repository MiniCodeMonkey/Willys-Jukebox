package control;

import static org.jouvieje.fmodex.defines.FMOD_INITFLAGS.FMOD_INIT_NORMAL;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_HARDWARE;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_LOOP_OFF;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_OPENMEMORY;
import static org.jouvieje.fmodex.defines.FMOD_TIMEUNIT.FMOD_TIMEUNIT_MS;
import static org.jouvieje.fmodex.defines.VERSIONS.FMOD_VERSION;
import static org.jouvieje.fmodex.defines.VERSIONS.NATIVEFMODEX_JAR_VERSION;
import static org.jouvieje.fmodex.defines.VERSIONS.NATIVEFMODEX_LIBRARY_VERSION;
import static org.jouvieje.fmodex.enumerations.FMOD_CHANNELINDEX.FMOD_CHANNEL_FREE;
import static org.jouvieje.fmodex.enumerations.FMOD_RESULT.FMOD_ERR_CHANNEL_STOLEN;
import static org.jouvieje.fmodex.enumerations.FMOD_RESULT.FMOD_ERR_INVALID_HANDLE;
import static org.jouvieje.fmodex.enumerations.FMOD_RESULT.FMOD_OK;
import static org.jouvieje.fmodex.utils.BufferUtils.newByteBuffer;
import static org.jouvieje.fmodex.utils.BufferUtils.newFloatBuffer;
import static org.jouvieje.fmodex.utils.BufferUtils.SIZEOF_INT;
import static org.jouvieje.fmodex.utils.BufferUtils.newIntBuffer;
import static org.jouvieje.fmodex.defines.FMOD_TIMEUNIT.FMOD_TIMEUNIT_MODORDER;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_2D;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_SOFTWARE;

import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.TimerTask;

import model.AudioTrack;
import model.DataException;
import model.QueueList;
import model.SettingsData;
import model.TrackList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import java.util.Timer;

import org.jouvieje.fmodex.Channel;
import org.jouvieje.fmodex.FmodEx;
import org.jouvieje.fmodex.Init;
import org.jouvieje.fmodex.Sound;

import org.jouvieje.fmodex.enumerations.FMOD_RESULT;
import org.jouvieje.fmodex.exceptions.InitException;
import org.jouvieje.fmodex.structures.FMOD_CREATESOUNDEXINFO;
import org.jouvieje.fmodex.utils.BufferUtils;

import db.dao.interfaces.IAudioTrackDAO;

import view.ServerView;

/**
 * 
 * @author Mini
 */
@SuppressWarnings("unused")
public class AudioManager
{
	private AudioTrack currentTrack = null;
	private Random random = new Random();
	private ServerView serverView;

	private org.jouvieje.fmodex.System system = new org.jouvieje.fmodex.System();
	private Sound music1 = new Sound();
	//private Sound music2 = new Sound();
	private Channel channel1 = new Channel();
	//private Channel channel2 = new Channel();
	private int currentMainChannel = 1;
	private IntBuffer buffer = null;
	private Timer songEndTimer = new Timer();
	//private Timer crossFadeTimer = new Timer();
	private TimerTask timerTask = null;
	//private TimerTask crossFadeTimerTask = null;
	//private int crossFadeSeconds = 0;
	//private float crossFadeVolume = 0.0f;
	
	public AudioManager(ServerView serverView)
	{
		this.serverView = serverView;
		
		initFmod();
	}
	
	public void setVolume(float vol)
	{
		if (channel1 != null)
			channel1.setVolume(vol);
	}
	
	public float getVolume()
	{
		if (channel1.isNull())
			return -1;
		
		FloatBuffer buffer = newFloatBuffer(1);
		channel1.getVolume(buffer);
		
		return buffer.get(0);
	}
	
	private Channel getCurrentMainChannel()
	{
		/*if ((currentMainChannel == 1 && channel1 == null) || (currentMainChannel == 2 && channel2 == null))
			switchCurrentMainChannel();
			
		if (currentMainChannel == 1)*/
			return channel1;
		/*else
			return channel2;*/
	}
	
	/*private Channel getCurrentSecondaryChannel()
	{
		if (currentMainChannel == 1)
			return channel2;
		else
			return channel1;
	}
	
	private void resetCurrentMainChannel()
	{
		if (currentMainChannel == 1)
			channel1 = new Channel();
		else
			channel2 = new Channel();
	}
	
	private void resetCurrentSecondaryChannel()
	{
		if (currentMainChannel == 1)
			channel2 = new Channel();
		else
			channel1 = new Channel();
	}*/
	
	private Sound getCurrentMainMusic()
	{
		//if (currentMainChannel == 1)
			return music1;
		//else
		//	return music2;
	}
	
	/*private Sound getCurrentSecondaryMusic()
	{
		if (currentMainChannel == 1)
			return music2;
		else
			return music1;
	}
	
	private void switchCurrentMainChannel()
	{
		currentMainChannel = (currentMainChannel == 1) ? 2 : 1;
	}*/
	
	public void initFmod()
	{
		System.out.println(System.getProperty("java.library.path"));
		try
		{
			Init.loadLibraries();
		}
		catch (InitException e)
		{
			System.out.println("NativeFmodEx error! " + e.getMessage());
			return;
		}
		
		if (NATIVEFMODEX_LIBRARY_VERSION != NATIVEFMODEX_JAR_VERSION)
		{
			System.out.println("Error!  NativeFmodEx library version (%08x) is different to jar version (%08x)" +
					NATIVEFMODEX_LIBRARY_VERSION + NATIVEFMODEX_JAR_VERSION);
			return;
		}
		
		FMOD_RESULT result;
		int version;

		/* Initialise FMOD */
		result = FmodEx.System_Create(system);
		errorCheck(result);

		buffer = newIntBuffer(1);
		result = system.getVersion(buffer);
		errorCheck(result);
		version = buffer.get(0);

		if(version < FMOD_VERSION) {
			System.out.println("Incorrect dll version");
		}

		/* Initialize FMOD. */
		result = system.init(32, FMOD_INIT_NORMAL, null);
		errorCheck(result);
		if(result != FMOD_OK) {
			System.out.println("FMOD could be init'd");
		}
	}
	
	public long getElapsedTime()
	{
		return getElapsedTime(false);
	}
	
	public long getElapsedTime(boolean ms)
	{
		if (buffer == null || getCurrentMainChannel().isNull())
			return -1;
		
		FMOD_RESULT result = getCurrentMainChannel().getPosition(buffer, FMOD_TIMEUNIT_MS);
		return (ms ? buffer.get(0) : buffer.get(0) / 1000); // Return elapsed time in seconds intead of ms
	}
	
	public long getDuration()
	{
		return getDuration(false);
	}
	
	public long getDuration(boolean ms)
	{
		if (buffer == null || getCurrentMainChannel().isNull() || getCurrentMainMusic().isNull())
			return -1;
		
		FMOD_RESULT result = getCurrentMainMusic().getLength(buffer, FMOD_TIMEUNIT_MS);
		return (ms ? buffer.get(0) : buffer.get(0) / 1000); // Return duration in seconds instead of ms
	}
	
	public void close()
	{
		
	}
	
	private void errorCheck(FMOD_RESULT result)
	{
		if(result != FMOD_OK) {
			String errstring = String.format("FMOD error! (%d): %s", result.asInt(), FmodEx.FMOD_ErrorString(result));
			System.out.println(errstring);
			//JOptionPane.showMessageDialog(this, errstring, "FMOD error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void play(boolean withCrossFading)
	{	
		if (currentTrack == null)
		{
			next(withCrossFading); // Find track first
			return;
		}
		
		// Check if file exists
		File file = new File(currentTrack.getFilename());
		
		if (!file.exists())
		{
			System.out.println("File does not exist for track! " + currentTrack.getName() + " - " + currentTrack.getArtist());
			next(withCrossFading); // Find a new track
			return;
		}
		
		// Add the track to the history list
		try
		{
			QueueList.getInstance().addToHistory(currentTrack);
		}
		catch (DataException e)
		{
			e.printStackTrace();
		}
		
		/*if (crossFadeSeconds > 0 && this.getRemainingTime() >= crossFadeSeconds && withCrossFading)
		{
			// Create stream in secondary channel
			FMOD_RESULT result;
			if(!getCurrentSecondaryMusic().isNull()) {
				if(!getCurrentSecondaryChannel().isNull()) {
					getCurrentSecondaryChannel().stop();
					resetCurrentSecondaryChannel();
				}
				getCurrentSecondaryMusic().release();
			}
			result = system.createStream(currentTrack.getFilename(), FMOD_2D | FMOD_SOFTWARE, null, getCurrentSecondaryMusic());
			errorCheck(result);
			
			// Play/Stop
			boolean isplaying = false;
	
			if(!getCurrentSecondaryChannel().isNull()) {
				ByteBuffer buffer = newByteBuffer(1);
				getCurrentSecondaryChannel().isPlaying(buffer);
				isplaying = buffer.get(0) != 0;
			}
	
			if(isplaying || getCurrentSecondaryMusic().isNull()) {
				if(!getCurrentSecondaryChannel().isNull()) {
					getCurrentSecondaryChannel().stop();
					resetCurrentSecondaryChannel();
				}
			}
			else {
				crossFadeVolume = 0.0f;
				system.playSound(FMOD_CHANNEL_FREE, getCurrentSecondaryMusic(), false, getCurrentSecondaryChannel());
				getCurrentSecondaryChannel().setVolume(crossFadeVolume);
			}
			
			if (crossFadeTimerTask != null)
				crossFadeTimerTask.cancel();
			
			crossFadeTimerTask = new TimerTask()
			{
				public void run()
				{
					if (crossFadeVolume >= 1.0f)
					{
						System.out.println("Cross fading done.");
						crossFadeTimerTask.cancel();
					}
					else
					{
						crossFadeVolume += 0.05f;
						System.out.println("Channel 1: " + (1.0f - crossFadeVolume) + " Channel 2: " + crossFadeVolume);
						getCurrentSecondaryChannel().setVolume(1.0f - crossFadeVolume);
						getCurrentMainChannel().setVolume(crossFadeVolume);
					}
				}
			};
			
			int rate = (int) ((crossFadeSeconds * 1000.0f) / 20.0f);
			System.out.println("Scheduling at rate " + rate);
			crossFadeTimer.scheduleAtFixedRate(crossFadeTimerTask, 0, rate);
			
			switchCurrentMainChannel();
			createSongEndTimer();
		}
		else*/
		{	
			// Create stream
			FMOD_RESULT result;
			if(!music1.isNull()) {
				if(!getCurrentMainChannel().isNull()) {
					getCurrentMainChannel().stop();
					//resetCurrentMainChannel();
				}
				music1.release();
			}
			result = system.createStream(currentTrack.getFilename(), FMOD_2D | FMOD_SOFTWARE, null, music1);
			errorCheck(result);
			
			// Play/Stop
			boolean isplaying = false;
	
			if(!getCurrentMainChannel().isNull()) {
				ByteBuffer buffer = newByteBuffer(1);
				getCurrentMainChannel().isPlaying(buffer);
				isplaying = buffer.get(0) != 0;
			}
	
			if(isplaying || music1.isNull()) {
				if(!getCurrentMainChannel().isNull()) {
					getCurrentMainChannel().stop();
					//resetCurrentMainChannel();
				}
			}
			else {
				system.playSound(FMOD_CHANNEL_FREE, music1, false, getCurrentMainChannel());
			}
			
			// Song end timer
			createSongEndTimer();
		}
	}
	
	private long getRemainingTime()
	{
		return this.getDuration() - this.getElapsedTime();
	}

	private AudioTrack pickRandomTrack()
	{
		try
		{
			if (TrackList.getInstance().count() <= 0)
				return null;
		}
		catch (DataException e2)
		{
			return null;
		}
		
		PlaylistManager playlistManager = new PlaylistManager();
		
		AudioTrack audioTrack = null;
		boolean canNotBePlayed = true;
		
		while (canNotBePlayed)
		{
			canNotBePlayed = false;
			
			try
			{
				IAudioTrackDAO audioTrackDAO = TrackList.getInstance().getRandomTrack();
				
				audioTrack = new AudioTrack(audioTrackDAO.getFilename(), false);
				audioTrack.setId(audioTrackDAO.getId());
				audioTrack.setName(audioTrackDAO.getName());
				audioTrack.setArtist(audioTrackDAO.getArtist());
				audioTrack.setAlbum(audioTrackDAO.getAlbum());
				audioTrack.setLastPlayed(audioTrackDAO.getLastPlayed());
			}
			catch (DataException e1)
			{
				System.out.println("Skipping playing " + (audioTrack.getName() == null ? "?" : audioTrack.getName()) + " because of: " + e1.getMessage());
				canNotBePlayed = true;
			}
			
			try
			{
				playlistManager.canEnqueue(audioTrack);
			}
			catch (PlaylistManagerException e)
			{
				System.out.println("Skipping playing " + audioTrack.getName() + " because of: " + e.getMessage());
				canNotBePlayed = true;
			}
		}
		
		return audioTrack;
	}
	
	public void next(boolean withCrossFading)
	{
		try
		{
			if (QueueList.getInstance().size() <= 0)
			{
				if ((currentTrack = pickRandomTrack()) == null)
				{
					// No tracks available
					return;
				}
			}
			else
			{
				try
				{
					currentTrack = QueueList.getInstance().popTrack();
				}
				catch (DataException e)
				{
					if (currentTrack == null)
					{
						next(withCrossFading);
						return;
					}
				}
			}
		}
		catch (DataException e)
		{
			e.printStackTrace();
			return;
		}
		
		currentTrack.setLastPlayed();
		play(withCrossFading);
		
		serverView.updateCurrentPlayingInfo();
		serverView.updateQueueList();
	}
	
	public boolean isPlaying()
	{
		return (currentTrack != null);
	}
	
	public AudioTrack getCurrentTrack()
	{
		return currentTrack;
	}

	public void setPosition(int position)
	{
		if (buffer != null && !getCurrentMainChannel().isNull() && !music1.isNull())
		{
			// position is in seconds multiply by 1000 to get ms
			getCurrentMainChannel().setPosition(position * 1000, FMOD_TIMEUNIT_MS);
			
			// Update timer
			createSongEndTimer();
			
			// Send update to all clients
			try
			{
				serverView.server.sendNowPlayingUpdate();
			}
			catch (Exception e)
			{
				// Do nothing if now playing update failed
			}
		}
	}
	
	private void createSongEndTimer()
	{
		if (timerTask != null)
			timerTask.cancel();
		
		timerTask = new TimerTask()
		{
			public void run()
			{
				try
				{
					try
					{
						next(true);
					}
					catch (Exception e)
					{
						System.out.println(e.getMessage());
					}
				}
				catch (ConcurrentModificationException e)
				{
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e1)
					{
						
					}
					
					run(); // Try again
				}
			}
		};
		
		/*if (Boolean.parseBoolean(SettingsData.getInstance().get("crossFadingEnabled")))
		{
			crossFadeSeconds = Integer.parseInt(SettingsData.getInstance().get("crossFadeSeconds"));
			
			if (crossFadeSeconds > 0 && getRemainingTime() > crossFadeSeconds)
			{
				songEndTimer.schedule(timerTask, (getDuration() - getElapsedTime() - crossFadeSeconds) * 1000);
				return;
			}
		}
		else
		{
			crossFadeSeconds = 0;
		}*/
		
		songEndTimer.schedule(timerTask, (getDuration() - getElapsedTime()) * 1000);
	}

	public void pause()
	{
		if (this.isPaused())
		{
			getCurrentMainChannel().setVolume(1.0f);
			getCurrentMainChannel().setPaused(false);
			createSongEndTimer();
		}
		else
		{
			//if (crossFadeTimerTask != null)
			//	crossFadeTimerTask.cancel();
			
			if (timerTask != null)
				timerTask.cancel(); // Cancel end timer task
			
			getCurrentMainChannel().setPaused(true);
			
			/*if (getCurrentSecondaryChannel() != null)
			{
				try
				{
					getCurrentSecondaryChannel().setPaused(true);
				} catch (NullPointerException e)
				{
					System.out.println("Secondary channel was NULL!");
				}
			}*/
		}
		
		// Send update to all clients
		try
		{
			serverView.server.sendNowPlayingUpdate();
		}
		catch (Exception e)
		{
			// Do nothing if now playing update failed
		}
	}

	public boolean isPaused()
	{
		ByteBuffer buffer = newByteBuffer(1);	
		getCurrentMainChannel().getPaused(buffer);
		
		return (buffer.get(0) == 1);
	}
}
