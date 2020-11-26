package com.company;
import java.util.*;

public class DataManager {

    HashMap<Integer,Site> SiteMap;
    public static final int sitenums = 10;

    public DataManager()
    {
        SiteMap = new HashMap<>();
        for(int i=0;i<sitenums;i++)
        {
            Site site = new Site(i+1);
            SiteMap.put(i+1,site);
        }
    }

    public Site get(int siteId)
    {
        return SiteMap.get(siteId);
    }


}
