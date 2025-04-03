package org.apd.threadPool;

import java.util.concurrent.BlockingQueue;

public class Worker extends Thread {
    BlockingQueue<Runnable> sharedTaskQueue;

    public Worker(BlockingQueue<Runnable> sharedTaskQ) {
        this.sharedTaskQueue = sharedTaskQ;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Runnable task = this.sharedTaskQueue.take();
                task.run();
            } catch (InterruptedException e) {
                break;
            }
        }
    }


    public void stopThread() {
        this.interrupt();
    }

}
