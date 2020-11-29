package com.company;
import java.util.*;
public class Site {

    int siteId;
    int[] valueArray;
    int[] committed_valueArray;
    boolean failed;
    boolean justRecovery;
    public static final int arraynums = 20;
    HashMap<Integer,List<Lock>> locktable;
    HashMap<Integer,Integer> commitedtimetable;
    int recoverytime;
    /**
     *
     * @param siteId
     */
    public Site(int siteId)
    {
        this.siteId = siteId;
        valueArray = new int[arraynums];
        committed_valueArray = new int[arraynums];
        for(int i=0;i<arraynums;i++)
        {
            valueArray[i] = (i+1)*10;
            committed_valueArray[i] = (i+1)*10;
        }
        locktable = new HashMap<>();
        justRecovery = false;
    }

    /**
     *
     * @param variableId
     * @return
     */
    public boolean HasVariable(int variableId)
    {
        if(variableId%2==0)
            return true;
        return siteId==variableId%10;
    }

    public boolean CanGetReadLock(int transactionId,int variableId)
    {
        if(!locktable.containsKey(variableId))
            return true;
        else
        {
            if(locktable.get(variableId).get(0).Locktype=='w'&&locktable.get(variableId).get(0).transactionId!=transactionId)
                return false;
            return true;
        }
    }

    public void AddReadLock(int transactionId, int variableId,int timestamp)
    {
        Lock lock = new Lock('R',timestamp,transactionId);
        if(!locktable.containsKey(variableId))
            locktable.put(variableId,new ArrayList<>());
        locktable.get(variableId).add(lock);
    }

    /**
     * when site failure, erase this site.
     */
    public void Sitefail()
    {
        locktable.clear();
    }

    public void write (int varId, int value) {
        valueArray[varId] = value;

    }

    public void SiteRecover(int timestamp)
    {
        recoverytime = timestamp;
        justRecovery = true;
        failed = false;
    }
}
