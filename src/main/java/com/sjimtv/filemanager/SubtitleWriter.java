package com.sjimtv.filemanager;

import com.sjimtv.showStructure.Show;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SubtitleWriter {


    public static void writeSubsToSRTs(ArrayList<String> dataFiles, Show show){
        int episode = 1;
        for (String dataFile : dataFiles){
            writeSubtoSRT(dataFile, show.getPath(), show.getEpisodes().get(episode++).getPath());
        }
    }

    private static void writeSubtoSRT(String dataFile, String showPath, String episodePath){
        String episodeName = new File(episodePath).getName();
        String pathDirectory = showPath + "\\Subs\\" + removeExtension(episodeName);

        File directory = new File(pathDirectory);
        if (!directory.exists()) directory.mkdirs();
        File file = new File(pathDirectory + "\\English.srt");

        try {
            PrintWriter writer = new PrintWriter(file.getAbsoluteFile(), StandardCharsets.UTF_8);
            writer.print(dataFile);
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static String removeExtension(String filename){
        return filename.substring(0, filename.lastIndexOf("."));

    }
}
