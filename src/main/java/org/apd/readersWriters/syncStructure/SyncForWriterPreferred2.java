package org.apd.readersWriters.syncStructure;

public class SyncForWriterPreferred2 implements SyncReadersWriters {
    private int numReaders;
    private int numWriters;
    private int numWritersWaiting;

    public SyncForWriterPreferred2() {
        numReaders = 0;
        numWriters = 0;
        numWritersWaiting = 0;
    }


    @Override
    public synchronized void writerEnter() {
        try {
                numWritersWaiting++;
                while (numWriters > 0 || numReaders > 0) {
                    wait();
                }
                numWritersWaiting--;
                numWriters++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void writerExit() {
        numWriters--;
        notifyAll();
    }

    @Override
    public synchronized void readerEnter() {
        try {
            while (numWriters > 0 || numWritersWaiting > 0) {
                wait();
            }

            numReaders++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void readerExit() {
            numReaders--;
            notifyAll();
    }
}
