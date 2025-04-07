package byog.Core;

import java.util.Random;
import java.awt.Font;

import edu.princeton.cs.introcs.StdDraw;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static double mousex;
    private static double mousey;
    private static TETile tilenow;
    private static TETile tilefuture;
    private static int[] playerpostionnow=new int[2];
    private static int[] playerpositionfuture=new int[2];

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
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
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        intiate(finalWorldFrame);
        finalWorldFrame[0][0]=Tileset.PLAYER;
        playerpostionnow[0]=0;
        playerpostionnow[1]=0;
        tilenow=Tileset.NOTHING;
        boolean gameover=false;
        long seed=0;
        mousex=StdDraw.mouseX();
        mousey=StdDraw.mouseY();
        BuildRandomWorld(seed, finalWorldFrame);
        ter.renderFrame(finalWorldFrame);
        while(!gameover){
            while(StdDraw.hasNextKeyTyped()){
                char control =StdDraw.nextKeyTyped();
                if(':'==control){
                    if(StdDraw.hasNextKeyTyped()){
                        if('q'==StdDraw.nextKeyTyped()){
                            System.out.println("game over, please close the window.");
                            gameover=true;
                        }
                        else System.out.println("illegal instruction : without q, please enter one more time");
                        break;
                    }
                }
                if('w'!=control&&'a'!=control&&'s'!=control&&'d'!=control){
                    System.out.println("illegal instruction, please enter one more time.");
                    break;
                }
                else playermove(finalWorldFrame, control);
            }
            if(mousemove(StdDraw.mouseX(), StdDraw.mouseY())) showtile(finalWorldFrame);
        }
        return finalWorldFrame;
    }

    private void playermove(TETile[][] world,int ch){
        if(ch=='w'){
            if(playerpostionnow[1]+1==HEIGHT) System.out.println("you can`t move across the bound.try other direction.");
            else if(world[playerpostionnow[0]][playerpostionnow[1]+1]==Tileset.WALL||
            world[playerpostionnow[0]][playerpostionnow[1]+1]==Tileset.LOCKED_DOOR)
            System.out.println("you can`t move across wall or locked door. go other place.");
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
            System.out.println("you can`t move across wall or locked door. go other place.");
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
            System.out.println("you can`t move across wall or locked door. go other place.");
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
            System.out.println("you can`t move across wall or locked door. go other place.");
            else{
                playerpositionfuture[0]=playerpostionnow[0]+1;
                playerpositionfuture[1]=playerpostionnow[1];
                drawplayer(world);
            }
        }
    }

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
        StdDraw.clear();;
        ter.renderFrame(world);
        if(mousex>=WIDTH||mousey>=HEIGHT||mousex<0||mousey<0) return;
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(5, HEIGHT-1, world[(int) mousex][(int) mousey].description());
        StdDraw.show();
    }

    //build a random world consists of rooms and hallways.
    private TETile[][] BuildRandomWorld(long s,TETile[][] rw){
        Random r=new Random(s);
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
                if(world[i][j]==Tileset.FLOOR) return true;   
            }
        }
        for(int i=data[3];i<data[3]+data[1];i+=data[1]-1){
            for(int j=data[2]+1;j<data[2]+data[0]-1;++j){
                if(world[j][i]==Tileset.FLOOR) return true;
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
    private static void intiate(TETile[][] world){
        for(int x=0;x<WIDTH;++x){
            for(int y=0;y<HEIGHT;++y) world[x][y]=Tileset.NOTHING;
        }
    }

    public static void main(String[] args){
        Game g=new Game();
        g.playWithInputString(null);
    }

}