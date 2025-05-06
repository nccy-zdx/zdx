package lab11.graphs;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;
    private boolean targetFound=false;


    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze=m;
        s=maze.xyTo1D(sourceX, sourceY);
        t=maze.xyTo1D(targetX, targetY);
        distTo[s]=0;
        edgeTo[s]=s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> q=new ConcurrentLinkedQueue<>();
        q.add(s);
        marked[s]=true;
        announce();
        while(!q.isEmpty()){
            if(targetFound) return;
            int v=q.remove();
            for(int w:maze.adj(v)){
                if(targetFound) return;
                if(!marked[w]){
                    q.add(w);
                    edgeTo[w]=v;
                    announce();
                    distTo[w]=distTo[v]+1;
                    announce();
                    marked[w]=true;
                    announce();
                    if(w==t){
                        targetFound=true;
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}