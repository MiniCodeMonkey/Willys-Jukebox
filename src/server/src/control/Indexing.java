package control;

import java.io.File;

import javax.swing.JOptionPane;

import model.AudioTrack;
import model.DataException;
import model.DuplicatedTrackException;
import model.TrackList;

/**
 * 
 * @author jeppe_kronborg
 */
public class Indexing extends Thread
{
	private long startTime;
	private int duplicates = 0;
	protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	
	// This methods allows classes to register for IndexingEvents
	public void addIndexingEventListener(IndexingEventListener listener)
	{
		listenerList.add(IndexingEventListener.class, listener);
	}
	
	// This methods allows classes to unregister for IndexingEvents
	public void removeIndexingEventListener(IndexingEventListener listener)
	{
		listenerList.remove(IndexingEventListener.class, listener);
	}
	
	// This private class is used to fire IndexingEvents
	void fireMyEvent(IndexingEvent evt)
	{
		Object[] listeners = listenerList.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2)
		{
			if (listeners[i] == IndexingEventListener.class)
			{
				((IndexingEventListener) listeners[i + 1]).indexingEventOccurred(evt);
			}
		}
	}
	
	private String searchPath;
	
	public Indexing(String filepath)
	{
		this.searchPath = filepath;
	}
	
	public void run()
	{
		startTime = System.currentTimeMillis();
		indexFolder(searchPath);
		
		long endTime = System.currentTimeMillis() - startTime;
		
		String duplicatesMessage = "";
		
		if (duplicates == 1)
		{
			duplicatesMessage = duplicates + " track was skipped because of duplication.";
		}
		else
		{
			duplicatesMessage = duplicates + " tracks was skipped because of duplication.";
		}
		
		// Fire last event with audio track set to null (end of indexing)
		IndexingEvent event = new IndexingEvent(this);
		event.setMessage("Indexing done in " + ((float)endTime / 1000.0) + " seconds\n" +
				duplicatesMessage);
		fireMyEvent(event);
	}
	
	public void indexFolder(String filepath)
	{
		// Fire indexing event as new folder is accessed
		IndexingEvent folderEvent = new IndexingEvent(this);
		folderEvent.setPath(filepath);
		fireMyEvent(folderEvent);
		
		File file = new File(filepath);
		File allFiles[] = file.listFiles();
		
		for (File currentFile : allFiles)
		{
			if (currentFile.isFile() && currentFile.getName().endsWith(".mp3") && !currentFile.getName().startsWith("."))
			{
				AudioTrack track = new AudioTrack(currentFile.getAbsolutePath(), true);
				try
				{
					TrackList.getInstance().addTrack(track);
				}
				catch (DataException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				catch (DuplicatedTrackException e)
				{
					System.out.println(e.getMessage());
					duplicates++;
				}
				
				// Fire indexing event as new track is added
				IndexingEvent event = new IndexingEvent(this);
				event.setAudioTrack(track);
				fireMyEvent(event);
			}
			else if (currentFile.isDirectory())
			{
				indexFolder(currentFile.getAbsolutePath());
			}
		}
	}
}
