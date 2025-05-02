package hw2;

import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {
    private double sqrtT; //sqrt of T.
    private double[] data; //collect experiment data.
    private double u; //average
    private double sigma; //standard deviation.
    private int j;
    private int k;

    public PercolationStats(int N,int T,PercolationFactory pf){
        if(N<=0||T<=0){
            IllegalArgumentException e=new IllegalArgumentException();
            throw e;
        }
        data=new double[T];
        sqrtT=Math.sqrt(T);
        u=0;
        sigma=0;
        for(int i=0;i<T;++i){
            Percolation p=pf.make(N);
            for(;p.numberOfOpenSites()<N*N;){
                k=StdRandom.uniform(0, N);
                j=StdRandom.uniform(0, N);
                p.open(k, j);
                if(p.percolates()) break;
            }
            data[i]=p.numberOfOpenSites();
            data[i]/=(N*N);
            u+=data[i];
        }
        u/=T;
        for(int i=0;i<T;++i) sigma+=(data[i]-u)*(data[i]-u);
        sigma/=(T-1);
        sigma=Math.sqrt(sigma);
    }

    public double mean(){
        return u;
    }

    public double stddev(){
        return sigma;
    }

    public double confidenceLow(){
        return u-1.96*sigma/sqrtT;
    }

    public double confidenceHigh(){
        return u+1.96*sigma/sqrtT;
    }

}