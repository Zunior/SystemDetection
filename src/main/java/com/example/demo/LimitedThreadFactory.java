package com.example.demo;

import java.util.concurrent.ThreadFactory;

public class LimitedThreadFactory implements ThreadFactory {
    private final int maxThreads;
    private int threadCount;

    public LimitedThreadFactory(int maxThreads) {
        this.maxThreads = maxThreads;
        this.threadCount = 0;
    }

    @Override
    public Thread newThread(Runnable r) {
        if (threadCount < maxThreads) {
            threadCount++;
            return new Thread(r);
        } else {
            throw new IllegalStateException("Thread limit reached");
        }
    }
}
