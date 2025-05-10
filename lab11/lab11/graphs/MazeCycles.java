package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean isCycle=false;
    private int[] parent;

    public MazeCycles(Maze m) {
        super(m);
        parent=new int[m.V()];
    }

    @Override
    public void solve() {
        dfs(0);
    }

    // Helper methods go here
    private void dfs(int v) {
        marked[v] = true;
        announce();
        if(isCycle) return;
        for (int w : maze.adj(v)) {
            if(isCycle) return;
            if(marked[w]&&!(parent[v]==w)){
                edgeTo[w]=v;
                announce();
                if(distTo[w]>distTo[v]+1){
                    distTo[w]=distTo[v]+1;
                    announce();
                }
                isCycle=true;
            }
            if (!marked[w]) {
                parent[w]=v;
                edgeTo[w] = v;
                announce();
                distTo[w] = distTo[v] + 1;
                announce();
                dfs(w);
            }
        }
    }
}

