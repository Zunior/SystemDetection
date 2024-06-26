package com.example.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.example.demo.Detection.destinationRootPath;

public class FileExtraction {

    private static final Logger logger = LogManager.getLogger(FileExtraction.class);


    public static List<FileInfoHolder> startExtraction(List<String> filePathList, File drive) {

        AtomicLong mediaCopiedSize = new AtomicLong(0);
        AtomicLong mediaTotalSize = new AtomicLong(0);
        mediaTotalSize = calculateMediaSize(drive.getAbsolutePath());

        List<FileInfoHolder> fileInfoList = new ArrayList<>();
        String tmpDir = Thread.currentThread().getName();
        for (String filePath : filePathList) {
            File file = new File(filePath);
            Path originalPathWithFileName = Paths.get(filePath);
            Path newTargetDir = createNewTargetDir(tmpDir, originalPathWithFileName);
            Path newPathWithFileName = Paths.get(newTargetDir + File.separator + file.getName());
            if (file.exists()) {
                try {
                    Files.createDirectories(newTargetDir);
                    copyWithProgress(originalPathWithFileName, newPathWithFileName, mediaCopiedSize, mediaTotalSize);
//                    Files.copy(originalPathWithFileName, newPathWithFileName, StandardCopyOption.REPLACE_EXISTING);
                    fileInfoList.add(new FileInfoHolder(originalPathWithFileName.toString(), newPathWithFileName.toString(), FileStatus.FILE_OK));
                } catch (IOException e) {
                    fileInfoList.add(new FileInfoHolder(originalPathWithFileName.toString(), newPathWithFileName.toString(), FileStatus.FILE_UNREADABLE));
                    logger.error("Error copying file: " + originalPathWithFileName, e);
                }
            } else {
                fileInfoList.add(new FileInfoHolder(originalPathWithFileName.toString(), newPathWithFileName.toString(), FileStatus.FILE_UNREADABLE));
                logger.error(file.getName() + " does not exist");
            }
        }

        return fileInfoList;
    }

    private static Path createNewTargetDir(String tmpDir, Path originalPathWithFileName) {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(destinationRootPath);
        pathBuilder.append(File.separator);
        pathBuilder.append(tmpDir);
        if (originalPathWithFileName.getNameCount() > 1) {
            pathBuilder.append(File.separator);
            pathBuilder.append(originalPathWithFileName.subpath(0, originalPathWithFileName.getNameCount() - 1));
        }

        return Paths.get(pathBuilder.toString());
    }

    private static AtomicLong calculateMediaSize(String absolutePath) {
        Path directory = Paths.get(absolutePath); // Replace with your directory path
        AtomicLong size = new AtomicLong(0);

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    size.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    if (exc instanceof AccessDeniedException) {
                        logger.info("Access denied to " + file);
                        // Skip this file/directory and continue
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    exc.printStackTrace();
                    // Terminate the walk
                    return FileVisitResult.TERMINATE;
                }
            });

            logger.info("Total size: " + size.get() + " bytes");
        } catch (IOException e) {
            logger.error("Error calculating media size", e);
        }

        return size;
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
                if (totalSize.get() > 0) {
                    double progress = (double) copiedSize.get() / totalSize.get() * 100;
                    logger.info("Progress: " + String.format("%.2f", progress));
                }
            }
        }
    }

}
