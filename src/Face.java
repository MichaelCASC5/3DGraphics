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
        double camX = cam.getX();
        double camY = cam.getY();
        double camYaw = cam.getYaw();
        
        double xDist,yDist;
        double a,b;
        
        for(int i=0;i<x.length;i++){
            xDist = x[i] - camX;
            yDist = y[i] - camY;
            
            a = xDist*Math.cos(camYaw/(180.0/Math.PI));
            b = yDist*Math.sin(camYaw/(180.0/Math.PI));
            
            x[i] = (a - b + camX);
            
            a = yDist*Math.cos(camYaw/(180.0/Math.PI));
            b = xDist*Math.sin(camYaw/(180.0/Math.PI));
            
            y[i] = (a + b + camY);
        }
    }
    /*
        * Rotates the 3D world about the X (horizontal and across the camera's view) axis.
    */
    public void rotateX(Camera cam){
        double camY = cam.getY();
        double camZ = cam.getZ();
        double camPitch = cam.getPitch();

        double yDist,zDist;
        double a,b;
        
        int count;
        count = 0;
        
        for(int i=0;i<x.length;i++){
            yDist = y[i] - camY;
            zDist = zPoints[i] - camZ;
            
            a = yDist*Math.cos(camPitch/(180.0/Math.PI));
            b = zDist*Math.sin(camPitch/(180.0/Math.PI));
            
            y[i] = (a - b + camY);
            
            a = zDist*Math.cos(camPitch/(180.0/Math.PI));
            b = yDist*Math.sin(camPitch/(180.0/Math.PI));
            
            z[i] = (a + b + camZ);
            
            if(y[i] < camY){
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
        double camX = cam.getX();
        double camY = cam.getY();
        double camZ = cam.getZ();
        double camWidth = cam.getWidth();
        double camHeight = cam.getHeight();

        int count = 0;
        for(int i=0;i<x.length;i++){
            double xDist,yDist,zDist;
            xDist = x[i] - camX;
            yDist = y[i] - camY;
            zDist = z[i] - camZ;
            
            double parr;
            parr = (1000.0/(yDist));
            parr=Math.abs(parr);
            
            x[i] = ((camWidth/2)+(parr*(xDist)));
            y[i] = (((camHeight/2)-(parr*zDist)));
            
            /*
                Counts if all the vertices of a face stretch beyond the camera's view.
                If all vertices do, then don't draw the face.
            */
            if(x[i] < 0 || x[i] > camWidth || y[i] < 0 || y[i] > camHeight){
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
        double camX = cam.getX();
        double camY = cam.getY();
        double camZ = cam.getZ();

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

        xDist = xsum - camX;
        yDist = ysum - camY;
        zDist = zsum - camZ;
        
        output = Math.sqrt(Math.pow(xDist,2) + Math.pow(yDist,2) + Math.pow(zDist,2));
        return output;
    }
    /*
        * Draws the face to the screen. Each RObject consists of an arraylist of faces.

        * Each face is represented by two arrays of x-coordinates and y coordinates.
        * Each face is simply a 2D polygon.
        * The vertices along the circumference of the polygon are stored in a circular 
        direction going around. When it comes time to draw the polygon to the screen, the
        algorithm draws a line from one vertex coordinate to the next.
        *
        * Drawing a rect:
        *   2           >           3
        *   ^                       v
        *   1 < (extra iteration) < 4
        *
    */
    public void draw(Graphics g, Camera cam){
        if(!draw){
            return;
        }
        double camWidth = cam.getWidth();
        double camHeight = cam.getHeight();

        int sc = cam.getScale();  
        g.setColor(Color.green);

        int firstX = 0;
        int firstY = 0;
        
        int prevX = 0;
        int prevY = 0;
        int curX, curY;
        for(int i=0;i<x.length;i++){
            curX = (int)x[i];
            curY = (int)y[i];

            //On the first iteration, record the position of the first vertex.
            if(i == 0){
                firstX = curX;
                firstY = curY;
            }
            //On ensuing iterations, draw a line from the previous vertex to the current vertex.
            else{
                g.drawLine(prevX,prevY,curX,curY);

                //On the last iteration, draw a line from the current vertex back to the original first vertex, closing the polygon.
                if(i == x.length-1){
                    g.drawLine(curX,curY,firstX,firstY);
                }
            }
            
            //Always save the current vertex for the next iteration, in which a line will be draw from this vertex to that one.
            prevX = curX;
            prevY = curY;

            //Doesn't let any line that goes off the screen to render.
            if((prevX < 0 || curX < 0 || prevX > camWidth || curX > camWidth)
                || (prevY < 0 || curY < 0 || prevY > camHeight || curY > camHeight)
                ){
                return;
            }
            
            // if(draw){
            //     g.fillRect(size(curX,sc),size(curY,sc),sc,sc);
            // }
        }
    }
    /*
        * A helper method to the draw() method.
        This draws a single line to the screen.
    */
    /*
    public void line(Graphics g, int sc, int x1, int y1, Vertex v, Camera cam){
        sc = 1;
        
        int x2,y2;
        x2 = (int)v.getX();
        y2 = (int)v.getY();

        if(!draw
            || (x1 < 0 || x2 < 0 || x1 > cam.getWidth() || x2 > cam.getWidth())
            || (y1 < 0 || y2 < 0 || y1 > cam.getHeight() || y2 > cam.getHeight())
            ){
            return;
        }

        // if(sc == 1){
        //     g.drawLine(x1,y1,x2,y2);
        //     return;
        // }

        int temp = 0;
        if(x1 > x2){
            temp = y1;
            y1 = y2;
            y2 = temp;

            temp = x1;
            x1 = x2;
            x2 = temp;
        }

        //Calculating Slope
        double slope = 0;
        if(x1 != x2){
            slope = (double)(y2 - y1)/(double)(x2 - x1);

            //Finding Y-intercept y = mx + b<-
            double b = x1 * slope;
            b = y1 - b;
            
            int ry = 0;
            for(int i=x1;i<x2;i+=sc){
                ry = (int)(slope*(double)i + b);
                
                int nexty = 0;
                if(i < x2 - 1){
                    nexty = (int)(slope*(double)(i+sc) + b) - ry;
                }
                if(nexty > 0){
                    // g.fillRect(i - i%sc,ry - ry%sc,sc,sc);
                    for(int j=ry;j<=ry+nexty*1;j+=sc){
                        g.fillRect(i - i%sc,j - j%sc,sc,sc);
                    }
                }else{
                    for(int j=ry;j>=ry+nexty*1;j-=sc){
                        g.fillRect(i - i%sc,j - j%sc,sc,sc);
                    }
                }
            }
        }else{
            if(y1 > y2){
                temp = y1;
                y1 = y2;
                y2 = temp;

                temp = x1;
                x1 = x2;
                x2 = temp;
            }
            // for(int i=y1;i<y2;i+=sc){
            //     g.fillRect(x1 - x1%sc,i - i%sc,sc,sc);
            // }
        }
    }*/
    /*
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
    }*/
    /*
        * Helper method to the draw() method.

        * Adjusts the scale of each pixel on the screen.
    */
    public int size(int n, int sc){
        int output = n/sc*sc;
        return output;
    }
}