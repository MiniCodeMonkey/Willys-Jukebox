package lastFM;

import java.util.ArrayList;
import java.util.Collection;

import model.AudioTrack;
import net.roarsoftware.lastfm.Artist;
import net.roarsoftware.lastfm.Track;
import net.roarsoftware.lastfm.User;

/**
 *
 * @author jeppe_kronborg
 */
public class LastFM {

    String key = "";
    String user = "";

    ArrayList<AudioTrack> topTracks = new ArrayList<AudioTrack>();
    ArrayList<AudioTrack> topArtists = new ArrayList<AudioTrack>();

    public void scrobble(){

    }

    /**
     *
     * @return a list with top tracks
     */
    public ArrayList<AudioTrack> getTopTracks(){


       Collection<Track> topTracksFromLastFM = User.getTopTracks(user, key);


       for(Track lastFMTrack : topTracksFromLastFM){
            AudioTrack track = new AudioTrack();

            track.setArtist(lastFMTrack.getArtist());
            track.setAlbum(lastFMTrack.getAlbum());
            track.setName(lastFMTrack.getName());
            track.setDuration(lastFMTrack.getDuration());
            track.setPlays(lastFMTrack.getPlaycount());

            topTracks.add(track);
       }

       return topTracks;
    }

    /**
     *
     * @return a list with top artists
     */
    public ArrayList<AudioTrack> getTopArtists(){

        Collection<Artist> topArtistsFromLastFM = User.getTopArtists(user, key);

        for(Artist lastFMArtist : topArtistsFromLastFM){
            AudioTrack track = new AudioTrack();

            track.setArtist(lastFMArtist.getName());
            track.setPlays(lastFMArtist.getPlaycount());

            topArtists.add(track);
        }
        return topArtists;
    }

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getUser(){
        return user;
    }

    public void setUser(String user){
        this.user = user;
    }

}
