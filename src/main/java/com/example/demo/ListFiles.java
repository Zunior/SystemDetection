package com.example.demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class ListFiles {
    public static void main(String[] args) {
        String path = "C:\\Users\\spopovic\\Desktop\\XBP tasks\\Autobahn";
        File directory = new File(path);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonTree = mapper.createObjectNode();
        StringBuilder stringTree = new StringBuilder();
        createJSON(directory, jsonTree, mapper);
        createFlatList(directory, stringTree);

        try {
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTree);
            System.out.println(jsonString);
            System.out.println(stringTree);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
