package com.company;
import java.util.*;
public class Site {

    int siteId;
    boolean failed;
    boolean justRecovery;
    public static final int arraynums = 20;
    HashMap<Integer,List<Lock>> locktable;
    HashMap<Integer,Lock> waitingfor_locktable;
    HashMap<Integer,List<Variable>> vartable;
    int recoverytime;
    int lastfailtime;
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

    public void ClearWaitLock(int TransactionId,int VarId)
    {
        if(waitingfor_locktable.containsKey(VarId)&&waitingfor_locktable.get(VarId).transactionId==TransactionId)
            waitingfor_locktable.remove(VarId);
    }

    public void AddWaitLock(int transactionId,int VarId,int timestamp)
    {
        if(!waitingfor_locktable.containsKey(VarId))
        {
            Lock lock = new Lock('W',timestamp,transactionId);
            waitingfor_locktable.put(1,lock);
        }
    }

    public void AddWriteLock(int transactionId,int variableId,int timestamp)
    {
        if(!locktable.containsKey(variableId))
        {
            locktable.put(variableId,new ArrayList<>());
            Lock lock = new Lock('W',timestamp,transactionId);
            locktable.get(variableId).add(0,lock);
            return;
        }
        if(locktable.get(variableId).get(0).Locktype=='W'&&locktable.get(variableId).get(0).transactionId==transactionId)
            return;
        Lock lock = new Lock('W',timestamp,transactionId);
        locktable.get(variableId).add(0,lock);
        return;

    }

    public boolean CanGetWriteLock(int transactionId,int variableId)
    {
        if(!locktable.containsKey(variableId))
            return true;
        else
        {
            if(locktable.get(variableId).get(0).transactionId!=transactionId)
                return false;
            if(locktable.get(variableId).get(0).Locktype=='W')
            {
                return true;
            }
            else
            {
                if(!waitingfor_locktable.containsKey(variableId))
                    return true;
                return false;
            }
        }
    }



    public boolean CanGetReadLock(int transactionId,int variableId)
    {
        if(!locktable.containsKey(variableId))
            return true;
        else
        {
            if(locktable.get(variableId).get(0).Locktype=='W'&&locktable.get(variableId).get(0).transactionId!=transactionId)
                return false;
            return true;
        }
    }

    public int GetWaitingId(int variableId)
    {
        return locktable.get(variableId).get(0).transactionId;
    }

    public void AddReadLock(int transactionId, int variableId,int timestamp)
    {
        if(!locktable.containsKey(variableId))
        {
            locktable.put(variableId,new ArrayList<>());
            Lock lock = new Lock('R',timestamp,transactionId);
            locktable.get(variableId).add(0,lock);
            return;
        }
        if(locktable.get(variableId).get(0).Locktype=='W'&&locktable.get(variableId).get(0).transactionId==transactionId)
            return;
        List<Lock> locks = locktable.get(variableId);
        for(int i=0;i<locks.size();i++)
        {
            if(locks.get(i).transactionId==transactionId)
                return;
        }
        Lock lock = new Lock('R',timestamp,transactionId);
        locktable.get(variableId).add(0,lock);
    }

    /**
     * when site failure, erase this site.
     */
    public void Sitefail()
    {
        locktable.clear();
        failed = true;
    }

    public void write (int varId, int value, int timestap) {
        List<Variable> lst = vartable.get(varId);
        Variable v = new Variable(timestap, value);
        lst.add(v);
    }

    public void SiteRecover(int timestamp, int lastfailtime)
    {
        recoverytime = timestamp;
        justRecovery = true;
        failed = false;
        lastfailtime = lastfailtime;
    }

    public void releaselock(int TransactionId)
    {
        for(int var:locktable.keySet())
        {
            List<Lock> locks = locktable.get(var);
            for(int i=0;i<locks.size();i++)
            {
                if(locks.get(i).transactionId==TransactionId)
                    locks.remove(locks.get(i));
            }
        }
    }
}
