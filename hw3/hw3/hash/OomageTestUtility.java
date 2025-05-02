package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        double min=oomages.size()/50;
        double max=oomages.size()/2.5;
        int[] num=new int[M];
        for(int i=0;i<M;++i) num[i]=0;
        for(int i=0;i<oomages.size();++i){
            int bucketnum=(oomages.get(i).hashCode()&0x7FFFFFFF)%M;
            ++num[bucketnum];
        }
        for(int i=0;i<M;++i){
            if(num[i]>max||num[i]<min) return false;
        }
        return true;
    }
}
