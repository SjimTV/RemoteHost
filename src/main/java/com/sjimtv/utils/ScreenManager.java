package com.sjimtv.utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class ScreenManager {

    public static final int PRIMARY_SCREEN = 0;
    public static final int EXTERNAL_SCREEN = 1;

    public static boolean isMultipleScreens(){
        return Screen.getScreens().size() != 1;
    }

    public static Rectangle2D getScreenBounds(int screen){
        Screen externalScreen = Screen.getScreens().get(screen);
        return externalScreen.getBounds();
    }

}
