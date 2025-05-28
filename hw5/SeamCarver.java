import java.awt.Color;

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
        if(x<0||x>=picture.width()||y<0||y>=picture.height()) throw new IndexOutOfBoundsException("coordinates are out of bound.");
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
            Node row0=new Node(energy(i, 0), i, 0,0,null);
            MinPQ<Node> pq=new MinPQ<>();
            pq.insert(row0);
            while (!pq.isEmpty()) {
                Node bsm=pq.delMin();
                if(bsm.rownum==picture.height()-1){
                    path.insert(bsm);
                    break;
                }
                else if(bsm.colnum==0){
                    if(energy(bsm.colnum, bsm.rownum+1)>energy(bsm.colnum+1, bsm.rownum+1)){
                        pq.insert(new Node(energy(bsm.colnum+1, bsm.rownum+1), bsm.colnum, bsm.rownum+1,bsm.previous,bsm));
                    }
                    else pq.insert(new Node(energy(bsm.colnum, bsm.rownum+1), bsm.colnum+1, bsm.rownum+1,bsm.previous,bsm));
                }
                else if(bsm.colnum==picture.width()-1){
                    if(energy(bsm.colnum, bsm.rownum+1)>energy(bsm.colnum-1, bsm.rownum+1)){
                        pq.insert(new Node(energy(bsm.colnum-1, bsm.rownum+1), bsm.colnum-1, bsm.rownum+1,bsm.previous,bsm));
                    }
                    else pq.insert(new Node(energy(bsm.colnum, bsm.rownum+1), bsm.colnum, bsm.rownum+1,bsm.previous,bsm));
                }
                else{
                    if(energy(bsm.colnum, bsm.rownum+1)>energy(bsm.colnum-1, bsm.rownum+1)
                    &&energy(bsm.colnum+1, bsm.rownum+1)>energy(bsm.colnum-1, bsm.rownum+1)){
                        pq.insert(new Node(energy(bsm.colnum-1, bsm.rownum+1), bsm.colnum-1, bsm.rownum+1,bsm.previous,bsm));
                    }
                    else if(energy(bsm.colnum-1, bsm.rownum+1)>energy(bsm.colnum+1, bsm.rownum+1)
                    &&energy(bsm.colnum, bsm.rownum+1)>energy(bsm.colnum+1, bsm.rownum+1)){
                        pq.insert(new Node(energy(bsm.colnum+1, bsm.rownum+1), bsm.colnum+1, bsm.rownum+1,bsm.previous,bsm));
                    }
                    else pq.insert(new Node(energy(bsm.colnum, bsm.rownum+1), bsm.colnum, bsm.rownum+1,bsm.previous,bsm));
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

    private class Node implements Comparable<Node>{
        public int colnum;
        public int rownum;
        public double previous;
        public Node preNode;
        public double energy;

        public Node(double energy,int colnum,int rownum,double previous,Node pre){
            this.colnum=colnum;
            this.rownum=rownum;
            this.previous=previous+energy;
            this.preNode=pre;
            this.energy=energy;
        }

        @Override
        public int compareTo(Node n){
            if(n.previous>previous) return -1;
            else if(n.previous<previous) return 1;
            else return 0;
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