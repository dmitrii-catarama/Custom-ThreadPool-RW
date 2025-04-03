package org.apd.readersWriters;

import org.apd.executor.LockType;
import org.apd.readersWriters.syncStructure.SyncReadersWriters;
import org.apd.storage.EntryResult;
import org.apd.storage.SharedDatabase;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class WriteTask implements Runnable {
    private final SharedDatabase sharedDatabase;
    private final int blockToWriteIndx;
    private final String data;
    private final SharedVars sharedVars;
    private final AtomicInteger inQueue;
    private final LockType lockType;


    public WriteTask(SharedDatabase sharedDatabase, int index, String data,
                     SharedVars sharedVars, AtomicInteger inQueue, LockType lockType) {

        this.sharedDatabase = sharedDatabase;
        this.blockToWriteIndx = index;
        this.data = data;
        this.sharedVars = sharedVars;
        this.inQueue = inQueue;
        this.lockType = lockType;
    }

    @Override
    public void run() {
        ConcurrentHashMap<Integer, SyncReadersWriters> syncWriters = this.sharedVars.getSyncWriters();
        syncWriters.computeIfAbsent(blockToWriteIndx, key -> SyncReadersWriters.getSyncByLockType(lockType));
        SyncReadersWriters sync = syncWriters.get(blockToWriteIndx);

        sync.writerEnter();
        EntryResult result = this.sharedDatabase.addData(blockToWriteIndx, data);
        this.sharedVars.addInSyncResultList(result);
        sync.writerExit();

        inQueue.decrementAndGet();
    }
}
