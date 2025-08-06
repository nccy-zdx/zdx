import java.util.Stack;

public class Trie {

    public Trie(String path) {
        In in=new In(path);
        String line;
        while((line=in.readLine())!=null){
            Stack<Node> s=new Stack<>();
            s.push(root);

            for(int i=0;i<line.length();++i){
                
                /*if(line.charAt(i)==33541){
                        Node n1=s.pop();
                        if(n1.links[256]==null){
                            Node n2=new Node();
                            n1.links[256]=n2;
                        }
                        s.push(n1.links[256]);
                        continue;
                }
                else if(line.charAt(i)==29483){
                        Node n1=s.pop();
                        if(n1.links[257]==null){
                            Node n2=new Node();
                            n1.links[257]=n2;
                        }
                        s.push(n1.links[257]);
                        continue;
                }
                else if(line.charAt(i)==38170){
                        Node n1=s.pop();
                        if(n1.links[258]==null){
                            Node n2=new Node();
                            n1.links[258]=n2;
                        }
                        s.push(n1.links[258]);
                        continue;
                }*/
                

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

    private static final int R=259;

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
        /*if(ch==33541) return contains(x.links[256], key, d+1);
        else if(ch==29483) return contains(x.links[257], key, d+1);
        else if(ch==38170) return contains(x.links[258], key, d+1);*/
        
        return contains(x.links[ch],key,d+1);
    }

    public boolean isLevelContains(String key){
        return isLevelContains(root, key, 0);
    }

    private boolean isLevelContains(Node x,String key,int d){
        if(x==null) return false;
        if(d==key.length()) return true;

        char ch=key.charAt(d);
        /*if(ch==33541) return isLevelContains(x.links[256], key, d+1);
        else if(ch==29483) return isLevelContains(x.links[257], key, d+1);
        else if(ch==38170) return isLevelContains(x.links[258], key, d+1);*/

        return isLevelContains(x.links[ch],key,d+1);
    }

}