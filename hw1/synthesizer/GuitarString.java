package synthesizer;

public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int freq=(int) frequency;
        buffer=new ArrayRingBuffer<>(SR/freq);
        for(int i=0;i<buffer.capacity();++i) buffer.enqueue(0.0);
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        for(int i=0;i<buffer.capacity();++i){
            double randomnum=Math.random()-0.5;
            if(buffer.isFull()) buffer.dequeue();
            buffer.enqueue(randomnum);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double fs=buffer.dequeue();
        double ave=(buffer.peek()+fs)/2*DECAY;
        buffer.enqueue(ave);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }

}