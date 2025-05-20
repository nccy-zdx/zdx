package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;

import java.awt.Color;
import java.util.Map;
import java.util.Random;

public class Clorus extends Creature{
        /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    /** creates plip with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /** creates a plip with energy equal to 1. */
    public Clorus() {
        this(1);
    }

    public Color color() {
        r=34;
        b=231;
        g = 0;
        return color(r, g, b);
    }

    public void move() {
        energy-=0.03;
    }

    public void attack(Creature c) {
        energy+=c.energy();
    }

    public void stay() {
        energy-=0.01;
    }
    
    public Clorus replicate() {
        Clorus descendant=new Clorus(energy/2.0);
        energy=energy/2.0;
        return descendant;
    }

    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        
        if(!hasEmpty(neighbors)){
            return new Action(Action.ActionType.STAY);
        }
        else if(hasPlip(neighbors)){
            for(Direction dir:neighbors.keySet()){
                if(neighbors.get(dir).name().equals("plip")){
                    attack(((Creature)neighbors.get(dir)));
                    return new Action(Action.ActionType.ATTACK, dir);
                }
            }
        }
        else if(energy>=1.0){
            for(Direction dir:neighbors.keySet()){
                if(neighbors.get(dir).name().equals("empty")){
                    return new Action(Action.ActionType.REPLICATE, dir);
                }
            }
        }
        Direction di;
        Random r=new Random();
        if(r.nextInt(4)==0){
            di=Direction.TOP;
        }
        else if(r.nextInt(4)==1){
            di=Direction.BOTTOM;
        }
        else if(r.nextInt(4)==2){
            di=Direction.LEFT;
        }
        else{
            di=Direction.RIGHT;
        }
        for(Direction dir:neighbors.keySet()){
            if(neighbors.get(dir).name().equals("empty")){
                di=dir;
                if(r.nextInt(2)==0) return new Action(Action.ActionType.MOVE, dir);
            }
        }
        return new Action(Action.ActionType.MOVE,di);
    }

    private boolean hasEmpty(Map<Direction, Occupant> neighbors){
        for(Occupant oc:neighbors.values()){
            if(oc.name().equals("empty")){
                return true;
            }
        }
        return false;
    }

    private boolean hasPlip(Map<Direction, Occupant> neighbors){
        for(Occupant oc:neighbors.values()){
            if(oc.name().equals("plip")){
                return true;
            }
        }
        return false;
    }
}
