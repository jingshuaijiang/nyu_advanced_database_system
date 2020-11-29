package com.company;

import java.util.*;
import java.util.Map;
import java.util.HashMap;

public class TransactionManager {
    HashMap<Integer,Transaction> TransactionMap;
    DataManager dm;
    int timestamp;


    public TransactionManager()
    {
        TransactionMap = new HashMap<>();
        dm = new DataManager();
        timestamp = 0;
    }
    /**
     * get the transaction object whose id is transactionid
     * @param TransactionId
     * @return
     */
    public Transaction get(int TransactionId) throws Exception
    {
        if(!TransactionMap.containsKey(TransactionId))
            throw new Exception("No such alive transaction "+TransactionId);
        return TransactionMap.get(TransactionId);
    }

    /**
     * begin transaction, initialize a transaction object.
     * @param TransactionId
     */
    public void begin(int TransactionId) throws Exception {
        TransactionInitChecker(TransactionId);
        Transaction transaction = new Transaction(this.timestamp,false);
        TransactionMap.put(TransactionId,transaction);
    }

    /**
     * begin transaction, initialize a read-only transaction object.
     * @param TransactionId
     * @return
     */
    public boolean beginRO(int TransactionId) throws Exception
    {
        TransactionInitChecker(TransactionId);
        Transaction transaction = new Transaction(this.timestamp,true);
        TransactionMap.put(TransactionId,transaction);
        return false;
    }
    /**
     * read operation,
     * @param TransactionId
     * @param VarId
     */
    public boolean Read(int TransactionId, int VarId)
    {
        if(!AliveChecker(TransactionId))
            return true;
        Transaction transaction = TransactionMap.get(TransactionId);
        if(transaction.blocked) return true;
        if(transaction.aborted) return true;
        //deal with readonly transaction read
        if(transaction.readonly)
        {
            return RORead(transaction,VarId);
        }
        //read from previous written value.
        if(transaction.cache.containsKey(VarId))
        {
            int value = transaction.cache.get(VarId);
            System.out.println("X"+String.valueOf(VarId)+":"+String.valueOf(value));
            return true;
        }
        //
        if(VarId%2==1)
        {
            int siteId = VarId%10+1;
            if(AcquireReadLock(TransactionId,VarId,siteId))
            {
                if(!transaction.accessedsites.contains(siteId))
                    transaction.accessedsites.add(siteId);
                int value = dm.GetSiteVariableValue(siteId,VarId);
                System.out.println("X"+String.valueOf(VarId)+":"+String.valueOf(value));
                return true;
            }
        }
        //replicated
        else
        {
            for(int i=1;i<=DataManager.sitenums;i++)
            {
                if(AcquireReadLock(TransactionId,VarId,i))
                {
                    if(!transaction.accessedsites.contains(i))
                        transaction.accessedsites.add(i);
                    int value = dm.GetSiteVariableValue(i,VarId);
                    System.out.println("X"+String.valueOf(VarId)+":"+String.valueOf(value));
                    return true;
                }
                else
                    break;
            }
        }
        return false;
    }

    public boolean RORead(Transaction transaction, int VarId)
    {
        if(VarId%2==1)
        {
            int siteid = VarId%10+1;
            //go and get history version
            //if failed then return false and wait.
            if(dm.SiteFailed(siteid))
                return false;
            else
            {
                //as long as it's up, we go inside and get the value whose version is before the start_time;
                int value = dm.RONonRepRead(VarId,transaction.start_time,siteid);
                System.out.println("X"+String.valueOf(VarId)+":"+String.valueOf(value));
                return true;
            }
        }
        //replicated ones
        else
        {
            for(int i=1;i<=10;i++)
            {
                //if site was failed or just recovery, just check next site.
                if(dm.SiteFailed(i))
                    continue;
                Site site = dm.get(i);
                //check next site
                String value = dm.RORepRead(VarId,transaction.start_time,i);
                if(value.equals("No"))
                    continue;
                else
                {
                    System.out.println("X"+String.valueOf(VarId)+":"+value);
                    return true;
                }
            }
        }
        //all the sites are not suitable for replicated variables.
        return false;
    }

