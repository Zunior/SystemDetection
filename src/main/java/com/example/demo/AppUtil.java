package com.example.demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class AppUtil {

    private AppUtil() {};

    public static List<String> CreateFilePathList(File rootFolder) {
        List<String> filePathList = new ArrayList<>();
        createFlatList(rootFolder, filePathList);
        return filePathList;
    }

    public static String CreateJsonList(File directory) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonTree = mapper.createObjectNode();
        createJSON(directory, jsonTree, mapper);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void createFlatList(File folder, List<String> filePathList) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    File directory = new File(file.getAbsolutePath());
                    createFlatList(directory, filePathList);
                } else {
                    filePathList.add(file.getAbsolutePath());
                }
            }
        }
    }

    private static void createJSON(File folder, ObjectNode jsonObject, ObjectMapper mapper) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    ObjectNode subFolder = mapper.createObjectNode();
                    jsonObject.set(file.getName(), subFolder);
                    createJSON(file, subFolder, mapper); // Recursive call
                } else {
                    jsonObject.put(file.getName(), "File");
                }
            }
        }
    }

    public static ThreadPoolExecutor createThreadPoolService() {
        int maxThreads = 5;
        int minThreads = 1;
        long keepAliveTime = 0L;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

        return new ThreadPoolExecutor(maxThreads, minThreads, keepAliveTime, timeUnit, workQueue);
    }
}
