package com.sjimtv.filemanager;

import com.sjimtv.showStructure.Episode;
import com.sjimtv.showStructure.Episodes;
import com.sjimtv.showStructure.Show;
import com.sjimtv.showStructure.Shows;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowFactory {

    public static Shows pullShows(String parentDirectory) {
        File showDirectory = new File(parentDirectory);
        if (!showDirectory.exists() || !showDirectory.isDirectory()) throw new Error("Parent directory does not exist");

        return listShowFiles(showDirectory);


    }

    private static Shows listShowFiles(File directory) {
        File[] directoryContent = directory.listFiles();
        Shows shows = new Shows();

        if (directoryContent == null) return shows;
        for (File file : directoryContent) {
            if (file.isDirectory()) {
                try {
                    shows.add(pullShow(file.getAbsolutePath()));
                } catch (Exception e){
                    System.out.println(file.getName() + " is not a valid Show Directory.");
                }
            }
        }
        return shows;
    }


    public static Show pullShow(String pathShowDirectory) {
        File showDirectory = new File(pathShowDirectory);
        if (!showDirectory.exists() || !showDirectory.isDirectory()) throw new Error("Show directory does not exist");

        String showPath = showDirectory.getAbsolutePath();
        String showName = showDirectory.getName();
        int showSeason = getSeason(showName);
        Episodes episodes = fillEpisodes(showDirectory);

        return new Show(showPath, showName, showSeason, episodes);
    }

    private static File[] listMediaFiles(File directory) {
        File[] directoryContent = directory.listFiles();
        ArrayList<File> mediaContent = new ArrayList<>();

        if (directoryContent == null) return mediaContent.toArray(File[]::new);
        for (File file : directoryContent) {
            if (isMedia(file)) mediaContent.add(file);
        }

        return mediaContent.toArray(File[]::new);
    }

    private static boolean isMedia(File file) {
        if (file.isDirectory()) return false;

        Matcher matcher = Pattern.compile("mp4|mkv").matcher(file.getName());
        return matcher.find();
    }

    private static Episodes fillEpisodes(File showDirectory) {
        Episodes episodes = new Episodes();

        File[] mediaFiles = listMediaFiles(showDirectory);
        int episodeCount = 1;
        for (File mediaFile : mediaFiles) {
            episodes.put(episodeCount, makeEpisode(mediaFile));
            episodeCount++;
        }
        return episodes;
    }

    private static Episode makeEpisode(File episodeFile) {
        Episode episode = new Episode(episodeFile.getAbsolutePath(), episodeFile.getName());
        return episode;
    }

    private static int getSeason(String showName) {
        Matcher matcher = Pattern.compile("[S]\\d{2}").matcher(showName);
        if (!matcher.find()) return 1;
        else {
            try {
                return Integer.parseInt(matcher.group().substring(1));
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }


}