    /**
     * write operation
     * @param TransactionId
     * @param VarId
     * @param Value
     */
    public boolean Write(int TransactionId, int VarId, int Value)
    {
        AliveChecker(TransactionId);
        Transaction transaction = TransactionMap.get(TransactionId);
        if(transaction.aborted) return true;
        if(VarId%2==1)
        {
            int siteId = VarId%10+1;
            if(dm.SiteFailed(siteId))
                return false;
            Site site = dm.get(siteId);
            site.CanGetWriteLock(TransactionId,VarId);
        }
        else
        {

        }

    }

    /**
     * block transaction with id TransactionId
     * @param TransactionId
     */
    public void BlockTransaction(int TransactionId)
    {
        Transaction transaction = TransactionMap.get(TransactionId);
        transaction.blocked = true;
    }

    /**
     * end transaction, print commit or abort
     * @param TransactionId
     */
    public boolean End(int TransactionId)
    {

        Transaction t = TransactionMap.get(TransactionId);

        if (t.blocked)  return false;

        if (t.aborted) {
            System.out.print("Transaction is already abort");
            TransactionMap.remove(TransactionId);
        } else {
            System.out.print("Transaction commit");
            //  Might package as a function
            //  write each value in cache to sites
            for (Map.Entry<Integer, Integer> entry : t.cache.entrySet()) {
                int varId = entry.getKey();
                int value = entry.getValue();


                LinkedList<Integer> sites = t.sites.get(varId);
                for (int siteId : sites) {
                    dm.write(varId, value, siteId, timestamp);
                }
            }

            //  release locks
            Iterator<Integer> it = t.accessedsites.iterator();
            while (it.hasNext()) {
                int siteId = it.next();
                dm.ReleaseSiteLocks(TransactionId, siteId);
            }
        }
        return true;
    }

    /**
     * print the value of the variables on all the site
     */
    public boolean Dump()
    {
        return true;
    }

    /**
     * Acquirereadlock
     * @param TransactionId
     * @param VariableId
     * @return
     */
    public boolean AcquireReadLock(int TransactionId, int VariableId,int siteId)
    {
        //none replicated variables
        if(dm.SiteFailed(siteId))
            return false;
        Site site = dm.get(siteId);
        if(VariableId%2==0&&site.justRecovery)
            return false;
        if(site.CanGetReadLock(TransactionId,VariableId))
        {
            site.AddReadLock(TransactionId,VariableId,timestamp);
            return true;
        }
        else
        {
            Transaction trans = TransactionMap.get(TransactionId);
            int waitid = site.GetWaitingId(VariableId);
            trans.WaitingForTransactionId = waitid;
            trans.blocked = true;
        }
        return false;
    }

    /**
     * acquire write lock
     * @param TransactionId
     * @param VariableId
     * @return
     */
    public boolean AcquireWriteLock(int TransactionId,int VariableId)
    {

    }

    /**
     * Detect deadlock
     */
    public void DetectDeadLock()
    {

    }

    public void AbortTransactions(int siteId)
    {
        for(int id:TransactionMap.keySet())
        {
            AbortTransaction(id);
        }
    }

    /**
     * abort the transaction object whose id is transactionid
     * @param TransactionId
     */
    public void AbortTransaction(int TransactionId)
    {
        Transaction trans = TransactionMap.get(TransactionId);
        trans.aborted = true;
        ReleaseLocks(TransactionId,trans.accessedsites);
    }

    public void ReleaseLocks(int TransactionId,HashSet<Integer> accessedsites)
    {
        if(accessedsites.isEmpty())
            return;
        for(int site:accessedsites)
        {
            dm.ReleaseSiteLocks(TransactionId,site);
        }
    }


    public void TransactionInitChecker(int TransactionId)
    {
        if(TransactionMap.containsKey(TransactionId))
        {
            System.out.println("Transaction has already been initilized "+TransactionId);
        }
    }

    public boolean AliveChecker(int TransactionId)
    {
        if(!TransactionMap.containsKey(TransactionId))
        {
            System.out.println("Transaction not alive "+TransactionId);
            return false;
        }
        return true;
    }

    public boolean Recover(int SiteId)
    {
        dm.Recover(SiteId,timestamp);
        return true;
    }

    public boolean Fail(int SiteId)
    {
        dm.Fail(SiteId,timestamp);
        AbortTransactions(SiteId);
        return true;
    }

}
