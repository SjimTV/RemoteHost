package com.sjimtv.filemanager;


import com.google.gson.Gson;
import com.sjimtv.showStructure.Show;
import com.sjimtv.showStructure.Shows;
import com.sjimtv.utils.JsonConverter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;


public class ShowInfoManager {

    private static final String directoryPath = File.separator + "Info";
    private static final String showInfoPath = directoryPath + File.separator + "ShowInfo.show";
    private static final String showImagePath = directoryPath + File.separator + "ShowImage.jpg";

    public static boolean showInfoExists(File showDirectory) {
        return new File(showDirectory, showInfoPath).exists();
    }


    public static void saveShowInfo(Show show) {
        String showInfo = JsonConverter.convertShowToJson(show);
        try {
            createDirectoryIfItDoesNotExist(show.getPath() + directoryPath);
            Files.write(Path.of(show.getPath() + showInfoPath), showInfo.getBytes());
            System.out.println("Saved ShowInfo for " + show.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Show loadShowFromShowInfo(File showDirectory) throws IOException {
        String showInfo = new String(FileUtils.readFileToByteArray(new File(showDirectory, showInfoPath)));
        return new Gson().fromJson(showInfo, Show.class);
    }

    public static boolean showImageExists(String showPath){
        return new File(showPath + showImagePath).exists();
    }

    public static void saveShowImage(String showPath, byte[] showImage){
        try {
            createDirectoryIfItDoesNotExist(showPath + directoryPath);
            Files.write(Path.of(showPath + showImagePath), showImage);
            System.out.println("Saved ShowImage for " + showPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadShowImage(String showPath) {
        File image = new File(showPath + showImagePath);
        System.out.println(image.getAbsolutePath());
        if (!image.exists()) return "NO_IMAGE_FOUND";

        try {
            byte[] fileContent = FileUtils.readFileToByteArray(image);
            return Base64.getEncoder().encodeToString(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
            return "NO_IMAGE_FOUND";
        }
    }

    private static void createDirectoryIfItDoesNotExist(String path) throws IOException {
        if (!new File(path).exists()){
            Files.createDirectory(Path.of(path));
        }
    }


}
