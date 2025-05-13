import edu.princeton.cs.algs4.Queue;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>> makeSingleItemQueues(Queue<Item> items) {
        Queue<Queue<Item>> singleitem=new Queue<>();
        for(Item item:items){
            Queue<Item> si=new Queue<>();
            si.enqueue(item);
            singleitem.enqueue(si);
        }
        return singleitem;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> sortQueue=new Queue<>();
        while(!q1.isEmpty()){
            if(q2.isEmpty()){
                sortQueue.enqueue(q1.dequeue());
                continue;
            }
            sortQueue.enqueue(getMin(q1, q2));
        }
        if(!q2.isEmpty()){
            while(!q2.isEmpty()){
                sortQueue.enqueue(q2.dequeue());
            }
        }
        return sortQueue;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(Queue<Item> items) {
        if(items.size()==1) return items;
        Queue<Item> q1=new Queue<>();
        Queue<Item> q2=new Queue<>();
        int count=0;
        for(Item item:items){
            if(count>=items.size()/2) q2.enqueue(item);
            else q1.enqueue(item);
            ++count;
        }
        q1=mergeSort(q1);
        q2=mergeSort(q2);
        items=mergeSortedQueues(q1, q2);
        return items;
    }

    public static void main(String[] args) {
        /*Used for test */
        Queue<String> exams=new Queue<String>();
        exams.enqueue("Synthetic Genomics");
        exams.enqueue("Nature Medcine Synthetic Biology");
        exams.enqueue("Pharmaceutical Technology");
        exams.enqueue("Pharmaceutical Machine and Engineering Design");
        exams.enqueue("Sports and Health");
        System.out.println(exams);
        System.out.println(MergeSort.mergeSort(exams));
    }
}
