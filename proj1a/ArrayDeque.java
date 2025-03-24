public class ArrayDeque<T> {
    private T[] items;
    private int size;

    private void resize(int capacity){
        if(items.length>=16){
            if(items.length/size>4){
                T[] sarr=(T[]) new Object[size*2];
                System.arraycopy(items, 0, sarr, 0, size);
                items=sarr;
                return;
            }
        }
        if(size==items.length){
            T[] arr=(T[]) new Object[capacity];
            System.arraycopy(items, 0, arr, 0, size);
            items=arr;
        } 
    }
    
    public ArrayDeque(){
        size=0;
        items=(T []) new Object[8];
    }

    public void addFirst(T item){
        resize(size*2);
        T[] arr=(T[]) new Object[items.length];
        arr[0]=item;
        System.arraycopy(items, 0, arr, 1, size+1);
        items=arr;
        ++size;
    }
    
    public void addLast(T item){
        resize(size*2);
        items[size]=item;
        ++size;
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
        T[] arr=(T[]) new Object[items.length];
        T item=items[0];
        System.arraycopy(items, 1, arr, 0, size-1);
        items=arr;
        resize(size*2);
        --size;
        return item;
    }

    public T removeLast(){
        if(size==0) return null;
        T item=items[size-1];
        --size;
        return item;
    }

    public T get(int index){
        return items[index];
    }

}
