package com.sjimtv.filemanager;

import com.sjimtv.showStructure.Show;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SubtitleManager {


    public static void writeSubsToSRTs(ArrayList<String> dataFiles, Show show){
        int episode = 1;
        for (String dataFile : dataFiles){
            writeSubtoSRT(dataFile, show.getPath(), show.getEpisodes().get(episode++).getPath());
        }
    }

    private static void writeSubtoSRT(String dataFile, String showPath, String episodePath){
        String subsFolderPath = showPath + File.separator + "Subs";

        File subsFolder = new File(subsFolderPath);
        if (!subsDirectoryExist(showPath)) createSubsFolder(subsFolder);

        File file = new File(getSubsPath(episodePath));
        try {
            PrintWriter writer = new PrintWriter(file.getAbsoluteFile(), StandardCharsets.UTF_8);
            writer.print(dataFile);
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void createSubsFolder(File subsFolder){
        if (!subsFolder.mkdirs()){
            System.out.println("Could not create Subs Folder...");
        }
    }

    public static String getSubsPath(String episodePath){
        String episodeName = new File(episodePath).getName();
        String showPath = episodePath.replace(episodeName, "");
        return showPath + "Subs" + File.separator + removeExtension(episodeName) + ".srt";
    }

    public static boolean subsDirectoryExist(String showPath){
       return new File(showPath + File.separator +"Subs").exists();
    }

    private static String removeExtension(String filename){
        return filename.substring(0, filename.lastIndexOf("."));
    }
}
