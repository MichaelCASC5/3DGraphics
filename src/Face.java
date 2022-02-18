import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Face{
    private double[] xPoints, yPoints, zPoints;
    private double[] x,y,z;
    
    // private int HEIGHT, WIDTH;
    private boolean draw;
    
    public Face(){
        xPoints = new double[0];
        yPoints = new double[0];
        zPoints = new double[0];
        
        x = new double[0];
        y = new double[0];
        z = new double[0];
        
        // HEIGHT = 0;
        // WIDTH = 0;
        
        draw = false;
    }
    public Face(double[] a, double[] b, double[] c){
        xPoints = new double[a.length];
        yPoints = new double[b.length];
        zPoints = new double[c.length];
        
        x = new double[a.length];
        y = new double[b.length];
        z = new double[c.length];
        
        setArray(xPoints,a);
        setArray(yPoints,b);
        setArray(zPoints,c);
        setArray(x,a);
        setArray(y,b);
        setArray(z,c);
        
        // HEIGHT = h;
        // WIDTH = w;
        
        draw = false;
    }
    public void setX(double[] n){
        xPoints = new double[n.length];
        x = new double[n.length];
        
        for(int i=0;i<n.length;i++){
            xPoints[i] = n[i];
            x[i] = n[i];
        }
    }
    public void setY(double[] n){
        yPoints = new double[n.length];
        y = new double[n.length];
        
        for(int i=0;i<n.length;i++){
            yPoints[i] = n[i];
            y[i] = n[i];
        }
    }
    public void setZ(double[] n){
        zPoints = new double[n.length];
        z = new double[n.length];
        
        for(int i=0;i<n.length;i++){
            zPoints[i] = n[i];
            z[i] = n[i];
        }
    }
    public double[] getXPoints(){
        return xPoints;
    }
    public double[] getYPoints(){
        return yPoints;
    }
    public double[] getZPoints(){
        return zPoints;
    }
    public double[] getX(){
        return x;
    }
    public double[] getY(){
        return y;
    }
    public double[] getZ(){
        return z;
    }
    public void setAll(Face f){
        xPoints = new double[f.getXPoints().length];
        yPoints = new double[f.getYPoints().length];
        zPoints = new double[f.getZPoints().length];
        
        x = new double[f.getXPoints().length];
        y = new double[f.getYPoints().length];
        z = new double[f.getZPoints().length];
        
        setArray(xPoints,f.getXPoints());
        setArray(yPoints,f.getYPoints());
        setArray(zPoints,f.getZPoints());
        
        setArray(x,f.getX());
        setArray(y,f.getY());
        setArray(z,f.getZ());
    }
    public void printXP(){
        for(int i=0;i<xPoints.length;i++){
            System.out.print(xPoints[i] + " ");
        }
        System.out.println();
    }
    public void printYP(){
        for(int i=0;i<yPoints.length;i++){
            System.out.print(yPoints[i] + " ");
        }
        System.out.println();
    }
    public void printZP(){
        for(int i=0;i<zPoints.length;i++){
            System.out.print(zPoints[i] + " ");
        }
        System.out.println();
    }
    public void setArray(double[] a, double[] b){
        if(a.length == b.length){
            for(int i=0;i<a.length;i++){
                a[i] = b[i];
            }
        }
    }
    public void reset(){
        for(int i=0;i<xPoints.length;i++){
            x[i] = xPoints[i];
            y[i] = yPoints[i];
            z[i] = zPoints[i];
        }
        draw = true;
    }
    public void toscreen(Camera cam){
        int count = 0;
        for(int i=0;i<x.length;i++){
            double xDist,yDist,zDist;
            xDist = x[i] - cam.getX();
            yDist = y[i] - cam.getY();
            zDist = z[i] - cam.getZ();
            
            double parr;
            parr = (1000.0/(yDist));
            parr=Math.abs(parr);
//            boolean neg = false;
//            if(yDist < 0){
//                neg = true;
//            }
            
            x[i] = ((cam.getWidth()/2)+(parr*(xDist)));
            y[i] = (((cam.getHeight()/2)-(parr*zDist)));
            
            /*
                Counts if all the vertices of a face stretch beyond the camera's view.
                If all vertices do, then don't draw the face.
                This is done here rather than in draw, as if it is done in draw, then
                only part of the single face will draw, and another part will not be.
            */
            if(x[i] < 0 || x[i] > cam.getWidth() || y[i] < 0 || y[i] > cam.getHeight()){
                count++;
            }
//            if(neg){
//                if(y[i] < HEIGHT*-1){
//                    y[i] = HEIGHT*-1;
//                }else if(y[i] < HEIGHT){
//                    y[i] = HEIGHT;
//                }
//                if(x[i] < WIDTH*-1){
//                    x[i] = WIDTH*-1;
//                }else if(x[i] < WIDTH){
//                    x[i] = WIDTH;
//                }
//            }
        }
        if(count >= x.length){
           draw = false;
        }
    }
    public void rotateZ(Camera cam){
        double xDist,yDist;
        double a,b;
        
        for(int i=0;i<x.length;i++){
            
            xDist = x[i] - cam.getX();
            yDist = y[i] - cam.getY();
            
            a = xDist*Math.cos(cam.getYaw()/(180.0/Math.PI));
            b = yDist*Math.sin(cam.getYaw()/(180.0/Math.PI));
            
            x[i] = (a - b + cam.getX());
            
            a = yDist*Math.cos(cam.getYaw()/(180.0/Math.PI));
            b = xDist*Math.sin(cam.getYaw()/(180.0/Math.PI));
            
            y[i] = (a + b + cam.getY());
        }
    }
    public void rotateX(Camera cam){
        double yDist,zDist;
        double a,b;
        
        int count;
        count = 0;
        
        for(int i=0;i<x.length;i++){
            yDist = y[i] - cam.getY();
            zDist = zPoints[i] - cam.getZ();
            
            a = yDist*Math.cos(cam.getPitch()/(180.0/Math.PI));
            b = zDist*Math.sin(cam.getPitch()/(180.0/Math.PI));
            
            y[i] = (a - b + cam.getY());
            
            a = zDist*Math.cos(cam.getPitch()/(180.0/Math.PI));
            b = yDist*Math.sin(cam.getPitch()/(180.0/Math.PI));
            
            z[i] = (a + b + cam.getZ());
            
            if(y[i] < cam.getY()){
                count++;
            }
        }
        if(count >= x.length){
            draw = false;
        }
    }
    public double dist(Camera cam){
        double output;
        
        int xsum=0,ysum=0,zsum=0;
        for(int i=0;i<xPoints.length;i++){
            xsum+=xPoints[i];
            ysum+=yPoints[i];
            zsum+=zPoints[i];
        }
        xsum/=xPoints.length;
        ysum/=yPoints.length;
        zsum/=zPoints.length;
        
        double xDist,yDist,zDist;

        xDist = xsum - cam.getX();
        yDist = ysum - cam.getY();
        zDist = zsum - cam.getZ();
        
        output = Math.sqrt(Math.pow(xDist,2) + Math.pow(yDist,2) + Math.pow(zDist,2));
        return output;
    }
    public void draw(Graphics g, Camera cam){
        ArrayList<Vertex> vertices = new ArrayList<>();
        int sc = cam.getScale();
        
        // for(int xi=0;xi<x.length;xi++){
        //     if(x[xi] == 2147483647){
        //         System.out.println("END");
        //         System.exit(0);
        //     }
        //     System.out.println(x[xi]);
        // }
            
        g.setColor(Color.green);

        Vertex v = new Vertex();
        Vertex start = new Vertex();
        int xScreen,yScreen;

        for(int i=0;i<x.length;i++){
            xScreen = (int)x[i];
            yScreen = (int)y[i];

            if(i == 0){
                start = new Vertex(x[i],y[i],0);
            }else{
                // g.drawLine((int)v.getX(),(int)v.getY(),xScreen,yScreen);
                line(g,sc,xScreen,yScreen,v,cam);

                if(i == x.length-1){
                    // g.drawLine((int)start.getX(),(int)start.getY(),xScreen,yScreen);
                    line(g,sc,xScreen,yScreen,start,cam);
                }
            }
            
            v = new Vertex(xScreen,yScreen,0);
            vertices.add(v);
            
            if(draw){
                g.fillRect(size(xScreen,sc),size(yScreen,sc),sc,sc);
            }
        }
    }
    public void line(Graphics g, int sc, int xScreen, int yScreen, Vertex v, Camera cam){
        if(!draw){
            return;
        }
        double slope = 0;
        if(v.getX() != xScreen){
            slope = (v.getY() - yScreen)/(v.getX() - xScreen);
            // slope = (yScreen - v.getY())/(xScreen - (v.getX()));
        }

        // int xDist = Math.abs(xScreen - (int)v.getX());

        double b = xScreen * slope;
        b = yScreen - b;

        // double newY = slope * xScreen + b;

        // int line_y = (int)(size((int)v.getY(),sc)*slope + b);
        // System.out.println((size((int)v.getY(),sc)) + ", " + v.getY() + ", " + slope + ", " + b);

        // int smallX, smallY;
        // int largeX, largeY;

        // if(xScreen < (int)v.getX()){
        //     smallX = xScreen;
        //     largeX = (int)v.getX();
        // }else{
        //     largeX = xScreen;
        //     smallX = (int)v.getX();
        // }
        // if(yScreen < (int)v.getY()){
        //     smallY = yScreen;
        //     largeY = (int)v.getY();
        // }else{
        //     largeY = yScreen;
        //     smallY = (int)v.getY();
        // }
        
        Vertex small;
        Vertex large;
        
        if(xScreen < (int)v.getX()){
            small = new Vertex(xScreen,0,0);
            large = new Vertex((int)v.getX(),0,0);
        }else{
            large = new Vertex(xScreen,0,0);
            small = new Vertex((int)v.getX(),0,0);
        }

        if(yScreen < (int)v.getY()){
            small.setY(yScreen);
            large.setY((int)v.getY());
        }else{
            small.setY((int)v.getY());
            large.setY(yScreen);
        }
        
        // System.out.println("===");
        // System.out.println(small.toString());
        // System.out.println(large.toString());

        if(small.getX() < 0){
            small.setX(0);
        }else if(small.getX() > cam.getWidth()){
            small.setX(cam.getWidth());
        }
        if(small.getY() < 0){
            small.setY(0);
        }else if(small.getY() > cam.getHeight()){
            small.setY(cam.getHeight());
        }

        if(large.getX() < 0){
            large.setX(0);
        }else if(large.getX() > cam.getWidth()){
            large.setX(cam.getWidth());
        }
        if(large.getY() < 0){
            large.setY(0);
        }else if(large.getY() > cam.getHeight()){
            large.setY(cam.getHeight());
        }
        
        /*
        if(smallX < 0){
            smallX = 0;
        }else if(smallX > cam.getWidth()){
           smallX = cam.getWidth();
        }else if(smallY < 0){
            smallY = 0;
        }else if(smallY > cam.getHeight()){
            smallY = cam.getHeight();
        }

        if(largeX < 0){
            largeX = 0;
        }else if(largeX > cam.getWidth()){
            largeX = cam.getWidth();
        }else if(largeY < 0){
            largeY = 0;
        }else if(largeY > cam.getHeight()){
            largeY = cam.getHeight();
        }
        */
        // System.out.println(large.getX());
        
        // for(int i=0;i<cam.getWidth();i+=sc){
        
        for(int i=(int)small.getX();i<=(int)(large.getX());i+=sc){
        //for(int i=0;i<end;i++)
        
        // for(int i=smallX;i<largeX;i++){
            // if(i > small.getX() && i < large.getX()){
                // if(Math.random() < 0.001)//{
                //     if(i == 0){
                //         System.out.println(small.getX());
                //         // System.pause(0);
                //     }else{
                //         // System.out.println("Nothing");
                //     }
                // }
                // for(int j=(int)small.getY();j<(int)(large.getY());j+=sc)
                // System.out.println(small.getY() + ", " + large.getY());
            // if(slope > 0){
                int now = size((int)(i * slope + b),sc);
                int next = size((int)((i+sc) * slope + b),sc);
            //     System.out.println(now + ", " + next);
                
                // if(Math.random() < 0.001)
                //     System.out.println(slope + ", " + i + ", " + next + ", " + now);
                
                // for(int j=0;j<next - now;j+=sc){
                // if(j != now)
                //     System.out.println(j + ", " + now + ", " + next);
                if(now > large.getY()){
                    now = size((int)large.getY(),sc);
                }else if(now < small.getY()){
                    now = size((int)small.getY(),sc);
                }

                if(next > large.getY()){
                    next = size((int)large.getY(),sc);
                }else if(next < small.getY()){
                    next = size((int)small.getY(),sc);
                }

                if(small.getX() == large.getX()){
                    now = size((int)small.getY(),sc);
                    next = size((int)large.getY(),sc);
                }

                int diff = next - now;
                int flip = 1;
                if(diff < 0){
                    flip = -1;
                    diff = Math.abs(diff);
                }
                
                // if(diff >= 0){
                    for(int j=0;j<=diff;j+=sc){
                        g.fillRect(size(i,sc),now+(j*flip),sc,sc);
                    }
                // }else{
                //     g.fillRect(size(i,sc),now,sc,sc);
                // }
                // }
                // g.fillRect(500,250,sc,sc);
            // }
                // }
            // }
        }
    }
    public int size(int n, int sc){
        int output = n/sc*sc;
        return output;
    }
}