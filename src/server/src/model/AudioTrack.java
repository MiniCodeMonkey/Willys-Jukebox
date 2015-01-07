package model;

import java.io.File;

import javax.swing.ImageIcon;

import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import db.dal.DALException;
import db.dal.impl.AudioTracks;

public class AudioTrack
{
	private String name;
	private String artist;
	private String album;
	private String filename;
	private long duration = -1;
	private int track_number;
	private int plays;
	private int lastPlayed = -1;
	private int trackId = -1;
	
	public AudioTrack()
	{
		
	}
	
	public AudioTrack(String filename, boolean shouldParse)
	{
		this.filename = filename;
		
		if (shouldParse)
			parseTags();
	}
	
	public ImageIcon getAlbumCoverImage()
	{
		AudioTracks audioTracks = new AudioTracks();
		try
		{
			return audioTracks.getAlbumCover(album, filename);
		}
		catch (DALException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void parseTags()
	{
		try
		{
			File src = new File(filename);
			MusicMetadataSet src_set = new MyID3().read(src);
			MusicMetadata metadata = (MusicMetadata) src_set.getSimplified();
			
			setName(metadata.getSongTitle());
			setArtist(metadata.getArtist());
			setAlbum(metadata.getAlbum());
			
			try
			{
				setTrackNumber(Integer.parseInt(metadata.getTrackNumberFormatted()));
			}
			catch (NumberFormatException e)
			{
				setTrackNumber(-1);
			}
			
			//setDuration(metadata.getDurationSeconds().intValue());
		}
		catch (Exception e)
		{
			System.out.println("Fejl: " + e);
		}
		
		// If no name was found, use file name
		if (getName() == null || getName().length() <= 0)
		{
			// Linux/OSX/Windows
			String filename = getFilename().replace('\\', '/');
			
			String[] fileBits = filename.split("/");
			
			// Ingen sti
			if (fileBits.length <= 1)
			{
				setName(filename.substring(0, getFilename().length() - 4));
			}
			else
			{
				// Tager kun selve filnavnet
				filename = fileBits[fileBits.length - 1];
				setName(filename.substring(0, filename.length() - 4));
			}
		}
	}
	
	public void setId(int id)
	{
		this.trackId = id;
	}
	
	public int getId()
	{
		return trackId;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getArtist()
	{
		return artist;
	}
	
	public void setArtist(String artist)
	{
		this.artist = artist;
	}
	
	public String getAlbum()
	{
		return album;
	}
	
	public void setAlbum(String album)
	{
		this.album = album;
	}
	
	public long getDuration()
	{
		return duration;
	}
	
	public void setDuration(long duration)
	{
		this.duration = duration;
	}
	
	public String getFilename()
	{
		return filename;
	}
	
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	
	public int getTrackNumber()
	{
		return track_number;
	}
	
	public void setTrackNumber(int track_number)
	{
		this.track_number = track_number;
	}
	
	public int getPlays()
	{
		return plays;
	}
	
	public void setPlays(int plays)
	{
		this.plays = plays;
	}
	
	public void setLastPlayed(){
		lastPlayed = (int)System.currentTimeMillis()/1000;
	}
	
	public void setLastPlayed(int lastPlayed){
		this.lastPlayed = lastPlayed;
	}
	
	public int getLastPlayed(){
		return lastPlayed;
	}
	
	public String toString()
	{
		/*
		 * return "Track Number: " + getTrackNumber() + "\nName: " + getName() +
		 * "\nArtist: " + getArtist() + "\nAlbum: " + getAlbum() +
		 * "\nDuration: " + getDuration() + "\nFile Name: " + getFilename();
		 */

		return getName();
	}
}
