package com.sjimtv.server;

import com.sjimtv.App;
import com.sjimtv.showStructure.Episode;
import com.sjimtv.showStructure.Shows;
import com.sjimtv.mediaplayer.Status;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestApiController {

    private static void waitForMediaPlayer(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e){
            System.out.println("WHO WAKES ME FROM MY SLEEP!!");
        }
    }


    @GetMapping("/pull_shows")
    public Shows pullShows(){
        return App.shows;
    }


    @PostMapping("/status")
    public Status updateStatus(@RequestBody Status updateStatus) {
        App.mediaController.updateStatus(updateStatus);
        waitForMediaPlayer();
        return App.mediaStatus.getStatus();
    }

    @GetMapping("/volume")
    public float changeVolume(@RequestParam(defaultValue = "-1f") float volume){
        if (volume != -1 && volume <= 1){
            App.mediaController.setVolume(volume);
            waitForMediaPlayer();
        }

        if (volume > 1) {
            App.mediaController.adjustVolume((int)volume);
            waitForMediaPlayer();
        }

        return App.mediaStatus.getVolume();
    }




}
