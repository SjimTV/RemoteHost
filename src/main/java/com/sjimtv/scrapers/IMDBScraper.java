package com.sjimtv.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMDBScraper {
    public static final String IMDB_NOT_FOUND = "IMDB_NOT_FOUND";
    public static final byte[] POSTER_NOT_FOUND = new byte[0];
    public static final String[] TITLES_NOT_FOUND = new String[] {};

    public static String getIMDBId(String show) {
        try {
            // Get IMDB ID
            Document imdbDocument = Jsoup.connect(formatIMDBUrl(show)).get();
            return extractShowIDfromIMDB(imdbDocument);
        } catch (IOException e) {
            e.printStackTrace();
            return IMDB_NOT_FOUND;
        }

    }

    private static String formatIMDBUrl(String show) {
        show = show.replace(" ", "+");
        return String.format(
                "https://www.imdb.com/find?q=%s&ref_=nv_sr_sm",
                show);
    }

    private static String extractShowIDfromIMDB(Document imdbDocument) {

        // Get chunk from first search result and find ID with REGEX
        String chunkTopSearchResult = imdbDocument.getElementsByClass("findResult odd").get(0).toString();
        Pattern pattern = Pattern.compile("/title/tt(.*?)/");
        Matcher matcher = pattern.matcher(chunkTopSearchResult);
        if (matcher.find()) return matcher.group(1);
        else return IMDB_NOT_FOUND;
    }

    public static byte[] getPosterFromIMDB(String imdbID) {
        String url = "https://www.imdb.com/title/tt" + imdbID;

        try {
            // Get IMDB Poster
            Document imdbDocument = Jsoup.connect(url).get();
            return extractPosterFromIMDB(imdbDocument);
        } catch (IOException e) {
            e.printStackTrace();
            return POSTER_NOT_FOUND;
        }
    }

    private static byte[] extractPosterFromIMDB(Document imdbDocument) {
        String downloadLink = extractPosterDownloadLink(imdbDocument);
        if (downloadLink.isEmpty()) return POSTER_NOT_FOUND;

        try {
            return downloadPoster(downloadLink);
        } catch (IOException e) {
            e.printStackTrace();
            return POSTER_NOT_FOUND;
        }
    }

    private static String extractPosterDownloadLink(Document imdbDocument) {
        String chunk = imdbDocument.getElementsByClass("poster").toString();
        Pattern pattern = Pattern.compile("src=\"(.*?)\"");
        Matcher matcher = pattern.matcher(chunk);
        if (matcher.find()) return matcher.group(1);
        else return "";

    }

    private static byte[] downloadPoster(String downloadLink) throws IOException {
        URL url = new URL(downloadLink);
        BufferedInputStream inputStream = new BufferedInputStream(url.openConnection().getInputStream());
        return inputStream.readAllBytes();
    }

    public static String[] getEpisodeTitles(String imdbID, int season) throws IOException {
        String url = String.format("https://www.imdb.com/title/tt%s/episodes?season=%d", imdbID, season);
        try {
            // Get IMDB Titles
            Document imdbDocument = Jsoup.connect(url).get();
            return extractTitlesFromIMDB(imdbDocument);
        } catch (IOException e) {
            e.printStackTrace();
            return TITLES_NOT_FOUND;
        }
    }

    private static String[] extractTitlesFromIMDB(Document imdbDocument) {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("empty"); // episodes start counting from 1...

        String chunk = imdbDocument.getElementsByClass("info").toString();
        boolean episodesLeft = true;
        int episode = 1;


        while (episodesLeft) {
            Pattern pattern = Pattern.compile(episode + "\" title=\"(.*?)\"");
            Matcher matcher = pattern.matcher(chunk);
            if (matcher.find()){
                System.out.println("Found title " + matcher.group(1));
                titles.add(matcher.group(1));
            }
            else episodesLeft = false;

            episode++;
        }

        return titles.toArray(String[]::new);

    }


}
