package byog.Core;

import java.util.Random;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static int inputseed=0;
    private int count=0;

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
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        intiate(finalWorldFrame);
        int seed=0;
        if(input.charAt(input.length()-1)=='q'&&input.charAt(input.length()-2)==':') quittype(input);
        if(input.charAt(0)=='n') seed=newgametype(input);
        else if(input.charAt(0)=='l') seed=loadtype();
        else{
            System.out.println("illegal or wrong instruction, please open one more time");
            return null;
        }
        BuildRandomWorld(seed, finalWorldFrame);
        return finalWorldFrame;
    }//n,s,l,:q

    private int newgametype(String input){
        int scount=0;
        for(int i=1;i<input.length();++i){
            if(input.charAt(i)=='s'){
                scount=i;
                break;
            }
        }
        if(scount==0){
            System.out.println("please enter s to save your input");
            return 0;
        }
        String str=input.substring(1, scount);
        inputseed=Integer.parseInt(str);
        ++count;
        return inputseed;
    }

    private int loadtype(){
        if(count==0){
            System.out.println("No prior game loaded");
            return 1;
        }
        return inputseed;
    }

    private void quittype(String input){
        input=input.substring(0, input.length()-2);
    }

    //build a random world consists of rooms and hallways.
    private TETile[][] BuildRandomWorld(int s,TETile[][] rw){
        Random r=new Random(s);
        int rn=r.nextInt(50)+100;
        for(int i=0;i<rn;++i){
            if(r.nextInt(2)==0) BuildRandomRoom_rectangular(r.nextInt(), rw);
            else BuildRandomHallway(s, rw);
        }
        return rw;
    }

    //build a random room which won`t cover anything.
    private void BuildRandomRoom_rectangular(int s,TETile[][] rr_rectangular){
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
    private void connect_rooms(int s,int[] data,TETile[][] world){
        if(!canroomconnect(data, world)) return;
        Random r=new Random(s);
        while(!roomfloorconnect(data,world)){
            BuildConnectHallway(r.nextInt(),data, world);
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
    private void BuildConnectHallway(int s,int[] data,TETile[][] world){
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
    private void BuildRandomHallway(int s,TETile[][] rh){
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
        TERenderer t=new TERenderer();
        t.initialize(WIDTH, HEIGHT);
        TETile[][] tx=new TETile[WIDTH][HEIGHT];
        Game g=new Game();
        tx=g.playWithInputString("n56545s");
        tx=g.playWithInputString("l");
        t.renderFrame(tx);
    }

}