package com.example.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.LinkedHashMap;
import java.util.List;

public class PDFCreation {

    private static final Logger logger = LogManager.getLogger(PDFCreation.class);

    public static void startPDFCreation(List<FileInfoHolder> fileInfoList) {

        for (FileInfoHolder fileInfoHolder : fileInfoList) {
            PDDocument document = new PDDocument();

            createFirstPage(document, fileInfoHolder);
            createSecondPage(document, fileInfoHolder);
            embedFile(document, fileInfoHolder);
        }

    }

    private static void embedFile(PDDocument document, FileInfoHolder fileInfoHolder) {
    }

    private static void createSecondPage(PDDocument document, FileInfoHolder fileInfoHolder) {
    }

    private static void createFirstPage(PDDocument document, FileInfoHolder fileInfoHolder) {
    }
}
