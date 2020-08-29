package com.sjimtv.server;

import com.sjimtv.App;
import com.sjimtv.filemanager.ShowFactory;
import com.sjimtv.mediaplayer.MediaController;
import com.sjimtv.showStructure.Episode;
import com.sjimtv.showStructure.Shows;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestApiController {

    @GetMapping("/test")
    public String helloWorld(@RequestParam(value = "name", defaultValue = "World") String name){
        return "Hello " + name;
    }

    @GetMapping("/pause")
    public String pause() throws InterruptedException {
        App.mediaController.pause();

        Thread.sleep(200); // mediastatus call is faster than pausing/playing media
        if (App.mediaStatus.isPlaying()) return "playing";
        else return "paused";
    }

    @GetMapping("/pull_shows")
    public Shows pullShows(){
        return ShowFactory.pullShows("C:\\Users\\sjim_\\Documents\\Series");
    }

    @PostMapping("/play_episode")
    public Episode playEpisode(@RequestBody Episode episode){
        App.mediaController.playEpisode(episode);
        return episode;
    }
}
