package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF set;
    private int[][] data;
    private int count;
    private int[] num;
    private int numcount;
    private int iden;
    private int[] bottom;
    private int bottomcount;
    private final int Length;
    private boolean isPercolate;

    //O(N^2), 0 presents that it`s blocked, while 1 means opened. Initialize it with blocked.
    public Percolation(int N){
        if(N<=0){
            IllegalArgumentException e=new IllegalArgumentException();
            throw e;
        }
        data=new int[N][N];
        set=new WeightedQuickUnionUF(N*N);
        num=new int[N];
        bottom=new int[N];
        isPercolate=false;
        Length=N-1;
        count=0;
        numcount=0;
        bottomcount=0;
        for(int i=0;i<N;++i){
            for(int j=0;j<N;++j){
                data[i][j]=0;
            }
        }
    }

    //helper method to save space.
    private void checkexception(int row, int col){
        if(row>Length||col>Length||row<0||col<0){
            IndexOutOfBoundsException e=new IndexOutOfBoundsException();
            throw e;
        }
    } 

    //open the site.
    public void open(int row,int col){
        checkexception(row, col);
        if(data[row][col]==1) return;
        if(row==0){
            num[numcount]=col;
            ++numcount;
        }
        if(row==Length){
            bottom[bottomcount]=col;
            ++bottomcount;
        }
        data[row][col]=1;
        ++count;//annoying judge below.
        if(col!=Length&&data[row][col+1]==1) set.union(row*data[0].length+col, row*data[0].length+col+1);  //right
        if(col!=0&&data[row][col-1]==1) set.union(row*data[0].length+col, row*data[0].length+col-1); //left
        if(row!=Length&&data[row+1][col]==1) set.union(row*data[0].length+col, (row+1)*data[0].length+col); //down
        if(row!=0&&data[row-1][col]==1) set.union(row*data[0].length+col, (row-1)*data[0].length+col); //upper
    }

    //check if site is open.
    public boolean isOpen(int row,int col){
        checkexception(row, col);
        return data[row][col]==1;
    }

    //check if it`s near other opens. If it is. Union them and check if a open site is a full site.
    public boolean isFull(int row,int col){
        checkexception(row, col);
        if(data[row][col]!=1) return false; //if it`s not open, return.
        if(row==0) return true;
        iden=set.find(row*data[0].length+col);
        for(int i=0;i<numcount;++i){
            if(iden==set.find(num[i])) return true;
        }
        return false;
    }

    //return number of open sites whether it`s a full site or not.
    public int numberOfOpenSites(){
        return count;
    }

    //check if percolates.Why there are so many bugs? Why?
    public boolean percolates(){//I can`t bear any more!!! I just subtract a redundant 1.....  now deleted.
        if(isPercolate) return true;
        for(int i=0;i<bottomcount;++i){
            iden=set.find(Length*data[0].length+bottom[i]);
            for(int j=0;j<numcount;++j){
                if(iden==set.find(num[j])){
                    isPercolate=true;
                    return true;
                }
            }
        }
        return isPercolate;
    }

    public static void main(String[] args) {
        //just for test.
    }
}
