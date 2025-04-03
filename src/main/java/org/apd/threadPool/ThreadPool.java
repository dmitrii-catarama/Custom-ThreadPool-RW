package org.apd.threadPool;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {
    private BlockingQueue<Runnable> taskQueue;
    private List<Worker> workers;

    public ThreadPool(int nrWorkers, int tasksNr) {
        this.taskQueue = new ArrayBlockingQueue<>(tasksNr);
        this.workers = new ArrayList<>(nrWorkers);

        for (int i = 0; i < nrWorkers; i++) {
            Worker worker = new Worker(taskQueue);
            this.workers.add(worker);
            worker.start();
        }
    }

    public void submit(Runnable task) {
        this.taskQueue.offer(task);
    }

    public void shutdown() {
        for (Worker worker : this.workers) {
            worker.stopThread();
        }
    }
}
