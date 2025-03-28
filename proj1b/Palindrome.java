public class Palindrome {
    
    public Deque<Character> wordToDeque(String word){
        ArrayDeque<Character> ad=new ArrayDeque<>();
        for(int i=0;i<word.length();++i) ad.addLast(word.charAt(i));
        return ad;
    }

    public boolean isPalindrome(String word){
        Deque<Character> d=wordToDeque(word);
        return isequal(d);
    }

    public boolean isPalindrome(String word,CharacterComparator cc){
        Deque<Character> d=wordToDeque(word);
        return isequal(d, cc);
    }

    private boolean isequal(Deque<Character> d){
        Deque<Character> dc=d;
        if(dc.size()==1||dc.isEmpty()) return true;
        char ch1=dc.removeFirst();
        char ch2=dc.removeLast();
        if(ch1==ch2) return isequal(dc);
        return false;
    }

    private boolean isequal(Deque<Character> d,CharacterComparator cc){
        Deque<Character> dc=d;
        if(dc.size()==1||dc.isEmpty()) return true;
        if(cc.equalChars(dc.removeFirst(), dc.removeLast())) return isequal(dc,cc);
        return false;
    }
}
