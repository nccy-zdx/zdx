import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import edu.princeton.cs.algs4.MinPQ;

public class test {
    private final int height=6;
    private final int width=4;
    public double[][] energies=new double[height][width];
    public double minEnergy=0;//
    public double maxEnergy=0;//
    public double mediumenergy=0;//
    public double[][] previouss=new double[height][width];
    public int count;

    public test(String filename){
        File f=new File(filename);
        try{
            Scanner s=new Scanner(f);
            int row=0;
            while(s.hasNextLine()){
                String temp=s.nextLine();
                String[] strs=temp.split("  ");
                for(int i=0;i<strs.length;++i){
                    energies[row][i]=Double.parseDouble(strs[i]);
                }
                ++row;
            }
            setMinEnergy();
            setMaxEnergy();
            mediumenergy=(maxEnergy+minEnergy)/2.0;
            s.close();
        }
        catch(FileNotFoundException e){

        }
        catch(ArrayIndexOutOfBoundsException e){

        }
    }
    
    private void setMinEnergy(){
        double min=energies[0][0];
        for(int i=0;i<width;++i){
            for(int j=0;j<height;++j){
                if(energies[j][i]<=min) min=energies[j][i];
            }
        }
        minEnergy=min;
    }

    private void setMaxEnergy(){
        double max=energies[0][0];
        for(int i=0;i<width;++i){
            for(int j=0;j<height;++j){
                if(energies[j][i]>=max) max=energies[j][i];
            }
        }
        maxEnergy=max;
    }

    public double energy(int col,int row){
        return energies[row][col];
    }

    public int[] findVerticalSeam(){
        int[] shortpath=new int[height];
        PriorityQueue<Node> path=new PriorityQueue<>();
        PriorityQueue<Node> pq=new PriorityQueue<>();
        for(int i=0;i<width;++i){
            Node row=new Node(energy(i, 0), i, 0, 0, null);
            pq.add(row);
        }
        //for(int i=0;i<width;++i){
            //Node row=new Node(energy(i, 0), i, 0, 0, null);
            Set<Node> set=new HashSet<>();
            //pq.add(row);
            while(!pq.isEmpty()){
                Node bsm=pq.remove();
                previouss[bsm.rownum][bsm.colnum]=bsm.previous;
                if(set.contains(bsm)) continue;
                else if(!set.contains(bsm)&&bsm.rownum!=height-1) set.add(bsm);
                ++count;
                if(bsm.rownum==height-1){
                    path.add(bsm);
                    //break;
                }
                else if(bsm.colnum==0){
                    Node n1=new Node(energy(bsm.colnum, bsm.rownum+1), bsm.colnum, bsm.rownum+1,bsm.previous,bsm);
                    Node n2=new Node(energy(bsm.colnum+1, bsm.rownum+1), bsm.colnum+1, bsm.rownum+1,bsm.previous,bsm);
                    checkAndAdd(pq, n1, bsm,set);
                    checkAndAdd(pq, n2, bsm,set);
                }
                else if(bsm.colnum==width-1){
                    Node n1=new Node(energy(bsm.colnum-1, bsm.rownum+1), bsm.colnum-1, bsm.rownum+1,bsm.previous,bsm);
                    Node n2=new Node(energy(bsm.colnum, bsm.rownum+1), bsm.colnum, bsm.rownum+1,bsm.previous,bsm);
                    checkAndAdd(pq, n1, bsm,set);
                    checkAndAdd(pq, n2, bsm,set);
                }
                else{
                    Node n1=new Node(energy(bsm.colnum-1, bsm.rownum+1), bsm.colnum-1, bsm.rownum+1,bsm.previous,bsm);
                    Node n2=new Node(energy(bsm.colnum, bsm.rownum+1), bsm.colnum, bsm.rownum+1,bsm.previous,bsm);
                    Node n3=new Node(energy(bsm.colnum+1, bsm.rownum+1), bsm.colnum+1, bsm.rownum+1,bsm.previous,bsm);
                    checkAndAdd(pq, n1, bsm,set);
                    checkAndAdd(pq, n2, bsm,set);
                    checkAndAdd(pq, n3, bsm,set);
                }
            }
            System.out.println(count);
            count=0;        
        //}
        Node minNode=path.remove();
        while(minNode.rownum!=0){
            shortpath[minNode.rownum]=minNode.colnum;
            minNode=minNode.preNode;
        }
        shortpath[0]=minNode.colnum;
        return shortpath;
    }

