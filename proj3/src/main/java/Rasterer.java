import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    //longitude 1 means 111194.87m.
    public Rasterer() {

    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        Boolean querysuccess;
        int depth=0;

        //set query_success
        if(params.get("ullat")>MapServer.ROOT_ULLAT||
        params.get("lrlat")<MapServer.ROOT_LRLAT||
        params.get("ullon")<MapServer.ROOT_ULLON||
        params.get("lrlon")>MapServer.ROOT_LRLON||
        params.get("ullat")<params.get("lrlat")||
        params.get("ullon")>params.get("lrlon")){
            querysuccess=false;
            results.put("query_success", querysuccess);
        }
        else{
            querysuccess=true;
            results.put("query_success", querysuccess);
        }
 
        //set depth
        double LonDPP=(params.get("lrlon")-params.get("ullon"))/params.get("w");
        double longth=(MapServer.ROOT_LRLON-MapServer.ROOT_ULLON)/MapServer.TILE_SIZE;
        double lanth=MapServer.ROOT_ULLAT-MapServer.ROOT_LRLAT;
        for(int i=0;i<8;++i){
            if(longth<=LonDPP){
                depth=i;
                results.put("depth", depth);
                break;
            }
            if(i==7){
                depth=7;
                results.put("depth", depth);
                break;
            }
            longth=longth/2.0;
            lanth=lanth/2.0;
        }

        //set raster_ _
        longth=longth*MapServer.TILE_SIZE;
        int downside=0,upperside=0,rightside=0,leftside=0;
        for(int i=0;i<Math.pow(2, depth);++i){
            if(params.get("lrlat")<MapServer.ROOT_ULLAT-lanth*i&&params.get("lrlat")>MapServer.ROOT_ULLAT-lanth*(i+1)){
                downside=i;
                results.put("raster_lr_lat", MapServer.ROOT_ULLAT-lanth*(i+1));
            }
            if(params.get("ullat")<MapServer.ROOT_ULLAT-lanth*i&&params.get("ullat")>MapServer.ROOT_ULLAT-lanth*(i+1)){
                upperside=i;
                results.put("raster_ul_lat", MapServer.ROOT_ULLAT-lanth*i);
            }
            if(params.get("lrlon")>MapServer.ROOT_ULLON+longth*i&&params.get("lrlon")<MapServer.ROOT_ULLON+longth*(i+1)){
                rightside=i;
                results.put("raster_lr_lon", MapServer.ROOT_ULLON+longth*(i+1));
            }
            if(params.get("ullon")>MapServer.ROOT_ULLON+longth*i&&params.get("ullon")<MapServer.ROOT_ULLON+longth*(i+1)){
                leftside=i;
                results.put("raster_ul_lon", MapServer.ROOT_ULLON+longth*i);
            }
        }

        //set render_grid
        String[][] grid=new String[downside-upperside+1][rightside-leftside+1];
        for(int i=0;i<downside-upperside+1;++i){
            for(int j=0;j<rightside-leftside+1;++j){
                grid[i][j]="d"+depth+"_x"+(leftside+j)+"_y"+(upperside+i)+".png";
            }
        }
        results.put("render_grid", grid);

        return results;
    }

}