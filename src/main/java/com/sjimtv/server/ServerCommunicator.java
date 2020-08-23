package com.sjimtv.server;

import com.sjimtv.App;
import com.sjimtv.filemanager.ShowFactory;
import com.sjimtv.mediaplayer.MediaController;
import com.sjimtv.showStructure.Shows;
import com.sjimtv.utils.JsonConverter;


public class ServerCommunicator {
    private final static String CONTROL_TAG = "CONTROL:";
    private final static String FILE_TAG = "FILE:";
    private final static String[] MESSAGE_TAGS = new String[] {CONTROL_TAG, FILE_TAG};

    private final static String CMD_PLAY = "PLAY";
    private final static String CMD_PAUSE = "PAUSE";
    private final static String CMD_STOP = "STOP";

    private final static String PULL_ALL = "PULL_ALL";
    private final static String PUSH_ALL = "PUSH_ALL";

    private final MediaController mediaController;



    public ServerCommunicator(){
        mediaController = App.mediaController;

    }

    public void getMessage(String message){
        redirectMessage(message);
    }

    private void redirectMessage(String message){
        String messageTag = "";
        String command = "";
        for (String TAG : MESSAGE_TAGS){
            if (message.startsWith(TAG)) {
                messageTag = TAG;
                command = removeTag(message, messageTag);
                break;
            }

        }

        switch (messageTag){
            case CONTROL_TAG: controlMessage(command);
            break;
            case FILE_TAG: fileMessage(command);
            break;
            default: System.out.println("Received message with an invalid Tag.\n" + message);
        }
    }

    private String removeTag(String message, String tag){
        return message.replaceFirst(tag, "");
    }

    private void controlMessage(String command) {
        switch (command){
            case CMD_PLAY: mediaController.setPause(false);
            break;

            case CMD_PAUSE: mediaController.setPause(true);
            break;

            case CMD_STOP: mediaController.stop();
            break;

            default: System.out.println("Received CONTROL message with an invalid Command\n" + command);
        }
    }

    private void fileMessage(String command) {
        switch (command) {
            case PULL_ALL: pushAllShows();
            break;

            default: System.out.println("Received FILE message with an invalid Command\n" + command);
        }
    }

    private void pushAllShows(){
        System.out.println("Pushing all shows out.");
        Shows allShows = ShowFactory.pullShows("C:\\Users\\sjim_\\Documents\\Series");
        String message = FILE_TAG + PUSH_ALL + JsonConverter.convertShowsToJson(allShows);

        App.server.sendMessage(message);
    }
}
