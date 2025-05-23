package hw3.hash;

import org.junit.Test;

import edu.princeton.cs.algs4.StdRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class TestComplexOomage {

    private static class failOomage implements Oomage{

        private int val;
        @Override
        public void draw(double x, double y, double scalingFactor){
            return;
        }

        @Override
        public int hashCode(){
            return val;
        }

        public static failOomage ranfailOomage(){
            failOomage x=new failOomage();
            x.val=StdRandom.uniform(0,1);
            return x;
        }
    }

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /* TODO: Create a list of Complex Oomages called deadlyList
     * that shows the flaw in the hashCode function.
     */
    
    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            ArrayList<Integer> list=new ArrayList<>(5);
            list.add(0,StdRandom.uniform(0,255));
            list.add(1,1);
            list.add(2,2);
            list.add(3,3);
            list.add(4,4);
            deadlyList.add(new ComplexOomage(list));
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    } 

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
