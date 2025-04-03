package org.apd.readersWriters.syncStructure;

import org.apd.executor.LockType;

public interface SyncReadersWriters {
    void writerEnter();
    void writerExit();
    void readerEnter();
    void readerExit();

    static SyncReadersWriters getSyncByLockType(LockType lockType) {
        if (lockType == LockType.ReaderPreferred) {
            return new SyncForReaderPreferred();
        } else if (lockType == LockType.WriterPreferred1) {
            return  new SyncForWriterPreferred1();
        } else {
            return new SyncForWriterPreferred2();
        }
    }
}
