package com.company;

public class Transaction {

    boolean blocked;

    int start_time;

    boolean aborted;

    boolean readonly;

    public Transaction(int start_time, boolean readonly)
    {
        this.start_time = start_time;
        this.readonly = readonly;
    }
}
