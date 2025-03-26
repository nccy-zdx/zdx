import static org.junit.Assert.*;

import org.junit.Test;
public class test {
    
    @Test
    public void testforlld(){
        LinkedListDeque<Integer> lld=new LinkedListDeque<>();
        lld.addFirst(0);
        lld.addLast(1);
        lld.addFirst(3);
        for(int i=0;i<50;++i) lld.addFirst(i);
        lld.removeFirst();
        lld.removeLast();
        int i=lld.getRecursive(0);
        assertEquals(48, i);
    }

    @Test
    public void testforad(){
        ArrayDeque<Integer> ad=new ArrayDeque<>();
        for(int i=0;i<10;++i) ad.addFirst(i);
        for(int i=0;i<8;++i) ad.removeLast();
        int i=ad.removeLast();
        assertEquals(3, i);

    }

}
