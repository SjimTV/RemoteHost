package com.sjimtv.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sjimtv.showStructure.Show;
import com.sjimtv.showStructure.Shows;

public class JsonConverter {
    public static String convertShowsToJson(Shows shows){
        return new Gson().toJson(shows);
    }

    public static String convertShowToJson(Show show){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(show);
    }
}
