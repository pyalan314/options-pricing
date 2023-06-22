/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OptionPricing;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
/**
 *
 * @author kailun
    private datapair tempdata;
    private DateFormat df;
 */

public class DataProcessor {
    private Vector mydata;
    private DateFormat df;
    public DataProcessor(String database) {
        df = new SimpleDateFormat("yyyy-MM-dd");
        mydata = new Vector();
        load(database);
    }
   public short countTotal(String date1, String date2) {
       short start=0; short end=0;
        try {
            start = getPosition(date1);
            end = getPosition(date2);
        }
        catch(ParseException e) { System.out.println("error in parsing date");}
        return (short) (end - start + 1);
    }
   public short countTrading(String date1, String date2) {
       short start=0; short end=0;
        try {
            start = getPosition(date1);
            end = getPosition(date2);
        }
        catch(ParseException e) { System.out.println("error in parsing date");}
        short count = 0;
        for(short i=start; i<=end; i++) {
            datapair tempdata = (datapair) mydata.get(i);
            count += tempdata.trading;
        }
        return count;
    }
    public short getPosition(String date) throws ParseException    {
        datapair tempdata = (datapair) mydata.get(0);
        Date date0 = df.parse(tempdata.date);
        Date currdate = df.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date0);
        long time0 = calendar.getTimeInMillis();
        calendar.setTime(currdate);
        long currtime = calendar.getTimeInMillis();
        long temp = (currtime - time0)/(1000*60*60*24);
        return (short) temp;
    }
    public void load(String s) {
        try {
            String str;
            StringTokenizer st;
            File f = new File(s);
            InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");
            BufferedReader breader = new BufferedReader (read);
            while((str = breader.readLine()) != null) {
                datapair tempdata = new datapair();
                st = new StringTokenizer(str, "\t", false);
                tempdata.date = st.nextToken();
                tempdata.trading = Short.parseShort(st.nextToken());
                mydata.add(tempdata);
            }
        } catch (IOException e) {System.out.println("error000 in reading file");}
    }
    public String[] loadmdate(String s) {
        String[] temp = new String[4];
        try {
            short i = 0;
            String str;
            StringTokenizer st;
            File f = new File(s);
            InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");
            BufferedReader breader = new BufferedReader (read);
            while((str = breader.readLine()) != null) {
                st = new StringTokenizer(str, "\t", false);
                temp[i++] = st.nextToken();
            }
        } catch (IOException e) {System.out.println("error in reading file");}
        return temp;
    }
    public Vector loadddate(String s) {
        Vector tempV = new Vector();
        try {
            String str;
            StringTokenizer st;
            File f = new File(s);
            InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");
            BufferedReader breader = new BufferedReader (read);
            while((str = breader.readLine()) != null) {
                st = new StringTokenizer(str, "\t", false);
                String[] temp = new String[3];
                temp[0] = st.nextToken();
                temp[1] = st.nextToken();
                temp[2] = st.nextToken();
                tempV.add(temp);
            }
        } catch (IOException e) {System.out.println("error in reading file");}
        return tempV;
    }
    public Vector loadcdate(String s) {
        Vector tempV = new Vector();
        try {
            String str;
            StringTokenizer st;
            File f = new File(s);
            InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");
            BufferedReader breader = new BufferedReader (read);
            while((str = breader.readLine()) != null) {
                String[] temp = new String[2];
                st = new StringTokenizer(str, "\t", false);
                temp[0] = st.nextToken();
                temp[1] = st.nextToken();
                tempV.add(temp);
            }
        } catch (IOException e) {System.out.println("error in reading file");}
        return tempV;
    }
    public float[] loadfloat(String s) {
        Vector tempV = new Vector();
        try {
            short i = 0;
            String str;
            StringTokenizer st;
            File f = new File(s);
            InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");
            BufferedReader breader = new BufferedReader (read);
            while((str = breader.readLine()) != null) {
                st = new StringTokenizer(str, "\t", false);
                tempV.add(Float.parseFloat(st.nextToken()));
            }
        } catch (IOException e) {System.out.println("error in reading file");}
        short size = (short) tempV.size();
        float[] temp = new float[size];
        for(int i=0; i<size; i++) temp[i] = (Float) tempV.get(i);
        return temp;
    }
    public void save(Vector c, String filename) {
        try{
            File f = new File(filename);
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
            BufferedWriter bwriter = new BufferedWriter(write);
            String str;
            for(int j=0; j<c.size(); j++) {
                str = (String) c.get(j);
                bwriter.write(str);
            }
            bwriter.close();
        } catch (IOException e) {System.out.println("error in saving file");}
    }
    public void save(double[][] a, float[] b, String filename) {
        try{
            File f = new File(filename);
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
            BufferedWriter bwriter = new BufferedWriter(write);
            String str;
            for(int j=0; j<a.length; j++) {
                str = a[j][0]/b[0] + "\t" +a[j][1]/b[1] + "\t" +a[j][2]/b[2] + "\r\n";
                bwriter.write(str);
            }
            bwriter.close();
        } catch (IOException e) {System.out.println("error in saving file");}
    }
    class datapair {
        public String date;
        public short trading;
    }
}
