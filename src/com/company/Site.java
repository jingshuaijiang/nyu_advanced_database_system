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
    public Site(int siteId,int arraynums)
    {
        this.siteId = siteId;
        valueArray = new int[arraynums];
        committed_valueArray = new int[arraynums];
        for(int i=0;i<arraynums;i++)
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
        if(variableId%2==0)
            return true;
        return siteId==variableId%10;
    }

    /**
     * when site failure, erase this site.
     */
    public void EraseSite()
    {

    }
}
