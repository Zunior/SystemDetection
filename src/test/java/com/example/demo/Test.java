package com.example.demo;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        File[] newDrives = {new File("C:\\"), new File("D:\\"), new File("E:\\")};
        File[] oldDrives = {new File("C:\\"), new File("E:\\")};

        File lastDrive = Detection.getLastDrivePath(newDrives, oldDrives);
        System.out.println(lastDrive.getAbsolutePath());
    }
}
