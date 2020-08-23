package com.sjimtv.server;

import com.sjimtv.App;
import com.sjimtv.mediaplayer.MediaController;
import com.sjimtv.mediaplayer.MediaManager;

public class MessageListener {
    private final MediaController mediaController;

    public MessageListener(){
        mediaController = App.mediaController;
    }

    public void getMessage(String message){
        System.out.println(message);
        mediaController.displayMessage(message);
    }
}
