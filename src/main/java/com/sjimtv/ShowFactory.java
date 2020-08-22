package com.sjimtv;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowFactory {

    public static Show fetchShow(File showDirectory){
        if (!showDirectory.exists() || !showDirectory.isDirectory()) throw new Error("Show directory does not exist");
        ArrayList<Episode> episodes = new ArrayList<>();
        ArrayList<File> mediaFiles = listMediaFiles(showDirectory);

        for (File mediaFile : mediaFiles){
            episodes.add(makeEpisode(mediaFile));
        }

        Show show = new Show(showDirectory.getAbsolutePath(), showDirectory.getName(), episodes);

        return show;
    }

    private static ArrayList<File> listMediaFiles(File directory){
        File[] directoryContent = directory.listFiles();
        ArrayList<File> mediaContent = new ArrayList<>();

        if (directoryContent == null) return mediaContent;
        for (File file : directoryContent){
            if (isMedia(file)) mediaContent.add(file);
        }

        return mediaContent;
    }

    private static boolean isMedia(File file){
        if (file.isDirectory()) return false;

        Matcher matcher = Pattern.compile("mp4|mkv").matcher(file.getName());
        return matcher.find();
    }

    private static Episode makeEpisode(File episodeFile){
        Episode episode = new Episode(episodeFile.getAbsolutePath(), episodeFile.getName());
        return episode;
    }


}
