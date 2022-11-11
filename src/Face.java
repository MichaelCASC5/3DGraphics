import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Face{
    /*
        * Declaration and instantiation.
    */

    /*
        * Global and local coordinate arrays.
    */
    private double[] xPoints, yPoints, zPoints; //global
    private double[] x,y,z;                     //local
    
    private boolean draw;
    
    /*
        * Default Constructor
    */
    public Face(){
        xPoints = new double[0];
        yPoints = new double[0];
        zPoints = new double[0];
        
        x = new double[0];
        y = new double[0];
        z = new double[0];
        
        draw = false;
    }
    /*
        * Parameterized Constructor

        * Accepts three arrays. Each for the x, y, and z coordinates of all vertices that form the array.
    */    
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
        
        draw = false;
    }
    /*
        * Accessor and mutator methods
    */
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
    /*
        * Set all geometry of a face to that of another face.

        * Accepts a Face object.
    */
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
    /*
        * Print statements
    */
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
    /*
        * Simple method that sets the contents of one array to that of another array.

        * Different from setting one array equal to another, which will pass the reference
    */
    public void setArray(double[] a, double[] b){
        if(a.length == b.length){
            for(int i=0;i<a.length;i++){
                a[i] = b[i];
            }
        }
    }
    /*
        * Resets the local array to the global array.
    */
    public void reset(){
        for(int i=0;i<xPoints.length;i++){
            x[i] = xPoints[i];
            y[i] = yPoints[i];
            z[i] = zPoints[i];
        }
        draw = true;
    }
    /*
        * Rotates the 3D world about the Z (vertical) axis.
    */
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
    /*
        * Rotates the 3D world about the X (horizontal and across the camera's view) axis.
    */
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
    /*
        * Converts the 3D environment to a 2D projection that can be drawn onto the GUI.

        * To be resourceful, it converts the local coordinates of the face to the 2D screen coordinates.
        The x and y local coordinates convert to 2D screen coordinates. The local z coordinates afterwards
        can be ignored, as they are not neccessary for drawing onto the 2D display.
    */
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
            
            x[i] = ((cam.getWidth()/2)+(parr*(xDist)));
            y[i] = (((cam.getHeight()/2)-(parr*zDist)));
            
            /*
                Counts if all the vertices of a face stretch beyond the camera's view.
                If all vertices do, then don't draw the face.
            */
            if(x[i] < 0 || x[i] > cam.getWidth() || y[i] < 0 || y[i] > cam.getHeight()){
                count++;
            }
        }
        if(count >= x.length){
           draw = false;
        }
    }
    /*
        * Calculates the distance of a face to the camera.
        Averages the center of the face from all its vertices
    */
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
    /*
        * Draws the face to the screen
    */
    public void draw(Graphics g, Camera cam){
        ArrayList<Vertex> vertices = new ArrayList<>();
        int sc = cam.getScale();
            
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
                g.drawLine((int)v.getX(),(int)v.getY(),xScreen,yScreen);
                // line(g,sc,xScreen,yScreen,v,cam);

                if(i == x.length-1){
                    g.drawLine((int)start.getX(),(int)start.getY(),xScreen,yScreen);
                    // line(g,sc,xScreen,yScreen,start,cam);
                }
            }
            
            v = new Vertex(xScreen,yScreen,0);
            vertices.add(v);
            
            if(draw){
                // g.fillRect(size(xScreen,sc),size(yScreen,sc),sc,sc);
            }
        }
    }
    /*
        * A helper method to the draw() method.
        This draws a single line to the screen.
    */
    public void line(Graphics g, int sc, int xScreen, int yScreen, Vertex v, Camera cam){
        if(!draw){
            return;
        }
        double slope = 0;
        if(v.getX() != xScreen){
            slope = (v.getY() - yScreen)/(v.getX() - xScreen);
        }

        double b = xScreen * slope;
        b = yScreen - b;
        
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
        
        for(int i=(int)small.getX();i<=(int)(large.getX());i+=sc){
        
                int now = size((int)(i * slope + b),sc);
                int next = size((int)((i+sc) * slope + b),sc);
            
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
                
                for(int j=0;j<=diff;j+=sc){
                    g.fillRect(size(i,sc),now+(j*flip),sc,sc);
                }
        }
    }
    /*
        * Helper method to the draw() method.

        * Adjusts the scale of each pixel on the screen.
    */
    public int size(int n, int sc){
        int output = n/sc*sc;
        return output;
    }
}