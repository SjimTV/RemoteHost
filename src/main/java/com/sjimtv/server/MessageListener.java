package com.sjimtv.server;

import com.sjimtv.mediaplayer.MediaManager;

public class MessageListener {
    private final MediaManager mediaManager;

    public MessageListener(MediaManager mediaManager){
        this.mediaManager = mediaManager;
    }

    public void getMessage(String message){
        System.out.println(message);
        mediaManager.displayMessage(message);
    }
}
