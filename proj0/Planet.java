public class Planet {
    
    //Gravity Constant
    static final double G=6.67*1e-11;

    //the parameter used;
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    // two initialize constructor,the second one for copy;
    public Planet(double xP,double yP,double xV,double yV,double m,String img){
        xxPos=xP;
        yyPos=yP;
        xxVel=xV;
        yyVel=yV;
        mass=m;
        imgFileName=img;
    }

    public Planet(Planet P){
        xxPos=P.xxPos;
        yyPos=P.yyPos;
        xxVel=P.xxVel;
        yyVel=P.yyVel;
        mass=P.mass;
        imgFileName=P.imgFileName;
    }

    //helper methods that used for calculate datas above;
    public double calcDistance(Planet Rel_P){
        double dx=Rel_P.xxPos-xxPos;
        double dy=Rel_P.yyPos-yyPos;
        double r=dx*dx+dy*dy;
        /*Bisection method for solving square root;though it can`t work well, maybe it`s because the restrictions;
        for(;r-r_2*r_2>0.1||r_2*r_2-r>0.1;){
           double r_2cp=r_2;
           if(r_2*r_2<r) r_2=(r_2cp+r_2)/2;
           if(r_2*r_2>r) r_2=r_2/2;
        }*/
        return Math.sqrt(r);
    }

    public double calcForceExertedBy(Planet Rel_P){
        double r=calcDistance(Rel_P);
        double gforce=G*mass*Rel_P.mass/(r*r);
        return gforce;
    }

    public double calcForceExertedByX(Planet Rel_P){
        double gforce=calcForceExertedBy(Rel_P);
        double dx=Rel_P.xxPos-xxPos;
        double r=calcDistance(Rel_P);
        double gforcex=gforce*dx/r;
        return gforcex;
    }

    public double calcForceExertedByY(Planet Rel_P){
        double gforce=calcForceExertedBy(Rel_P);
        double dy=Rel_P.yyPos-yyPos;
        double r=calcDistance(Rel_P);
        double gforcey=gforce*dy/r;
        return gforcey;
    }

    public double calcNetForceExertedByX(Planet[] planets){
        double gforcenetx=0;
        for(Planet planet:planets){
            if(!this.equals(planet)) gforcenetx+=calcForceExertedByX(planet);
        }
        return gforcenetx;
    }

    public double calcNetForceExertedByY(Planet[] planets){
        double gforcenety=0;
        for(Planet planet:planets){
            if(!this.equals(planet)) gforcenety+=calcForceExertedByY(planet);
        }
        return gforcenety;
    }

    public void update(double time,double forcex,double forcey){
        double ax=forcex/mass;
        double ay=forcey/mass;
        xxVel+=time*ax;
        yyVel+=time*ay;
        xxPos+=time*xxVel;
        yyPos+=time*yyVel;
    }

    //draw planet onto the screen
    public void draw(){
        StdDraw.picture(xxPos, yyPos, imgFileName);
    }

}
