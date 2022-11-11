import java.util.ArrayList;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Manager{
    /*
        * Reading Files
        * Helper method to the read() method.
        * Append vertices to new face generated
    */
    public static double[] append(double[] a, double n){
        double[] output = new double[a.length+1];
        
        for(int i=0;i<a.length;i++){
            output[i] = a[i];
        }
        
        output[output.length-1] = n;
        
        return output;
    }
    /*
        * Reading Files
    */
    public static ArrayList<RObject> read(String d){
        ArrayList<RObject> scene;
        scene = new ArrayList<>();

        System.out.println(System.getProperty("user.dir"));
        File directory = new File("C:\\Users\\micha\\Documents\\NetBeansProjects\\Radium Engine\\" + d);
        
        File[] files = directory.listFiles();
        
        ArrayList<Vertex> vertices;
        
        RObject r;
        Face f;
        
        double[] x;
        double[] y;
        double[] z;
        
        boolean addScene = false;

        Scanner reader;
        String line;
        Vertex v;
        for(int i=0;i<files.length;i++){
            System.out.println(files[i].getName());
            r = new RObject();
            
            x = new double[0];
            y = new double[0];
            z = new double[0];
            
            vertices = new ArrayList<>();
            
            try{
                reader = new Scanner(files[i]);
                
                int num;
                while(reader.hasNextLine()){
                    v = new Vertex();
                    
                    line = reader.nextLine();
                    
                    if(line.substring(0,2).equals("v ")){
                        line = line.substring(2);
                        num = line.indexOf(" ");
                        v.setX(Double.parseDouble(line.substring(0,num)));
                        line = line.substring(num+1);
                        
                        num = line.indexOf(" ");
                        v.setY(Double.parseDouble(line.substring(0,num)));
                        line = line.substring(num+1);
                        
                        num = line.length();
                        v.setZ(Double.parseDouble(line.substring(0,num)));
                        
                        vertices.add(v);
                    }
                    
                    if(line.substring(0,2).equals("f ")){
                        line = line.substring(2);
                        num = line.indexOf(" ");
                        
                        String face;
                        boolean collect;
                        collect = true;
                        while(collect){
                            if(num != -1){
                                face = line.substring(0,num);
                                line = line.substring(num+1);
                                num = line.indexOf(" ");
                            }else{
                                face = line.substring(0,line.length());
                                collect = false;
                            }
                            
                            int index;
                            index = face.indexOf("/");
                            face = face.substring(0,index);
                            index = Integer.parseInt(face);
                            index--;
                            
                            x = append(x,vertices.get(index).getX());
                            y = append(y,vertices.get(index).getZ()*-1);
                            z = append(z,vertices.get(index).getY());
                            
                            addScene = true;
                        }
                    }
                    
                    if(addScene){
                        f = new Face(x,y,z);
                        r.addFace(f);
                        
                        x = new double[0];
                        y = new double[0];
                        z = new double[0];

                        scene.add(r);
                        
                        addScene = false;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        return scene;
    }
}