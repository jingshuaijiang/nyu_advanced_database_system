package com.company;

public class Parser {

    public static String[] parse(String query) {
        String res[] = new String[4];
        if(query.indexOf("begin") != -1) {
            res[0] = query.substring(0,query.indexOf("("));
            int sidx = query.indexOf("T") + 1;
            int eidx = query.indexOf(")");
            query = query.substring(sidx, eidx).trim();
            res[1] = query;
        }
        else if (query.indexOf("R") != -1) {
            res[0] = "R";
            int sidx = query.indexOf("(") + 1;
            int eidx = query.indexOf(")");
            query = query.substring(sidx, eidx).trim();
            String[] paralst = query.split(",");
            for (int i = 0; i < paralst.length-1; ++i) {
                res[i+1] = paralst[i].substring(1);
            }
        }
        else if (query.indexOf("W") != -1) {
            res[0] = "W";
            int sidx = query.indexOf("(") + 1;
            int eidx = query.indexOf(")");
            query = query.substring(sidx, eidx).trim();
            String[] paralst = query.split(",");
            for (int i = 0; i < paralst.length-1; ++i) {
                res[i+1] = paralst[i].substring(1);
            }
            res[3] = paralst[2];
        }
        else if (query.indexOf("end") != -1) {
            res[0] = "end";
            int sidx = query.indexOf("(") + 1;
            int eidx = query.indexOf(")");
            query = query.substring(sidx, eidx).trim();
            query = query.substring(1);
            res[1] = query;
        }
        else if (query.indexOf("recover") != -1) {
            res[0] = "recover";
            int sidx = query.indexOf("(") + 1;
            int eidx = query.indexOf(")");
            query = query.substring(sidx, eidx).trim();
            res[1] = query;
        }
        else if (query.indexOf("fail") != -1) {
            res[0] = "fail";
            int sidx = query.indexOf("(") + 1;
            int eidx = query.indexOf(")");
            query = query.substring(sidx, eidx).trim();
            res[1] = query;
        }
        else if (query.indexOf("dump") != -1) {
            res[0] = "dump";
        }
        return res;
    }
}
