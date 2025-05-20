package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator{
    private int period;
    private double factor;
    private int state;
    
    public AcceleratingSawToothGenerator(int period,double factor){
        this.period=period;
        this.factor=factor;
        state=0;
    }

    @Override
    public double next(){
        state=state+1;
        double x=state%period;
        return normalize(x);
    }

    private double normalize(double x){
        if(x==0){
            period=(int)(period*factor);
            state=0;
            return -1;
        }
        else{
            double k=2.0/period; 
            return k*x-1;
        }
    }

    /*public static void main(String[] args) {
        Generator generator=new AcceleratingSawToothGenerator(200,1.1);
        GeneratorAudioVisualizer gav=new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(4096, 1000000);
    }*/
}
