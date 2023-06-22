/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package OptionPricing;
import java.util.Random;
import java.util.Vector;
/**
 *
 * @author kailun
 */
class Simulator {
    //integer
    private short finalS, finalV, periodnum, N;
    private short[] tradingdate, discountdate;
    //fixed number
    private float unitvalue, fixedP, distP, knockinP, covP, callP, accfac;
    private float dt, sqrdt;
    private float[] initS, isp, q, corr, r, yield;
    private float[] rho, rhosr, mrr, vofV, longV, initV;
    private float[][] s,sp;
    //varying number
    private double maxline, minline;
    private double[][] maxST, minST;
    private double time, price;
    //other
    private String[] mdate;
    private DataProcessor mydp;
    private Vector ddate, cdate, output, output2;
    private Random gen;
    public Simulator (Item arg[]) {
        mydp = new DataProcessor(arg[0].getText());
        readInData(arg);
        initialize();
        for(short a=1; a<=N; a++) {
             for(short n=0; n<10000; n++) {
                 double [][] temp = HES();
                 payoff(temp, true);
                 payoff(temp, false);
             }
             System.out.println(a+"0000 generated");
        }
        mydp.save(output, "_output\\withcall.txt");
        mydp.save(output2, "_output\\withoutcall.txt");
        mydp.save(minST, isp, "_output\\minST.txt");
        mydp.save(maxST, isp, "_output\\maxST.txt");
        /*initialize();
        set0rate();
        for(short a=1; a<=N; a++) {
             for(short n=0; n<10000; n++) {
                payoff(HES());
             }
             System.out.println(a+"0000 generated");
        }
        mydp.save(output, "output_req0_withcall.txt");
        mydp.save(output2, "output_req0_withoutcall.txt");
        mydp.save(minST, isp, "output_req0_minST.txt");
        mydp.save(maxST, isp, "output_req0_maxST.txt");*/
    }
    public double[][] BS() {
        double[][] ST = new double[finalS][3];
        double[] normal = new double[3], z = new double[3];
        for(short j=0; j<3; j++) ST[0][j] = initS[j];
        for (short i=1; i<finalS; i++){
            for(short j=0; j<3; j++)    normal[j] = gen.nextGaussian();
            for(short j=0; j<3; j++)    {
                z[j] = 0;
                for(short k=0; k<3; k++) z[j] += corr[3*j+k] * normal[k];
                ST[i][j] = ST[i-1][j]* Math.exp (r[i] - initV[j]*initV[j]/2 * dt + initV[j] * sqrdt * z[j]);
            }
        }
        return ST;
    }
    public double[][] HES() {
        double[][] ST = new double[finalS][3];
        double[] normal = new double[3], z = new double[3], zz = new double[3];
        double[] v = new double[3], rootv = new double[3];
        for(short j=0; j<3; j++) {
            ST[0][j] = initS[j];
            v[j] = initV[j];
        }
        for (short i=1; i<finalS;  i++){
            for(short j=0; j<3; j++)    normal[j] = gen.nextGaussian();
            for(short j=0; j<3; j++)    {
                rootv[j] = Math.sqrt(v[j]);
                z[j] = corr[3*j] * normal[0] + corr[3*j+1] * normal[1] + corr[3*j+2] * normal[2];
                zz[j] = rho[j] * z[j] + rhosr[j] * gen.nextGaussian();
                ST[i][j] = ST[i-1][j] * (1 + r[i] - q[j] * dt + rootv[j] * sqrdt * z[j]);
                v[j] = Math.abs(v[j] + mrr[j] * (longV[j] - v[j]) * dt + zz[j] * sqrdt * vofV[j] * rootv[j]);
           }
        }
        return ST;
    }
    public void payoff(double[][] ST, boolean iscallable) {
        short[] worst = new short[finalS];
        short idx0, idx1, calltime = 11;
        double[] ratio = new double[finalS];
        boolean indc = false, indknock = false;
        double payoffamt = 0;
        //find worst performance share and its ratio
        for(short i=1; i<finalS; i++){
            ratio[i] = ST[i][0]/isp[0];
            worst[i] = 0;
            for(short j=1; j<3; j++) {
                if(ST[i][j]/isp[j] < ratio[i]) {
                    ratio[i] = ST[i][j]/isp[j];
                    worst[i] = j;
                }
            }
        }
        //call feature
        if(iscallable) {
            for(short i=0; i < periodnum-1; i++) {
                idx0 = tradingdate[i];
                idx1 = discountdate[i];
                if(ratio[idx0] > callP){
                    indc = true;
                    payoffamt += ST[idx1][worst[idx0]] / ST[idx0][worst[idx0]] / yield[idx1];
                    calltime = i;
                    break;
                }
            }
        }
        //first distribution
        idx0 = tradingdate[0];
        idx1 = discountdate[0];
        if (ratio[idx0] > fixedP)
            payoffamt += accfac / yield[idx1];
        //second to last distribution
	for (short i=1; i <= calltime; i++){
            short count = 0;
            for (short j=(short) (tradingdate[i-1] + 1);j <= tradingdate[i];j++){
                if(ratio[j] > distP) count++;
            }
            idx0 = tradingdate[i];
            idx1 = discountdate[i];
            payoffamt += accfac * count / (tradingdate[i] - tradingdate[i-1] + 1) / yield[idx1];
	}
        //conversion settlement
	if(!indc){
            //check knockin event
            for(short i=0; i<finalV; i++) {
                if(ratio[i] < knockinP) {
                    indknock = true;
                    break;
                }
            }
            idx0 = tradingdate[periodnum-1];
            idx1 = discountdate[periodnum-1];
            if((indknock && ratio[idx0] > covP) || (!indknock))
                payoffamt += ST[idx1][worst[idx0]] / ST[idx0][worst[idx0]] / yield[idx1];
             else
                payoffamt += ST[idx1][worst[idx0]] / (covP * isp[worst[idx0]]) / yield[idx1];
	}
        payoffamt = unitvalue * payoffamt;
        
        if(iscallable) output.add(calltime+"\t"+payoffamt+"\r\n");
        else output2.add(payoffamt+"\r\n");
        if(payoffamt>maxline) {copyST(ST,maxST); maxline = payoffamt;}
        else if(payoffamt<minline){ copyST(ST,minST); minline = payoffamt;}
    }
    public double getPrice() {return price;}
    public double getTime() {return time;}
    private void readInData(Item[] arg) {
        short i = 1;
        unitvalue = arg[i++].getValue();
        mdate = mydp.loadmdate(arg[i++].getText());
        s = new float[3][];
        for(short j=0; j<3; j++) s[j] = mydp.loadfloat(arg[i++].getText());
        sp = new float[3][];
        for(short j=0; j<3; j++) sp[j] = mydp.loadfloat(arg[i++].getText());
        corr = mydp.loadfloat(arg[i++].getText());
        fixedP = arg[i++].getValue();
        distP = arg[i++].getValue();
        knockinP = arg[i++].getValue();
        covP = arg[i++].getValue();
        callP = arg[i++].getValue();
        ddate = mydp.loadddate(arg[i++].getText());
        cdate = mydp.loadcdate(arg[i++].getText());
        accfac = arg[i++].getValue();
        r = mydp.loadfloat(arg[i++].getText());
        yield = mydp.loadfloat(arg[i++].getText());
        N = (short) arg[i++].getValue();
        tradingdate = new short[]{42, 81, 121, 165, 207, 247, 286, 327, 368, 412, 454, 496};
        discountdate = new short[]{44, 83, 123, 167, 209, 249, 288, 329, 370, 414, 456, 498};
        System.out.println("Loading finished.");
    }
    private void initialize() {
        periodnum = (short) ddate.size();              //should be 12
        dt = (float) (1.0 / 248.0);
        sqrdt = (float) Math.sqrt(dt);
        finalV = 497;
        finalS = 499;
        gen = new Random();
        initS = new float[3];
        isp = new float[3];
        q = new float[3];
        initV = new float[3];
        mrr = new float[3];
        longV = new float[3];
        vofV = new float[3];
        rho = new float[3];
        rhosr = new float[3];
        maxST = new double[finalS][3];
        minST = new double[finalS][3];
        for(short j=0; j<3; j++) {
            isp[j] = s[j][0];
            initS[j] = s[j][1];
            q[j] = s[j][3];
            initV[j] = s[j][4];
            mrr[j] = sp[j][0];
            longV[j] = sp[j][1];
            vofV[j] = sp[j][2];
            rho[j] = sp[j][3];
            rhosr[j] = (float) Math.sqrt(1 - rho[j]*rho[j]);
        }
        output = new Vector(10000); output2 = new Vector(10000);
        maxline = 13000; minline = 600;
        //String start = ((String[]) ddate.get(0))[0];
        //discountdate = new short[period];
        //tradingdate = new short[period];
        //for(short j=0; j<period; j++) {
            //String[] temp = (String[]) ddate.get(j);;
            //discountdate[j] = (short) (mydp.countTrading(start, temp[2]) - 1);
            //tradingdate[j] = mydp.countTrading(temp[0], temp[1]);
        //}
        //finalV = mydp.countTrading(mdate[1], mdate[2]);  //should be 497
        //finalS = mydp.countTrading(mdate[1], mdate[3]);
    }
    public void set0rate() {
        for(short j=0; j<finalS; j++) {
            r[j] = 0;
            yield[j] = 1;
        }
    }
    public void copyST(double[][] a,double[][] b) {
        for(short i=0; i<a.length; i++)
            for (int j=0; j<3; j++)
                b[i][j] = a[i][j];
    }
    public void printST(double[][] a) {
        for(short i=0; i<a.length; i++)
            System.out.println(a[i][0]/isp[0]+"\t"+a[i][1]/isp[1]+"\t"+a[i][2]/isp[2]);
        System.out.println("=================================");
    }
}