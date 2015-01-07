package control;

import java.util.EventObject;

import model.AudioTrack;


public class IndexingEvent extends EventObject
{
	private static final long serialVersionUID = 3316568661337655416L;
	private AudioTrack track = null;
	private String path = null;
	private String message = null;
	
	public IndexingEvent(Object source)
	{
		super(source);
	}
	
	public void setAudioTrack(AudioTrack track)
	{
		this.track = track;
	}
	
	public AudioTrack getAudioTrack()
	{
		return track;
	}

	public void setPath(String filepath)
	{
		this.path = filepath;
	}
	
	public String getPath()
	{
		return this.path;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}
}
