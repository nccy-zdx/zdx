import static org.junit.Assert.*;

import org.junit.Test;
public class test {
    
    @Test
    public void testforlld(){
        LinkedListDeque<Integer> lld=new LinkedListDeque<>();
        lld.addFirst(0);
        lld.addLast(1);
        lld.addFirst(3);
        int i=lld.getRecursive(1);
        assertEquals(0, i);
    }

    @Test
    public void testforad(){
        ArrayDeque<Integer> ad=new ArrayDeque<>();
        ad.addFirst(1);
        ad.addLast(0);
        for(int i=0;i<10;++i) ad.addFirst(i);
        assertEquals(12, ad.size());
        int i=ad.get(0);
        assertEquals(9,i);
        int j=ad.removeFirst();
        assertEquals(9, j);
        int k=ad.get(9);
        assertEquals(1, k);
        int m=ad.removeLast();
        assertEquals(0, m);
    }

}
