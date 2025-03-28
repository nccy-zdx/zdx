import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testisPalindrome(){
        String[] strs=new String[]{"a","cat","fire","racecar","A","noon","","rancor","aaaaab","Aa","bb","000","abcddcbA","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"};
        assertTrue(palindrome.isPalindrome(strs[13]));
    }

    @Test
    public void testisPalindrome1(){
        CharacterComparator cc=new OffByOne();
        String[] strs=new String[]{"a","cat","fe","ffge","racecar","A","noon","","flake","cbflkead"};
        assertTrue(palindrome.isPalindrome(strs[9], cc));
    }
}
