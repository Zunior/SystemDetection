package com.example.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Detection {

    private static final Logger logger = LogManager.getLogger(Detection.class);

    static File[] oldListRoot = File.listRoots();
    static boolean runDetection = true;

    public static void main(String[] args) {
        startDetectionThread();
    }

    public static void startDetectionThread() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.warn("Device detection stopped working", e);
                }
                startDetection();
            }
        });
        t.start();
    }

    private static void startDetection() {

        if (File.listRoots().length > oldListRoot.length) {
            File drive = getLastDrivePath(File.listRoots(), oldListRoot);
            logger.info("new drive detected");
            oldListRoot = File.listRoots();
            if (runDetection) {
                logger.info("drive: %s detected".formatted(drive.getAbsolutePath()));

                List<String> filePathList = AppUtil.CreateFilePathList(File.listRoots()[File.listRoots().length - 1]);
                System.out.println(Arrays.toString(filePathList.toArray()));

                List<FileInfoHolder> fileInfoList = FileExtraction.startExtraction(filePathList);

                PDFCreation.startPDFCreation(fileInfoList);
            }
        } else if (File.listRoots().length < oldListRoot.length) {
            logger.info("drive: %s detected".formatted(getLastDrivePath(oldListRoot, File.listRoots()).getAbsolutePath()));

            oldListRoot = File.listRoots();
        }

    }

    public static File getLastDrivePath(File[] newDriveList, File[] oldDriveList) {
        List<File> newList = new ArrayList<>(Arrays.asList(newDriveList));
        List<File> oldList = new ArrayList<>(Arrays.asList(oldDriveList));
        newList.removeIf(item1 -> oldList.stream().anyMatch(item2 -> item2.getAbsolutePath().equals(item1.getAbsolutePath())));

        return newList.get(0);
    }

    public static boolean isRunDetection() {
        return runDetection;
    }

    public static void setRunDetection(boolean runDetection) {
        Detection.runDetection = runDetection;
    }
}
