import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import java.awt.image.BufferedImage;

public class Face{
    /*
        * Declaration and instantiation.
    */

    /*
        * Global and local coordinate arrays.
    */
    private Vertex[] global; //global coordinates
    private Vertex[] local; //local coordinates

    private Vertex normG, normL;

    private boolean draw;
    
    /*
        * Default Constructor
    */
    public Face(){
        global = new Vertex[0];
        local = new Vertex[0];

        normG = new Vertex();
        normL = new Vertex();
        
        draw = false;
    }
    /*
        * Parameterized Constructor
    */    
    public Face(Vertex[] v, Vertex n){
        global = new Vertex[v.length];
        local = new Vertex[v.length];

        normG = new Vertex();
        normL = new Vertex();

        setArray(global,v);
        setArray(local,v);

        normG.setAll(n);
        normL.setAll(n);
        
        draw = false;
    }
    /*
        * Accessor and mutator methods
    */
    /*
        * Accessor Methods:
    */
    public Vertex[] getGlobal(){
        return global;
    }
    public Vertex[] getLocal(){
        return local;
    }
    public Vertex getNormalG(){
        return normG;
    }
    public Vertex getNormalL(){
        return normL;
    }
    public boolean getDraw(){
        return draw;
    }
    /*
        * Mutator Methods:
    */
    public void setGlobal(Vertex[] v){
        global = new Vertex[v.length];
        setArray(global,v);
    }
    public void setLocal(Vertex[] v){
        local = new Vertex[v.length];
        setArray(local,v);
    }
    public void setNormalG(Vertex v){
        normG = v;
    }
    public void setNormalL(Vertex v){
        normL = v;
    }
    public void setDraw(boolean b){
        draw = b;
    }
    /*
        * Set all geometry of a face to that of another face.
        * Accepts a Face object.
    */
    public void setAll(Face f){
        global = new Vertex[f.getGlobal().length];
        local = new Vertex[f.getGlobal().length];

        setArray(global,f.getGlobal());
        setArray(local,f.getLocal());

        normG.setAll(f.getNormalG());
        normL.setAll(f.getNormalL());
    }
    /*
        * Print statements
    */
    public void printGlobal(){
        for(int i=0;i<global.length;i++){
            System.out.print(global[i] + " ");
        }
        System.out.println();
    }
    public void printLocal(){
        for(int i=0;i<local.length;i++){
            System.out.print(local[i] + " ");
        }
        System.out.println();
    }
    /*
        * Simple method that sets the contents of one array to that of another array.
        * Different from setting one array equal to another, which will pass the reference
    */
    public void setArray(Vertex[] a, Vertex[] b){
        if(a.length == b.length){
            for(int i=0;i<a.length;i++){
                a[i] = new Vertex();
                a[i].setAll(b[i]);
            }
        }
    }


    // * * * BEGINNING 3D WORLD SPACE MATHEMATICAL COMPUTATION OF ROTATIONS, TRANSLATIONS, AND PROJECTION. * * *


