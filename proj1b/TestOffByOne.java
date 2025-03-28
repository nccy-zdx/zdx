import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testequalchars(){
        char x='a';
        char y='b';
        char z='a';
        char m='b';
        char n='c';
        assertTrue(offByOne.equalChars(n,y));
    }
}
