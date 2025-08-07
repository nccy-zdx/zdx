import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.MaxPQ;


public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    private static Trie trie=new Trie(dictPath);
    private static String[] strs;
    private static MaxPQ<Node> maxpq=new MaxPQ<>();
    private static HashSet<String> hs;

    private static class Node implements Comparable<Node>{
        public int[][] premap;
        public String preStr;

        public Node(char letter,String preString,int[][] premap,int i,int j){
            preStr=preString;
            preStr+=letter;
            this.premap=new int[premap.length][premap[0].length];
            for(int m=0;m<premap.length;++m){
                for(int n=0;n<premap[0].length;++n){
                    this.premap[m][n]=premap[m][n];
                }
            }
            this.premap[i][j]=1;
        }

        @Override
        public int compareTo(Node n){
            if(n.preStr.length()<preStr.length()) return 1;
            else if(n.preStr.length()==preStr.length()){
                for(int i=0;i<preStr.length();++i){
                    if(n.preStr.charAt(i)>preStr.charAt(i)){
                        return 1;
                    }
                    else if(n.preStr.charAt(i)<preStr.charAt(i)){
                        return -1;
                    }
                }
                return 0;
            }
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
        hs=new HashSet<>();
        
        for(int i=0;i<strs.length;++i){
            for(int j=0;j<strs[i].length();++j){
                int[][] premap=new int[strs.length][strs[i].length()];
                String str=new String();
                Node n=new Node(strs[i].charAt(j), str, premap, i, j);
                search(i, j, n);
            }
        }

        Iterator<Node> it=maxpq.iterator();
        int count=0;
        while(it.hasNext()){
            list.add(it.next().preStr);
            if(count>=k-1) break;
            ++count;
        }
        MaxPQ<Node> pq=new MaxPQ<>();
        maxpq=pq;

        return list;
    }

    private static void help(int i,int j,Node n){
        Node n1=new Node(strs[i].charAt(j), n.preStr, n.premap, i, j);
        search(i, j, n1);
    }

    private static void search(int i,int j,Node n){
        if(i==0&&j==0){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i+1][j+1]!=1) help(i+1, j+1, n);
            }
            
        }
        else if(i==0&&j==strs[i].length()-1){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
                if(n.premap[i+1][j-1]!=1) help(i+1, j-1, n);
            }
        }
        else if(i==strs.length-1&&j==0){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i-1][j+1]!=1) help(i-1, j+1, n);
            }
        }
        else if(i==strs.length-1&&j==strs[i].length()-1){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
                if(n.premap[i-1][j-1]!=1) help(i-1, j-1, n);
            }
        }
        else if(i==0){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i+1][j+1]!=1) help(i+1, j+1, n);
                if(n.premap[i+1][j-1]!=1) help(i+1, j-1, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
            }
        }
        else if(j==0){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i+1][j+1]!=1) help(i+1, j+1, n);
                if(n.premap[i-1][j+1]!=1) help(i-1, j+1, n);
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
            }
        }
        else if(i==strs.length-1){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
                if(n.premap[i][j+1]!=1) help(i, j+1, n);
                if(n.premap[i-1][j+1]!=1) help(i-1, j+1, n);
                if(n.premap[i-1][j-1]!=1) help(i-1, j-1, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
            }
        }
        else if(j==strs[i].length()-1){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(n.premap[i+1][j]!=1) help(i+1, j, n);
                if(n.premap[i][j-1]!=1) help(i, j-1, n);
                if(n.premap[i+1][j-1]!=1) help(i+1, j-1, n);
                if(n.premap[i-1][j-1]!=1) help(i-1, j-1, n);
                if(n.premap[i-1][j]!=1) help(i-1, j, n);
            }
        }
        else{
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
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

}