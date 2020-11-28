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

    public void ProcessCommand()
    {

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

    }

    /**
     * read operation,
     * @param TransactionId
     * @param Var
     */
    public void Read(int TransactionId, int VarId) throws Exception
    {
        AliveChecker(TransactionId);

    }

    /**
     * write operation
     * @param TransactionId
     * @param Var
     * @param Value
     */
    public void Write(int TransactionId, int VarId, int Value) throws Exception
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
    public void End(int TransactionId)
    {

    }

    /**
     * print the value of the variables on all the site
     */
    public void Dump()
    {

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

            }
        }
        else
        {
            for(int i=0;i<DataManager.sitenums;i++)
            {
                Site site = dm.get(i+1);
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

    public boolean Recover(int SiteId) {

        return true;
    }

    public boolean Fail(int SiteId) {

        return true;
    }

}
