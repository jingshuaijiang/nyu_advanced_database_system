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
            String query = br.readLine();
            String[] res= Parser.parse(query);
            if(res[0].equals("begin"))
            {
                tm.begin();
            }
            else if(res[0].equals("beginro"))
            {

            }
            else if(res[0].equals("R"))
            {

            }
            else if(res[0].equals("W"))
            {
                tm.Write();
            }
            else if(res[0].equals("recover"))
            {

            }
            else if(res[0].equals("fail"))
            {

            }
            else if(res[0].equals("dump"))
            {

            }
            else if(res[0].equals("end"))
            {

            }

        } catch (Exception e) {
            System.out.println("Problem with file");
            e.printStackTrace();
        }

        while()
	// write your code here
    }
}
