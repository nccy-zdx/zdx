import static org.junit.Assert.assertEquals;

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

                if(minpq.contains(n)){//11,10/*n.sumlength>=bsm.sumlength+g.distance(n.id, bsm.id)*/
                    if((n.sumlength>bsm.sumlength+g.distance(n.id, bsm.id))&&bsm.compareTo(n.preNode)>0){
                        n.preNode=bsm;
                        n.sumlength=bsm.sumlength+n.prelength();
                    }
                }
                else if(!set.contains(adj)){//00
                    n.preNode=bsm;
                    n.destationNode=destation;
                    n.sumlength=bsm.sumlength+n.prelength();
                    minpq.add(n);
                }
                else{//01
                    if((n.sumlength>bsm.sumlength+g.distance(n.id, bsm.id))&&bsm.compareTo(n.preNode)>0){
                        n.preNode=bsm;
                        n.sumlength=bsm.sumlength+n.prelength();
                        minpq.add(n);
                    }
                }
            }
            if(minpq.isEmpty()) break;
            else bsm=minpq.remove();

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
        GraphDB graph=new GraphDB("../library-sp18/data/berkeley-2018.osm.xml");
        List<Long> actual=Router.shortestPath(graph, -122.22938646460354, 37.83665820842015,
        -122.29980246809829, 37.87519067303892);
        System.out.println(actual.size());
        System.out.println(actual);
    }
    /*
     * 
     *  expected:<[206171144, 956454388, 266452326, 956454385, 206171143, 266452324, 956454380, 956454379, 53086040, 266452323, 
     * 206171142, 266452509, 206171141, 266452508, 2402257533, 53086037, 2402257531, 206170698, 4619281997, 206170697, 206170696,
     *  53086034, 53086033, 244500277, 206170888, 206170887, 265520333, 265520332, 53086032, 265520330, 53044497, 53042912, 256544313,
     *  256544312, 256544311, 256544310, 53086029, 956504928, 956504927, 53043659, 956504828, 206172768, 1212905261, 1212905280,
     *  1212905365, 1212905316, 53024909, 244499344, 201618993, 4598334234, 1212905318, 553970499, 244499345, 53024871, 1212905383,
     *  53018859, 1212905320, 553970965, 53024868, 1212905283, 1212905374, 1212905287, 1212905333, 1212905372, 201604652, 310941666,
     *  201540494, 1212905264, 53024867, 1212905317, 53023283, 2391023541, 206093380, 1212905301, 310941757, 53023281, 310941754, 
     * 206093379, 53023279, 53023277, 53023275, 663304140, 53023273, 53111749, 2430670102, 542970828, 53090709, 542971100, 53047615,
     *  53080700, 53020795, 53131689, 53153110, 2817627483, 53091486, 688195477, 53085776, 688195480, 688195487, 4744448230, 52997894,
     *  4744448231, 2853241698, 4744448233, 53043600, 53043602, 53043597, 53040552, 53112629, 53112630, 1237053684, 53099302,
     *  4744448238, 1237053671, 53099304, 53112632, 957658297, 957660711, 247367019, 1237053564, 53096039, 1237053757, 1237053743, 
     * 53065529, 1237053731, 53112636, 1237031707, 53112620, 1237031711, 5061052907, 1237053749, 53112621, 95323761, 1237053736,
     *  1237053724, 2240045668, 53112624, 2240045667, 430992572, 53090197, 430992573, 240469672, 53085437, 53085434, 1237053582, 
     * 92951507, 53085433, 1237053638, 92951634, 53041552, 4559569769, 53085432, 4559569771, 53085430, 53085428, 53085427, 1994957632,
     *  302803293, 53050644, 4235206796, 53050642, 4235206798, 4235206794, 53126424, 4235206793, 4235206789, 53077537, 4235205788, 
     * 53066471, 53056112, 4235205783, 53126421, 1237062765, 53126420, 283261522, 1237062769, 283261516, 4168260357, 283261515, 
     * 4235206800, 283261510, 4235206806, 4168260359, 283261580, 4235206819, 1612672431, 283261579, 2644106909, 4213425298, 2644106915,
     *  4213425301, 243673080, 4213425304, 53097108, 4213425308, 53117370, 4213425312, 4168260372, 4213425314, 53125563, 4168260376, 
     * 53082545, 4213378291, 53108749, 4213378294, 4213378297, 53064691, 4168260381, 4213378301, 53093983, 4213378304, 53129594, 
     * 1745072094, 53100804, 53111881, 4168148254, 53079164, 4213378309, 3701917214, 4213378312, 53079132, 4213378315, 4168148258,
     *  4213378317, 53085639, 4213378321, 4625653663, 4213378323, 53082730, 4213378326, 53020933, 4213378330, 4168148264, 4467801822,
     *  4213378333, 53063521, 4213378337, 4213378339, 3701955177, 3701955182, 4213378340, 53130822, 4213378345, 53130824, 4213378347,
     *  2215072710, 1762740841, 1762740838, 1762740840, 1762740843, 1762740846, 1762740845, 1762740847, 4218827267, 53030282, 
     * 4218827270, 4217221750, 53030281, 4217221751, 4217221748, 2908411319, 1762740913, 1762740916, 1762740919, 5061098659, 
     * 1762740918, 1762740920, 4217221774, 240448878, 53055566, 4217221781, 4924539066, 3695834681, 4217112424, 240448928, 1789060383,
     *  1789060384, 1789060386, 1789060388, 240448922, 53055166,// 240448934, 1789060377, 1789060379, 1789060381, 1789060382, 240448932,
     *  2002199665, 2002199657, 4217112433, 53093329, 4217112435, 2002199681, 2002199667, 4217112453, 53091840,// 53131024, 53131020,
     *  53060268]> 
     * but was:<[206171144, 956454388, 266452326, 956454385, 206171143, 266452324, 956454380, 956454379, 53086040, 
     * 266452323, 206171142, 266452509, 206171141, 266452508, 2402257533, 53086037, 2402257531, 206170698, 4619281997, 206170697,
     *  206170696, 53086034, 53086033, 244500277, 206170888, 206170887, 265520333, 265520332, 53086032, 265520330, 53044497, 53042912,
     *  256544313, 256544312, 256544311, 256544310, 53086029, 956504928, 956504927, 53043659, 956504828, 206172768, 1212905261, 
     * 1212905280, 1212905365, 1212905316, 53024909, 244499344, 201618993, 4598334234, 1212905318, 553970499, 244499345, 53024871, 
     * 1212905383, 53018859, 1212905320, 553970965, 53024868, 1212905283, 1212905374, 1212905287, 1212905333, 1212905372, 201604652, 
     * 310941666, 201540494, 1212905264, 53024867, 1212905317, 53023283, 2391023541, 206093380, 1212905301, 310941757, 53023281, 
     * 310941754, 206093379, 53023279, 53023277, 53023275, 663304140, 53023273, 53111749, 2430670102, 542970828, 53090709, 542971100,
     *  53047615, 53080700, 53020795, 53131689, 53153110, 2817627483, 53091486, 688195477, 53085776, 688195480, 688195487, 4744448230,
     *  52997894, 4744448231, 2853241698, 4744448233, 53043600, 53043602, 53043597, 53040552, 53112629, 53112630, 1237053684, 53099302,
     *  4744448238, 1237053671, 53099304, 53112632, 957658297, 957660711, 247367019, 1237053564, 53096039, 1237053757, 1237053743,
     *  53065529, 1237053731, 53112636, 1237031707, 53112620, 1237031711, 5061052907, 1237053749, 53112621, 95323761, 1237053736, 
     * 1237053724, 2240045668, 53112624, 2240045667, 430992572, 53090197, 430992573, 240469672, 53085437, 53085434, 1237053582, 
     * 92951507, 53085433, 1237053638, 92951634, 53041552, 4559569769, 53085432, 4559569771, 53085430, 53085428, 53085427, 1994957632,
     *  302803293, 53050644, 4235206796, 53050642, 4235206798, 4235206794, 53126424, 4235206793, 4235206789, 53077537, 4235205788,
     *  53066471, 53056112, 4235205783, 53126421, 1237062765, 53126420, 283261522, 1237062769, 283261516, 4168260357, 283261515, 
     * 4235206800, 283261510, 4235206806, 4168260359, 283261580, 4235206819, 1612672431, 283261579, 2644106909, 4213425298, 2644106915,
     *  4213425301, 243673080, 4213425304, 53097108, 4213425308, 53117370, 4213425312, 4168260372, 4213425314, 53125563, 4168260376,
     *  53082545, 4213378291, 53108749, 4213378294, 4213378297, 53064691, 4168260381, 4213378301, 53093983, 4213378304, 53129594, 
     * 1745072094, 53100804, 53111881, 4168148254, 53079164, 4213378309, 3701917214, 4213378312, 53079132, 4213378315, 4168148258,
     *  4213378317, 53085639, 4213378321, 4625653663, 4213378323, 53082730, 4213378326, 53020933, 4213378330, 4168148264, 4467801822,
     *  4213378333, 53063521, 4213378337, 4213378339, 3701955177, 3701955182, 4213378340, 53130822, 4213378345, 53130824, 4213378347,
     *  2215072710, 1762740841, 1762740838, 1762740840, 1762740843, 1762740846, 1762740845, 1762740847, 4218827267, 53030282, 
     * 4218827270, 4217221750, 53030281, 4217221751, 4217221748, 2908411319, 1762740913, 1762740916, 1762740919, 5061098659, 1762740918,
     *  1762740920, 4217221774, 240448878, 53055566, 4217221781, 4924539066, 3695834681, 4217112424, 240448928, 1789060383, 1789060384,
     *  1789060386, 1789060388, 240448922, 53055166, 4217112436, 53093332, 4217112438, 2002199666, 2002199655, 4217112455, 4217112456,
     *  53091841, 4217112460, 4217112461, 53098218, 53131024, 53131020, 53060268]>
     */

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
