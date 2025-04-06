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
        for(int i=0;i<40;++i) lld.removeLast();
        int i=lld.getRecursive(6);
        assertEquals(39, i);
    }

    @Test
    public void testforad(){
        ArrayDeque<Integer> ad=new ArrayDeque<>();
        for(int i=0;i<50;++i) ad.addLast(i);
        for(int i=0;i<50;++i) ad.removeFirst();
        for(int i=0;i<50;++i) ad.addFirst(i);
        int i=ad.removeLast();
        assertEquals(5, i);

    }

}
