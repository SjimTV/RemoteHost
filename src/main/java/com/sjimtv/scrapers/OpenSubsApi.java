package com.sjimtv.scrapers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sjimtv.showStructure.Show;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenSubsApi {

    private static final String API_URL = "https://rest.opensubtitles.org/search/";
    private static final String API_USERAGENT = "fotexsubtitles";

    private static final String API_SEARCH_EPISODE = "episode-";
    private static final String API_SEARCH_SEASON = "season-";
    private static final String API_SEARCH_LANGUAGE = "sublanguageid-";

    public static ArrayList<String> getSubtitleDownloadLinks(Show show) throws Exception {

        String imdbID = show.getImdbID();
        int season = show.getSeason();
        int episodeCount = show.getEpisodes().size();

        ArrayList<String> subtitleDownloadLinks = new ArrayList<>();

        for (int episode = 1; episode <= episodeCount; episode++){
            subtitleDownloadLinks.add(requestDownloadLink(show, episode));
            try {
                System.out.println(String.format("\nRequest download link %d/%d", episode, episodeCount));
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return subtitleDownloadLinks;
    }


    private static String requestDownloadLink (Show show, int episode) throws Exception {
        URL url = buildSearchUrl(show, episode, "eng");

        assert url != null;
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestProperty("User-Agent", API_USERAGENT);
        request.connect();

        JsonArray subtitleArray = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent())).getAsJsonArray();

        String downloadLink;
        if (subtitleArray.size() == 1)  downloadLink = getDownloadlinkFromJson(subtitleArray.get(0).getAsJsonObject());
        else downloadLink = getDownloadlinkFromJson(findBestSubtitle(subtitleArray, show.getMediaTypeFlags()));


        downloadLink = downloadLink.replace(".gz", ""); // Strip extension

        return downloadLink;
    }

    private static URL buildSearchUrl(Show show, int episode, String language) {

        String seasonNumber = Integer.toString(show.getSeason());
        String episodeNumber = Integer.toString(episode);

        StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append(API_URL);
        urlBuilder.append("imdbid-");
        urlBuilder.append(show.getImdbID());
        urlBuilder.append("/");

        if (show.getEpisodes().size() != 1){
            urlBuilder.append(API_SEARCH_EPISODE);
            urlBuilder.append(episodeNumber);
            urlBuilder.append("/");

            urlBuilder.append(API_SEARCH_SEASON);
            urlBuilder.append(seasonNumber);
            urlBuilder.append("/");
        }

        urlBuilder.append(API_SEARCH_LANGUAGE);
        urlBuilder.append(language);
        urlBuilder.append("/");

        try {
            return new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> downloadSubtitles(ArrayList<String> subtitleDownloadLinks) throws IOException {
        ArrayList<String> subtitles = new ArrayList<>();

        int counter = 1;
        for (String subtitleDownloadLink : subtitleDownloadLinks){
            subtitles.add(downloadSubtitle(new URL(subtitleDownloadLink)));
            try {
                System.out.println(String.format("Downloaded subtitle %d/%d", counter++, subtitleDownloadLinks.size()));
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return subtitles;
    }

    private static String downloadSubtitle(URL url) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                url.openStream()));

        StringBuilder subtitle = new StringBuilder();
        String buffer;

        while ((buffer = bufferedReader.readLine()) != null) {
            subtitle.append(buffer);
            subtitle.append(System.lineSeparator());
        }

        return subtitle.toString();

    }

    private static JsonObject findBestSubtitle(JsonArray jsonArray, String[] mediaTypeFlag) throws Exception {

        if (jsonArray.isJsonNull()) throw new Exception("No Subtitles found!");

        if (mediaTypeFlag.length != 0){
            JsonArray filteredJsonArray = filterJsonByIdentifiers(jsonArray, mediaTypeFlag);
            System.out.println(filteredJsonArray.get(0).getAsJsonObject().get("MovieReleaseName").getAsString());
            return filteredJsonArray.get(0).getAsJsonObject();


        }
        // No flag returns first search result
        System.out.println("Did not found flag returning first search result..");
        return jsonArray.get(0).getAsJsonObject();

    }

    private static JsonArray filterJsonByIdentifiers(JsonArray jsonArray, String[] identifiers){
        for (String identifier : identifiers) {
            jsonArray = filterJsonByIdentifier(jsonArray, identifier);

        }
        return jsonArray;
    }

    private static JsonArray filterJsonByIdentifier(JsonArray jsonArray, String identifier){

        JsonArray filteredJsonArray = new JsonArray();
        Pattern pattern =  Pattern.compile(identifier, Pattern.CASE_INSENSITIVE);

        for (JsonElement subtitleJson : jsonArray){
            Matcher matcher = pattern.matcher(subtitleJson.getAsJsonObject().get("MovieReleaseName").getAsString());
            if (matcher.find()){
                filteredJsonArray.add(subtitleJson);
            }
        }
        if (filteredJsonArray.size() == 0) return jsonArray;
        System.out.println(String.format("Filtered %d subtitles with flag : %s", filteredJsonArray.size(), identifier));
        return filteredJsonArray;
    }

    private static String getDownloadlinkFromJson(JsonObject subtitleJson){
        return subtitleJson.get("SubDownloadLink").getAsString();
    }
}
