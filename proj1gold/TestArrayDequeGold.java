import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayDequeGold {

    String message;

    @Test
    public void test(){
        StudentArrayDeque<Integer> sad=new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads=new ArrayDequeSolution<>();
        for(Integer i=-100;i<100;++i){
            double randomnumber= StdRandom.uniform(-1.0,1.0);
            if(randomnumber<-0.5){
                message+="addFirst("+i+")\n";
                sad.addFirst(i);
                ads.addFirst(i);
                assertEquals(message,sad.size(), ads.size());
            }
            if(randomnumber>0.5){
                message+="addLast("+i+")\n";
                sad.addLast(i);
                ads.addLast(i);
                assertEquals(message,ads.size(), sad.size());
            }
            if(randomnumber>=0&&randomnumber<=0.5){
                if(sad.size()==0) continue;
                Integer x=sad.removeFirst();
                Integer y=ads.removeFirst();
                message+="removeFirst():"+x+"\n";
                assertEquals(message,x, y);
            }
            if(randomnumber<0&&randomnumber>=-0.5){
                if(sad.size()==0) continue;
                Integer x=sad.removeLast();
                Integer y=ads.removeLast();
                message+="removeLast():"+x+"\n";
                assertEquals(message,x, y);
            }
        }
    }

}
