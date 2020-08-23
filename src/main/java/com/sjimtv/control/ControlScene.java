package com.sjimtv.control;

import com.sjimtv.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import uk.co.caprica.vlcj.player.base.AudioApi;
import uk.co.caprica.vlcj.player.base.ControlsApi;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.net.URL;
import java.util.ResourceBundle;

public class ControlScene implements Initializable {
    private EmbeddedMediaPlayer mediaPlayer;
    private ControlsApi mediaController;
    private AudioApi audioController;

    private TimelineManager timelineManager;

    @FXML
    private Slider seekbar, volumebar;

    private boolean isTimelineDragged = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mediaPlayer = App.mediaManager.getEmbeddedMediaPlayer();
        mediaController = mediaPlayer.controls();
        audioController = mediaPlayer.audio();

        timelineManager = new TimelineManager(mediaPlayer);
        timelineManager.subscribeOnPosition(this);

        initializeSeekbar();
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
            timelineManager.setPosition((float) seekbar.getValue() / 1000);
            mediaController.setPause(false);
            isTimelineDragged = false;
        });
    }

    public void positionListener(float position){
        if (!isTimelineDragged) seekbar.setValue(position * 1000);
    }

    private void initializeVolumebar(){
        volumebar.setOnMouseDragged(mouseEvent -> {
            audioController.setVolume((int) volumebar.getValue());
        });
    }

    public void playPauseClicked(ActionEvent actionEvent) {
        mediaController.pause();
    }


}
