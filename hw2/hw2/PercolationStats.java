package hw2;

import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {
    private static Percolation p[];
    private final int T;
    private int N;
    private static double sT;
    private static double[] data;
    private static double u=0;
    private static double sigma=0;

    public PercolationStats(int N,int T,PercolationFactory pf){
        p=new Percolation[T];
        for(int i=0;i<T;++i) p[i]=pf.make(N);
        this.T=T;
        this.N=N;
        data=new double[T];
        sT=Math.sqrt(T);
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
        for(int i=0;i<T;++i) data[i]=experiment(p[i]);
        for(int i=0;i<data.length;++i) u+=data[i];
        u=u/T;
        return u;
    }

    public double stddev(){
        if(u==0) u=mean();
        for(int i=0;i<data.length;++i){
            sigma+=(data[i]-u)*(data[i]-u);
        }
        sigma/=(T-1);
        return sigma;
    }

    public double confidenceLow(){
        return u-1.96*sigma/sT;
    }

    public double confidenceHigh(){
        return u+1.96*sigma/sT;
    }

}
