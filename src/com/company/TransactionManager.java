package com.company;
import java.util.*;

public class TransactionManager {
    List<Transaction> LiveTransList;
    HashMap<Integer,Transaction> TransactionMap;
    DataManager dm;
    int timestamp;
    /**
     *
     */
    public TransactionManager()
    {
        LiveTransList = new LinkedList<>();
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

    /**
     * begin transaction, initialize a transaction object.
     * @param TransactionId
     */
    public void begin(int TransactionId) throws Exception {
        TransactionInitChecker(TransactionId);
        Transaction transaction = new Transaction(this.timestamp,false);
        TransactionMap.put(TransactionId,transaction);
        LiveTransList.add(transaction);
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
        LiveTransList.add(transaction);
    }

    /**
     * read operation,
     * @param TransactionId
     * @param Var
     */
    public void Read(int TransactionId, String Var) throws Exception
    {
        AliveChecker(TransactionId);

    }

    /**
     * write operation
     * @param TransactionId
     * @param Var
     * @param Value
     */
    public void Write(int TransactionId, String Var, int Value) throws Exception
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
