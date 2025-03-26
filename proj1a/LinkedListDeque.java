public class LinkedListDeque<T> {
    private Node sentinel=new Node(null, null, null);
    private int size;
    private Node itx;

    private class Node{
        T it;
        Node prev;
        Node next;

        public Node(T it,Node m,Node n){
            this.it=it;
            prev=m;
            next=n;
        }

    }

    public LinkedListDeque(){
        size=0;
        sentinel.prev=sentinel;
        sentinel.next=sentinel;
        itx=sentinel;
    }//......

    public void addFirst(T it){
        ++size;
        Node itn=new Node(it, sentinel, sentinel.next);
        sentinel.next.prev=itn; 
        sentinel.next=itn;
    }

    public void addLast(T it){
        ++size;
        Node itn=new Node(it, sentinel.prev, sentinel);
        sentinel.prev.next=itn;
        sentinel.prev=itn;
    }

    public boolean isEmpty(){
        if(size==0) return true;
        return false;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        if(!isEmpty()){
            Node n=sentinel.next;
            while(n!=sentinel){
                System.out.print(n.it+" ");
                n=n.next;
            }
        }
        else System.out.println("null deque");
    }

    public T removeFirst(){
        if(size==0) return null;
        T itf=sentinel.next.it;
        sentinel.next.next.prev=sentinel;
        sentinel.next=sentinel.next.next;
        --size;
        return itf;
    }

    public T removeLast(){
        if(size==0) return null;
        T itl=sentinel.prev.it;
        sentinel.prev.prev.next=sentinel;
        sentinel.prev=sentinel.prev.prev;
        --size;
        return itl;
    }

    public T get(int index){
        if(index<0||index>=size) return null;
        Node n=sentinel.next;
        int i=0;
        while(i!=index){
            ++i;
            n=n.next;
        }
        return n.it;
    }

    public T getRecursive(int index){
        if(index<0||index>=size||isEmpty()) return null;
        if(index!=0) itx=itx.next;
        else return itx.next.it; 
        return getRecursive(index-1);
    }

}