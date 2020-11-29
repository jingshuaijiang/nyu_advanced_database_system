package com.company;
import java.util.*;

public class DataManager {

    HashMap<Integer,Site> SiteMap;
    public static final int sitenums = 10;
    HashMap<Integer,Boolean> SiteFailure;
    HashMap<Integer,Integer> SiteFailTime;

    public DataManager()
    {
        SiteMap = new HashMap<>();
        for(int i=0;i<sitenums;i++)
        {
            Site site = new Site(i+1);
            SiteMap.put(i+1,site);
            SiteFailure.put(i+1,false);
            SiteFailTime.put(i+1,-1);
        }
    }

    public Site get(int siteId)
    {
        return SiteMap.get(siteId);
    }

    public int GetLastFailTime(int siteId)
    {
        return SiteFailTime.get(siteId);
    }

    public boolean SiteFailed(int siteId)
    {
        return SiteFailure.get(siteId);
    }

    public void write (int varId, int value, int timestamp) {
        boolean even = varId % 2 == 0;
        if (even) {
            for (Map.entry<Integer,Site> entry : SiteMap.entrySet() ) {
                Site s = entry.getValue();
                if (!s.failed) {
                    s.write(varId, value, timestamp);
                }
            }
        }
        else {


        }
    }
    public void Fail(int SiteId,int timestamp)
    {
        Site site = get(SiteId);
        site.Sitefail();
        SiteFailure.put(SiteId,true);
        SiteFailTime.put(SiteId,timestamp);
    }

    public void Recover(int SiteId, int timestamp)
    {
        Site site = get(SiteId);
        site.SiteRecover(timestamp);
        SiteFailure.put(SiteId,false);
    }

    public int GetRecoverTime(int siteId)
    {
        return get(siteId).recoverytime;
    }


}
