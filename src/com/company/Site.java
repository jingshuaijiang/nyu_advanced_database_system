package com.company;
import java.util.*;
public class Site {

    int siteId;
    boolean failed;
    boolean justRecovery;
    public static final int arraynums = 20;
    HashMap<Integer,List<Lock>> locktable;
    HashMap<Integer,List<Variable>> vartable;
    int recoverytime;
    /**
     *
     * @param siteId
     */
    public Site(int siteId)
    {
        this.siteId = siteId;
        for(int i=0;i<arraynums;i++)
        {
            Variable var = new Variable(10*i,-1);
            vartable.put(i+1,new LinkedList<>());
            vartable.get(i+1).add(var);
        }
        locktable = new HashMap<>();
        justRecovery = false;
    }

    public int GetValue(int variableId)
    {
        return vartable.get(variableId).get(0).value;
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
        failed = true;
    }

    public void SiteRecover(int timestamp)
    {
        recoverytime = timestamp;
        justRecovery = true;
        failed = false;
    }
}
