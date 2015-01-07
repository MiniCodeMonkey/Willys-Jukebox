package db.daf.interfaces;

import java.util.List;

import model.AudioTrack;
import db.daf.DAFException;

public interface IQueueListDAF
{
	public void addTrackToQueue(AudioTrack audioTrack) throws DAFException;
	public void deleteTrackFromQueue(AudioTrack audioTrack) throws DAFException;
	public List<AudioTrack> getQueue() throws DAFException;
	public int getQueueCount() throws DAFException;
}
