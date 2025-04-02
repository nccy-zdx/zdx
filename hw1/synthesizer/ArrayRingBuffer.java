package synthesizer;

import java.util.Iterator;

public class ArrayRingBuffer<T>  extends AbstractBoundedQueue<T>{
    /* Index for the next dequeue or peek. */
    private int first;            
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;
    /* */
    private class arb_iterator implements Iterator<T> {
        private int ptr;

        public arb_iterator(){
            ptr=0;
        }

        public boolean hasNext(){
            return ptr!=fillCount;
        }

        public T next(){
            T item=rb[ptr];
            ++ptr;
            return item;
        }
        
    }

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first=0;
        last=0;
        fillCount=0;
        this.capacity=capacity;
        rb=(T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        if(isFull()){
            throw new RuntimeException("Ring Buffer Overflow");
        }
        ++fillCount;
        rb[last]=x;
        if(last==capacity-1) last=0;
        else ++last;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        if(isEmpty()){
            throw new RuntimeException("Ring Buffer Underflow");
        }
        --fillCount;
        T item=rb[first];
        if(first==capacity-1) first=0;
        else ++first;
        return item;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        if(isEmpty()){
            throw new RuntimeException("Ring Buffer Underflow");
        }
        return rb[first];
    }

    @Override
    public Iterator<T> iterator(){
        return new arb_iterator();
    }

}
