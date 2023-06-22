/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OptionPricing;

import java.util.Vector;

/**
 *
 * @author kailun
 */
public class DataOutput {
    DataProcessor mydp;
    Vector ddateS, dcount;
    public DataOutput() {
        /*ddateS = new Vector();
        dcount = new Vector();
        mydp = new DataProcessor("datedb.txt");
        ddateS = mydp.loadddate("distdate.txt");
        String start = ((String[]) ddateS.get(0))[0];
        for(int i =0; i<ddateS.size(); i++) {
            String[] temp = (String[]) ddateS.get(i);
            int[] tempint = new int[2];
            tempint[1] = mydp.countTrading(start, temp[2])-1;
            tempint[0] = mydp.countTrading(temp[0], temp[1]);
            dcount.add(tempint);
        }
                    int sum1=0, sum2=0;
        for(int i =0; i<dcount.size(); i++) {
            int[] tempint = (int[]) dcount.get(i);
            System.out.println(tempint[0]+" "+tempint[1]);
            if(i<6) sum1+=tempint[1];
            else sum2+= tempint[1];
        }
        System.out.println(sum1+" "+sum2);*/
        double[][] data = new double[][]{{0,0},{1,1},{2,4},{3,9},{4,16},{5,25},{6,36}};
        //Spline sp = new Spline();
        Spline sp = new Spline(data);
        double[] xout = new double[]{1.5,2.5,3.5,4.5};
        printM(sp.out(xout));

    }
    public void printM(double[] mt) {
        for(short i=0; i<mt.length; i++) System.out.print(mt[i]+" ");
        System.out.println();
    }
    public void printM(double[][] mt) {
        for(short i=0; i<mt.length; i++) {
            for(short j=0; j<mt[i].length; j++) System.out.print(mt[i][j]+"\t");
            System.out.println();
        }
        System.out.println();
    }
}
