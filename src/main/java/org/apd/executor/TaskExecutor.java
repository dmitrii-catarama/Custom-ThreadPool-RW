package org.apd.executor;

import org.apd.readersWriters.ReadTask;
import org.apd.readersWriters.SharedVars;
import org.apd.readersWriters.WriteTask;
import org.apd.storage.EntryResult;
import org.apd.storage.SharedDatabase;
import org.apd.threadPool.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/* DO NOT MODIFY THE METHODS SIGNATURES */
public class TaskExecutor {
    private final SharedDatabase sharedDatabase;

    public TaskExecutor(int storageSize, int blockSize, long readDuration, long writeDuration) {
        sharedDatabase = new SharedDatabase(storageSize, blockSize, readDuration, writeDuration);
    }

    public List<EntryResult> ExecuteWork(int numberOfThreads, List<StorageTask> tasks, LockType lockType) {
        /* IMPLEMENT HERE THE THREAD POOL, ASSIGN THE TASKS AND RETURN THE RESULTS */

        int tasksNr = tasks.size();
        ThreadPool tp = new ThreadPool(numberOfThreads, tasksNr);
        AtomicInteger inQueue = new AtomicInteger(0);
        SharedVars sharedVars = new SharedVars();

        for (StorageTask task : tasks) {
            inQueue.incrementAndGet();
            if (task.isWrite())
                tp.submit(new WriteTask(sharedDatabase, task.index(), task.data(), sharedVars, inQueue, lockType));
            else
                tp.submit(new ReadTask(sharedDatabase, task.index(), sharedVars, inQueue, lockType));
        }

        while (true) {
            int tasksRemaining = inQueue.get();
            if (tasksRemaining == 0) {
                tp.shutdown();
                break;
            }
        }

        return sharedVars.getSyncResultList();
    }


    public List<EntryResult> ExecuteWorkSerial(List<StorageTask> tasks) {
        var results = tasks.stream().map(task -> {
            try {
                if (task.isWrite()) {
                    return sharedDatabase.addData(task.index(), task.data());
                } else {
                    return sharedDatabase.getData(task.index());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();

        return results.stream().toList();
    }
}
