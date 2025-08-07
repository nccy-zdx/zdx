import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.MaxPQ;


public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    private static Trie trie;
    private static MaxPQ<Node> maxpq=new MaxPQ<>();
    private static HashSet<String> hs;
    private static String[] strs;

    private static class Node implements Comparable<Node>{
        public String preStr;
        public List<Integer> l;

        public Node(char letter,String preString,List<Integer> list,int i,int j){
            preStr=preString+letter;
            l=new ArrayList<>();
            l.addAll(list);
            l.add(i*501+j);
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

        trie=new Trie(dictPath);
        List<String> list=new ArrayList<>();
        hs=new HashSet<>();
        
        for(int i=0;i<strs.length;++i){
            for(int j=0;j<strs[0].length();++j){
                List<Integer> l=new ArrayList<>();
                String str=new String();
                Node n=new Node(strs[i].charAt(j), str, l, i, j);
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
        strs=null;
        trie=null;

        return list;
    }

    private static void help(int i,int j,Node n){
        Node n1=new Node(strs[i].charAt(j), n.preStr, n.l, i, j);
        search(i, j, n1);
    }

    private static void search(int i,int j,Node n){
        if(i==0&&j==0){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(!n.l.contains((i+1)*501+j)) help(i+1, j, n);
                if(!n.l.contains(i*501+j+1)) help(i, j+1, n);
                if(!n.l.contains((i+1)*501+j+1)) help(i+1, j+1, n);
            }
            
        }
        else if(i==0&&j==strs[i].length()-1){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(!n.l.contains((i+1)*501+j)) help(i+1, j, n);
                if(!n.l.contains(i*501+j-1)) help(i, j-1, n);
                if(!n.l.contains((i+1)*501+j-1)) help(i+1, j-1, n);
            }
        }
        else if(i==strs.length-1&&j==0){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(!n.l.contains((i-1)*501+j)) help(i-1, j, n);
                if(!n.l.contains(i*501+j+1)) help(i, j+1, n);
                if(!n.l.contains((i-1)*501+j+1)) help(i-1, j+1, n);
            }
        }
        else if(i==strs.length-1&&j==strs[i].length()-1){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(!n.l.contains((i-1)*501+j)) help(i-1, j, n);
                if(!n.l.contains(i*501+j-1)) help(i, j-1, n);
                if(!n.l.contains((i-1)*501+j-1)) help(i-1, j-1, n);
            }
        }
        else if(i==0){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(!n.l.contains((i+1)*501+j)) help(i+1, j, n);
                if(!n.l.contains(i*501+j+1)) help(i, j+1, n);
                if(!n.l.contains((i+1)*501+j+1)) help(i+1, j+1, n);
                if(!n.l.contains((i+1)*501+j-1)) help(i+1, j-1, n);
                if(!n.l.contains(i*501+j-1)) help(i, j-1, n);
            }
        }
        else if(j==0){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(!n.l.contains((i+1)*501+j)) help(i+1, j, n);
                if(!n.l.contains(i*501+j+1)) help(i, j+1, n);
                if(!n.l.contains((i+1)*501+j+1)) help(i+1, j+1, n);
                if(!n.l.contains((i-1)*501+j+1)) help(i-1, j+1, n);
                if(!n.l.contains((i-1)*501+j)) help(i-1, j, n);
            }
        }
        else if(i==strs.length-1){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(!n.l.contains((i-1)*501+j)) help(i-1, j, n);
                if(!n.l.contains(i*501+j+1)) help(i, j+1, n);
                if(!n.l.contains((i-1)*501+j+1)) help(i-1, j+1, n);
                if(!n.l.contains((i-1)*501+j-1)) help(i-1, j-1, n);
                if(!n.l.contains(i*501+j-1)) help(i, j-1, n);
            }
        }
        else if(j==strs[i].length()-1){
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(!n.l.contains((i+1)*501+j)) help(i+1, j, n);
                if(!n.l.contains(i*501+j-1)) help(i, j-1, n);
                if(!n.l.contains((i+1)*501+j-1)) help(i+1, j-1, n);
                if(!n.l.contains((i-1)*501+j-1)) help(i-1, j-1, n);
                if(!n.l.contains((i-1)*501+j)) help(i-1, j, n);
            }
        }
        else{
            if(trie.isLevelContains(n.preStr)){
                if(n.preStr.length()>=3&&trie.contains(n.preStr)&&!hs.contains(n.preStr)){
                    maxpq.insert(n);
                    hs.add(n.preStr);
                }
                if(!n.l.contains((i+1)*501+j)) help(i+1, j, n);
                if(!n.l.contains(i*501+j+1)) help(i, j+1, n);
                if(!n.l.contains((i+1)*501+j+1)) help(i+1, j+1, n);
                if(!n.l.contains((i-1)*501+j+1)) help(i-1, j+1, n);
                if(!n.l.contains((i-1)*501+j)) help(i-1, j, n);
                if(!n.l.contains((i-1)*501+j-1)) help(i-1, j-1, n);
                if(!n.l.contains(i*501+j-1)) help(i, j-1, n);
                if(!n.l.contains((i+1)*501+j-1)) help(i+1, j-1, n);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Boggle.solve(7, "exampleBoard.txt"));
        System.out.println(Boggle.solve(7, "smallBoard.txt"));
        System.out.println(Boggle.solve(7, "smallBoard2.txt"));
    }

}