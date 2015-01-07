package db.daf.interfaces;

import java.util.List;

import model.AudioTrack;
import db.daf.DAFException;

public interface IHistoryListDAF
{
	public void addTrackToHistory(AudioTrack audioTrack) throws DAFException;
	public List<AudioTrack> getHistory() throws DAFException;	
}
