package com.sjimtv.server;

import com.sjimtv.App;
import com.sjimtv.showStructure.Episode;
import com.sjimtv.showStructure.Shows;
import com.sjimtv.mediaplayer.Status;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestApiController {


    @GetMapping("/pause")
    public String pause() throws InterruptedException {
        App.mediaController.pause();

        Thread.sleep(200); // mediastatus call is faster than pausing/playing media
        if (App.mediaStatus.isPlaying()) return "playing";
        else return "paused";
    }

    @GetMapping("/pull_shows")
    public Shows pullShows(){
        return App.shows;
    }

    @PostMapping("/play_episode")
    public Episode playEpisode(@RequestBody Episode episode){
        App.mediaController.playEpisode(episode);
        return episode;
    }

    @PostMapping("/status")
    public Status updateStatus(@RequestBody Status updateStatus) throws InterruptedException {
        App.mediaController.updateStatus(updateStatus);
        Thread.sleep(200);
        return App.mediaStatus.getStatus();
    }

}
