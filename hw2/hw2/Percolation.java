package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF setabove;
    private WeightedQuickUnionUF setbelow;
    private int[][] data; //record opendata with 1
    private int count;   //record the number of open
    private final int Length; //N-1
    
    //Sacrfice space will upgrade runtime a lot...
    //O(N^2), 0 presents that it`s blocked, while 1 means opened. Initialize it with blocked.
    public Percolation(int N){
        if(N<=0){
            IllegalArgumentException e=new IllegalArgumentException();
            throw e;
        }
        data=new int[N][N];
        setabove=new WeightedQuickUnionUF(N*N+2);
        setbelow=new WeightedQuickUnionUF(N*N+1);
        Length=N-1;
        count=0;
        for(int i=0;i<N;++i){
            for(int j=0;j<N;++j) data[i][j]=0;
        }
    }

    //helper method to save space and enhance readablity.
    private void checkException(int row, int col){
        if(row>Length||col>Length||row<0||col<0){
            IndexOutOfBoundsException e=new IndexOutOfBoundsException();
            throw e;
        }
    } 

    //helper method to save space and increase readablity.
    private void union(int row,int col){
        setabove.union(row, col);
        setbelow.union(row, col);
    }

    //Use disjointset,fast. O(1).
    public void open(int row,int col){
        checkException(row, col);
        if(data[row][col]==1) return;
        data[row][col]=1;
        ++count;
        if(col!=Length&&data[row][col+1]!=0) union(row*data[0].length+col, row*data[0].length+col+1); //right
        if(col!=0&&data[row][col-1]!=0) union(row*data[0].length+col, row*data[0].length+col-1); //left
        if(row!=0&&data[row-1][col]!=0) union(row*data[0].length+col, (row-1)*data[0].length+col); //upper
        else if(row==0) union(col, (Length+1)*(Length+1));
        if(row!=Length&&data[row+1][col]!=0) union(row*data[0].length+col, (row+1)*data[0].length+col); //down
        else if(row==Length) setabove.union(row*data[0].length+col, (Length+1)*(Length+1)+1);
    }

    //check if site is open. O(1).
    public boolean isOpen(int row,int col){
        checkException(row, col);
        return data[row][col]==1;
    }

    //check if it`s near other opens. If it is. Union them and check if a open site is a full site. O(N).Now O(1).
    public boolean isFull(int row,int col){
        checkException(row, col);
        if(data[row][col]!=1) return false;
        return setbelow.connected((Length+1)*(Length+1), row*data[0].length+col)&&
        setabove.connected((Length+1)*(Length+1), row*data[0].length+col);
    }

    //return number of open sites whether it`s a full site or not. O(1).
    public int numberOfOpenSites(){
        return count;
    }

    //check if percolates. O(N^2). Needed to be improved to at least O(N).Now O(1).Now O(N)....,Now O(1). All O(1).wuwu
    public boolean percolates(){
        return setabove.connected((Length+1)*(Length+1), (Length+1)*(Length+1)+1);
    }

    public static void main(String[] args) { //just for test. 
    }    
}
