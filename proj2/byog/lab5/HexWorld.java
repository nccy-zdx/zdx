package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;


/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;
    private static final Random RANDOM = new Random(0);
    
    private static void line(TETile[][] linewd,int lh,Pos p,TETile T,int s){
        if(lh-p.py<s){
            for(int i=0;i<(lh-p.py)*2+s;++i){
                linewd[p.px+s-lh+p.py-1+i][lh]=T;
            }
        }
        else{
            for(int i=0;i<(p.py+2*s-1-lh)*2+s;++i){
                linewd[p.px+i+s-p.py-2*s+lh][lh]=T;
            }
        }
    }

    private static void intiate(TETile[][] world){
        for(int x=0;x<WIDTH;++x){
            for(int y=0;y<HEIGHT;++y){
                world[x][y]=Tileset.NOTHING;
            }
        }
    }

    public static void addHexagon(TETile[][] world,int s,Pos p,TETile T){
        for(int j=p.py;j<p.py+2*s;++j){
            line(world, j,p,T,s);
        }
    }

    public static class Pos {
        public int px;
        public int py;

        public Pos(int x,int y){
            px=x;
            py=y;
        }

    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(11);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.PLAYER;
            case 3: return Tileset.FLOOR;
            case 4: return Tileset.GRASS;
            case 5: return Tileset.LOCKED_DOOR;
            case 6: return Tileset.MOUNTAIN;
            case 7: return Tileset.SAND;
            case 8: return Tileset.TREE;
            case 9: return Tileset.WATER;
            case 10: return Tileset.UNLOCKED_DOOR;
            default: return Tileset.NOTHING;
        }
    }

    private static Pos bot_pos(int i,int s){
        if(i%2==0){
            Pos p=new Pos(i/2*(4*s-2), 0);
            return p;
        }
        else{
            i=(i+1)/2;
            Pos p=new Pos(2*s-1+(i-1)*(4*s-2), s);
            return p;
        }
    }

    private static void ncolhex(Pos p,TETile[][] world,int s){
        int col_entity=HEIGHT/(2*s);
        if(p.py==0){
            for(int i=0;i<col_entity;++i){
                addHexagon(world, s, p, randomTile());
                p.py+=2*s;
            }
        }
        else{
            for(int i=0;i<col_entity-1;++i){
                addHexagon(world, s, p, randomTile());
                p.py+=2*s;
            }
        }
    }

    public static void terrhexagon(int s,TETile[][] world){
        int col_num=WIDTH/(4*s-2);
        for(int i=0;i<2*col_num-1;++i){
            ncolhex(bot_pos(i,s), world, s);
        }
    }

    public static void main(String[] args){
        TERenderer ter=new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] heaworld=new TETile[WIDTH][HEIGHT];
        intiate(heaworld);
        terrhexagon(3, heaworld);
        ter.renderFrame(heaworld);
    }

}
