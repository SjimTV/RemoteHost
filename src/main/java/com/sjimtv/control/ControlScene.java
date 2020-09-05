package com.sjimtv.control;

import com.sjimtv.App;
import com.sjimtv.mediaplayer.MediaController;
import com.sjimtv.mediaplayer.MediaStatus;
import com.sjimtv.showStructure.Episode;
import com.sjimtv.showStructure.Episodes;
import com.sjimtv.showStructure.Show;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ControlScene implements Initializable {

    private MediaController mediaController;
    private MediaStatus mediaStatus;


    @FXML
    private Slider seekbar, volumebar;

    private boolean isTimelineDragged = false;

    @FXML
    private ListView<Show> showList, episodeList;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mediaController = App.mediaController;
        mediaStatus = App.mediaStatus;

        initializeSeekbar();
        initializeSeekbarTimeline();
        initializeVolumebar();
        initializeShowList();

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

    private void initializeShowList(){
        ObservableList<Show> showObservableList = FXCollections.observableArrayList(App.shows);

        showList.setItems(showObservableList);
        showList.setCellFactory(param -> new ListCell<Show>() {
            @Override
            protected void updateItem(Show show, boolean empty) {
                super.updateItem(show, empty);

                if (empty || show == null || show.getName() == null) {
                    setText(null);
                } else {
                    setText(show.getName());
                }
            }
        });


    }

    public void playPauseClicked(ActionEvent actionEvent) {
        mediaController.pause();
    }


}
