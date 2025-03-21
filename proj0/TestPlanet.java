public class TestPlanet {
    public static void main(String[] args){
            
    Planet planet1=new Planet(0,0,1,1,1,"star.gif");
    Planet planet2=new Planet(0,1,1,1,1,"sun.gif");
    System.out.println(planet1.calcForceExertedBy(planet2));
    }

}
