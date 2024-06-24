package com.example.demo;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

public class DirectoryCopierWithProgress {

    private DirectoryCopierWithProgress() {}

    public static void CopyFiles(Path sourceDir, Path targetDir) {

        try {
            Files.createDirectories(targetDir); // Create the target directory if it doesn't exist

            AtomicLong totalSize = new AtomicLong(0);
            AtomicLong copiedSize = new AtomicLong(0);

            // Calculate total size for progress calculation
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    totalSize.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }
            });

            // Copy files and directories
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetSubDir = targetDir.resolve(sourceDir.relativize(dir));
                    Files.createDirectories(targetSubDir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = targetDir.resolve(sourceDir.relativize(file));
                    copyWithProgress(file, targetFile, copiedSize, totalSize);
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("Directory copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyWithProgress(Path sourceFile, Path targetFile,
                                         AtomicLong copiedSize, AtomicLong totalSize) throws IOException {
        try (InputStream in = Files.newInputStream(sourceFile);
             OutputStream out = Files.newOutputStream(targetFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
                copiedSize.addAndGet(bytesRead);
                double progress = (double) copiedSize.get() / totalSize.get() * 100;
                System.out.printf("Progress: %.2f%%%n", progress);
            }
        }
    }
}
