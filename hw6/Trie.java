import java.io.BufferedReader;
import java.io.FileReader;

public class Trie {

    public Trie(String path){
        try{
            BufferedReader br=new BufferedReader(new FileReader(path));
            String line;
            while((line=br.readLine())!=null){
                put(line);
            }
            br.close();
        }
        catch(Exception e){}
    }

    private static final int R=128;

    class Node{
        boolean exists;
        Node[] links;

        public Node(){
            links=new Node[R];
            exists=false;
        }
    }

    public Node root=new Node();
    
    public void put(String key){
        put(root, key, 0);
    }

    private Node put(Node x,String key,int d){
        if(x==null) x=new Node();
        if(d==key.length()){
            x.exists=true;
            return x;
        }

        char ch=key.charAt(d);
        x.links[ch]=put(x.links[ch], key, d+1);
        return x;
    }

    public boolean contains(String key){
        return contains(root,key,0);
    }

    private boolean contains(Node x,String key,int d){
        if(x==null) return false;
        if(d==key.length()) return x.exists;

        char ch=key.charAt(d);
        return contains(x.links[ch],key,d+1);
    }

    public static void main(String[] args) {
        Trie t=new Trie("words.txt");
        System.out.println(t.contains("se"));
    }
}