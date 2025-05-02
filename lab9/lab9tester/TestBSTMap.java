package lab9tester;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import edu.princeton.cs.algs4.In;
import lab9.BSTMap;

/**
 * Tests by Brendan Hu, Spring 2015, revised for 2018 by Josh Hug
 */
public class TestBSTMap {

    @Test
    public void sanityGenericsTest() {
        try {
            BSTMap<String, String> a = new BSTMap<String, String>();
            BSTMap<String, Integer> b = new BSTMap<String, Integer>();
            BSTMap<Integer, String> c = new BSTMap<Integer, String>();
            BSTMap<Boolean, Integer> e = new BSTMap<Boolean, Integer>();
        } catch (Exception e) {
            fail();
        }
    }

    //assumes put/size/containsKey/get work
    @Test
    public void sanityClearTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1 + i);
            //make sure put is working via containsKey and get
            assertTrue(null != b.get("hi" + i));
            assertTrue(b.get("hi" + i).equals(1 + i));
            assertTrue(b.containsKey("hi" + i));
        }
        assertEquals(455, b.size());
        b.clear();
        assertEquals(0, b.size());
        for (int i = 0; i < 455; i++) {
            assertTrue(null == b.get("hi" + i) && !b.containsKey("hi" + i));
        }
    }

    // assumes put works
    @Test
    public void sanityContainsKeyTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertFalse(b.containsKey("waterYouDoingHere"));
        b.put("waterYouDoingHere", 0);
        assertTrue(b.containsKey("waterYouDoingHere"));
    }

    // assumes put works
    @Test
    public void sanityGetTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(null, b.get("starChild"));
        assertEquals(0, b.size());
        b.put("starChild", 5);
        assertTrue(((Integer) b.get("starChild")).equals(5));
        b.put("KISS", 5);
        assertTrue(((Integer) b.get("KISS")).equals(5));
        assertNotEquals(null, b.get("starChild"));
        assertEquals(2, b.size());
    }

    // assumes put works
    @Test
    public void sanitySizeTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(0, b.size());
        b.put("hi", 1);
        assertEquals(1, b.size());
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
        }
        assertEquals(456, b.size());
    }

    //assumes get/containskey work
    @Test
    public void sanityPutTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("hi", 1);
        assertTrue(b.containsKey("hi"));
        assertTrue(b.get("hi") != null);
    }

    @Test
    public void sanityRemoveTest(){
        Random r=new Random();
        BSTMap<Integer, String> b = new BSTMap<Integer, String>();
        for(int i=1;i<1000;++i){
            int num=r.nextInt(i);
            if(r.nextInt(10)==0){
                b.put(num,i+"th");
                assertTrue(b.get(num)!=i+"th");
            }
            if(r.nextInt(10)==1){
                b.remove(num);
                assertTrue(!b.containsKey(num));
            }     
        }
    }

    public static void main(String[] args) {
        //jh61b.junit.TestRunner.runTests(TestBSTMap.class);
        Random r=new Random();
        BSTMap<Integer, String> b = new BSTMap<Integer, String>();
        /*for(int i=1;i<10000;++i){
            int num=r.nextInt(i);
                try{
                    b.put(num,i+"th");
                    b.get(num);
                    b.keySet();
                    if(r.nextBoolean()) b.remove(num);
                    else if(r.nextBoolean()) b.remove(num,i+"th");
                }
                catch(NullPointerException e){
                    System.out.println(num);
                    for(int key:b){
                        System.out.print(key+" ");
                    }
                    throw e;
                }
                catch(Exception e){

                }
        } */
        b.put(10, null);
        b.remove(null);
    }
}
