package com.company;

public class Lock {
    char Locktype;
    int AcquireTime;
    int transactionId;

    public Lock(char Locktype,int AcquireTime,int transactionId)
    {
        this.Locktype = Locktype;
        this.AcquireTime = AcquireTime;
        this.transactionId = transactionId;
    }
}
