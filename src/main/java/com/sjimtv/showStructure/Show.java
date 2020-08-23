package com.sjimtv.showStructure;

import java.util.HashMap;

public class Show {
    private String path;
    private String name;
    private int season;
    private Episodes episodes;

    public Show(){
        // Empty Constructor for JSON Parsing
    }

    public Show(String path, String name, int season, Episodes episodes) {
        this.path = path;
        this.name = name;
        this.season = season;
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
}
