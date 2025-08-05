import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Stack;

public class Trie {

    public Trie(String path) throws Exception{
        BufferedReader br=new BufferedReader(new FileReader(path));
        String line;
        while((line=br.readLine())!=null){
            Stack<Node> s=new Stack<>();
            s.push(root);

            for(int i=0;i<line.length();++i){
                
                if(line.charAt(i)==33541){
                        Node n1=s.pop();
                        if(n1.links[128]==null){
                            Node n2=new Node();
                            n1.links[128]=n2;
                        }
                        s.push(n1.links[128]);
                        continue;
                }
                else if(line.charAt(i)==29483){
                        Node n1=s.pop();
                        if(n1.links[129]==null){
                            Node n2=new Node();
                            n1.links[129]=n2;
                        }
                        s.push(n1.links[129]);
                        continue;
                }
                else if(line.charAt(i)==38170){
                        Node n1=s.pop();
                        if(n1.links[130]==null){
                            Node n2=new Node();
                            n1.links[130]=n2;
                        }
                        s.push(n1.links[130]);
                        continue;
                }
                

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
        br.close();
    }

    private static final int R=256;

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
        if(ch==33541) return contains(x.links[128], key, d+1);
        else if(ch==29483) return contains(x.links[129], key, d+1);
        else if(ch==38170) return contains(x.links[130], key, d+1);
        
        return contains(x.links[ch],key,d+1);
    }

    public boolean isLevelContains(String key){
        return isLevelContains(root, key, 0);
    }

    private boolean isLevelContains(Node x,String key,int d){
        if(x==null) return false;
        if(d==key.length()) return true;

        char ch=key.charAt(d);
        if(ch==33541) return isLevelContains(x.links[128], key, d+1);
        else if(ch==29483) return isLevelContains(x.links[129], key, d+1);
        else if(ch==38170) return isLevelContains(x.links[130], key, d+1);

        return isLevelContains(x.links[ch],key,d+1);
    }

}