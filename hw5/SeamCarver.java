import java.awt.Color;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture){
        this.picture=picture;
    }
    
    public Picture picture(){
        return picture;
    }

    public int width(){
        return picture.width();
    }

    public int height(){
        return picture.height();
    }

    public double energy(int x,int y){
        if(x<0||x>=picture.width()||y<0||y>=picture.height()) throw new IndexOutOfBoundsException("col:"+x+"  row:"+y);
        double rx,gx,bx,ry,gy,by,sumx,sumy;
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

        sumx=rx*rx+gx*gx+bx*bx;
        sumy=ry*ry+gy*gy+by*by;

        return sumx+sumy;
    } 

    public int[] findHorizontalSeam(){
        Picture transpicture=new Picture(picture.height(), picture.width());
        for(int i=0;i<picture.width();++i){
            for(int j=0;j<picture.height();++j){
                transpicture.set(j, i, picture.get(i, j));
            }
        }
        Picture reservepic=new Picture(picture);
        picture=transpicture;
        int[] shortpath=findVerticalSeam();
        picture=reservepic;
        return shortpath;
    }

    public int[] findVerticalSeam(){
        int[] shortpath=new int[picture.height()];
        MinPQ<Node> path=new MinPQ<>();
        for(int i=0;i<picture.width();++i){
            Node row=new Node(energy(i, 0), i, 0, 0, null,0);
            for(int j=0;j<picture.width();++j){
                double goalenergy=energy(j, picture.height()-1);
                PriorityQueue<Node> pq=new PriorityQueue<>();
                Set<Node> set=new HashSet<>();
                pq.add(row);
                while(!pq.isEmpty()){
                    Node bsm=pq.remove();
                    if(set.contains(bsm)) continue;
                    else set.add(bsm);
                    if(bsm.rownum==picture.height()-1){
                        if(bsm.colnum==j){
                            path.insert(bsm);
                            break;
                        }
                        else continue;
                    }
                    else if(bsm.colnum==0){
                        Node n1=new Node(energy(bsm.colnum, bsm.rownum+1), bsm.colnum, bsm.rownum+1,bsm.previous,bsm,goalenergy);
                        Node n2=new Node(energy(bsm.colnum+1, bsm.rownum+1), bsm.colnum+1, bsm.rownum+1,bsm.previous,bsm,goalenergy);
                        if(pq.contains(n1)&&bsm.energy<=minUpperEnergy(n1)){
                            pq.remove(n1);
                            pq.add(n1);
                        }
                        else if(!pq.contains(n1)) pq.add(n1);
                        if(pq.contains(n2)&&bsm.energy<=minUpperEnergy(n2)){
                            pq.remove(n2);
                            pq.add(n2);
                        }
                        else if(!pq.contains(n2)) pq.add(n2);
                    }
                    else if(bsm.colnum==picture.width()-1){
                        Node n1=new Node(energy(bsm.colnum-1, bsm.rownum+1), bsm.colnum-1, bsm.rownum+1,bsm.previous,bsm,goalenergy);
                        Node n2=new Node(energy(bsm.colnum, bsm.rownum+1), bsm.colnum, bsm.rownum+1,bsm.previous,bsm,goalenergy);
                        if(pq.contains(n1)&&bsm.energy<=minUpperEnergy(n1)){
                            pq.remove(n1);
                            pq.add(n1);
                        }
                        else if(!pq.contains(n1)) pq.add(n1);
                        if(pq.contains(n2)&&bsm.energy<=minUpperEnergy(n2)){
                            pq.remove(n2);
                            pq.add(n2);
                        }
                        else if(!pq.contains(n2)) pq.add(n2);
                    }
                    else{
                        Node n1=new Node(energy(bsm.colnum-1, bsm.rownum+1), bsm.colnum-1, bsm.rownum+1,bsm.previous,bsm,goalenergy);
                        Node n2=new Node(energy(bsm.colnum, bsm.rownum+1), bsm.colnum, bsm.rownum+1,bsm.previous,bsm,goalenergy);
                        Node n3=new Node(energy(bsm.colnum+1, bsm.rownum+1), bsm.colnum+1, bsm.rownum+1,bsm.previous,bsm,goalenergy);
                        if(pq.contains(n1)&&bsm.energy<=minUpperEnergy(n1)){
                            pq.remove(n1);
                            pq.add(n1);
                        }
                        else if(!pq.contains(n1)) pq.add(n1);
                        if(pq.contains(n2)&&bsm.energy<=minUpperEnergy(n2)){
                            pq.remove(n2);
                            pq.add(n2);
                        }
                        else if(!pq.contains(n2)) pq.add(n2);
                        if(pq.contains(n3)&&bsm.energy<=minUpperEnergy(n3)){
                            pq.remove(n3);
                            pq.add(n3);
                        }
                        else if(!pq.contains(n3)) pq.add(n3);
                    }
                }
            }
        }
        Node minNode=path.delMin();
        while(minNode.rownum!=0){
            shortpath[minNode.rownum]=minNode.colnum;
            minNode=minNode.preNode;
        }
        shortpath[0]=minNode.colnum;
        return shortpath;
    }

    private double minUpperEnergy(Node n){
        if(n.colnum==0){
            double energy1=energy(0, n.rownum-1);
            double energy2=energy(1, n.rownum-1);
            if(energy1>energy2) return energy2;
            else return energy1;
        }
        else if(n.colnum==picture.width()-1){
            double energy1=energy(picture.width()-1, n.rownum-1);
            double energy2=energy(picture.width()-2, n.rownum-1);
            if(energy1>energy2) return energy2;
            else return energy1;
        }
        else{
            double energy1=energy(n.colnum, n.rownum-1);
            double energy2=energy(n.colnum+1, n.rownum-1);
            double energy3=energy(n.colnum-1, n.rownum-1);
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
        public double energy;
        public double goalenergy;

        public Node(double energy,int colnum,int rownum,double previous,Node pre,double goalenergy){
            this.colnum=colnum;
            this.rownum=rownum;
            this.previous=previous+energy;
            this.preNode=pre;
            this.energy=energy;
            this.goalenergy=goalenergy;
        }

        @Override
        public int compareTo(Node n){        
            if(n.previous+(picture.height()-1-n.rownum)*n.goalenergy>previous+(picture.height()-1-rownum)*goalenergy) return -1;
            else if(n.previous+(picture.height()-1-n.rownum)*n.goalenergy<previous+(picture.height()-1-rownum)*goalenergy) return 1;
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

    public void removeHorizontalSeam(int[] seam){
        if(seam.length!=picture.width()) throw new IllegalArgumentException();
        SeamRemover.removeHorizontalSeam(picture, seam);
    }

    public void removeVerticalSeam(int[] seam){
        if(seam.length!=picture.height()) throw new IllegalArgumentException();
        SeamRemover.removeVerticalSeam(picture, seam);
    }

}