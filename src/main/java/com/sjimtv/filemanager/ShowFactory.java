package com.sjimtv.filemanager;

import com.sjimtv.scrapers.IMDBScraper;
import com.sjimtv.scrapers.OpenSubsApi;
import com.sjimtv.showStructure.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                    e.printStackTrace();
                    System.out.println("Something went wrong here");
                }
            }
        }
        return shows;
    }


    public static Show pullShow(String pathShowDirectory) {
        File showDirectory = new File(pathShowDirectory);
        if (!showDirectory.exists() || !showDirectory.isDirectory()) throw new Error("Show directory does not exist");

        if (ShowInfoManager.showInfoExists(showDirectory)) {
            System.out.println("ShowInfo available");
            return loadShow(showDirectory);
        } else {
            System.out.println("ShowInfo not available, making ShowInfo");
            return createShow(showDirectory);
        }

    }

    private static Show loadShow(File showDirectory) {
        try {
            return ShowInfoManager.loadShowFromShowInfo(showDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            return createShow(showDirectory);
        }
    }

    private static Show createShow(File showDirectory) {
        String showPath = showDirectory.getAbsolutePath();
        String showName = showDirectory.getName();
        int showSeason = getSeason(showName);

        String imdbID = IMDBScraper.getIMDBId(showName);

        String showImage = getShowImage(showPath, imdbID);


        File[] mediaFiles = listMediaFiles(showDirectory);
        Episodes episodes;
        if (mediaFiles.length == 1) episodes = fillMovie(mediaFiles[0], showName);
        else episodes = fillEpisodes(mediaFiles, imdbID, showSeason);

        String[] mediaTypeFlags = findMediaTypeFlags(episodes.get(1).getPath());
        Show show = new Show(showPath, showName, showSeason, showImage, imdbID, mediaTypeFlags, episodes);

        if (!subtitlesExist(show)) generateSubtitles(show);

        ShowInfoManager.saveShowInfo(show);

        return show;
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

    private static Episodes fillMovie(File mediaFile, String showName){
        Episodes episodes = new Episodes();
        episodes.put(1, EpisodeFactory.makeEpisode(mediaFile, showName));

        return episodes;
    }

    private static Episodes fillEpisodes(File[] mediaFiles, String imdbID, int season) {
        Episodes episodes = new Episodes();


        try {
            String[] episodeTitles = getEpisodeTitles(imdbID, season, mediaFiles.length);
            int episodeCount = 1;
            for (File mediaFile : mediaFiles) {
                episodes.put(episodeCount, EpisodeFactory.makeEpisode(mediaFile, episodeTitles[episodeCount]));
                episodeCount++;
            }
        } catch (ArrayIndexOutOfBoundsException e){
            String[] episodeTitles = generateStandardTitles(mediaFiles.length);
            int episodeCount = 1;
            for (File mediaFile : mediaFiles) {
                episodes.put(episodeCount, EpisodeFactory.makeEpisode(mediaFile, episodeTitles[episodeCount]));
                episodeCount++;
            }
            System.out.println("Did not found enough titles...");
        }

        return episodes;
    }

    private static String[] getEpisodeTitles(String imdbID, int season, int episodesCount) {
        try {
            String[] titles = IMDBScraper.getEpisodeTitles(imdbID, season);
            if (Arrays.equals(titles, IMDBScraper.TITLES_NOT_FOUND) || titles.length == 1) return generateStandardTitles(episodesCount);
            else return titles;
        } catch (IOException e) {
            e.printStackTrace();
            return generateStandardTitles(episodesCount);
        }
    }

    private static String[] generateStandardTitles(int episodeCount){
        ArrayList<String> standardTitles = new ArrayList<>();
        for (int i = 0; i <= episodeCount; i++){
            standardTitles.add("Episode " + i);
        }

        return standardTitles.toArray(String[]::new);
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

    private static String getShowImage(String showPath, String imdbID) {
        if (!ShowInfoManager.showImageExists(showPath)) createShowImage(showPath, imdbID);
        return ShowInfoManager.loadShowImage(showPath);
    }

    private static void createShowImage(String showPath, String imdbID) {
        System.out.printf("Get Show image for %s from IMDB \n", showPath);
        byte[] imageBytes = IMDBScraper.getPosterFromIMDB(imdbID);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ShowInfoManager.saveShowImage(showPath, imageBytes);
    }

    private static String[] findMediaTypeFlags(String episodeName) {
        ArrayList<String> mediaTypeFlags = new ArrayList<>();

        for (String mediaTypeFlag : MediaFlags.mediaTypeFlags) {
            Pattern pattern = Pattern.compile(mediaTypeFlag, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(episodeName);
            while (matcher.find()) {
                mediaTypeFlags.add(mediaTypeFlag);
            }
        }


        return mediaTypeFlags.toArray(String[]::new);
    }

    private static boolean subtitlesExist(Show show) {
        return new File(show.getPath() + File.separator + "Subs").exists();
    }

    private static void generateSubtitles(Show show) {
        try {
            System.out.println("Getting Subtitles for " + show.getName());

            ArrayList<String> subtitleDownloadLinks = OpenSubsApi.getSubtitleDownloadLinks(show);
            ArrayList<String> subtitles = OpenSubsApi.downloadSubtitles(subtitleDownloadLinks);
            SubtitleWriter.writeSubsToSRTs(subtitles, show);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No Subtitles Downloaded..");
        }

    }


}
