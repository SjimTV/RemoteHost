package com.sjimtv;

import java.util.ArrayList;

public class Show {
    private final String path;
    private final String name;
    private final ArrayList<Episode> episodes;

    public Show(String path, String name, ArrayList<Episode> episodes) {
        this.path = path;
        this.name = name;
        this.episodes = episodes;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }
}
