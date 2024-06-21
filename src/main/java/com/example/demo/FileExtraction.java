package com.example.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FileExtraction {

    private static final Logger logger = LogManager.getLogger(FileExtraction.class);

    private static String destinationPath;


    public static List<FileInfoHolder> startExtraction(List<String> filePathList) {
        final LinkedHashMap<String, FileStatus> fileStatusMap = new LinkedHashMap<>();
        List<FileInfoHolder> fileInfoList = new ArrayList<>();
        for (String filePath : filePathList) {
            File file = new File(filePath);
            Path originalPathWithFileName = Paths.get(filePath);
            Path newPathWithFileName = Paths.get(destinationPath + "\\" + originalPathWithFileName);
            if (file.exists()) {
                try {
                    Files.copy(originalPathWithFileName, newPathWithFileName, StandardCopyOption.REPLACE_EXISTING);
                    fileInfoList.add(new FileInfoHolder(originalPathWithFileName.toString(), newPathWithFileName.toString(), FileStatus.FILE_OK));
//                    fileStatusMap.put(originalPathWithFileName.toString(), FileStatus.FILE_OK);
                } catch (IOException e) {
                    fileInfoList.add(new FileInfoHolder(originalPathWithFileName.toString(), newPathWithFileName.toString(), FileStatus.FILE_UNREADABLE));
//                    fileStatusMap.put(originalPathWithFileName.toString(), FileStatus.FILE_UNREADABLE);
                    logger.error("Error copying file: " + originalPathWithFileName, e);
                }
            } else {
                fileInfoList.add(new FileInfoHolder(originalPathWithFileName.toString(), newPathWithFileName.toString(), FileStatus.FILE_UNREADABLE));
//                fileStatusMap.put(originalPathWithFileName.toString(), FileStatus.FILE_UNREADABLE);
                logger.error(file.getName() + " does not exist");
            }
        }

        return fileInfoList;
    }
}
