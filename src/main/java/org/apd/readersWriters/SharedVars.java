package org.apd.readersWriters;

import org.apd.readersWriters.syncStructure.SyncReadersWriters;
import org.apd.storage.EntryResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;

public class SharedVars {
    private List<EntryResult> syncResultList; // lista sincronizata de rezultate
    private final ConcurrentHashMap<Integer, SyncReadersWriters> syncWriters; // mapa pentru a permite mai multe scrieri in baza de date


    public SharedVars() {
        this.syncResultList = Collections.synchronizedList(new ArrayList<>());
        this.syncWriters = new ConcurrentHashMap<>();
    }

    public List<EntryResult> getSyncResultList() {
        return syncResultList;
    }

    public void addInSyncResultList(EntryResult result) {
        this.syncResultList.add(result);
    }

    public ConcurrentHashMap<Integer, SyncReadersWriters> getSyncWriters() {
        return syncWriters;
    }
}
