package org.apd.readersWriters.syncStructure;

import java.util.concurrent.Semaphore;

public class SyncForWriterPreferred1 implements SyncReadersWriters {

    private int numReaders; /* nr. cititori care folosesc resursa */
    private int numWriters; /* nr. scriitori care folosesc resursa */

    private final Semaphore atomicSection; /* intrare secțiune atomică */
    private final Semaphore readerSem; /* intarzie cititori daca nw>0 sau dw>0 */
    private final Semaphore writerSem; /* intarzie scriitori daca nw>0 sau nr>0 */

    private int numReadersWaiting; /* nr. cititori care asteapta eliberarea resursei */
    private int numWritersWaiting; /* nr. scriitori care asteapta eliberarea resursei */

    public SyncForWriterPreferred1() {
        numReaders = 0;
        numWriters = 0;
        atomicSection = new Semaphore(1);
        readerSem = new Semaphore(0);
        writerSem = new Semaphore(0);
        numReadersWaiting = 0;
        numWritersWaiting = 0;
    }

    @Override
    public void writerEnter() {
        try {
            atomicSection.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (numReaders > 0 || numWriters > 0) {
            numWritersWaiting++;
            atomicSection.release();

            try {
                writerSem.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        numWriters++;
        atomicSection.release();
    }

    @Override
    public void writerExit() {
        try {
            atomicSection.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        numWriters--;
        if (numReadersWaiting > 0 && numWritersWaiting == 0) {
            numReadersWaiting--;
            readerSem.release();

        } else if (numWritersWaiting > 0) {
            numWritersWaiting--;
            writerSem.release();
        } else if (numReadersWaiting == 0 && numWritersWaiting == 0) {
            atomicSection.release();
        }
    }

    @Override
    public void readerEnter() {
        try {
            atomicSection.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (numWriters > 0 || numWritersWaiting > 0) {
            numReadersWaiting++;
            atomicSection.release();

            try {
                readerSem.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.numReaders++;

        if (numReadersWaiting > 0) {
            numReadersWaiting--;
            readerSem.release();
        } else if (numReadersWaiting == 0) {
            atomicSection.release();
        }
    }

    @Override
    public void readerExit() {
        try {
            atomicSection.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        numReaders--;

        if (numReaders == 0 && numWritersWaiting > 0) {
             numWritersWaiting--;
            writerSem.release();
        } else if (numReaders > 0 || numWritersWaiting == 0) {
            atomicSection.release();
        }
    }
}
