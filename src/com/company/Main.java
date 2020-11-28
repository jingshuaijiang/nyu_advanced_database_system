package com.company;
import java.util.*;
import java.io.*;

public class Main {


    public static void main(String[] args) {
        TransactionManager tm = new TransactionManager();

        List<String[]> insWaitlist = new LinkedList<>();

        String inputfile = args[0];
        boolean status, inList;
        int idx = 0;
        String query;
        status = inList = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputfile));
            while (true) {
                String[] res = null;
                inList = false;

                if (!insWaitlist.isEmpty()) {
                    query = insWaitlist.get(idx);
                    res = Parser.parse(query);
                    inList = true;
                }
                else {
                    if ( (query = br.readLine()) != null)
                        res = Parser.parse(query);
                }

                if (res[0].equals("begin")) {
                    tm.begin(Integer.parseInt(res[1]));
                } else if (res[0].equals("beginro")) {
                    status = !tm.beginRO(Integer.parse(res[1]));
                } else if (res[0].equals("R")) {
                    status = !tm.Read(Integer.parseInt(res[1]), Integer.parseInt(res[2]));
                } else if (res[0].equals("W")) {
                    status = !tm.Write(Integer.parseInt(res[1]), Integer.parseInt(res[2]), Integer.parseInt(res[3]));
                } else if (res[0].equals("recover")) {
                    status = !tm.Recover(Integer.parseInt(res[1]));
                } else if (res[0].equals("fail")) {
                    status = !tm.Fail(Integer.parseInt(res[1]));
                } else if (res[0].equals("dump")) {
                    status = !tm.Dump();
                } else if (res[0].equals("end")) {
                    status = !tm.End(Integer.parseInt(res[1]));
                }

                if (status) {
                    if (inList) {
                        insWaitlist.remove(idx);
                    }
                    idx = 0;
                } else {
                    if (!inList) {
                        insWaitlist.add(res);
                        idx = 0;
                    }
                    else {
                        idx ++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Problem with file");
            e.printStackTrace();
        }
    }
	// write your code here
}