package com.company;
import java.util.*;

public class TransactionManager {
    List<Transaction> LiveTransList;
    HashMap<Integer,Transaction> TransactionMap;

    /**
     *
     */
    public TransactionManager()
    {
        LiveTransList = new LinkedList<>();
        TransactionMap = new HashMap<>();
    }

    /**
     * get the transaction object whose id is transactionid
     * @param TransactionId
     * @return
     */
    public Transaction get(int TransactionId)
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

    /**
     * begin transaction, initialize a transaction object.
     * @param TransactionId
     */
    public void begin(int TransactionId)
    {

    }

    /**
     * begin transaction, initialize a read-only transaction object.
     * @param TransactionId
     */
    public void beginRO(int TransactionId)
    {

    }

    /**
     * read operation,
     * @param TransactionId
     * @param Var
     */
    public void Read(int TransactionId, String Var)
    {

    }

    /**
     * write operation
     * @param TransactionId
     * @param Var
     * @param Value
     */
    public void Write(int TransactionId, String Var, int Value)
    {

    }

    /**
     * block transaction with id TransactionId
     * @param TransactionId
     */
    public void BlockTransaction(int TransactionId)
    {

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


}
