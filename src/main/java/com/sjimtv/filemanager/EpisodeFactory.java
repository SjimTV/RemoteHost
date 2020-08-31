package com.sjimtv.filemanager;

import com.sjimtv.App;
import com.sjimtv.scrapers.IMDBScraper;
import com.sjimtv.showStructure.Episode;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.io.File;
import java.io.IOException;

public class EpisodeFactory {

    static Episode makeEpisode(File episodeFile, String name) {
        String path = episodeFile.getAbsolutePath();
        double duration = tryGetDuration(episodeFile);

        return new Episode(path, name, duration);
    }


    private static double tryGetDuration(File episodeFile) {
        try{
            return getDuration(episodeFile);
        } catch (IOException e){
            e.printStackTrace();
            return 0;
        }
    }

    private static double getDuration(File episodeFile) throws IOException {
        FFprobe ffprobe = new FFprobe(App.class.getResource("ffprobe.exe").getPath());
        FFmpegProbeResult probeResult = ffprobe.probe(episodeFile.getAbsolutePath());

        return probeResult.getFormat().duration;
    }
}
