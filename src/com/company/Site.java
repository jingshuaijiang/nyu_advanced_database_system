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
        vartable = new HashMap<>();
        for(int i=1;i<=arraynums;i++)
        {

            if(i%2==0)
            {
                Variable var = new Variable(-1,10*i);
                vartable.put(i,new LinkedList<>());
                vartable.get(i).add(var);
            }
            else
            {
                if(i%10+1==siteId)
                {
                    Variable var = new Variable(-1,10*i);
                    vartable.put(i,new LinkedList<>());
                    vartable.get(i).add(var);
                }
            }
        }
        locktable = new HashMap<>();
        justRecovery = false;
        waitingfor_locktable = new HashMap<>();
    }

    public int GetValue(int variableId)
    {
        int size = vartable.get(variableId).size();
        return vartable.get(variableId).get(size-1).value;
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

    public int GetVarLastCommitedTime(int variableId)
    {
        List<Variable> history = vartable.get(variableId);
        int size = vartable.get(variableId).size();
        return vartable.get(variableId).get(size-1).version;
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
            waitingfor_locktable.put(VarId,lock);
        }
    }

    public void AddWriteLock(int transactionId,int variableId,int timestamp)
    {
        if(!locktable.containsKey(variableId)||locktable.get(variableId).size()==0)
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
        if(!locktable.containsKey(variableId)||locktable.get(variableId).size()==0)
            return true;
        else
        {
//            if(locktable.get(variableId).get(0).transactionId!=transactionId)
//                return false;
//            if(locktable.get(variableId).get(0).Locktype=='W')
//            {
//                return true;
//            }
//            else
//            {
//                if(!waitingfor_locktable.containsKey(variableId))
//                    return true;
//                return false;
//            }
            if(locktable.get(variableId).get(0).Locktype=='W')
            {
                if(locktable.get(variableId).get(0).transactionId!=transactionId)
                    return false;
                return true;
            }
            else{
                List<Lock> locks = locktable.get(variableId);
                for(int i=0;i<locks.size();i++)
                {
                    if(locks.get(i).transactionId!=transactionId)
                        return false;
                }
                if(!waitingfor_locktable.containsKey(variableId)||waitingfor_locktable.get(variableId).transactionId==transactionId)
                    return true;
                return false;
            }
        }
    }



    public boolean CanGetReadLock(int transactionId,int variableId)
    {
        if(!locktable.containsKey(variableId)||locktable.get(variableId).size()==0)
            return true;
        else
        {
            if(locktable.get(variableId).get(0).Locktype=='W'&&locktable.get(variableId).get(0).transactionId!=transactionId)
                return false;
            return true;
        }
    }

    public int GetWaitingId(int variableId,int transactionId)
    {
        List<Lock> lists = locktable.get(variableId);
        for(int i=0;i<lists.size();i++)
        {
            if(lists.get(i).transactionId!=transactionId)
                return lists.get(i).transactionId;
        }
        if(waitingfor_locktable.containsKey(variableId))
            return waitingfor_locktable.get(variableId).transactionId;
        return -1;
    }

    public void AddReadLock(int transactionId, int variableId,int timestamp)
    {
        if(!locktable.containsKey(variableId)||locktable.get(variableId).size()==0)
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
        waitingfor_locktable.clear();
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
                    locks.remove(locks.get(i--));
            }
        }
    }
}
