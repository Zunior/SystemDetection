package com.example.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Detection {
    static File[] oldListRoot = File.listRoots();

    public static void main(String[] args) {
        waitForNotifying();
    }

    public static void waitForNotifying() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (File.listRoots().length > oldListRoot.length) {
                        File drive = getLastDrivePath(File.listRoots(), oldListRoot);
                        System.out.println("new drive detected");
                        oldListRoot = File.listRoots();
                        System.out.println("drive"+ drive +" detected");
                        System.out.println(AppUtil.CreateFlatFileList(File.listRoots()[File.listRoots().length - 1]));
                        System.out.println(AppUtil.CreateJsonList(File.listRoots()[File.listRoots().length - 1]));

                    } else if (File.listRoots().length < oldListRoot.length) {
                        System.out.println(oldListRoot[oldListRoot.length-1]+" drive removed");

                        oldListRoot = File.listRoots();

                    }

                }
            }
        });
        t.start();
    }

    private static File getLastDrivePath(File[] files, File[] oldListRoot) {
        List<String> newList = new ArrayList<>();
        List<String> oldList = new ArrayList<>();
        for (File file: files) {
            newList.add(file.getAbsolutePath());
        }
        for (File file: oldListRoot) {
            oldList.add(file.getAbsolutePath());
        }
        newList.removeAll(oldList);

        return (File) Arrays.stream(files).filter(item -> item.getAbsolutePath().equals(newList.get(0)));
    }
}
