import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import java.awt.image.BufferedImage;

public class RObject{
    /*
        * Declaration and instantiation.
    */
    private Face[] faces;
    
    public RObject(){
        faces = new Face[0];
    }
    /*
        * Adds a face to the RObject.
    */
    public void addFace(Face f){
        faces = Arrays.copyOf(faces,faces.length+1);
        faces[faces.length-1] = new Face(f);
    }
    /*
        * Returns the amount of faces in the RObject.
    */
    public int getFaceCount(){
        return faces.length;
    }
    /*
        * Performs all 3D logic. Such as translation and rotation in the 3D environment.
    */
    public void transform(Face f, Camera cam){
        f.reset();
        f.rotateZ(cam);
        f.rotateX(cam);
        f.normals(cam);
        f.project(cam);

        /*
            * This is the Rearranger.
            
            * At the moment it has a long way to go for optimization.
        */
        if(false){//Disabled
            Face temp = new Face();
            for(int i=0;i<faces.length;i++){
                for(int j=i+1;j<faces.length;j++){
                    if(faces[j].dist(cam) > faces[i].dist(cam)){
                        temp.setAll(faces[i]);
                        faces[i].setAll(faces[j]);
                        faces[j].setAll(temp);
                    }
                }
            }
        }
    }
    /*
        * Paints RObject onto canvas, by painting each face.
    */
    public void paint(Graphics g, BufferedImage canvas, Camera cam){
        for(int i=0;i<faces.length;i++){
            transform(faces[i],cam);
            faces[i].paint(g,canvas,cam);
        }
    }
}