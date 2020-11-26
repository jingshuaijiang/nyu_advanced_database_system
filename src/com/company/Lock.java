package com.company;

public class Lock {
    char Locktype;
    int AcquireTime;

    public void Lock(char Locktype,int AcquireTime)
    {
        this.Locktype = Locktype;
        this.AcquireTime = AcquireTime;
    }
}
