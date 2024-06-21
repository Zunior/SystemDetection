package com.example.demo;

public enum FileStatus {
    FILE_UNREADABLE("File unreadable"),
    FILE_OK("File OK");

    private final String value;

    FileStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
