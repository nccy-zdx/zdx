package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF set;
    private int[][] data;
    private static int count;

    //O(N^2), 0 presents that it`s blocked, while 1 means opened. Initialize it with blocked.
    public Percolation(int N){
        if(N<=0){
            IllegalArgumentException e=new IllegalArgumentException();
            throw e;
        }
        data=new int[N][N];
        set=new WeightedQuickUnionUF(N*N);
        for(int i=0;i<N;++i){
            for(int j=0;j<N;++j){
                data[i][j]=0;
            }
        }
    }

    //helper method to save space.
    private void checkexception(int row, int col){
        if(row>data[0].length-1||col>data[0].length-1||row<0||col<0){
            IndexOutOfBoundsException e=new IndexOutOfBoundsException();
            throw e;
        }
    } 

    //open the site.
    public void open(int row,int col){
        checkexception(row, col);
        data[row][col]=1;
        ++count;
    }

    //check if site is open.
    public boolean isOpen(int row,int col){
        checkexception(row, col);
        return data[row][col]==1;
    }

    //check if it`s near other opens. If it is. Union them and check if a open site is a full site.
    public boolean isFull(int row,int col){
        checkexception(row, col);
        if(!isOpen(row, col)) return false; //if it`s not open, return.
        if(col!=data[0].length-1&&data[row][col+1]==1) set.union(row*data[0].length+col, row*data[0].length+col+1);  //right
        if(col!=0&&data[row][col-1]==1) set.union(row*data[0].length+col, row*data[0].length+col-1); //left
        if(row!=data[0].length-1&&data[row+1][col]==1) set.union(row*data[0].length+col, (row+1)*data[0].length+col); //upper
        if(row!=0&&data[row-1][col]==1) set.union(row*data[0].length+col, (row-1)*data[0].length+col); //down
        for(int i=0;i<data[0].length-1;++i){
            if(set.connected(row*data[0].length+col, i)) return true;
        }
        return false;
    }

    //return number of open sites whether it`s a full site or not.
    public int numberOfOpenSites(){
        return count;
    }

    //check if percolates.
    public boolean percolates(){
        for(int i=0;i<data[0].length-1;++i){
            if(isFull(data[0].length-1, i)) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        
    }
}
