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
        for(int i=0;i<10;++i) lld.removeLast();
        int i=lld.getRecursive(10);
        assertEquals(39, i);
    }

    @Test
    public void testforad(){
        ArrayDeque<Integer> ad=new ArrayDeque<>();
        ad.addLast(5);
        ad.addFirst(7);
        ad.addFirst(8);
        ad.addLast(9);
        ad.addFirst(10);
        ad.addFirst(11);
        ad.addFirst(12);
        ad.addFirst(13);
        ad.addFirst(14);
        int i=ad.get(8);
        assertEquals(9, i);

    }

}
