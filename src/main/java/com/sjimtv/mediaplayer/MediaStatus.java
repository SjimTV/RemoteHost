package com.sjimtv.mediaplayer;

import com.sjimtv.App;
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

    private boolean isPositionSubscribed;

    public MediaStatus(EmbeddedMediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
        statusApi = mediaPlayer.status();
        infoApi = mediaPlayer.media().info();
        audioApi = mediaPlayer.audio();

        status = Status.initialize();
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

    public void subscribePositionListener(){
        if (isPositionSubscribed) return;
        isPositionSubscribed = true;
        System.out.println("Subscribed position listener");
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (isPositionSubscribed) {
                        App.server.sendMessage("POS:" + getPosition());
                        Thread.sleep(200);
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void unsubscribePositionListener(){
        System.out.println("Unsubscribed position listener");
        isPositionSubscribed = false;
    }

}
