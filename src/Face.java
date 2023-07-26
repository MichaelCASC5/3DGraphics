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
    public Face(Face f){
        setAll(f);
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

        normG = new Vertex(f.getNormalG());
        normL = new Vertex(f.getNormalL());
        
        draw = f.getDraw();
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
        if(a.length == b.length){//Remove if statement if norm objs are removed in favor of last coordinate in vtx array
            for(int i=0;i<b.length;i++){
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
    public void project(Camera cam){
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
    public void paint(Graphics g, BufferedImage canvas, Camera cam){
        if(!draw){
            return;
        }
        double camWidth = cam.getWidth();
        double camHeight = cam.getHeight();

        int sc = cam.getScale();
        Color normCol = new Color((int)Math.abs(normG.getX()*255),(int)Math.abs(normG.getY()*255),(int)Math.abs(normG.getZ()*255));
        g.setColor(normCol);

        int firstX = 0;
        int firstY = 0;
        
        int prevX = 0;
        int prevY = 0;
        int curX, curY;

        //Two arrays for drawing polygons
        int[] xPoints = new int[local.length];
        int[] yPoints = new int[local.length];
        for(int i=0;i<local.length;i++){
            xPoints[i] = (int)local[i].getX();
            yPoints[i] = (int)local[i].getY();

            curX = (int)local[i].getX();
            curY = (int)local[i].getY();

            //On the first iteration, record the position of the first vertex.
            if(i == 0){
                firstX = curX;
                firstY = curY;
            }
            //On ensuing iterations, draw a line from the previous vertex to the current vertex.
            else{
                // g.drawLine(prevX,prevY,curX,curY);

                //On the last iteration, draw a line from the current vertex back to the original first vertex, closing the polygon.
                if(i == local.length-1){
                    // g.drawLine(curX,curY,firstX,firstY);
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
        }

        g.fillPolygon(xPoints, yPoints, local.length);

    }
    /*
        * Helper method to the draw() method.

        * Adjusts the scale of each pixel on the screen.
    */
    public int size(int n, int sc){
        int output = n/sc*sc;
        return output;
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
}