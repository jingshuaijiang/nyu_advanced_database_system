package com.company;
import com.sun.jdi.connect.Connector;

import javax.xml.crypto.Data;
import java.util.*;

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
     */
    public void beginRO(int TransactionId) throws Exception
    {
        TransactionInitChecker(TransactionId);
        Transaction transaction = new Transaction(this.timestamp,true);
        TransactionMap.put(TransactionId,transaction);
        TakeSnapshot(TransactionId);
    }

    public void TakeSnapshot(int TransacionId)
    {
        Transaction transaction = TransactionMap.get(TransacionId);
        for(int i=1;i<=20;i++)
        {
            if(i%2==1)
            {
                int siteid = i%10+1;
                if(dm.SiteFailed(siteid))
                    continue;
                transaction.snapshot.put(i,dm.get(siteid).GetValue(i));
            }
            else
            {
                for(int j=1;j<=10;j++)
                {
                    if(dm.SiteFailed(j))
                        continue;
                    Site site = dm.get(j);
                    if(site.commitedtimetable.get(i)<transaction.start_time&&dm.GetLastFailTime(j)<site.commitedtimetable.get(i))
                    {
                        transaction.snapshot.put(i,site.GetValue(i));
                        break;
                    }
                }
            }
        }
    }

    /**
     * read operation,
     * @param TransactionId
     * @param VarId
     */
    public boolean Read(int TransactionId, int VarId) throws Exception
    {
        AliveChecker(TransactionId);
        Transaction transaction = TransactionMap.get(TransactionId);
        if(transaction.readonly)
        {
            return RORead(transaction,VarId);
        }
        if(!AcquireReadLock(TransactionId,VarId))
            return false;
        else
        {

        }
    }

    public boolean RORead(Transaction transaction, int VarId)
    {
        if(transaction.snapshot.containsKey(VarId))
        {
            int value = transaction.snapshot.get(VarId);
            System.out.println("X"+String.valueOf(VarId)+":"+String.valueOf(value));
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * write operation
     * @param TransactionId
     * @param VarId
     * @param Value
     */
    public boolean Write(int TransactionId, int VarId, int Value) throws Exception
    {
        AliveChecker(TransactionId);

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

    }

    /**
     * print the value of the variables on all the site
     */
    public void Dump()
    {

    }

    public void Fail(int SiteId)
    {
        dm.Fail(SiteId,timestamp);

    }

    public void Recover(int SiteId)
    {
        dm.Recover(SiteId,timestamp);
    }

    /**
     * Acquirereadlock
     * @param TransactionId
     * @param VariableId
     * @return
     */
    public boolean AcquireReadLock(int TransactionId, int VariableId)
    {
        if(VariableId%2==1)
        {
            int siteId = VariableId%10;
            Site site = dm.get(siteId);
            if(site.CanGetReadLock(TransactionId,VariableId))
            {
                site.AddReadLock(TransactionId,VariableId,timestamp);
                return true;
            }
            return false;
        }
        else
        {
            for(int i=0;i<DataManager.sitenums;i++)
            {
                Site site = dm.get(i+1);
                if(site.CanGetReadLock(TransactionId,VariableId))
                {
                    site.AddReadLock(TransactionId,VariableId,timestamp);
                    return true;
                }
                else
                    continue;
            }
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

    /**
     * abort the transaction object whose id is transactionid
     * @param TransactionId
     */
    public void AbortTransaction(int TransactionId)
    {

    }


    public void TransactionInitChecker(int TransactionId) throws Exception
    {
        if(TransactionMap.containsKey(TransactionId))
        {
            throw new Exception("Transaction has already been initilized "+TransactionId);
        }
    }

    public void AliveChecker(int TransactionId) throws Exception
    {
        if(!TransactionMap.containsKey(TransactionId))
        {
            throw new Exception("Transaction not alive "+TransactionId);
        }
    }


}
