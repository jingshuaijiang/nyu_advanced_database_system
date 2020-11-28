package com.company;
import java.util.*;
import java.io.*;

public class Main {


    public static void main(String[] args) {
        TransactionManager tm = new TransactionManager();

        List<String[]> insWaitlist = new LinkedList<>();

        String inputfile = args[0];
        boolean status, inList;
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputfile));
            String query;
            while ((true) {
                String[] res = null;
                inList = false;
                if (insWaitlist.empty()) {
                    if ( (query = br.readLine()) != null)
                        res = Parser.parse(query);
                    else
                        break;
                }
                else {
                    res = insWaitlist.get(0);
                    inList = true;
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

                if (status && inList) {
                    insWaitlist.remove(0);
                }
                else if (!status && !inList) {
                    insWaitlist.add(res);
                }
                else {

                }


            }
        } catch (Exception e) {
            System.out.println("Problem with file");
            e.printStackTrace();
        }
    }
	// write your code here
}