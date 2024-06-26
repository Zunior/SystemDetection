package com.example.demo;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class Detection {

    private static final Logger logger = LogManager.getLogger(Detection.class);

    static File[] oldListRoot = File.listRoots();
    static boolean runDetection = true;
    static int maxThreads = 5;
    static final String destinationRootPath = "C:\\!Test";
    static final long detectionInterval = 1000;

    public static void main(String[] args) {
        ThreadPoolExecutor executorService = AppUtil.createThreadPoolService();
        startDetectionThread(executorService);
    }

    public static void startDetectionThread(ThreadPoolExecutor executorService) {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(detectionInterval);
                } catch (InterruptedException e) {
                    logger.warn("Device detection stopped working", e);
                }
                startDetection(executorService);
            }
        });
        t.start();
    }

    private static void startDetection(ThreadPoolExecutor executor) {

        if (File.listRoots().length > oldListRoot.length) {
            File drive = getLastDrivePath(File.listRoots(), oldListRoot);
            logger.info("new drive detected");
            oldListRoot = File.listRoots();
            if (runDetection) {
                logger.info("drive: %s detected".formatted(drive.getAbsolutePath()));

                if (executor.getActiveCount() >= maxThreads) {
                    logger.info("Max threads reached");
                    return;
                }
                // Create a new thread for each detection
                executor.submit(() -> {
                    String threadName = Thread.currentThread().getName();

                    // get type of inserted drive
                    FileSystemView fsv = FileSystemView.getFileSystemView();
                    String driveType = fsv.getSystemTypeDescription(drive);
                    logger.info(driveType);

                    List<String> filePathList = AppUtil.CreateFilePathList(drive);
                    if (filePathList.isEmpty()) {
                        logger.info("No files found on drive: %s".formatted(drive.getAbsolutePath()));
                        return;
                    }
//                    System.out.println(Arrays.toString(filePathList.toArray()));

                    List<FileInfoHolder> fileInfoList = FileExtraction.startExtraction(filePathList, drive);

                    PDFCreation.startPDFCreation(fileInfoList);

                    String tmpDirectory = destinationRootPath + File.separator + threadName;
                    try {
                        FileUtils.deleteDirectory(new File(tmpDirectory));
                    } catch (IOException e) {
                        logger.error("Error deleting directory: %s".formatted(tmpDirectory), e);
                    }
                });
//                executor.shutdown();
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
