package com.sjimtv.mediaplayer;

import com.sjimtv.utils.ScreenManager;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OutputStageManager {

    private final Rectangle2D screenBounds;
    private final Rectangle2D viewPortBounds = new Rectangle2D(0, 0, 600, 400);

    private final Stage outputStage;
    private ImageView outputView;

    private final boolean isFullScreenMode;



    public OutputStageManager() {
        isFullScreenMode = ScreenManager.isMultipleScreens();

        screenBounds = calculateScreenBounds();
        outputStage = createOutputStage();
    }

    private Rectangle2D calculateScreenBounds(){
        if (isFullScreenMode) return ScreenManager.getScreenBounds(ScreenManager.EXTERNAL_SCREEN);
        else return ScreenManager.getScreenBounds(ScreenManager.PRIMARY_SCREEN);
    }

    private Stage createOutputStage(){
        Stage outputStage = new Stage();
        Scene outputScene = createOutputScene();

        moveStageToRightScreen(outputStage);
        if (isFullScreenMode) setStageFullScreen(outputStage);
        else setStageAsViewPort(outputStage);

        outputStage.setScene(outputScene);

        return outputStage;
    }

    private Scene createOutputScene() {
        outputView = createImageView();
        VBox outputContainer = createImageContainer(outputView);

        return new Scene(outputContainer);
    }

    private VBox createImageContainer(ImageView imageView) {
        VBox imageContainer = new VBox(imageView);
        imageContainer.setStyle("-fx-background-color: #000000;");
        imageContainer.setAlignment(Pos.CENTER);

        return imageContainer;
    }

    private ImageView createImageView() {
        ImageView fixedImageView = new ImageView();
        fixedImageView.setPreserveRatio(true);
        if (isFullScreenMode) fixedImageView.setFitWidth(screenBounds.getWidth());
        else fixedImageView.setFitWidth(viewPortBounds.getWidth());

        return fixedImageView;
    }

    private void moveStageToRightScreen(Stage stage){
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
    }

    private void setStageFullScreen(Stage stage){
        stage.setMaximized(true);
        stage.initStyle(StageStyle.UNDECORATED);
    }

    private void setStageAsViewPort(Stage stage) {
        stage.initStyle(StageStyle.DECORATED);
        stage.setHeight(viewPortBounds.getHeight());
        stage.setWidth(viewPortBounds.getWidth());
    }



    public Stage getOutputStage() {
        return outputStage;
    }

    public ImageView getOutputView() {
        return outputView;
    }
}
