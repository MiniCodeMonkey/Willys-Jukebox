package db.dal.interfaces;

import java.util.List;

import javax.swing.ImageIcon;

import model.DuplicatedTrackException;
import db.dal.DALException;
import db.dao.interfaces.IAudioTrackDAO;

public interface IAudioTracks
{
	public IAudioTrackDAO getAudioTrack(int id) throws DALException;
	public int createAudioTrack(IAudioTrackDAO audioTrack) throws DALException, DuplicatedTrackException;
	public void editAudioTrack(IAudioTrackDAO audioTrack) throws DALException;
	public void deleteAudioTrack(int id) throws DALException;
	public List<IAudioTrackDAO> getAudioTracks() throws DALException;
	public void deleteAudioTracks() throws DALException;
	public List<String> getAlbumsForArtistWithLetter(String letter) throws DALException;
	public List<IAudioTrackDAO> getTracksByWildcardSearch(String query) throws DALException;
	public List<IAudioTrackDAO> getTracksByNameSearch(String query, boolean exact) throws DALException;
	public List<IAudioTrackDAO> getTracksByArtistSearch(String query, boolean exact) throws DALException;
	public List<IAudioTrackDAO> getTracksByAlbumSearch(String query, boolean exact) throws DALException;
	public int getAudioTrackCount() throws DALException;
	public String getAlbumForArtistWithLetter(String letter, int index) throws DALException;
	public ImageIcon getAlbumCover(String album, String filename) throws DALException;
	public void setAlbumCover(String album, ImageIcon albumCover) throws DALException;
	public IAudioTrackDAO getRandomTrack() throws DALException;
}
