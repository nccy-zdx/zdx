package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    private int size;
    private int[][] tiles;
    private final int BLANK=0;

    public Board(int[][] tiles){
        size=tiles[0].length;
        this.tiles=new int[size][size];
        for(int i=0;i<tiles[0].length;++i){
            for(int j=0;j<tiles[0].length;++j){
                this.tiles[i][j]=tiles[i][j];
            }
        }
    }

    public int tileAt(int i,int j){ 
        if(i<0||i>size-1||j<0||j>size-1) throw new IndexOutOfBoundsException("Index out of Bound.");
        return tiles[i][j];
    }

    public int size(){
        return size;
    }

    @Override
    public Iterable<WorldState> neighbors(){
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming(){
        int distance=0;
        for(int i=0;i<size;++i){
            for(int j=0;j<size;++j){
                if(tileAt(i, j)==BLANK) continue;
                if(i==size-1&&j==size-1){
                    if(tiles[i][j]!=0) ++distance;
                    break;
                }
                if((i*size+j+1)!=tiles[i][j]){
                    ++distance;
                }
            }
        }
        return distance;
    }

    public int manhattan(){
        int distance=0;
        for(int i=0;i<size;++i){
            for(int j=0;j<size;++j){
                if(tileAt(i, j)==BLANK) continue;
                if(i==size-1&&j==size-1){
                    if(tiles[i][j]==0) break;
                }
                if((i*size+j+1)!=tiles[i][j]){
                    if(tiles[i][j]%size==0){
                        distance=distance+size-1-j+Math.abs(tiles[i][j]/size-1-i);
                    }
                    else{
                        distance=distance+Math.abs(tiles[i][j]/size-i)+Math.abs(j-tiles[i][j]%size+1);
                    }
                }
            }
        }
        return distance;
    }

    /*public static void main(String[] args) {
        int[][] tile=new int[2][2];
        tile[0][0]=2;
        tile[0][1]=3;
        tile[1][0]=0;
        tile[1][1]=1;
        Board b=new Board(tile);
        System.out.println(b.manhattan());
    }*///test

    @Override
    public int estimatedDistanceToGoal(){
        return manhattan();
    }

    public boolean equals(Object y){
        if(this==y) return true;
        if(y==null) return false;
        if(y.getClass()!=this.getClass()) return false;
        Board that=(Board) y;
        if(that.size()!=size) return false;
        for(int i=0;i<size;++i){
            for(int j=0;j<size;++j){
                if(tiles[i][j]!=that.tileAt(i, j)) return false;
            }
        }
        return true;
    }


    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    @Override
    public int hashCode(){
        return tiles.hashCode();
    }

}