    /*
        * Resets the local data to the global data.
    */
    public void reset(){
        for(int i=0;i<global.length;i++){
            local[i].setAll(global[i]);
        }
        normL.setAll(normG);
        draw = true;

        //Mounting normal to hitchhike
        local = Manager.append(local,normG);
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
        
        for(int i=0;i<local.length;i++){
            if(i==local.length-1){camX=0;camY=0;}//No translation for normal hitchhiker
            
            xDist = local[i].getX() - camX;
            yDist = local[i].getY() - camY;
            
            a = xDist*Math.cos(camYaw/(180.0/Math.PI));
            b = yDist*Math.sin(camYaw/(180.0/Math.PI));
            
            local[i].setX(a - b + camX);
            
            a = yDist*Math.cos(camYaw/(180.0/Math.PI));
            b = xDist*Math.sin(camYaw/(180.0/Math.PI));
            
            local[i].setY(a + b + camY);
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
        
        for(int i=0;i<local.length;i++){
            if(i==local.length-1){camY=0;camZ=0;}//No translation for normal hitchhiker
            
            yDist = local[i].getY() - camY;
            zDist = local[i].getZ() - camZ; //Edited from global to local
            
            a = yDist*Math.cos(camPitch/(180.0/Math.PI));
            b = zDist*Math.sin(camPitch/(180.0/Math.PI));
            
            local[i].setY(a - b + camY);
            
            a = zDist*Math.cos(camPitch/(180.0/Math.PI));
            b = yDist*Math.sin(camPitch/(180.0/Math.PI));
            
            local[i].setZ(a + b + camZ);
            
            if(local[i].getY() < camY){
                count++;
            }
        }
        //If all vertices of the face are behind the y position of the camera, do not draw the face.
        if(count >= local.length-1){//-1 to not account for normal
            draw = false;
        }
    }
    /*
        * Computes whether the face is facing the camera or not.
    */
    public void normals(Camera cam){
        double camX = cam.getX();
        double camY = cam.getY();
        double camZ = cam.getZ();

        //Dismounting normal from hitchhike
        normL = local[local.length-1];
        local = Arrays.copyOf(local, local.length - 1);

        //Adding the normal to the first vertex
        Vertex sum = new Vertex(local[0]);
        sum.add(normL);

        //Computing distances
        double behind = Math.sqrt(Math.pow(local[0].getX()-camX,2) + Math.pow(local[0].getY()-camY,2) + Math.pow(local[0].getZ()-camZ,2));
        double ahead = Math.sqrt(Math.pow(sum.getX()-camX,2) + Math.pow(sum.getY()-camY,2) + Math.pow(sum.getZ()-camZ,2));

        //If the point ahead (sum) of the chosen vertex is further than that vertex, the face faces away. Do not draw.
        if(ahead > behind){
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
        if(!draw){
            return;
        }
        double camX = cam.getX();
        double camY = cam.getY();
        double camZ = cam.getZ();
        double camWidth = cam.getWidth();
        double camHeight = cam.getHeight();

        /*
            * Computing normal's relationship with the camera.
            * If normal faces away from the camera, don't draw the face.
            * Note: The vector heading of the virtual camera is always [0,1,0]
        */

        int count = 0;
        for(int i=0;i<local.length;i++){
            //Calculating distances
            Vertex dist = new Vertex(
                local[i].getX() - camX,
                local[i].getY() - camY,
                local[i].getZ() - camZ
            );
            
            //Computing Parralax
            double parr = Math.abs(1000.0/dist.getY());
            
            local[i].setX(camWidth/2 + parr*dist.getX());
            local[i].setY(camHeight/2 - parr*dist.getZ());
            
            /*
                * Counts if all the vertices of a face stretch beyond the camera's view.
                * If all vertices do, then don't draw the face.
            */
            if(local[i].getX() < 0 || local[i].getX() > camWidth || local[i].getY() < 0 || local[i].getY() > camHeight){
                count++;
            }
        }

        if(count >= local.length){
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
        for(int i=0;i<global.length;i++){
            xsum+=global[i].getX();
            ysum+=global[i].getY();
            zsum+=global[i].getZ();
        }
        xsum/=global.length;
        ysum/=global.length;
        zsum/=global.length;
        
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
    public void draw(Graphics g, BufferedImage canvas, Camera cam){
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
        for(int i=0;i<local.length;i++){
            curX = (int)local[i].getX();
            curY = (int)local[i].getY();

            //On the first iteration, record the position of the first vertex.
            if(i == 0){
                firstX = curX;
                firstY = curY;
            }
            //On ensuing iterations, draw a line from the previous vertex to the current vertex.
            else{
                g.drawLine(prevX,prevY,curX,curY);
                // line(g,canvas,cam,prevX,prevY,curX,curY);

                //On the last iteration, draw a line from the current vertex back to the original first vertex, closing the polygon.
                if(i == local.length-1){
                    g.drawLine(curX,curY,firstX,firstY);
                    // line(g,canvas,cam,curX,curY,firstX,firstY);
                }
            }
            
            //Always save the current vertex for the next iteration, in which a line will be draw from this vertex to that one.
            prevX = curX;
            prevY = curY;

            //Doesn't let any line that goes off the screen to render.
            if((prevX < 0 || curX < 0 || prevX > camWidth || curX > camWidth)
                || (prevY < 0 || curY < 0 || prevY > camHeight || curY > camHeight)
                ){
                // return;
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
    public void line(Graphics g, BufferedImage canvas, Camera cam, int prevX, int prevY, int curX, int curY){
        double camWidth = cam.getWidth();
        double camHeight = cam.getHeight();

        // g.drawLine(prevX,prevY,curX,curY);

        //Calculating Slope
        double slope = 0;
        if(prevX != curX){
            slope = (double)(curY - prevY)/(double)(curX - prevX);

            //Finding Y-intercept y = mx + b<-
            double b = prevX * slope;
            b = prevY - b;

            int y;
            Color c = Color.red;
            int color = c.getRGB();

            //Makes sure the for loop always runs from left to right
            int larger;
            int smaller;

            if(curX > prevX){
                larger = curX;
                smaller = prevX;
            }else{
                larger = prevX;
                smaller = curX;
            }

            //If statements separate loops that draw from left to right and up to down
            if(Math.abs(slope) < 1){

            //Draws from left to right
            for(int i=smaller;i<larger;i++){
                y = (int)(slope*i + b);
                // g.fillRect(i,y,1,1);
                
                if(i > 0 && i < camWidth && y > 0 && y < camHeight){
                    canvas.setRGB(i, y, color);
                    // g.fillRect(i,y,2,2);
                }
            }
            
            }else{
                // Draws from top to bottom
                if(curY > prevY){
                    larger = curY;
                    smaller = prevY;
                }else{
                    larger = prevY;
                    smaller = curY;
                }

                for(int i=smaller;i<larger;i++){
                    y = (int)((-1 / slope)*i - (b + (1 / slope)));
                    // y = (int)(slope*i + b);
                    // g.fillRect(y,i,1,1);
                    
                    if(i > 0 && i < camHeight && y > 0 && y < camWidth){
                        canvas.setRGB(y, i, color);
                        // g.fillRect(i,y,2,2);
                    }
                }
                // g.drawLine(prevX,prevY,curX,curY);
            }
            // g.setRGB(100, 100, 255);
        }
    }
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