import java.io.*;
public class NBody{

    //the images index
    public static String imgindex="E:\\cs61b_da_java\\zdx\\proj0\\images\\";
 
    public static String backgroundpath= imgindex+"starfield.jpg";

    //read universe redius from txt
    public static double ReadRadius(String filename){
        double r=0;
        try{
            FileReader fr=new FileReader(filename);
            BufferedReader buf=new BufferedReader(fr);
            
            //pass the first line
            buf.readLine();
            
            r=Double.parseDouble(buf.readLine());
            fr.close();
            return r;
        }
        catch(IOException e){
            System.out.println("Can`t find file");
        }
        return r;
    }

    //read planet`s data and store in planet class
    public static Planet[] readPlanets(String filename){
        Planet[] planets=new Planet[5];
        try{
            FileReader fr=new FileReader(filename);
            BufferedReader buf=new BufferedReader(fr);

            //pass the two lines above
            buf.readLine();buf.readLine();
            
            String thisline;
            int count=0;
            while((thisline=buf.readLine())!=null){
                String[] strs=thisline.split("\\s+",7);
                for(int i=1;i<strs.length;++i){
                    Planet p=new Planet(Double.parseDouble(strs[1]),Double.parseDouble(strs[2]),
                    Double.parseDouble(strs[3]),Double.parseDouble(strs[4]),
                    Double.parseDouble(strs[5]),imgindex+strs[6]);
                    planets[count]=p;//I don`t want to say anything,ok???
                }
                ++count;
                //stop when all planets have been stored
                if(count==5) break;
            }
            fr.close();
            }
        catch(IOException e){
            System.out.println("Can`t find file");
        }
        return planets;
    }

    public static void main(String[] args){
        double T=Double.parseDouble(args[0]);
        double dt=Double.parseDouble(args[1]);
        String filename=args[2];
        double radius=ReadRadius(filename);
        Planet[] planets=readPlanets(filename);
        StdDraw.setScale(-radius,radius);
        StdDraw.enableDoubleBuffering();
        for(double t=0.0;t<=T;t+=dt){
            double[] xForces=new double[5];
            double[] yForces=new double[5];
            for(int i=0;i<5;++i){
                xForces[i]=planets[i].calcNetForceExertedByX(planets);
                yForces[i]=planets[i].calcNetForceExertedByY(planets);
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, backgroundpath);
            for(Planet planet:planets){
                planet.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
        System.out.println(planets.length);
        System.out.println(radius);
        for(Planet planet:planets){
            System.out.println(planet.xxPos+"  "+planet.yyPos+"  "+
            planet.xxVel+"  "+planet.yyVel+"  "+planet.mass+"     "+planet.imgFileName);
        }
    }
}