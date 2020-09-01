package com.sjimtv.showStructure;


public class Episode {
    private String path;
    private String name;

    private double duration;

    private String subtitlePath;

    public Episode(){
        // Empty Constructor for JSON Parsing
    }

    public Episode(String path, String name, double duration) {
        this.path = path;
        this.name = name;
        this.duration = duration;
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

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public String getSubtitlePath() {
        return subtitlePath;
    }

    public void setSubtitlePath(String subtitlePath) {
        this.subtitlePath = subtitlePath;
    }
}
