import java.awt.Color;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energies;
    private Picture transPicture;
    private boolean isHorizonal=false;
    private int[] seam;
    private double minEnergy;

    public SeamCarver(Picture picture){
        this.picture=new Picture(picture);
        minEnergy=energy(0, 0);
    }

    private void settrans(){
        transPicture=new Picture(picture.height(), picture.width());
        for(int i=0;i<picture.width();++i){
            for(int j=0;j<picture.height();++j){
                transPicture.set(j, i, picture.get(i, j));
            }
        }
    }

    private void setEnergies(int[] seam){
        if(seam==null){
            energies=new double[picture.height()][picture.width()];
            for(int i=0;i<picture.width();++i){
                for(int j=0;j<picture.height();++j){
                    energies[j][i]=energy(i, j);
                    if(minEnergy>energies[j][i]) minEnergy=energies[j][i];
                }
            }
        }
        else{
            for(int i=0;i<seam.length;++i){
                if(seam[i]==0){
                    energies[i][0]=energy(0, i);
                    energies[i][picture.width()-1]=energy(picture.width()-1, i);
                } 
                else if(seam[i]==picture.width()){
                    energies[i][picture.width()-1]=energy(picture.width()-1, i);
                    energies[i][0]=energy(0, i); 
                } 
                else{
                    energies[i][seam[i]-1]=energy(seam[i]-1, i);
                    energies[i][seam[i]]=energy(seam[i], i);
                }
                for(int j=seam[i]+1;j<picture.width();++j){
                    energies[i][j]=energies[i][j+1];
                }
            }
        }
    }
    
    public Picture picture(){
        return new Picture(picture);
    }

    public int width(){
        return picture.width();
    }

    public int height(){
        return picture.height();
    }

    public double energy(int x,int y){
        if(x<0||x>=picture.width()||y<0||y>=picture.height()) throw new IndexOutOfBoundsException("col:"+x+"  row:"+y);
        double rx,gx,bx,ry,gy,by;
        Color front,back,upper,down;

        if(x==picture.width()-1) front=picture.get(0, y);
        else front=picture.get(x+1, y);
        if(x==0) back=picture.get(picture.width()-1, y);
        else back=picture.get(x-1, y);
        if(y==picture.height()-1) down=picture.get(x, 0);
        else down=picture.get(x, y+1);
        if(y==0) upper=picture.get(x, picture.height()-1);
        else upper=picture.get(x, y-1);

        rx=Math.abs(front.getRed()-back.getRed());
        gx=Math.abs(front.getGreen()-back.getGreen());
        bx=Math.abs(front.getBlue()-back.getBlue());
        ry=Math.abs(down.getRed()-upper.getRed());
        gy=Math.abs(down.getGreen()-upper.getGreen());
        by=Math.abs(down.getBlue()-upper.getBlue());

        return rx*rx+gx*gx+bx*bx+ry*ry+gy*gy+by*by;
    } 

    public int[] findHorizontalSeam(){
        Picture reservepic=new Picture(picture);
        isHorizonal=true;
        settrans();
        picture=transPicture;
        int[] shortpath=findVerticalSeam();
        picture=reservepic;
        isHorizonal=false;
        return shortpath;
    }

    public int[] findVerticalSeam(){
        Node minNode=null;
        int[] shortpath=new int[picture.height()];
        Set<Node> set=new HashSet<>();
        PriorityQueue<Node> pq=new PriorityQueue<>();
        double[][] previouss;
        if(isHorizonal){
            setEnergies(seam);
            previouss=new double[transPicture.height()][transPicture.width()];
        }
        else{
            setEnergies(seam);
            previouss=new double[picture.height()][picture.width()];
        }
        for(int i=0;i<picture.width();++i){
            Node row=new Node(i, 0, 0, null);
            pq.add(row);
        }//W

        while(!pq.isEmpty()){
            Node bsm=pq.remove();
            previouss[bsm.rownum][bsm.colnum]=bsm.previous;
            if(!set.contains(bsm)) set.add(bsm);
            if(bsm.rownum==picture.height()-1){
                minNode=bsm;
                break;
            }
            else if(bsm.colnum==0){
                Node n1=new Node(bsm.colnum, bsm.rownum+1,bsm.previous,bsm);
                Node n2=new Node(bsm.colnum+1, bsm.rownum+1,bsm.previous,bsm);
                checkAndAdd(n1, pq, bsm, set, previouss);
                checkAndAdd(n2, pq, bsm, set, previouss);
            }
            else if(bsm.colnum==picture.width()-1){
                Node n1=new Node(bsm.colnum-1, bsm.rownum+1,bsm.previous,bsm);
                Node n2=new Node(bsm.colnum, bsm.rownum+1,bsm.previous,bsm);
                checkAndAdd(n1, pq, bsm, set, previouss);
                checkAndAdd(n2, pq, bsm, set, previouss);
            }
            else{
                Node n1=new Node(bsm.colnum-1, bsm.rownum+1,bsm.previous,bsm);
                Node n2=new Node(bsm.colnum, bsm.rownum+1,bsm.previous,bsm);
                Node n3=new Node(bsm.colnum+1, bsm.rownum+1,bsm.previous,bsm);
                checkAndAdd(n1, pq, bsm, set, previouss);
                checkAndAdd(n2, pq, bsm, set, previouss);
                checkAndAdd(n3, pq, bsm, set, previouss);
            }
        }//W*H

        while(minNode.rownum!=0){
            shortpath[minNode.rownum]=minNode.colnum;
            minNode=minNode.preNode;
        }//H
        shortpath[0]=minNode.colnum;

        return shortpath;
    }

    private void checkAndAdd(Node n,PriorityQueue<Node> pq,Node bsm,Set<Node> set,double[][] previouss){
        if(set.contains(n)) return;
        if(pq.contains(n)&&bsm.previous<minUpperPrevious(n,previouss)){
            pq.remove(n);
            pq.add(n);
            previouss[n.rownum][n.colnum]=n.previous;
        }
        else if(!pq.contains(n)){
            pq.add(n);
            previouss[n.rownum][n.colnum]=n.previous;
        }
    }

    private double minUpperPrevious(Node n,double[][] previouss){
        if(n.colnum==0){
            double energy1=previouss[n.rownum-1][0];
            double energy2=previouss[n.rownum-1][1];
            if(energy1==0) return energy2;
            else if(energy2==0) return energy1;
            if(energy1>energy2) return energy2;
            else return energy1;
        }
        else if(n.colnum==picture.width()-1){
            double energy1=previouss[n.rownum-1][picture.width()-1];
            double energy2=previouss[n.rownum-1][picture.width()-2];
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

        public Node(int colnum,int rownum,double previous,Node pre){
            this.colnum=colnum;
            this.rownum=rownum;
            this.previous=previous+energies[rownum][colnum];
            this.preNode=pre;
        }

        @Override
        public int compareTo(Node n){      
            if(n.previous+(picture.height()-1-n.rownum)*minEnergy>previous+(picture.height()-1-rownum)*minEnergy) return -1;
            else if(n.previous+(picture.height()-1-n.rownum)*minEnergy<previous+(picture.height()-1-rownum)*minEnergy) return 1;
            else return 0;
        }

        @Override
        public boolean equals(Object obj) {
            Node n=((Node)obj);
            if(n.rownum==rownum&&n.colnum==colnum) return true;
            else return false;
        }

        @Override
        public int hashCode() {
            return (rownum+53)*53+colnum;
        }//哈希啊哈希，你让我好找又好想啊。
        
    }

    public void removeHorizontalSeam(int[] seam){
        if(seam.length!=picture.width()) throw new IllegalArgumentException();
        picture=SeamRemover.removeHorizontalSeam(picture, seam);
        this.seam=seam;
    }

    public void removeVerticalSeam(int[] seam){
        if(seam.length!=picture.height()) throw new IllegalArgumentException();
        picture=SeamRemover.removeVerticalSeam(picture, seam);
        this.seam=seam;
    }

}