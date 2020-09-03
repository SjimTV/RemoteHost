package com.sjimtv.mediaplayer;

public class Status {

    private boolean newMedia;
    private int showIndex;
    private int episodeNumber;
    private boolean isPaused;

    public Status(){

    }

    public static Status initialize(){
        Status status = new Status();
        status.showIndex = -1;
        status.episodeNumber = -1;
        status.isPaused = true;
        return status;
    }

    public void setNewMedia(boolean newMedia) {
        this.newMedia = newMedia;
    }

    public boolean isNewMedia() {
        return newMedia;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

}
