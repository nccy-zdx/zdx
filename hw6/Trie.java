import java.util.Stack;

public class Trie {

    public Trie(String path) {
        In in=new In(path);
        String line;
        while((line=in.readLine())!=null){
            Stack<Node> s=new Stack<>();
            s.push(root);

            for(int i=0;i<line.length();++i){
                
                if(line.charAt(i)==232) continue;
                else if(line.charAt(i)==233) continue;
                else if(line.charAt(i)==234) continue;

                Node n1=s.pop();
                if(n1.links[line.charAt(i)]==null){
                        Node n2=new Node();
                        n1.links[line.charAt(i)]=n2;
                }
                s.push(n1.links[line.charAt(i)]);
            }

            Node n=s.pop();
            n.exists=true;
        }
        in.close();
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
    
    public boolean contains(String key){
        return contains(root,key,0);
    }

    private boolean contains(Node x,String key,int d){
        if(x==null) return false;
        if(d==key.length()) return x.exists;

        char ch=key.charAt(d);
        return contains(x.links[ch],key,d+1);
    }

    public boolean isLevelContains(String key){
        return isLevelContains(root, key, 0);
    }

    private boolean isLevelContains(Node x,String key,int d){
        if(x==null) return false;
        if(d==key.length()) return true;

        char ch=key.charAt(d);
        return isLevelContains(x.links[ch],key,d+1);
    }

}