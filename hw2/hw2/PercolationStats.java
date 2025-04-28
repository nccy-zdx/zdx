package hw2;

public class PercolationStats {
    private static Percolation p;
    private final int T;
    private double mean;

    public PercolationStats(int N,int T,PercolationFactory pf){
        p=pf.make(N);
        this.T=T;
    }

    public double mean(){
        return 1;
    }

    public double stddev(){
        return 1;
    }

    public double confidenceLow(){
        return 1;
    }

    public double confidenceHigh(){
        return 1;
    }

}
