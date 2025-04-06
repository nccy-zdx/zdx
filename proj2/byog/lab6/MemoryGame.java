package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        int seed =12;
        MemoryGame game = new MemoryGame(40, 40,seed);
        game.startGame();
    }

    public MemoryGame(int width, int height,int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        rand=new Random(seed);
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public String generateRandomString(int n) {
        char[] ch=new char[n];
        for(int i=0;i<n;++i) ch[i]=CHARACTERS[rand.nextInt(26)];
        String str=new String(ch);
        return str;
    }

    public void drawFrame(String s) {
        if(s==" "){
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.show();
            return;
        }
        else if(s.length()==1){
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(width/2, height/2, s);
            Font smallFont = new Font("Monaco", Font.BOLD, 15);
            StdDraw.setFont(smallFont);
            StdDraw.text(5, height-1, "Round:"+round);
            StdDraw.text(width/2, height-1, "Watch");
            StdDraw.text(width-10, height-1, ENCOURAGEMENT[rand.nextInt(7)]);
            StdDraw.line(0, height - 2, width, height - 2);
            StdDraw.show();
        }
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
    }

    public void flashSequence(String letters) {
        for(int i=0;i<letters.length();++i){
            String str=letters.substring(i, i+1);
            drawFrame(str);
            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException e){
                System.out.println("Thread problems,InterruptedException happened.");
            }
            drawFrame(" ");
            try{
                Thread.sleep(500);
            }
            catch(InterruptedException e){
                System.out.println("Thread problems,InterruptedException happened.");
            }
        }
    }

    public String solicitNCharsInput(int n) {
        String str;
        char[] ch=new char[n];
        int count=0;
        while(StdDraw.hasNextKeyTyped()){
            ch[count]=StdDraw.nextKeyTyped();
            ++count;
            if(count==n) break;
        }
        str=new String(ch); 
        return str;
    }

    public void startGame() {
        round=1;
        gameOver=false;
        while(!gameOver){
            drawFrame("Round:"+round);
            String randomstring=generateRandomString(round);
            flashSequence(randomstring);
            String userstr=solicitNCharsInput(round);
            if(randomstring.equals(userstr)) ++round;
            else gameOver=true;
        }
    }

}
