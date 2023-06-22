/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OptionPricing;

/**
 *
 * @author kailun
 */
public class Spline {
    double[][] s;
    double[] x;
    short n;
    public Spline(double[][] data) {
        n = (short) (data.length - 1);
        s = new double[n][4];
        x = new double[n];
        double[] h = new double[n];
        double[] d = new double[n];
        double[] u = new double[n-1];
        double[] m = new double[n+1];
        for(short i=0; i<n; i++) {
            h[i] = data[i+1][0] - data[i][0];
            d[i] = (data[i+1][1] - data[i][1]) / h[i];
        }
        for(short i=0; i<n-1; i++) u[i] = 6 * (d[i+1] - d[i]);
        m[0] = 0; m[n] = 0;
        u[0] = u[0] - h[0] * m[0];
        u[n-2] = u[n-2] - h[n-1] * m[n];
        //double[][] A = new double[n-1][n-1];
        //for(short i=0; i<n-1; i++) A[i][i] = 2 * (h[i+1] + h[i]);
        //for(short i=0; i<n-2; i++) A[i][i+1] = h[i+1];
        //for(short i=1; i<n-1; i++) A[i][i-1] = h[i];
        {
            double[] a = new double[n-1];
            double[] b = new double[n-1];
            double[] c = new double[n-1];
            for(short i=0; i<n-1; i++) {
                a[i] = h[i];
                b[i] = 2*(h[i]+h[i+1]);
                c[i] = h[i+1];
            }
            double[] temp = solve2(a,b,c,u);
            for(short i=1; i<n; i++) m[i] = temp[i-1];
        }
        for(short i=0; i<n; i++) {
            x[i] = data[i][0];
            s[i][0] = data[i][1];
            s[i][1] = d[i] - h[i] * (2*m[i]+m[i+1])/6;
            s[i][2] = 0.5 * m[i];
            s[i][3] = (m[i+1] - m[i]) / (6*h[i]);
        }

    }
    public Spline() {
        double[][] A = new double[][]{
            {1,2,0,0},{3,4,5,0},{0,6,7,8},{0,0,9,10}
        };
        double[] b = new double[]{1,1,1,1};
        solve(A,b);
    }
    public double[] out(double xout[]) {
        short length = (short) xout.length;
        double[] yout = new double[length];
        for(short i=0; i<length; i++) {
            short j;
            double xi = xout[i];
            for(j=0; j<n; j++) if(x[j+1] >= xi) break;
            yout[i] = s[j][0] + s[j][1] * (xi-x[j]) + s[j][2] * Math.pow((xi-x[j]),2) + s[j][3] * Math.pow(xi-x[j],3);
        }
        return yout;
    }
    public double[] solve(double[][] A, double[] b) {
        short M = (short) A.length;
        double[][] L = new double[M][M];
        for (int i=0; i<M; i++) {
            for (int j=0; j<M; j++) {
                if(i==j) L[i][j] = 1;
                //else L[i][j] = 0;
            }
        }
        for (int i=0; i<M; i++) {
            for (int j=i+1; j<M; j++) {
                L[j][i] = A[j][i]/A[i][i];
                A[j][i] = 0;
                for (int k=i+1; k<M; k++) A[j][k] = A[j][k] - L[j][i]*A[i][k];
            }
        }
        printM(L);
        printM(A);
        double[] y = new double[M];
        for(short i=0; i<M; i++) {
            double sum = 0;
            for(int j=0; j<i; j++) sum += y[j] * L[i][j];
            y[i] = (b[i] - sum) / L[i][i];
        }
        double[] x = new double[M];
        for(short i=(short) (M - 1); i>=0; i--) {
            double sum = 0;
            for(short j=(short) (i + 1); j<M; j++) sum += x[j] * A[i][j];
            x[i] = (y[i] - sum) / A[i][i];
        }
        return x;
    }
    public double[] solve2 (double[] a, double[] b, double[] c, double[] d) {
        short M = (short) d.length;
        double[] x = new double[M];
        double[] a2 = new double[M];
        double[] b2 = new double[M];
        double[] d2 = new double[M];
        b2[0] = b[0];
        d2[0] = d[0];
        for(short i=1; i<M; i++) {
            a2[i] = a[i] / b2[i-1];
            b2[i] = b[i] - a[i] * c[i-1] / b2[i-1];
            d2[i] = d[i] - a2[i] * d2[i-1];
        }
        x[M-1] = d2[M-1]/b2[M-1];
        for (short i=(short) (M - 2); i>=0; i--) x[i] = (d2[i] - c[i] * x[i+1]) / b2[i];
        return x;
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