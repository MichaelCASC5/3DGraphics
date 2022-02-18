import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class RObject{
    private ArrayList<Face> faces;
    
    public RObject(){
        
        faces = new ArrayList<>();
        
//        Face f;
//        f = new Face(HEIGHT,WIDTH);//Bottom
//        f.setX(-200, -200, 200, 200);
//        f.setY(200, -200, -200, 200);
//        f.setZ(-100, -100, -100, -100);
//        faces.add(f);
        
//        Face f;
//        f = new Face(HEIGHT,WIDTH);//Bottom
//        f.setX(0, 0, 100, 100);
//        f.setY(200, 0, 0, 200);
//        f.setZ(0, 0, 0, 0);
//        faces.add(f);
//        
//        f = new Face(HEIGHT,WIDTH);//Back
//        f.setX(0, 0, 100, 100);
//        f.setY(200, 200, 200, 200);
//        f.setZ(100, 0, 0, 100);
//        faces.add(f);
//        
//        f = new Face(HEIGHT,WIDTH);
//        f.setX(0, 0, 100, 100);
//        f.setY(200, 0, 0, 200);
//        f.setZ(100, 0, 0, 100);
//        faces.add(f);
    }
    public void addFace(Face f){
        faces.add(f);
    }
    public int getFaceCount(){
        return faces.size();
    }
    public void actions(Camera cam){
        for(int i=0;i<faces.size();i++){
            faces.get(i).reset();
            faces.get(i).rotateZ(cam);
            faces.get(i).rotateX(cam);
            faces.get(i).toscreen(cam);
        }
        /*
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
        */
    }
    public void draw(Graphics g, Camera cam){
        // int colnum = 0;
        for(int i=0;i<faces.size();i++){
//            if(colnum > 255){
//                colnum = 255;
//            }
//            g.setColor(new Color(colnum,0,0));
            
            faces.get(i).draw(g,cam);
//            colnum+=25;
        }
    }
}