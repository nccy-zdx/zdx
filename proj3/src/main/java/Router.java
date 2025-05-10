import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,double destlon, double destlat) {
        List<Long> shortestNode=new ArrayList<>();
        Stack<Long> store=new Stack<>();
        Set<String> set=new HashSet<>();
        PriorityQueue<GraphDB.Node> minpq=new PriorityQueue<>();

        StringBuffer sb=new StringBuffer();
        sb.append(g.closest(stlon, stlat));
        String startkey=new String(sb);

        StringBuffer sb0=new StringBuffer();
        sb0.append(g.closest(destlon, destlat));
        String destationkey=new String(sb0);

        GraphDB.Node start=g.Nodes.get(startkey);
        GraphDB.Node destation=g.Nodes.get(destationkey);

        //A* algorithm
        GraphDB.Node bsm=start;
        bsm.preNode=null;
        bsm.destationNode=destation;
        bsm.StartNode=start;
        bsm.sumlength=0;

        minpq.add(bsm);
        set.add(startkey);

        while (!bsm.id.equals(destation.id)) {//Long is also an Object......
            for(Long adjkey:g.adjacent(bsm.id)){
                if(bsm.preNode!=null&&adjkey.equals(bsm.preNode.id)) continue;

                StringBuffer sb1=new StringBuffer();
                sb1.append(adjkey);
                String adj=new String(sb1);
                GraphDB.Node n=g.Nodes.get(adj);

                if(minpq.contains(n)){
                    if(n.sumlength>bsm.sumlength+g.distance(n.id, bsm.id)){
                        n.preNode=bsm;
                        n.sumlength=bsm.sumlength+n.prelength();
                    }
                }
                else if(!set.contains(adj)){
                    n.preNode=bsm;
                    n.destationNode=destation;
                    n.StartNode=start;
                    n.sumlength=bsm.sumlength+n.prelength();
                    minpq.add(n);
                }
            }
            if(minpq.isEmpty()){
                break;
            }
            else bsm=minpq.poll();

            StringBuffer sbb=new StringBuffer();
            sbb.append(bsm.id);
            String bsmid=new String(sbb);

            set.add(bsmid);
        }

        while(bsm!=null){
            store.push(bsm.id);
            bsm=bsm.preNode;
        }

        while(!store.isEmpty()){
            shortestNode.add(store.pop());
        }

        return shortestNode;
    }

    public static void main(String[] args) {
        GraphDB graph = new GraphDB("../library-sp18/data/berkeley-2018.osm.xml");
        List<Long> actual=Router.shortestPath(graph, -122.27572799879053, 37.873826273550726, 
        -122.28924763881035, 37.83640692189568);
        System.out.println(actual.size());
        System.out.println(actual);
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {


        return null; // FIXME
    }

    /*Test Failed!
Found route differs for input: {end_lat=37.83640692189568, start_lon=-122.27572799879053, start_lat=37.873826273550726, 
    end_lon=-122.28924763881035}.
 expected:<[4226425298, 53093343, 4226425299, 4226425300, 4970151578, 2283661440, 4226425303, 53093345, 4226425302, 
 4168189958, 4226425297, 53055185, 4226425293, 4168189955, 4226425290, 53042657, 4226423184, 4168189954, 4168189953, 2144915128, 
 53116963, 2144915123, 307506663, 4168189948, 4070600862, 4168189947, 4070600865, 4228032718, 53121255, 4228032715, 4168189945, 
 2146034304, 57066394, 3516414498, 1937165440, 4168189944, 1937165366, 1937165369, 57066388, 4228032696, 4168189942, 4228031775, 
 53146078, 4228031773, 4228031753, 53063545, 4228031747, 4228031740, 53041081, 4168189935, 4228031736, 53020968, 4228031733, 
 4168189933, 4924581610, 4235130338, 53082738, 4235130335, 4235130332, 53085610, 4235130329, 4235130327, 53079143, 4235130324, 
 4235130322, 53111887, 4235130319, 3200739869, 825409988, 4235130317, 53129615, 4235130314, 4235130309, 53037903, 4235130306,
  4235130302,53064674, 4235130299, 4233843999, 53082568, 4233843996, 4169547883, 4233843978, 53117382, 4233843970, 275787148, 687179338, 
  4181957443, 370473849, 1996736279, 1996736256, 275786806, 275787147, 687179339, 275782495, 275782494, 275782493, 275782456, 
  1996736253, 275782492, 2086720410, 2086720385, 2086736118, 275782450, 2086736113, 2086720455, 2086720446, 4169543150, 2086720418, 
  2086736119, 275782491, 2086736160, 275782489, 4169543148, 275782486, 275782485, 1237053680, 275782484, 275777737, 275777736,
   1237053721, 275772512, 4169547882, 275772427, 53088328, 1237053687, 92951597, 4169547881, 1237053659, 92951553, 92951540, 
   2345081235, 92951523, 2345081238, 1237053598, 240469592, 92951507, 1237053689, 4169547879, 92951489, 92951651, 4169547878, 
   92951470, 92951456, 4168227707, 92951442, 1237060556, 92951421, 1237060526, 280699441, 283261586, 1237060564, 4168227702, 
   1237060562, 280699442, 280699449, 280699448, 53102030, 53111590, 53124570, 53124572, 4923455149, 4602705885, 280698106, 
   53124574, 2386673037, 2386673028, 2386673022, 5049820823, 5049820822, 5049820821, 5049820820, 2386673011, 2386673007, 
   52984802, 2386672987, 52984798, 52984795, 3231927622, 53149464, 53149462, 4923455202]
   but was:<[4226425298, 53093343,4226425299, 4226425300, 4970151578, 2283661440, 4226425303, 53093345, 4226425302, 4168189958, 
   4226425297, 53055185, 4226425293, 4168189955, 4226425290, 53042657, 4226423184, 4168189954, 4168189953, 2144915128, 
   53116963, 2144915123, 307506663, 4168189948, 4070600862, 4168189947, 4070600865, 4228032718, 53121255, 4228032715, 
   4168189945, 2146034304,57066394, 3516414498, 1937165440, 4168189944, 1937165366, 1937165369, 57066388, 4228032696, 
   4168189942, 4228031775, 53146078, 4228031773, 4228031753, 53063545, 4228031747, 4228031740, 53041081, 4168189935, 
   4228031736, 53020968,  4228031733, 4168189933, 4924581610, 4235130338, 53082738, 4235130335, 4235130332, 53085610, 
   4235130329, 4235130327,  53079143, 4235130324, 4235130322, 53111887, 4235130319, 3200739869, 825409988, 4235130317, 
   53129615, 4235130314, 4235130309, 53037903, 4235130306, 4235130302, 53064674, 4235130299, 4233843999, 53082568, 4233843996,
    4169547883,  4233843978, 53117382, 4233843970, 275787148, 687179338, 4181957443, 370473849, 1996736279, 1996736256, 275786806, 
        275787147, 687179339, 275782495, 275782494, 275782493, 275782456, 1996736253, 275782492, 2086720410, 2086720385,
         2086736118, 275782450, 2086736113, 2086720455, 2086720446, 4169543150, 2086720418, 2086736119, 275782491, 2086736160,
          275782489, 4169543148, 275782486, 275782485, 1237053680, 275782484, 275777737, 275772970, 275782947, 275772972,
           1237053619, 92951567, 1237053704, 92951582, 92951385, 1237053673, 53085973, 2345081236, 53107939, 4169547880,
            2345081240, 1237053616, 240469594, 53085433, 1237053675, 4550436140, 53107920, 53096361, 4168227709, 53124565, 
            53113346, 53124566, 1237060520, 4607053146, 4607053154, 92951682, 4168227704, 1237060523, 53124567, 283261585,
             283261586, 1237060564, 4168227702, 1237060562, 280699442, 280699449, 280699448, 53102030, 53111590, 53124570,
              53124572, 4923455149, 4602705885, 280698106, 53124574, 2386673037, 2386673028, 2386673022, 5049820823, 5049820822,
               5049820821, 5049820820, 2386673011, 2386673007, 52984802, 2386672987, 52984798, 52984795, 3231927622, 53149464,
                53149462, 4923455202]>
    at AGRouterTest.runTest:70 (AGRouterTest.java)
    at AGRouterTest.testShortestPath:77 (AGRouterTest.java)*/


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
