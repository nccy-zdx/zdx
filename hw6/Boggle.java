import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.MaxPQ;


public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    private static Trie trie=new Trie(dictPath);
    private static String[] strs;
    private static MaxPQ<Node> maxpq=new MaxPQ<>();

    private static class Node implements Comparator<Node>{
        public char letter;
        public int[][] premap;
        public String preStr;
        public Node preNode;

        public Node(char letter,Node preNode,String preString,int[][] premap,int i,int j){
            this.letter=letter;
            this.preNode=preNode;
            preStr=preString;
            preStr+=letter;
            this.premap=premap;
            this.premap[i][j]=1;
        }

        @Override
        public int compare(Node n,Node x){
            if(n.preStr.length()>x.preStr.length()) return 1;
            else if(n.preStr.length()==x.preStr.length()) return 0;
            else return -1;
        }

    }

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath){
        if(k<=0) throw new IllegalArgumentException("k is non-positive. k:"+k);
        In in=new In(boardFilePath);
        strs=in.readAllLines();
        for(int i=1;i<strs.length;++i){
            if(strs[i].length()!=strs[0].length()) throw new IllegalArgumentException(i+"line is wrong");
        }

        List<String> list=new ArrayList<>();
        
        for(int i=0;i<strs.length;++i){
            for(int j=0;j<strs[i].length();++j){
                int[][] premap=new int[strs.length][strs[i].length()];
                String str=new String();
                Node n=new Node(strs[i].charAt(j), null, str, premap, i, j);
                search(i, j, n);
            }
        }

        Iterator<Node> it=maxpq.iterator();
        int count=0;
        while(it.hasNext()){
            list.add(it.next().preStr);
            if(count>=k) break;
            System.out.println(list.get(count)+"  "+count);
            ++count;
        }
        MaxPQ<Node> pq=new MaxPQ<>();
        maxpq=pq;

        return list;
    }

    private static void help(int i,int j,Node n){
        Node n1=new Node(strs[i].charAt(j), n, n.preStr, n.premap, i, j);
        search(i, j, n1);
    }

    private static void search(int i,int j,Node n){
        if(i==0&&j==0){
            if(trie.contains(n.preStr)){
                if(n.preStr.length()>=3) maxpq.insert(n);
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i+1][j+1]!=1) help(i+1, j+1, n);
            }
        }
        else if(i==0&&j==strs[i].length()-1){
            if(trie.contains(n.preStr)){
                if(n.preStr.length()>=3) maxpq.insert(n);
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
                if(n.premap[i+1][j-1]!=1) help(i+1, j-1, n);
            }
        }
        else if(i==strs.length-1&&j==0){
            if(trie.contains(n.preStr)){
                if(n.preStr.length()>=3) maxpq.insert(n);
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i-1][j+1]!=1) help(i-1, j+1, n);
            }
        }
        else if(i==strs.length-1&&j==strs[i].length()-1){
            if(trie.contains(n.preStr)){
                if(n.preStr.length()>=3) maxpq.insert(n);
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
                if(n.premap[i-1][j-1]!=1) help(i-1, j-1, n);
            }
        }
        else if(i==0){
            if(trie.contains(n.preStr)){
                if(n.preStr.length()>=3) maxpq.insert(n);
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i+1][j+1]!=1) help(i+1, j+1, n);
                if(n.premap[i+1][j-1]!=1) help(i+1, j-1, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
            }
        }
        else if(j==0){
            if(trie.contains(n.preStr)){
                if(n.preStr.length()>=3) maxpq.insert(n);
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i+1][j+1]!=1) help(i+1, j+1, n);
                if(n.premap[i-1][j+1]!=1) help(i-1, j+1, n);
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
            }
        }
        else if(i==strs.length-1){
            if(trie.contains(n.preStr)){
                if(n.preStr.length()>=3) maxpq.insert(n);
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i-1][j+1]!=1) help(i-1, j+1, n);
                if(n.premap[i-1][j-1]!=1) help(i-1, j-1, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
            }
        }
        else if(j==strs[i].length()-1){
            if(trie.contains(n.preStr)){
                if(n.preStr.length()>=3) maxpq.insert(n);
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
                if(n.premap[i+1][j-1]!=1) help(i+1, j-1, n);
                if(n.premap[i-1][j-1]!=1) help(i-1, j-1, n);
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
            }
        }
        else{
            if(trie.contains(n.preStr)){
                if(n.preStr.length()>=3) maxpq.insert(n);
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i+1][j+1]!=1) help(i+1, j+1, n);
                if(n.premap[i-1][j+1]!=1) help(i-1, j+1, n);
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
                if(n.premap[i-1][j-1]!=1) help(i-1, j-1, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
                if(n.premap[i+1][j-1]!=1) help(i+1, j-1, n);
            }
        }
    }

    public static void main(String[] args) {
        try {
            Boggle.solve(7, "exampleBoard.txt");
        } catch (Exception e){
            System.out.println(111);
        }
    }

}