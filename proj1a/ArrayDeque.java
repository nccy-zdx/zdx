public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;

    private void resize(int capacity){
        T[] arr=(T[]) new Object[capacity];
        System.arraycopy(items, nextfirst+1, arr, arr.length+nextfirst-items.length, items.length-nextfirst-1);
        System.arraycopy(items, 0, arr, 0, nextlast);
        nextfirst=arr.length+nextfirst-items.length-1;
        items=arr;
    }
    
    public ArrayDeque(){
        size=0;
        items=(T []) new Object[8];
        nextfirst=4;
        nextlast=5;
    }

    public void addFirst(T item){
        if(size==items.length) resize(size*2);
        ++size;
        items[nextfirst]=item;
        if(nextfirst>0) --nextfirst;
        else nextfirst=items.length-1;
    }
    
    public void addLast(T item){
        if(size==items.length) resize(size*2);
        ++size;
        items[nextlast]=item;
        if(nextlast<items.length-1) ++nextlast;
        else nextlast=0;
    }

    public boolean isEmpty(){
        if(size==0) return true;
        return false;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        for(int i=0;i<size;++i){
            System.out.print(items[i]+" ");
        }
    }

    public T removeFirst(){
        if(size==0) return null;
        ++nextfirst;
        --size;
        T item=items[nextfirst];
        return item;
    }

    public T removeLast(){
        if(size==0) return null;
        --nextlast;
        --size;
        T item=items[nextlast];
        return item;
    }

    public T get(int index){
        if(index<0||index>size) return null;
        if(nextfirst+index>=size){
            return items[index-items.length+nextfirst];
        }
        else{
            return items[nextfirst+index+1];
        }
    }

}