    private void checkAndAdd(PriorityQueue<Node> pq,Node n,Node bsm,Set<Node> set){ 
        if(pq.contains(n)&&bsm.previous<minUpperPrevious(n)){
            pq.remove(n);
            pq.add(n);
            previouss[n.rownum][n.colnum]=n.previous;
        }
        else if(!pq.contains(n)&&!set.contains(n)){
            pq.add(n);
            previouss[n.rownum][n.colnum]=n.previous;
        }
    }

    private double minUpperPrevious(Node n){
        if(n.colnum==0){
            double energy1=previouss[n.rownum-1][0];
            double energy2=previouss[n.rownum-1][1];
            if(energy1==0) return energy2;
            else if(energy2==0) return energy1;
            if(energy1>energy2) return energy2;
            else return energy1;
        }
        else if(n.colnum==width-1){
            double energy1=previouss[n.rownum-1][width-1];
            double energy2=previouss[n.rownum-1][width-2];
            if(energy1==0) return energy2;
            else if(energy2==0) return energy1;
            if(energy1>energy2) return energy2;
            else return energy1;
        }
        else{
            double energy1=previouss[n.rownum-1][n.colnum];
            double energy2=previouss[n.rownum-1][n.colnum+1];
            double energy3=previouss[n.rownum-1][n.colnum-1];
            
            if(energy1==0&&energy2==0) return energy3;
            else if(energy1==0&&energy3==0) return energy2;
            else if(energy2==0&&energy3==0) return energy1;
            
            if(energy1==0){
                if(energy3>energy2) return energy2;
                else return energy3;
            }
            else if(energy2==0){
                if(energy1>energy3) return energy3;
                else return energy1;
            }
            else if(energy3==0){
                if(energy1>energy2) return energy2;
                else return energy1;
            }

            if(energy1>energy2&&energy3>energy2) return energy2;
            else if(energy3>energy1&&energy2>energy1) return energy1;
            else return energy3;
        }
    }

    private class Node implements Comparable<Node>{
        public int colnum;
        public int rownum;
        public double previous;
        public Node preNode;

        public Node(double energy,int colnum,int rownum,double previous,Node pre){
            this.colnum=colnum;
            this.rownum=rownum;
            this.previous=previous+energy;
            this.preNode=pre;
        }

        @Override
        public int compareTo(Node n){        
            if(n.previous+h(n)>previous+h(this)) return -1;
            else if(n.previous+h(n)<previous+h(this)) return 1;
            else return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj==null) return false;
            if(obj==this) return true;
            if(obj.getClass()!=this.getClass()) return false;
            Node n=((Node)obj);
            if(n.rownum==rownum&&n.colnum==colnum) return true;
            else return false;
        }

        @Override
        public int hashCode() {
            return colnum<<31+rownum;
        }
    }

    private double h(Node n){
        return (height-1-n.rownum)*minEnergy;
    }

    public static void main(String[] args) {
        test t=new test("test1.txt");
        int[] seam=t.findVerticalSeam();
        double sumseam=0;
        for(int i=0;i<seam.length;++i){
            //System.out.println(seam[i]+"   "+t.energy(seam[i], i));
            sumseam+=t.energy(seam[i], i);
        }
        //System.out.println(t.count);
        System.out.println(sumseam);
    }
}