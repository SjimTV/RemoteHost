package com.sjimtv.control;

import com.sjimtv.control.ControlScene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import uk.co.caprica.vlcj.player.base.ControlsApi;
import uk.co.caprica.vlcj.player.base.StatusApi;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class TimelineManager {

    private final ControlsApi mediaController;
    private final StatusApi mediaStatus;

    public TimelineManager(EmbeddedMediaPlayer mediaPlayer) {
        mediaController = mediaPlayer.controls();
        mediaStatus = mediaPlayer.status();

    }

    public void subscribeOnPosition(ControlScene controlScene) {
        Timeline positionEvent = new Timeline(
                new KeyFrame(Duration.millis(500),
                        event -> {
                            controlScene.positionListener(getPosition());
                        })
        );
        positionEvent.setCycleCount(Timeline.INDEFINITE);
        positionEvent.play();
    }

    public void setPosition(float floatingPoint) {
        mediaController.setPosition(floatingPoint);
    }

    public float getPosition() {
        return (float) mediaStatus.time() / mediaStatus.length();
    }
}
