package com.sjimtv.mediaplayer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.player.base.*;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import static uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory.videoSurfaceForImageView;


public class MediaManager {

    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer embeddedMediaPlayer;

    private final ControlsApi controlApi;
    private final AudioApi audioApi;


    private String mediaName;
    private boolean isNewMedia = true;
    private boolean isMute = false;

    public MediaManager(ImageView outputView) {
        factory = new MediaPlayerFactory();
        embeddedMediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        embeddedMediaPlayer.videoSurface().set(videoSurfaceForImageView(outputView));

        controlApi = embeddedMediaPlayer.controls();
        audioApi = embeddedMediaPlayer.audio();

        setupMediaPlayingEvent();

    }


    private void setupMediaPlayingEvent() {
        embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                super.playing(mediaPlayer);
                System.out.println("Playing");

            }
        });

        embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                super.mediaPlayerReady(mediaPlayer);
                System.out.println("Ready");
                if (isNewMedia) initializeNewMedia();
            }
        });
    }


    private void initializeNewMedia() {
        isNewMedia = false;
        System.out.println("New Media " + embeddedMediaPlayer.media().info().mrl());
        audioApi.setMute(isMute);
        displayMessage("Now Playing :  " + mediaName);

    }

    public void playTestClip(String url, String mediaName, boolean isMute) {

        this.mediaName = mediaName;
        this.isMute = isMute;

        embeddedMediaPlayer.submit(() -> embeddedMediaPlayer.media().play(url));
        embeddedMediaPlayer.controls().setRepeat(true);

    }

    public void displayMessage(String string) {
        embeddedMediaPlayer.marquee().set(Marquee.marquee()
                .position(MarqueePosition.CENTRE)
                .text(string)
                .size(60)
                .timeout(5000)
                .enable());
    }

    public EmbeddedMediaPlayer getEmbeddedMediaPlayer() {
        return embeddedMediaPlayer;
    }


    public void release() {
        embeddedMediaPlayer.controls().stop();
        embeddedMediaPlayer.release();
        factory.release();

    }
}
