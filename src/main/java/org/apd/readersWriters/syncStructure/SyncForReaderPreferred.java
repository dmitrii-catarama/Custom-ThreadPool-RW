package org.apd.readersWriters.syncStructure;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SyncForReaderPreferred implements SyncReadersWriters {
    private final AtomicInteger readersInBlock;  // nr de cititori in block cu date
    private final Semaphore startReadWrite; // Semafor pentru un singur scriitor

    public SyncForReaderPreferred() {
        this.readersInBlock = new AtomicInteger(0);
        this.startReadWrite = new Semaphore(1);
    }

    public void readerEnter() {
        try {
            synchronized (this) {
                // synchronized ca sa nu avem "cursa" in intrarea in if si in "get" readersInBlock
                if (readersInBlock.getAndIncrement() == 0) {
                    startReadWrite.acquire();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void readerExit() {
        if (readersInBlock.decrementAndGet() == 0)
            startReadWrite.release();
    }

    public void writerEnter() {
        try {
            startReadWrite.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void writerExit() {
        startReadWrite.release();
    }
}
