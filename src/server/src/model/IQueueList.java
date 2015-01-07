/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;

import control.PlaylistManagerException;

/**
 * 
 * @author jeppe_kronborg
 */
public interface IQueueList
{
	public void addTrackToQueue(AudioTrack track) throws PlaylistManagerException, DataException;
	
	public ArrayList<AudioTrack> getTracks() throws DataException;
	
	public int size() throws DataException;
	
	public AudioTrack popTrack() throws DataException;
}
