/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    private static int maxlength;
    public static String[] sort(String[] asciis) {
        String[] sort=new String[asciis.length];
        int max=Integer.MIN_VALUE;
        System.arraycopy(asciis, 0, sort, 0, sort.length);
        for(String str:asciis){
            max=max>str.length() ? max:str.length();
        }
        maxlength=max;
        for(int i=0;i<max;++i){
            sortHelperLSD(sort, max-i-1);
        }
        return sort;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        String[] fixed=new String[asciis.length];
        for(int i=0;i<asciis.length;++i){
            if(asciis[i].length()<maxlength){
                fixed[i]=pad(asciis[i], maxlength-asciis[i].length());
            }
            else{
                fixed[i]=asciis[i];
            }
        }
        int[] count=new int[256];
        for(String str:fixed){
            ++count[str.charAt(index)];
        }
        int[] starts=new int[256];
        int pos=0;
        for(int i=0;i<starts.length;++i){
            starts[i]=pos;
            pos+=count[i];
        }
        String[] sort=new String[asciis.length];
        for(int i=0;i<asciis.length;++i){
            String str=fixed[i];
            int position=starts[str.charAt(index)];
            sort[position]=asciis[i];
            starts[str.charAt(index)]+=1;
        }
        System.arraycopy(sort, 0, asciis, 0, sort.length);
        return;
    }

    private static String pad(String pad,int num){
        for(int i=0;i<num;++i){
            pad+="_";
        }
        return pad;
    }

    /*public static void main(String[] args) {
        String[] data=new String[12];
        int sum=95+114+145+212+280+338+413+495+557+611+632+650+680+699+727+749+765+795+814+822+870;
        int sum1=909+1076+1158+1179+1222+1237+1275+1269+1297+1329;
        System.out.println(sum);
        System.out.println(sum1);

        data[0]="103";
        data[1]="197";
        data[2]="237";
        data[3]="35";
        data[4]="115";
        data[5]="52";
        data[6]="141";
        data[7]="126";
        data[8]="163";
        data[9]="169";
        data[10]="120";
        data[11]="91";
        String[] strs=sort(data);
        for(int i=0;i<strs.length;++i){
            System.out.println(strs[i]);
        }
    }*/

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
