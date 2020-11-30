package com.company;
import java.util.*;

public class DataManager {

    HashMap<Integer,Site> SiteMap;
    public static final int sitenums = 10;
    HashMap<Integer,Boolean> SiteFailure;
    HashMap<Integer,List<Integer>> SiteFailTime;

    public DataManager()
    {
        SiteMap = new HashMap<>();
        SiteFailure = new HashMap<>();
        SiteFailTime = new HashMap<>();
        for(int i=0;i<sitenums;i++)
        {
            Site site = new Site(i+1);
            SiteMap.put(i+1,site);
            SiteFailure.put(i+1,false);
            SiteFailTime.put(i+1,new LinkedList<>());
            SiteFailTime.get(i+1).add(-1);
        }
    }

    public Site get(int siteId)
    {
        return SiteMap.get(siteId);
    }

    public int GetLastFailTime(int siteId)
    {
        int size = SiteFailTime.get(siteId).size();
        return SiteFailTime.get(siteId).get(size-1);
    }

    public boolean SiteFailed(int siteId)
    {
        return SiteFailure.get(siteId);
    }

    public void write (int varId, int value, int siteId, int timestamp) {
        Site s = SiteMap.get(siteId);
        s.write(varId, value, timestamp);
    }

    public int RONonRepRead(int variableId,int timestamp,int siteId)
    {
        Site site = get(siteId);
        List<Variable> history = site.vartable.get(variableId);
        for(int i=history.size()-1;i>=0;i--)
        {
            if(history.get(i).version<=timestamp)
            {
                return history.get(i).value;
            }
        }
        return -1;
    }

    public String RORepRead(int variableId,int timestamp,int siteId)
    {
        Site site = get(siteId);
        List<Variable> history = site.vartable.get(variableId);
        for(int i=history.size()-1;i>=0;i--)
        {
            //looking for the version before ro start
            if(history.get(i).version>timestamp)
                continue;
            //found
            int lastcommitedtimebeforestart = history.get(i).version;
            //check if it is always up during time range
            if(AlwaysUp(lastcommitedtimebeforestart,timestamp,siteId))
                return String.valueOf(history.get(i).value);
            //if not, then this site can not trust.
            break;
        }
        return "No";
    }

    public boolean AlwaysUp(int lastcommitedtimebeforestart, int starttime,int siteId)
    {
        List<Integer> failedtime = SiteFailTime.get(siteId);
        for(int i=0;i<failedtime.size();i++)
        {
            int time = failedtime.get(i);
            if(time<starttime&&time>lastcommitedtimebeforestart)
                return false;
        }
        return true;
    }

    public void Fail(int SiteId,int timestamp) {
        Site site = get(SiteId);
        site.Sitefail();
        SiteFailure.put(SiteId, true);
        SiteFailTime.get(SiteId).add(timestamp);
    }

    public void Recover(int SiteId, int timestamp)
    {
        Site site = get(SiteId);
        int size = SiteFailTime.get(SiteId).size();
        site.SiteRecover(timestamp,SiteFailTime.get(SiteId).get(size-1));
        SiteFailure.put(SiteId,false);
    }

    public int GetSiteVariableValue(int SiteId, int VariableId)
    {
        Site site = get(SiteId);
        return site.GetValue(VariableId);
    }

    public int GetRecoverTime(int siteId)
    {
        return get(siteId).recoverytime;
    }

    public void ReleaseSiteLocks(int TransactionId,int siteid)
    {
        if(SiteFailed(siteid))
            return;
        Site site = get(siteid);
        site.releaselock(TransactionId);
    }


}
