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

    public MediaManager(ImageView outputView) {
        factory = new MediaPlayerFactory();
        embeddedMediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        embeddedMediaPlayer.videoSurface().set(videoSurfaceForImageView(outputView));

        mediaController = new MediaController(embeddedMediaPlayer);
        mediaStatus = new MediaStatus(embeddedMediaPlayer);

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
