package com.company;
import java.util.*;
import java.io.*;

public class Main {

    /**
     * main function
     * entry of our program
     * @param args
     * @author allen
     */
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
                //reading instructions from the waitlist
                if (!insWaitlist.isEmpty() && idx < insWaitlist.size()) {
                    System.out.println("idx:" + idx);
                    res = insWaitlist.get(idx);
                    System.out.println("inlst:");
                    for (String s : res)    System.out.print(s + ' ');
                    System.out.println();
                    inList = true;
                }
                //read instructions from the input file
                else {
                    if ( (query = br.readLine()) != null) {
                        System.out.println("query: "+query);
                        res = Parser.parse(query);
                        System.out.println("text:");
                        for (String s : res)    System.out.print(s + ' ');
                        System.out.println();
                    }
                    else{
                        System.out.print("no more input");
                        break;
                    }
                }
                if (res[0].equals("begin")) {
                    status = tm.begin(Integer.parseInt(res[1]));
                } else if (res[0].equals("beginRO")) {
                    status = tm.beginRO(Integer.parseInt(res[1]));
                } else if (res[0].equals("R")) {
                    status = tm.Read(Integer.parseInt(res[1]), Integer.parseInt(res[2]));
                } else if (res[0].equals("W")) {
                    status = tm.Write(Integer.parseInt(res[1]), Integer.parseInt(res[2]), Integer.parseInt(res[3]));
                } else if (res[0].equals("recover")) {
                    status = tm.Recover(Integer.parseInt(res[1]));
                } else if (res[0].equals("fail")) {
                    status = tm.Fail(Integer.parseInt(res[1]));
                } else if (res[0].equals("dump")) {
                    status = tm.Dump();
                } else if (res[0].equals("end")) {
                    status = tm.End(Integer.parseInt(res[1]));
                }

                System.out.println("status:" + status);
                System.out.println();
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
                tm.timestamp++;
                if (tm.timestamp % 1 == 0)  tm.DetectDeadLock();
            }
        } catch (Exception e) {
            System.out.println("Problem with file");
            e.printStackTrace();
        }
    }
	// write your code here
}