package com.sjimtv;

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

    private static Scene scene;
    OutputStageManager outputStageManager;

    public static VLCPlayerManager vlcPlayerManager;

    @Override
    public void start(Stage stage) throws IOException {
        showOutputStage();
        initializeVLCPlayer(outputStageManager.getOutputView());

        showControlStage(stage);

        Server serverThread = new Server();
        serverThread.start();
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
        vlcPlayerManager = new VLCPlayerManager(outputView);
        //vlcPlayerManager.playTestClip(TestCases.testMediaClip, "Media", false);
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

        vlcPlayerManager.release();
        System.out.println("Released VLC Player \nExiting program..");
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }

}