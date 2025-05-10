package hw4.puzzle;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private int minnum;
    private List<WorldState> shortpath=new ArrayList<>();

    private class SearchNode implements Comparable<SearchNode>{
        WorldState World;
        int NumOfMove;
        SearchNode previousNode;

        public SearchNode(WorldState world,int numofmove,SearchNode previousnode){
            World=world;
            NumOfMove=numofmove;
            previousNode=previousnode;
        }

        @Override
        public int compareTo(SearchNode sn){
            if((sn.World.estimatedDistanceToGoal()+sn.NumOfMove)>(World.estimatedDistanceToGoal()+NumOfMove)){
                return -1;
            }
            else if((sn.World.estimatedDistanceToGoal()+sn.NumOfMove)<(World.estimatedDistanceToGoal()+NumOfMove)){
                return 1;
            }
            else{
                return 0;
            }
        }
    }

    public Solver(WorldState initial){
        SearchNode BSM=new SearchNode(initial, 0, null);
        MinPQ<SearchNode> pq=new MinPQ<>(); //受不了了，总算找到了该用的了wc.
        while(!BSM.World.isGoal()){
            for(WorldState ws:BSM.World.neighbors()){
                if(BSM.previousNode!=null&&BSM.previousNode.World.equals(ws)) continue;
                pq.insert(new SearchNode(ws, BSM.NumOfMove+1, BSM));
            }
            BSM=pq.delMin();
        }//A* algorithm finished.
        minnum=BSM.NumOfMove;
        WorldState[] data=new WorldState[minnum+1];
        for(int i=0;BSM.previousNode!=null;++i){
            data[minnum-i]=BSM.World;
            BSM=BSM.previousNode;
        }
        data[0]=initial;
        for(int i=0;i<data.length;++i) shortpath.add(data[i]);
    }

    public int moves(){
        return minnum;
    }

    public Iterable<WorldState> solution(){
        return shortpath;
    }
}    