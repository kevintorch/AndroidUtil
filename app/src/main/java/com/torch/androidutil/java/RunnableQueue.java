package com.torch.androidutil.java;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class RunnableQueue implements Runnable {

    private final Queue<Runnable> runnableQueue = new LinkedList<>();

    @Override
    public void run() {
        runnableQueue.iterator().forEachRemaining(Runnable::run);
    }

    public void add(Runnable runnable) {
        runnableQueue.add(runnable);
    }

    public void addAll(Runnable... runnables) {
        runnableQueue.addAll(Arrays.asList(runnables));
    }


}
