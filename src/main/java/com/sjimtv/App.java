package com.sjimtv;

import com.sjimtv.mediaplayer.MediaController;
import com.sjimtv.mediaplayer.MediaManager;
import com.sjimtv.mediaplayer.MediaStatus;
import com.sjimtv.mediaplayer.OutputStageManager;
import com.sjimtv.server.MessageListener;
import com.sjimtv.server.Server;
import com.sjimtv.showStructure.Show;
import com.sjimtv.filemanager.ShowFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {


    OutputStageManager outputStageManager;

    private static MediaManager mediaManager;
    public static MediaController mediaController;
    public static MediaStatus mediaStatus;

    @Override
    public void start(Stage stage) throws IOException {
        showOutputStage();
        initializeVLCPlayer(outputStageManager.getOutputView());

        showControlStage(stage);

        Server serverThread = new Server(new MessageListener());
        serverThread.start();

        testClip();
    }

    private void showOutputStage(){
        outputStageManager = new OutputStageManager();

        Stage outputStage = outputStageManager.getOutputStage();
        outputStage.show();
    }

    private void showControlStage(Stage controlStage) throws IOException{
        Scene controlScene = new Scene(loadFXML("controlScene"));
        controlStage.setScene(controlScene);

        controlStage.show();
        controlScene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    private void initializeVLCPlayer(ImageView outputView){
        mediaManager = new MediaManager(outputView);
        mediaController = mediaManager.getMediaController();
        mediaStatus = mediaManager.getMediaStatus();
    }


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void closeWindowEvent(WindowEvent event){
        try{
            stop();
        } catch (Exception e){
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
        launch();
    }

    private void testClip(){
        //mediaController.playMedia(TestCases.testMediaClip);
        Show rickAndMorty = ShowFactory.pullShow("C:\\Users\\sjim_\\Documents\\Series\\Rick and Morty S04");
        mediaController.playEpisode(rickAndMorty.getEpisodes().get(5));
    }

}