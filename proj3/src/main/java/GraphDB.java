import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    Map<String,Node> Nodes=new LinkedHashMap<>();
    private Map<String,Edge> Edges=new LinkedHashMap<>();

    static class Node implements Comparable<Node>{
        Long id;
        double lon;
        double lat;
        Map<String,String> extrainfo;
        List<Edge> Edges;
        Node preNode;
        Node destationNode;
        double sumlength=100000000;

        public Node(String id,String lon,String lat){
            this.id=Long.parseLong(id);
            this.lon=Double.parseDouble(lon);
            this.lat=Double.parseDouble(lat);
            extrainfo=new HashMap<>();
            Edges=new ArrayList<>();
        }

        public Node(Long id,double lon,double lat){
            this.id=id;
            this.lon=lon;
            this.lat=lat;
            extrainfo=new HashMap<>();
            Edges=new ArrayList<>();
        }

        public double prelength(){//probably here has something wrong because it`s not the path distance but great-circle distance.
            return distance(lon,lat,preNode.lon,preNode.lat);
        }

        public double todestation(){
            return distance(lon,lat,destationNode.lon,destationNode.lat);
        }

        @Override
        public int compareTo(Node n){
            if(todestation()+sumlength<n.todestation()+n.sumlength) return -1;
            else if(todestation()+sumlength>n.todestation()+n.sumlength) return 1;
            else return 0;
        }

    }

    static class Edge{
        Long id;
        Map<String,String> extrainfo;
        List<String> Nodesid;

        public Edge(String id){
            this.id=Long.parseLong(id);
            extrainfo=new HashMap<>();
            Nodesid=new ArrayList<>();
        }
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Map<String,Node> nodes=new LinkedHashMap<>();
        for(Edge e:Edges.values()){
            for(String key:e.Nodesid){
                List<Long> adj=((List<Long>)adjacent(Long.parseLong(key)));
                if(!adj.isEmpty()){
                    nodes.put(key, Nodes.get(key));
                }
            }
        }
        Nodes=nodes;
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        List<Long> vertices=new ArrayList<>();
        for(Node n:Nodes.values()){
            vertices.add(n.id);
        }
        return vertices;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        StringBuffer sb=new StringBuffer();
        sb.append(v);
        String id=new String(sb);
        List<Long> adja=new ArrayList<>();
        List<Edge> e=Nodes.get(id).Edges;
        for(int i=0;i<e.size();++i){
            List<String> strs=e.get(i).Nodesid;
            for(int j=0;j<strs.size();++j){
                if(strs.get(j).equals(id)){
                    if(j!=0){
                        adja.add(Long.parseLong(strs.get(j-1)));
                    }
                    if(j!=strs.size()-1){
                        adja.add(Long.parseLong(strs.get(j+1)));
                    }
                    break;
                }
            }
        }
        return adja;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double min=10000;
        long minid=0;
        for(Node n:Nodes.values()){
            double dist=distance(lon, lat, n.lon, n.lat);
            if(dist<min){
                min=dist;
                minid=n.id;
            }
        }
        return minid;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        StringBuffer sb=new StringBuffer();
        sb.append(v);
        String id=new String(sb);
        return this.Nodes.get(id).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        StringBuffer sb=new StringBuffer();
        sb.append(v);
        String id=new String(sb);
        return this.Nodes.get(id).lat;
    }

    void addNode(Node n){
        this.Nodes.put(n.id.toString(), n);
    }

    void addEdge(Edge e){
        Edges.put(e.id.toString(), e);
    }
  
}