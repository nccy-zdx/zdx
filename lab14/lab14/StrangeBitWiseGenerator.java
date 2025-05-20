package lab14;

import lab14lib.Generator;

public class StrangeBitWiseGenerator implements Generator{
        private int state;
    private int period;

    public StrangeBitWiseGenerator(int period){
        this.period=period;
        state=0;
    }

    @Override
    public double next(){
        state=state+1;
        double x=state&(state>>3)&(state>>8)%period;
        return normalize(x);
    }

    private double normalize(double x){
        if(x==0) return -1;
        else{
            double k=2.0/period; 
            //System.out.println(k);
            return k*x-1;
        }
    }

    /*public static void main(String[] args) {
        Generator generator=new StrangeBitWiseGenerator(512);
        GeneratorAudioVisualizer gav=new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(4096, 1000000);
    }*/

}
