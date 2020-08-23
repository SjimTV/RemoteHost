package com.sjimtv.control;

import com.sjimtv.App;
import com.sjimtv.mediaplayer.MediaController;
import com.sjimtv.mediaplayer.MediaStatus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import uk.co.caprica.vlcj.player.base.AudioApi;
import uk.co.caprica.vlcj.player.base.ControlsApi;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.net.URL;
import java.util.ResourceBundle;

public class ControlScene implements Initializable {
    private MediaController mediaController;
    private MediaStatus mediaStatus;


    @FXML
    private Slider seekbar, volumebar;

    private boolean isTimelineDragged = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mediaController = App.mediaController;
        mediaStatus = App.mediaStatus;


        initializeSeekbar();
        initializeSeekbarTimeline();
        initializeVolumebar();
    }

    private void initializeSeekbar(){
        seekbar.setOnMousePressed(mouseEvent -> {
            System.out.println("PRESS");
            mediaController.setPause(true);
            isTimelineDragged = true;
        });
        seekbar.setOnMouseReleased(mouseEvent -> {
            System.out.println("RELEASE");
            mediaController.setPosition((float) seekbar.getValue() / 1000);
            mediaController.setPause(false);
            isTimelineDragged = false;
        });
    }

    private void initializeSeekbarTimeline(){
        Timeline positionEvent = new Timeline(
                new KeyFrame(Duration.millis(500),
                        event -> {
                    if (mediaStatus.isPlaying())
                            positionListener(mediaStatus.getPosition());
                        })
        );
        positionEvent.setCycleCount(Timeline.INDEFINITE);
        positionEvent.play();
    }

    public void positionListener(float position){
        if (!isTimelineDragged) seekbar.setValue(position * 1000);
    }

    private void initializeVolumebar(){
        volumebar.setOnMouseDragged(mouseEvent -> {
            mediaController.setVolume((float) (volumebar.getValue() / 100));
        });
    }

    public void playPauseClicked(ActionEvent actionEvent) {
        mediaController.pause();
    }


}
