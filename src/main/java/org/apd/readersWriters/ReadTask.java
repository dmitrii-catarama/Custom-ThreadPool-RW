package org.apd.readersWriters;

import org.apd.executor.LockType;
import org.apd.readersWriters.syncStructure.SyncReadersWriters;
import org.apd.storage.EntryResult;
import org.apd.storage.SharedDatabase;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadTask implements Runnable {
    private final SharedDatabase sharedDatabase;
    private final int blockToReadIndx;
    private final SharedVars sharedVars;
    private final AtomicInteger inQueue;
    private final LockType lockType;

    public ReadTask(SharedDatabase sharedDatabase, int index,
                    SharedVars sharedVars, AtomicInteger inQueue, LockType lockType) {

        this.sharedDatabase = sharedDatabase;
        this.blockToReadIndx = index;
        this.sharedVars = sharedVars;
        this.inQueue = inQueue;
        this.lockType = lockType;
    }

    @Override
    public void run() {
        ConcurrentHashMap<Integer, SyncReadersWriters> syncWriters = this.sharedVars.getSyncWriters();
        syncWriters.computeIfAbsent(blockToReadIndx, key -> SyncReadersWriters.getSyncByLockType(lockType));
        SyncReadersWriters sync = syncWriters.get(blockToReadIndx);

        sync.readerEnter();
        EntryResult result = this.sharedDatabase.getData(blockToReadIndx);
        this.sharedVars.addInSyncResultList(result);
        sync.readerExit();

        this.inQueue.decrementAndGet();
    }
}
