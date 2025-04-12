package byog.Core;

import java.util.Random;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.princeton.cs.introcs.StdDraw;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Game {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static double mousex;
    private static double mousey;
    private static TETile tilenow;
    private static TETile tilefuture;
    private static int[] playerpostionnow=new int[2];
    private static int[] playerpositionfuture=new int[2];
    private static long seed;
    private String dir="E:\\cs61b_da_java\\zdx\\proj2\\byog\\Core\\data.txt";
    private File file=new File(dir);
    private static String loaddata;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        startUI();
        int count=0;
        while(true){
            if(StdDraw.hasNextKeyTyped()){
                char control=StdDraw.nextKeyTyped();
                if(control=='n'){
                    int i=1;
                    char[] ch=new char[10];
                    boolean enternum=false;
                    ch[0]='n';
                    while(!enternum){
                        while(StdDraw.hasNextKeyTyped()){
                            char keytyped=StdDraw.nextKeyTyped();
                            if('s'==keytyped) enternum=true;
                            ch[i]=keytyped;
                            ++i;
                        }
                    }
                    String str=new String(ch);
                    newgameseedexe(str);
                    break;
                }
                else if(control=='l'){
                    break;
                }
                else if(control=='q'){
                    break;
                }
                else{
                    System.out.println("your enter is illegal, please enter one more time.");
                    ++count;
                    if(count>10){
                        System.out.println("you have mistaken more than 10 times,please restart.");
                        return;
                    }
                }
            }
        }
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        intiate(finalWorldFrame);
        finalWorldFrame[0][0]=Tileset.PLAYER;
        playerpostionnow[0]=0;
        playerpostionnow[1]=0;
        tilenow=Tileset.NOTHING;
        mousex=StdDraw.mouseX();
        mousey=StdDraw.mouseY();
        BuildRandomWorld(finalWorldFrame);
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(finalWorldFrame);
        keyplay(finalWorldFrame);
    }

    //method for using keyboard.
    private void keyplay(TETile[][] world){
        while(true){
            while(StdDraw.hasNextKeyTyped()){
                char control =StdDraw.nextKeyTyped();
                if(':'==control){
                    while(true){
                        while(StdDraw.hasNextKeyTyped()){
                            char cont=StdDraw.nextKeyTyped();
                            if('q'==cont){
                                System.out.println("Game over. Every dream has an end. It`s time to wake up.");
                                System.exit(1);
                            }
                            else{
                                System.out.println("illegal instruction : without q, please enter one more time");
                                break;
                            }
                        }
                    }
                }
                else playermove(world, control);
            }
            if(mousemove(StdDraw.mouseX(), StdDraw.mouseY())) showtile(world);
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     */
    public TETile[][] playWithInputString(String input) {
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        intiate(finalWorldFrame);
        String strexecution=StartStep(input);
        finalWorldFrame[playerpostionnow[0]][playerpostionnow[1]]=Tileset.PLAYER;
        BuildRandomWorld(finalWorldFrame);
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(finalWorldFrame);
        for(int i=0;i<strexecution.length();++i) playermove(finalWorldFrame, strexecution.charAt(i));
        writedata();
        return finalWorldFrame;
    }

    //write game data to the datafile.
    private void writedata(){
        try{
            FileWriter fw=new FileWriter(file);
            fw.write(tilenow.description()+'\n'+playerpostionnow[0]+'\n'+playerpostionnow[1]+'\n'+seed+'\n');
            fw.flush();
            fw.close();
        }
        catch(IOException e){
            System.out.println("Can`t write into file.");
            System.exit(1);
        }
    }

    //read datafile.
    private void readdata(){
        try{
            FileReader fr=new FileReader(file);
            char[] ch=new char[40];
            if(fr.read(ch)==0){
                System.out.println("there is no load here,please start a new game.");
                System.exit(1);
            }
            loaddata(ch);
            fr.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Can`t open file.");
            System.exit(1);       
        }
        catch(IOException e){
            System.out.println("Can`t read file.");
            System.exit(1);   
        }
    }

    //load data.
    private void loaddata(char[] data){
        int npos=0;
        int ncount=0;
        for(int i=0;i<data.length;++i){
            if(data[i]=='\n'){
                switch (ncount) {
                    case 0:
                        convertchararrtoString(npos, i, data);
                        if(loaddata.equals(Tileset.NOTHING.description())) tilenow=Tileset.NOTHING;
                        else if(loaddata.equals(Tileset.FLOOR.description())) tilenow=Tileset.FLOOR;
                        break;
                    case 1:
                        convertchararrtoString(npos, i, data);
                        playerpostionnow[0]=Integer.parseInt(loaddata);
                        break;
                    case 2:
                        convertchararrtoString(npos, i, data);
                        playerpostionnow[1]=Integer.parseInt(loaddata);
                        break;
                    case 3:
                        convertchararrtoString(npos, i, data);
                        seed=Long.parseLong(loaddata);
                        break;
                }
                npos=i+1;
                ++ncount;
            }
        }
    }

    //helper method.
    private void convertchararrtoString(int npos,int i,char[] data){
        char[] ch=new char[i-npos];
        for(int j=0;j<i-npos;++j) ch[j]=data[npos+j];
        loaddata=new String(ch);
    }

    //analysis the input String.
    private String StartStep(String input){
        startUI();
        int count=0;
        while(true){
            if(input.contains(":q")){
                for(int i=0;i<input.length();++i){
                    if(input.charAt(i)==':'){
                        String anoinput=input.substring(0, i);
                        playWithInputString(anoinput);
                        System.out.println("Game over. Every dream has an end. It`s time to wake up.");
                        System.exit(1);
                    }
                }
            }
            else if(input.startsWith("n")){
                playerpostionnow[0]=0;
                playerpostionnow[1]=0;
                tilenow=Tileset.NOTHING;
                return newgameseedexe(input);
            }
            else if(input.startsWith("l")){
                readdata();
                return input.substring(1,input.length());
            }
            else{
                System.out.println("your enter is illegal, please enter one more time.");
                ++count;
                if(count>10){
                    System.out.println("you have mistaken more than 10 times,please restart.");
                    System.exit(1);
                } 
            }
        }
    }

    //set seed with the given number. and return executions.
    private String newgameseedexe(String input){
        for(int i=1;i<input.length();++i){
            if(input.charAt(i)=='s'){
                String strnum=input.substring(1, i);
                String strexec=input.substring(i+1, input.length());
                seed=Long.parseLong(strnum);
                return strexec;
            }
        }
        System.out.println("your input doesn`t have char s, please restart.");
        System.exit(1);
        return null;
    }

    //draw start UI
    private void startUI(){
        StdDraw.setCanvasSize(WIDTH*16, HEIGHT*16);
        StdDraw.clear(StdDraw.BLACK);
        Font fonts = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fonts);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5, 0.75, "Push    Box!");
        Font fonte = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fonte);
        StdDraw.text(0.5, 0.6, "New Game (N)");
        StdDraw.text(0.5, 0.5, "Load Game (L)");
        StdDraw.text(0.5, 0.4, " Quit Game (Q)");
        StdDraw.show();
    }

    //change static constants to make player move.
    private void playermove(TETile[][] world,char ch){
        if('w'!=ch&&'a'!=ch&&'s'!=ch&&'d'!=ch){
            System.out.println("illegal instruction, please enter one more time.");
            return;
        }
        if(ch=='w'){
            if(playerpostionnow[1]+1==HEIGHT) System.out.println("you can`t move across the bound.try other direction.");
            else if(world[playerpostionnow[0]][playerpostionnow[1]+1]==Tileset.WALL||
            world[playerpostionnow[0]][playerpostionnow[1]+1]==Tileset.LOCKED_DOOR)
            System.out.println("you can`t move across wall or locked door. go to other places.");
            else{
                playerpositionfuture[0]=playerpostionnow[0];
                playerpositionfuture[1]=playerpostionnow[1]+1;
                drawplayer(world);
            }
        }
        else if(ch=='a'){
            if(playerpostionnow[0]-1==-1) System.out.println("you can`t move across the bound.try other direction.");
            else if(world[playerpostionnow[0]-1][playerpostionnow[1]]==Tileset.WALL||
            world[playerpostionnow[0]-1][playerpostionnow[1]]==Tileset.LOCKED_DOOR)
            System.out.println("you can`t move across wall or locked door. go to other places.");
            else{
                playerpositionfuture[0]=playerpostionnow[0]-1;
                playerpositionfuture[1]=playerpostionnow[1];
                drawplayer(world);
            }
        }
        else if(ch=='s'){
            if(playerpostionnow[1]-1==-1) System.out.println("you can`t move across the bound.try other direction.");
            else if(world[playerpostionnow[0]][playerpostionnow[1]-1]==Tileset.WALL||
            world[playerpostionnow[0]][playerpostionnow[1]-1]==Tileset.LOCKED_DOOR)
            System.out.println("you can`t move across wall or locked door. go to other places.");
            else{
                playerpositionfuture[0]=playerpostionnow[0];
                playerpositionfuture[1]=playerpostionnow[1]-1;
                drawplayer(world);
            }
        }
        else{
            if(playerpostionnow[0]+1==WIDTH) System.out.println("you can`t move across the bound.try other direction.");
            else if(world[playerpostionnow[0]+1][playerpostionnow[1]]==Tileset.WALL||
            world[playerpostionnow[0]+1][playerpostionnow[1]]==Tileset.LOCKED_DOOR)
            System.out.println("you can`t move across wall or locked door. go to other places.");
            else{
                playerpositionfuture[0]=playerpostionnow[0]+1;
                playerpositionfuture[1]=playerpostionnow[1];
                drawplayer(world);
            }
        }
    }

    //draw player`s position using static constants and updata these constants.
    private void drawplayer(TETile[][] world){
        tilefuture=world[playerpositionfuture[0]][playerpositionfuture[1]];
        world[playerpositionfuture[0]][playerpositionfuture[1]]=Tileset.PLAYER;
        world[playerpostionnow[0]][playerpostionnow[1]]=tilenow;
        tilenow=tilefuture;
        playerpostionnow[0]=playerpositionfuture[0];
        playerpostionnow[1]=playerpositionfuture[1];
        StdDraw.clear();
        ter.renderFrame(world);
    }

    //check if mouse has moved.
    private boolean mousemove(double x,double y){
        if(x!=mousex||y!=mousey){
            mousex=x;
            mousey=y;
            return true;
        }
        return false;
    }

    //show the tile that mouse is pointing at.
    private void showtile(TETile[][] world){
        StdDraw.clear();
        ter.renderFrame(world);
        if(mousex>=WIDTH||mousey>=HEIGHT||mousex<0||mousey<0) return;
        Font font = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(5, HEIGHT-1, world[(int) mousex][(int) mousey].description());
        StdDraw.show();
    }

    //build a random world consists of rooms and hallways.
    private TETile[][] BuildRandomWorld(TETile[][] rw){
        Random r=new Random(seed);
        int rn=r.nextInt(50)+100;
        for(int i=0;i<rn;++i){
            if(r.nextInt(2)==0) BuildRandomRoom_rectangular(r.nextLong(), rw);
            else BuildRandomHallway(r.nextLong(), rw);
        }
        return rw;
    }

    //build a random room which won`t cover anything.
    private void BuildRandomRoom_rectangular(long s,TETile[][] rr_rectangular){
        Random r=new Random(s);
        int len=r.nextInt(10)+3;
        int wid=r.nextInt(10)+3;
        int rlx=r.nextInt(WIDTH-len+1);
        int rly=r.nextInt(HEIGHT-wid+1);
        if(rlx+len>=WIDTH||rly+wid>=HEIGHT) return;
        int[] data=new int[]{len,wid,rlx,rly};
        if(roomcover(data, rr_rectangular)) return;
        for(int i=rlx;i<rlx+len;++i){
            for(int j=rly;j<rly+wid;++j){
                if(i==rlx||i==rlx+len-1){
                    rr_rectangular[i][j]=Tileset.WALL;
                    continue;
                }
                else if(j==rly||j==rly+wid-1){
                    rr_rectangular[i][j]=Tileset.WALL;
                    continue;
                }
                rr_rectangular[i][j]=Tileset.FLOOR;
            }
        }
        connect_rooms(s,data, rr_rectangular);
        if(r.nextInt(2)==1){
            int x=r.nextInt(len-2)+rlx+1;
            int y;
            if(r.nextInt(2)==1) y=rly;
            else y=rly+wid-1;
            if(wallaroundnothing(x, y, rr_rectangular)){
                if(r.nextInt(2)==1) rr_rectangular[x][y]=Tileset.LOCKED_DOOR;
                else rr_rectangular[x][y]=Tileset.UNLOCKED_DOOR;
            }
        }
        else{
            int x;
            int y=r.nextInt(wid-2)+rly+1;
            if(r.nextInt(2)==1) x=rlx;
            else x=rlx+len-1;
            if(wallaroundnothing(x, y, rr_rectangular)){
                if(r.nextInt(2)==1) rr_rectangular[x][y]=Tileset.LOCKED_DOOR;
                else rr_rectangular[x][y]=Tileset.UNLOCKED_DOOR;
            }
        }
    }

    //check if the wall is next to nothing.
    private boolean wallaroundnothing(int x, int y,TETile[][] world){
        if((y+1<HEIGHT&&world[x][y+1]==Tileset.NOTHING)||
        (x+1<WIDTH&&world[x+1][y]==Tileset.NOTHING)||
        (x-1>-1&&world[x-1][y]==Tileset.NOTHING)||
        (y-1>-1&&world[x][y-1]==Tileset.NOTHING)) return true;
        return false;
    }

    //room can`t cover room or hallway.or will you be happy when your rooms or hallways were ruined?
    private boolean roomcover(int[] data,TETile[][] world){
        for(int i=data[2];i<data[2]+data[0];i+=data[0]-1){
            for(int j=data[3];j<data[3]+data[1];++j){
                if(world[i][j]!=Tileset.NOTHING) return true;
            }
        }
        for(int j=data[3];j<data[3]+data[1];j+=data[1]-1){
            for(int i=data[2];i<data[2]+data[0];++i){
                if(world[i][j]!=Tileset.NOTHING) return true;
            }
        }
        return false;
    }

    //room should be connect.otherwise you can`t go into it,right?
    private void connect_rooms(long s,int[] data,TETile[][] world){
        if(!canroomconnect(data, world)) return;
        Random r=new Random(s);
        while(!roomfloorconnect(data,world)){
            BuildConnectHallway(r.nextLong(),data, world);
        }
    }

    //check whether or not the room given is connected.
    private boolean roomfloorconnect(int[] data,TETile[][] world){
        for(int i=data[2];i<data[2]+data[0];i+=data[0]-1){
            for(int j=data[3]+1;j<data[3]+data[1]-1;++j){
                if(world[i][j]==Tileset.FLOOR||world[i][j]==Tileset.UNLOCKED_DOOR) return true;   
            }
        }
        for(int i=data[3];i<data[3]+data[1];i+=data[1]-1){
            for(int j=data[2]+1;j<data[2]+data[0]-1;++j){
                if(world[j][i]==Tileset.FLOOR||world[j][i]==Tileset.UNLOCKED_DOOR) return true;
            }
        }
        return false;
    }

    //create random hallway that connects rooms.
    private void BuildConnectHallway(long s,int[] data,TETile[][] world){
        int[] hdata;
        for(int i=data[2]+1;i<data[2]+data[0]-1;++i){
            for(int j=data[3];j<data[3]+data[1];j+=data[1]-1){
                for(int m=1;m<HEIGHT;++m){
                    if(j!=data[3]&&(m+j<HEIGHT-1)){
                        hdata=new int[]{m,1,i-1,j};
                        if(connect(hdata,world)&&(!hallwaycover(hdata, world))){
                            drawverticalhallway(hdata, world);
                            return;
                        }
                    }
                    else if(j==data[3]&&(data[3]-m>0)){
                        hdata=new int[]{m,1,i-1,j-m+1};
                        if(connect(hdata,world)&&(!hallwaycover(hdata, world))){
                            drawverticalhallway(hdata, world);
                            return;
                        }
                    }
                }
            }
        }//vertical search
        for(int i=data[2];i<data[2]+data[0];i+=data[0]-1){
            for(int j=data[3]+1;j<data[3]+data[1]-1;++j){
                for(int m=1;m<WIDTH;++m){
                    if(i!=data[2]&&(m+i<WIDTH-1)){
                        hdata=new int[]{m,0,i,j-1};
                        if(connect(hdata, world)&&(!hallwaycover(hdata, world))){
                            drawhorizontalhallway(hdata, world);
                            return;
                        }
                    }
                    else if(i==data[2]&&(data[2]-m>0)){
                        hdata=new int[]{m,0,i-m+1,j-1};
                        if(connect(hdata, world)&&(!hallwaycover(hdata, world))){
                            drawhorizontalhallway(hdata, world);
                            return;
                        }
                    }
                }
            }
        }//horizontal search
        BuildRandomRoom_rectangular(s, world); 
    }

    //nothing should cover others.no matter what they are.
    private boolean hallwaycover(int[] data,TETile[][] world){
        if((data[1]==1)&&(data[3]+data[0]<HEIGHT-1)){
            for(int i=data[2];i<data[2]+3;++i){
                for(int j=data[3]+1;j<data[3]+data[0]-1;++j){
                    if(world[i][j]!=Tileset.NOTHING) return true;
                }
            }
        }
        else if((data[1]==0)&&(data[2]+data[0]<WIDTH-1)){
            for(int i=data[3];i<data[3]+3;++i){
                for(int j=data[2]+1;j<data[2]+data[0]-1;++j){
                    if(world[j][i]!=Tileset.NOTHING) return true;
                }
            }
        }
        return false;
    }

    //helper methods
    private void drawverticalhallway(int[] data,TETile[][] world){
        for(int a=data[2];a<data[2]+3;++a){
            for(int b=data[3];b<data[3]+data[0];++b){
                if(a==data[2]+1){
                    if(b==data[3]||b==data[3]+data[0]-1){
                        Random r=new Random(a);
                        if(r.nextInt(10)==1){
                            world[a][b]=Tileset.UNLOCKED_DOOR;
                            continue;
                        }
                    }
                    world[a][b]=Tileset.FLOOR;
                    continue;
                }
                if(world[a][b]==Tileset.FLOOR) continue;
                world[a][b]=Tileset.WALL;
            }
        }
    }

    //helper methods
    private void drawhorizontalhallway(int[] data,TETile[][] world){
        for(int a=data[2];a<data[2]+data[0];++a){
            for(int b=data[3];b<data[3]+3;++b){
                if(b==data[3]+1){
                    if(a==data[2]||a==data[2]+data[0]-1){
                        Random r=new Random(a);
                        if(r.nextInt(10)==1){
                            world[a][b]=Tileset.UNLOCKED_DOOR;
                            continue;
                        }
                    }
                    world[a][b]=Tileset.FLOOR;
                    continue;
                }
                if(world[a][b]==Tileset.FLOOR) continue;
                world[a][b]=Tileset.WALL;
            }
        }
    }

    //check if the room can be connected.
    private boolean canroomconnect(int[] data,TETile[][] world){
        for(int i=data[3]+1;i<data[3]+data[1]-1;++i){
            for(int j=0;j<WIDTH;++j){
                if(world[j][i]==Tileset.FLOOR) return true;
            }
        }
        for(int i=data[2]+1;i<data[2]+data[0]-1;++i){
            for(int j=0;j<HEIGHT;++j){
                if(world[i][j]==Tileset.FLOOR) return true;
            }
        }
        return false;
    }

    //build a random hallway that connects rooms or hallways.
    private void BuildRandomHallway(long s,TETile[][] rh){
        Random r=new Random(s);
        int len=r.nextInt(15)+1;
        /*build vertical hallway while r.nextInt==1,otherwise
         * build horizontal hallway. Plus, the probablity of 
         * these two are same.
         */
        if(1==r.nextInt(2)){
            int rlx=r.nextInt(WIDTH-2);
            int rly=r.nextInt(HEIGHT-len+1);
            if(rly+len>=HEIGHT) return;
            int[] data=new int[]{len,1,rlx,rly};
            if(!connect(data, rh)) return;
            if(hallwaycover(data, rh)) return;
            drawverticalhallway(data, rh);
        }
        else{
            int rlx=r.nextInt(WIDTH-len+1);
            int rly=r.nextInt(HEIGHT-2);
            int[] data=new int[]{len,0,rlx,rly};
            if(rlx+len>=WIDTH) return;
            if(!connect(data, rh)) return;
            if(hallwaycover(data, rh)) return;
            drawhorizontalhallway(data, rh);
        }
    }

    //hallway should connect room or hallway with room or hallway.
    private boolean connect(int[] data, TETile[][] world){
        if(data[1]==1){
            if((data[3]!=0&&world[data[2]+1][data[3]-1]==Tileset.FLOOR)&&
            (data[3]!=HEIGHT-3&&world[data[2]+1][data[3]+data[0]]==Tileset.FLOOR)) return true;
        }//vertical hallway
        else{
            if((data[2]!=0&&world[data[2]-1][data[3]+1]==Tileset.FLOOR)&&
            (data[2]!=WIDTH-3&&world[data[2]+data[0]][data[3]+1]==Tileset.FLOOR)) return true;
        }//horizontal hallway
        return false;
    }

    //initiate the world with Nothing.
    private void intiate(TETile[][] world){
        for(int x=0;x<WIDTH;++x){
            for(int y=0;y<HEIGHT;++y) world[x][y]=Tileset.NOTHING;
        }
    }

    public static void main(String[] args){
        Game g=new Game();
        g.playWithInputString("n123swww");
    }

}