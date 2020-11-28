package com.company;
import java.util.*;
import java.io.*;

public class Main {


    public static void main(String[] args) {
        TransactionManager tm = new TransactionManager();

        List<String[]> insWaitlist = new LinkedList<>();

        String inputfile = args[0];
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputfile));
            String query;
            while ((query = br.readLine()) != null) {

                String[] res = Parser.parse(query);
                if (res[0].equals("begin")) {
                    tm.begin(Integer.parseInt(res[1]));
                } else if (res[0].equals("beginro")) {
                    if (!tm.beginRO(Integer.parse(res[1])))
                        insWaitlist.add(res);
                } else if (res[0].equals("R")) {
                    if (!tm.Read(Integer.parseInt(res[1]), Integer.parseInt(res[2])))
                        insWaitlist.add(res);
                } else if (res[0].equals("W")) {
                    if (!tm.Write(Integer.parseInt(res[1]), Integer.parseInt(res[2]), Integer.parseInt(res[3])))
                        insWaitlist.add(res);
                } else if (res[0].equals("recover")) {

                } else if (res[0].equals("fail")) {

                } else if (res[0].equals("dump")) {

                } else if (res[0].equals("end")) {

                }
            }
        } catch (Exception e) {
            System.out.println("Problem with file");
            e.printStackTrace();
        }
    }
	// write your code here
}