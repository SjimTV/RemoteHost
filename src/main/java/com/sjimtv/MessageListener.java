package com.sjimtv;

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
