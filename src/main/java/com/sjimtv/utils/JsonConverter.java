package com.sjimtv.utils;


import com.google.gson.Gson;
import com.sjimtv.showStructure.Show;
import com.sjimtv.showStructure.Shows;

public class JsonConverter {
    public static String convertShowsToJson(Shows shows){
        return new Gson().toJson(shows);
    }

    public static String convertShowToJson(Show show){
        return new Gson().toJson(show);
    }
}
