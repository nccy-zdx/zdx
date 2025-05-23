public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;
    private double loadfactor;

    //resize for bigger capacity
    private void resize(int capacity){
        T[] arr=(T[]) new Object[capacity];
        if(nextfirst>nextlast){
            for(int i=0;i<items.length;++i) arr[i]=items[i];
            nextlast=nextfirst+1;
            nextfirst=arr.length-1;
        }
        else{
            for(int i=0;i<nextlast;++i) arr[i]=items[i];
            for(int i=0;i<items.length-nextfirst-1;++i) arr[arr.length+nextfirst-items.length+i+1]=items[nextfirst+1+i];
            nextfirst=arr.length+nextfirst-items.length;
        }
        items=arr;
        loadfactor=(double)size/(double)items.length;
    }

    //resize for smaller capacity
    private void resize(){
        T[] arr=(T[]) new Object[items.length/2];
        if(nextfirst>nextlast){
            for(int i=0;i<nextlast;++i) arr[i]=items[i];
            for(int i=0;i<items.length-nextfirst-1;++i) arr[arr.length+nextfirst-items.length+i+1]=items[nextfirst+1+i];
            nextfirst=arr.length+nextfirst-items.length;
        }
        else{
            int count=0;
            for(int i=nextfirst+1;i<nextlast;++i) arr[count++]=items[i];
            nextlast=count;
            nextfirst=arr.length-1;
        }
        items=arr;
        loadfactor=(double)size/(double)items.length;
    }
    
    public ArrayDeque(){
        size=0;
        items=(T []) new Object[8];
        nextfirst=4;
        nextlast=5;
        loadfactor=(double)size/(double)items.length;
    }

    public void addFirst(T item){
        if(size==items.length) resize(size*2);
        if(loadfactor<=0.25&&items.length>=16) resize();
        ++size;
        items[nextfirst]=item;
        loadfactor=(double)size/(double)items.length;
        if(nextfirst>0) --nextfirst;
        else nextfirst=items.length-1;
    }
    
    public void addLast(T item){
        if(size==items.length) resize(size*2);
        if(loadfactor<0.25&&items.length>=16) resize();
        ++size;
        items[nextlast]=item;
        loadfactor=(double)size/(double)items.length;
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
        for(int i=0;i<items.length;++i){
            System.out.print(items[i]+" ");
        }
    }

    public T removeFirst(){
        if(isEmpty()) return null;
        if(nextfirst<items.length-1) ++nextfirst;
        else nextfirst=0;
        --size;
        T item=items[nextfirst];
        loadfactor=(double)size/(double)items.length;
        if(loadfactor<=0.25&&items.length>=16) resize();
        return item;
    }

    public T removeLast(){
        if(isEmpty()) return null;
        if(nextlast>0) --nextlast;
        else nextlast=items.length-1;
        --size;
        T item=items[nextlast];
        loadfactor=(double)size/(double)items.length;
        if(loadfactor<=0.25&&items.length>=16) resize();
        return item;
    }

    public T get(int index){
        if(index<0||index>size||isEmpty()) return null;
        if(nextfirst+index>=items.length-1){
            return items[index-items.length+nextfirst+1];
        }
        else{
            return items[nextfirst+index+1];
        }
    }

}