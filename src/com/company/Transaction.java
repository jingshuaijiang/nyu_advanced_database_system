package com.company;
import java.util.HashMap;
import java.util.HashSet;
public class Transaction {

    boolean blocked;

    int start_time;

    boolean aborted;

    boolean readonly;

    HashMap<Integer,Integer> snapshot;

    HashMap<Integer, Integer> cache;

    HashSet<Integer> accessedsites;

    public Transaction(int start_time, boolean readonly)
    {
        this.start_time = start_time;
        this.readonly = readonly;
        if(readonly)
            snapshot = new HashMap<>();
    }

}
