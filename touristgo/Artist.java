package com.example.user.touristgo;

/**
 * Created by sirinebaba on 3/16/17.
 */

public class Artist {
    String artistId;
    String artistName;
    String artistGenre;

    public Artist() {

    }

    public Artist(String artistId, String artistGenre, String artistName) {
        this.artistId = artistId;
        this.artistGenre = artistGenre;
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistGenre() {
        return artistGenre;
    }
}
