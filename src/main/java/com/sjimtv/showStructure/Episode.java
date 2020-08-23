package com.sjimtv.showStructure;


public class Episode {
    private String path;
    private String name;

    public Episode(){
        // Empty Constructor for JSON Parsing
    }

    public Episode(String path, String name) {
        this.path = path;
        this.name = name;
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
}
