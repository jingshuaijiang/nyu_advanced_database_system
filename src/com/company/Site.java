package com.company;

public class Site {

    int siteId;
    int[] valueArray;
    int[] committed_valueArray;
    boolean justRecovery;

    /**
     *
     * @param siteId
     */
    public Site(int siteId)
    {
        this.siteId = siteId;
        valueArray = new int[20];
        committed_valueArray = new int[20];
        for(int i=0;i<20;i++)
        {
            valueArray[i] = (i+1)*10;
            committed_valueArray[i] = (i+1)*10;
        }
    }

    /**
     *
     * @param variableId
     * @return
     */
    public boolean HasVariable(int variableId)
    {

    }

    /**
     * when site failure, erase this site.
     */
    public void EraseSite()
    {

    }
}
