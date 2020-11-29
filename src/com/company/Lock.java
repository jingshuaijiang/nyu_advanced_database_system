package com.company;

public class Lock {
    char Locktype;
    int AcquireTime;
    int transactionId;

    int siteId;
    int varId;



    public  Lock(char Locktype,int AcquireTime,int transactionId)
    {
        this.Locktype = Locktype;
        this.AcquireTime = AcquireTime;
        this.transactionId = transactionId;
    }
}
