package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF set;
    private int[][] data; //record opendata with 1
    private int[][] fulldata; //record fulldata with 1.
    private int count;   //record the number of open
    private final int Length; //N-1
    private boolean isPercolate;
    
    //sacrifice space for time.
    //O(N^2), 0 presents that it`s blocked, while 1 means opened. Initialize it with blocked.
    public Percolation(int N){
        if(N<=0){
            IllegalArgumentException e=new IllegalArgumentException();
            throw e;
        }
        data=new int[N][N];
        fulldata=new int[N][N];
        set=new WeightedQuickUnionUF(N*N);
        isPercolate=false;
        Length=N-1;
        count=0;
        for(int i=0;i<N;++i){
            for(int j=0;j<N;++j){
                data[i][j]=0;
                fulldata[i][j]=0;
            }
        }
    }

    //helper method to save space.
    private void checkException(int row, int col){
        if(row>Length||col>Length||row<0||col<0){
            IndexOutOfBoundsException e=new IndexOutOfBoundsException();
            throw e;
        }
    } 

    //open the site. O(1). how to implement update?
    public void open(int row,int col){
        checkException(row, col);
        if(data[row][col]==1) return;   //avoid repeated case.
        data[row][col]=1;  //set it to open.
        ++count; //updata counter.
        if(col!=Length&&data[row][col+1]==1) set.union(row*data[0].length+col, row*data[0].length+col+1); //right
        if(col!=0&&data[row][col-1]==1) set.union(row*data[0].length+col, row*data[0].length+col-1); //left
        if(row!=Length&&data[row+1][col]==1) set.union(row*data[0].length+col, (row+1)*data[0].length+col); //down
        if(row!=0&&data[row-1][col]==1) set.union(row*data[0].length+col, (row-1)*data[0].length+col); //upper*/
        if(row==0){
            if(data[0].length==1) isPercolate=true;
            union_full(row, col);
        } //top open is always full.
        else{
            if(col!=Length&&fulldata[row][col+1]==1) union_full(row, col); //right
            else if(col!=0&&fulldata[row][col-1]==1) union_full(row, col); //left
            else if(row!=Length&&fulldata[row+1][col]==1) union_full(row, col); //down
            else if(row!=0&&fulldata[row-1][col]==1){
                if(row==Length) isPercolate=true;
                union_full(row, col); 
            } //upper
        }
    }

    //for transport. Recursion.
    private void union_full(int row,int col){
        fulldata[row][col]=1;
        if(row==Length&&!isPercolate) isPercolate=true;
        if(col!=Length&&data[row][col+1]==1&&fulldata[row][col+1]!=1) union_full(row, col+1); //right
        if(col!=0&&data[row][col-1]==1&&fulldata[row][col-1]!=1) union_full(row, col-1); //left
        if(row!=Length&&data[row+1][col]==1&&fulldata[row+1][col]!=1) union_full(row+1, col); //down
        if(row!=0&&data[row-1][col]==1&&fulldata[row-1][col]!=1) union_full(row-1, col);  //upper
    }

    //check if site is open. O(1).
    public boolean isOpen(int row,int col){
        checkException(row, col);
        return data[row][col]==1;
    }

    //check if it`s near other opens. If it is. Union them and check if a open site is a full site. O(N).
    public boolean isFull(int row,int col){
        checkException(row, col);
        return fulldata[row][col]==1;
    }

    //return number of open sites whether it`s a full site or not. O(1).
    public int numberOfOpenSites(){
        return count;
    }

    //check if percolates. O(N^2). Needed to be improved to at least O(N).Now O(1).
    public boolean percolates(){      
        return isPercolate;
    }

    public static void main(String[] args) {
        //just for test.
    }
}
