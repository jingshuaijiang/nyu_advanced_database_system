package com.company;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
public class Transaction {

    boolean blocked;
    int start_time;
    boolean aborted;
    boolean readonly;

    int WaitingForTransactionId;

    HashMap<Integer,Integer> snapshot;

    HashMap<Integer, Integer> cache;
    HashSet<Integer> accessedsites;

    //  allen's instant variables
    //  varId, siteId
    HashMap<Integer, LinkedList<Integer>> sites;

    public Transaction(int start_time, boolean readonly)
    {
        this.start_time = start_time;
        this.readonly = readonly;
        if(readonly)
            snapshot = new HashMap<>();
    }




}
