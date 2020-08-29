package com.sjimtv.mediaplayer;

import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.player.base.StatusApi;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MediaStatus {
    private final EmbeddedMediaPlayer mediaPlayer;
    private final StatusApi statusApi;
    private final InfoApi infoApi;

    public MediaStatus(EmbeddedMediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
        statusApi = mediaPlayer.status();
        infoApi = mediaPlayer.media().info();
    }

    public float getPosition(){
        return (float) statusApi.time() / statusApi.length();
    }

    public String getPathOfCurrentPlaying(){
        try {
            return infoApi.mrl();
        } catch (NullPointerException e){
            return "MRL Unavailable";
        }

    }

    public boolean isPlaying() {
        return statusApi.isPlaying();
    }

}
