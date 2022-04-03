package com.sjimtv.filemanager;

import com.sjimtv.showStructure.Show;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubtitleManager {


    public static void writeSubsToSRTs(ArrayList<String> dataFiles, Show show) {
        int episode = 1;
        for (String dataFile : dataFiles) {
            writeSubtoSRT(dataFile, show.getPath(), show.getEpisodes().get(episode++).getPath());
        }
    }

    private static void writeSubtoSRT(String dataFile, String showPath, String episodePath) {
        String subsFolderPath = showPath + File.separator + "Subs";

        File subsFolder = new File(subsFolderPath);
        if (!subsDirectoryExist(showPath)) createSubsFolder(subsFolder);

        File file = new File(getSubsPath(episodePath));
        try {
            PrintWriter writer = new PrintWriter(file.getAbsoluteFile(), StandardCharsets.UTF_8);
            writer.print(dataFile);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createSubsFolder(File subsFolder) {
        if (!subsFolder.mkdirs()) {
            System.out.println("Could not create Subs Folder...");
        }
    }

    public static String getSubsPath(String episodePath) {
        String episodeName = new File(episodePath).getName();
        String showPath = episodePath.replace(episodeName, "");
        return showPath + "Subs" + File.separator + removeExtension(episodeName) + ".srt";


    }

    public static File getEnglishSubtitle(String episodePath) {
        String episodeName = new File(episodePath).getName();
        String showPath = episodePath.replace(episodeName, "");

        String subsPath = showPath + "Subs";

        if (!new File(subsPath).exists()) {
            System.out.println("NO SUBS FOLDER");
            return null;
        }

        File[] subs = new File(subsPath).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".srt");
            }
        });

        if (subs == null || subs.length == 0) {
            subsPath = subsPath + File.separator + removeExtension(episodeName);

            subs = new File(subsPath).listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".srt");
                }
            });
        }


        return pickBestEnglishSubtitle(subs);

    }

    private static File pickBestEnglishSubtitle(File[] subs) {
        List<File> englishSubs = new ArrayList<>();
        for (File sub : subs) {
            if (sub.getName().toLowerCase(Locale.ROOT).contains("english")) englishSubs.add(sub);
        }

        String bestSub = "";
        long highestSize = 0;

        for (File englishSub : englishSubs) {
            long size = englishSub.length();
            if (size > highestSize) {
                highestSize = size;
                bestSub = englishSub.getPath();
            }
        }

        return new File(bestSub);
    }

    public static boolean subsDirectoryExist(String showPath) {
        return new File(showPath + File.separator + "Subs").exists();
    }


    private static String removeExtension(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }
}
