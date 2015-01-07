package model;

import db.dao.interfaces.IAudioTrackDAO;

public class DuplicatedTrackException extends Exception
{
	private static final long serialVersionUID = 8344066281905773532L;

	public DuplicatedTrackException(String message)
	{
		super(message);
	}
	
	public DuplicatedTrackException(Exception e)
	{
		super(e.getMessage());
	}

	public DuplicatedTrackException(IAudioTrackDAO audioTrack)
	{
		super("Audiotrack: " + audioTrack.getName() + " - " + audioTrack.getArtist() + " does already exist in index.");
	}
}