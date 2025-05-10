package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v)-maze.toX(t))+Math.abs(maze.toY(v)-maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        MinPQ<Node> pq=new MinPQ<>();
        Node init=new Node(s, h(s));
        pq.insert(init);
        while (!targetFound) {
            Node v=pq.delMin();
            marked[v.w]=true;
            announce();
            for(int w:maze.adj(v.w)){
                if(!marked[w]){
                    pq.insert(new Node(w, h(w)));
                    if(w==t){
                        targetFound=true;
                        break;
                    }
                }
            }
            marked[pq.min().w]=true;
            announce();
            edgeTo[pq.min().w]=distTo[v.w]+1;
            announce();
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

    private class Node implements Comparable<Node>{
        private int w;
        private int hw;

        public Node(int w,int hw){
            this.w=w;
            this.hw=hw;
        }

        @Override
        public int compareTo(Node x){
            if(x.hw>hw) return -1;
            else if(x.hw<hw) return 1;
            else return 0;
        }
    }

}

