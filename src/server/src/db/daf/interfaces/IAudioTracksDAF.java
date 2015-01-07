package db.daf.interfaces;

import java.util.List;

import model.AudioTrack;
import model.DuplicatedTrackException;
import db.daf.DAFException;
import db.dao.interfaces.IAudioTrackDAO;

public interface IAudioTracksDAF
{
	public AudioTrack getAudioTrack(int id) throws DAFException;
	public void createAudioTrack(AudioTrack audioTrack) throws DAFException, DuplicatedTrackException;
	public void editAudioTrack(AudioTrack audioTrack) throws DAFException;
	public void deleteAudioTrack(int id) throws DAFException;
	public List<AudioTrack> getAudioTracks() throws DAFException;
	public void deleteAudioTracks() throws DAFException;
	public List<AudioTrack> getAudioTracksByWildcardSearch(String query) throws DAFException;
	public List<AudioTrack> getAudioTracksByNameSearch(String query) throws DAFException;
	public List<AudioTrack> getAudioTracksByArtistSearch(String query) throws DAFException;
	public List<AudioTrack> getAudioTracksByAlbumSearch(String query) throws DAFException;
	public int getAudioTrackCount() throws DAFException;
	public List<String> getAlbumsForArtistWithLetter(String letter) throws DAFException;
	public List<AudioTrack> getAlbumTracksForArtistWithLetter(String letter, int index) throws DAFException;
	public IAudioTrackDAO getRandomTrack() throws DAFException;
}
