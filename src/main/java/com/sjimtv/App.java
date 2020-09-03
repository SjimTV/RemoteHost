package com.sjimtv;

import com.sjimtv.filemanager.SubtitleManager;
import com.sjimtv.mediaplayer.MediaController;
import com.sjimtv.mediaplayer.MediaManager;
import com.sjimtv.mediaplayer.MediaStatus;
import com.sjimtv.mediaplayer.OutputStageManager;
import com.sjimtv.scrapers.IMDBScraper;
import com.sjimtv.server.ServerCommunicator;
import com.sjimtv.server.Server;
import com.sjimtv.showStructure.Show;
import com.sjimtv.filemanager.ShowFactory;
import com.sjimtv.showStructure.Shows;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.util.Random;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * JavaFX App
 */

@SpringBootApplication
public class App extends Application {


    OutputStageManager outputStageManager;

    private static MediaManager mediaManager;
    public static MediaController mediaController;
    public static MediaStatus mediaStatus;
    public static Server server;

    public static Shows shows;

    @Override
    public void start(Stage stage) throws IOException {
        showOutputStage();
        initializeVLCPlayer(outputStageManager.getOutputView());
        setBackgroundClip();

        showControlStage(stage);

        server = new Server(new ServerCommunicator());
        server.start();

        initializeShows();
    }

    private void showOutputStage() {
        outputStageManager = new OutputStageManager();

        Stage outputStage = outputStageManager.getOutputStage();
        outputStage.show();
    }

    private void showControlStage(Stage controlStage) throws IOException {
        Scene controlScene = new Scene(loadFXML("controlScene"));
        controlStage.setScene(controlScene);

        controlStage.show();
        controlScene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    private void initializeVLCPlayer(ImageView outputView) {
        mediaManager = new MediaManager(outputView);
        mediaController = mediaManager.getMediaController();
        mediaStatus = mediaManager.getMediaStatus();
    }

    private void setBackgroundClip() {
        String path = "/background.mov";
        mediaController.playMedia(path);
        mediaController.setRepeat(true);
    }

    private void initializeShows() {
        shows = ShowFactory.pullShows("C:\\Users\\sjim_\\Documents\\Series");
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void closeWindowEvent(WindowEvent event) {
        try {
            stop();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        mediaManager.release();
        System.out.println("Released VLC Player \nExiting program..");
        System.exit(0);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        launch();
    }
}