package hw2;

import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {
    private  Percolation p[];
    private final int T; //repetitive time.
    private int N;
    private double sqrtT; //sqrt of T.
    private double[] data; //collect experiment data.
    private double u; //average
    private double sigma; //standard deviation.

    public PercolationStats(int N,int T,PercolationFactory pf){
        if(N<=0||T<=0){
            IllegalArgumentException e=new IllegalArgumentException();
            throw e;
        }
        p=new Percolation[T];
        this.T=T;
        this.N=N;
        data=new double[T];
        sqrtT=Math.sqrt(T);
        u=0;
        sigma=0;
        for(int i=0;i<T;++i){
            p[i]=pf.make(N);
            data[i]=experiment(p[i]);
            u+=data[i];
        }
        u/=T;
        for(int i=0;i<T;++i) sigma+=(data[i]-u)*(data[i]-u);
        sigma/=(T-1);
        sigma=Math.sqrt(sigma);
    }

    private double experiment(Percolation p){
        int count;
        for(count=0;count<N*N;++count){
            int i=StdRandom.uniform(0, N-1);
            int j=StdRandom.uniform(0, N-1);
            p.open(i, j);
            if(p.percolates()) break;
        }
        return count;
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
