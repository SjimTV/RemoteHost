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

    private final MediaController mediaController;
    private final MediaStatus mediaStatus;

    private boolean isNewMedia = true;

    public MediaManager(ImageView outputView) {
        factory = new MediaPlayerFactory();
        embeddedMediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        embeddedMediaPlayer.videoSurface().set(videoSurfaceForImageView(outputView));

        mediaController = new MediaController(embeddedMediaPlayer);
        mediaStatus = new MediaStatus(embeddedMediaPlayer);

        setupMediaPlayingEvent();

    }


    private void setupMediaPlayingEvent() {
        embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                super.playing(mediaPlayer);
                System.out.println("Playing");
                mediaStatus.isPlaying(true);

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
        mediaController.displayMessage("Now Playing :  " + mediaStatus.getPathOfCurrentPlaying());
    }

    public MediaController getMediaController() {
        return mediaController;
    }

    public MediaStatus getMediaStatus() {
        return mediaStatus;
    }

    public void release() {
        embeddedMediaPlayer.controls().stop();
        embeddedMediaPlayer.release();
        factory.release();

    }
}
