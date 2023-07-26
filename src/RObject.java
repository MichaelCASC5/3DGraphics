import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import java.awt.image.BufferedImage;

public class RObject{
    /*
        * Declaration and instantiation.
    */
    private ArrayList<Face> faces;
    
    public RObject(){
        faces = new ArrayList<>();
    }
    /*
        * Adds a face to the RObject.
    */
    public void addFace(Face f){
        faces.add(f);
    }
    /*
        * Returns the amount of faces in the RObject.
    */
    public int getFaceCount(){
        return faces.size();
    }
    /*
        * Performs all 3D logic. Such as translation and rotation in the 3D environment.
    */
    public void actions(Camera cam){
        for(int i=0;i<faces.size();i++){
            faces.get(i).reset();
            faces.get(i).rotateZ(cam);
            faces.get(i).rotateX(cam);
            faces.get(i).normals(cam);
            faces.get(i).toscreen(cam);
        }
        /*
            * This is the Rearranger.
            
            * At the moment it has a long way to go for optimization.
        */
        if(false){//Disabled
            Face temp = new Face();
            for(int i=0;i<faces.size();i++){
                for(int j=i+1;j<faces.size();j++){
                    if(faces.get(j).dist(cam) > faces.get(i).dist(cam)){
                        temp.setAll(faces.get(i));
                        faces.get(i).setAll(faces.get(j));
                        faces.get(j).setAll(temp);
                    }
                }
            }
        }
    }
    /*
        * Draws RObject to screen, by drawing each face.
    */
    public void draw(Graphics g, BufferedImage canvas, Camera cam){
        for(int i=0;i<faces.size();i++){
            faces.get(i).draw(g,canvas,cam);
        }
    }
}