package com.example.demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class AppUtil {

    private AppUtil() {};

    public static String CreateFlatFileList(File directory) {
        StringBuilder stringTree = new StringBuilder();
        createFlatList(directory, stringTree);
        return stringTree.toString();
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

    private static void createFlatList(File folder, StringBuilder stringTree) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    File directory = new File(file.getAbsolutePath());
                    createFlatList(directory, stringTree);
                } else {
                    stringTree.append(file.getName() + " ; " + file.getAbsolutePath() + "\n");
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
}
