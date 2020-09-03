package com.sjimtv.mediaplayer;

import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.player.base.AudioApi;
import uk.co.caprica.vlcj.player.base.StatusApi;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MediaStatus {
    private final EmbeddedMediaPlayer mediaPlayer;
    private final StatusApi statusApi;
    private final InfoApi infoApi;
    private final AudioApi audioApi;

    private Status status;

    public MediaStatus(EmbeddedMediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
        statusApi = mediaPlayer.status();
        infoApi = mediaPlayer.media().info();
        audioApi = mediaPlayer.audio();

        status = new Status();
    }

    public Status getStatus(){
        status.setIsPaused(!statusApi.isPlaying());
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public float getPosition(){
        return (float) statusApi.time() / statusApi.length();
    }

    public boolean isPlaying() {
        return statusApi.isPlaying();
    }

    public float getVolume(){
        return (float) mediaPlayer.audio().volume() / 100;

    }

}
