package com.example.demo;

public class FileInfoHolder {
    private String originalFilePath;
    private String newFilePath;
    private FileStatus fileStatus;

    public FileInfoHolder(String originalFilePath, String newFilePath, FileStatus fileStatus) {
        this.originalFilePath = originalFilePath;
        this.newFilePath = newFilePath;
        this.fileStatus = fileStatus;
    }

    public String getOriginalFilePath() {
        return originalFilePath;
    }

    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
    }

    public String getNewFilePath() {
        return newFilePath;
    }

    public void setNewFilePath(String newFilePath) {
        this.newFilePath = newFilePath;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }
}
