package com.company;
import java.util.HashMap;
public class Transaction {

    boolean blocked;

    int start_time;

    boolean aborted;

    boolean readonly;

    HashMap<Integer,Integer> snapshot;

    public Transaction(int start_time, boolean readonly)
    {
        this.start_time = start_time;
        this.readonly = readonly;
        if(readonly)
            snapshot = new HashMap<>();
    }

}
