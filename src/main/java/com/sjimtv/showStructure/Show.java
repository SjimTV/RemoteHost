package com.sjimtv.showStructure;

import java.util.HashMap;

public class Show {
    private String path;
    private String name;
    private int season;

    private String showImage;
    private String imdbID;
    private String[] mediaTypeFlags;

    private Episodes episodes;

    public Show(){
        // Empty Constructor for JSON Parsing
    }

    public Show(String path, String name, int season, String showImage,String imdbID, String[] mediaTypeFlags, Episodes episodes) {
        this.path = path;
        this.name = name;
        this.season = season;

        this.showImage = showImage;
        this.imdbID = imdbID;
        this.mediaTypeFlags = mediaTypeFlags;

        this.episodes = episodes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Episodes getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Episodes episodes) {
        this.episodes = episodes;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setShowImage(String showImage) {
        this.showImage = showImage;
    }

    public String getShowImage() {
        return showImage;
    }

    public String[] getMediaTypeFlags() {
        return mediaTypeFlags;
    }

    public void setMediaTypeFlags(String[] mediaTypeFlags) {
        this.mediaTypeFlags = mediaTypeFlags;
    }
}
