import java.util.ArrayList;
import java.util.Arrays;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Manager{
    /*
        * Reading Files
        * Helper method to the read() method.
        * Append vertices to new face generated
    */
    public static Vertex[] append(Vertex[] a, Vertex n){
        Vertex[] output = Arrays.copyOf(a,a.length+1);
        output[output.length-1] = new Vertex(n.getX(),n.getY(),n.getZ());
        return output;
    }
    /*
        * Reading Files
    */
    public static ArrayList<RObject> read(String d){
        //Setting up file reader
        System.out.println(System.getProperty("user.dir"));
        File directory = new File("C:\\Users\\micha\\Documents\\NetBeansProjects\\Radium Engine\\" + d);
        File[] files = directory.listFiles();

        //Setting up scene
        ArrayList<RObject> scene;
        scene = new ArrayList<>();
        
        //Setting up the RObject with the face and vertex class that will be used to push data into it
        RObject r;
        r = new RObject();
        Face f;
        ArrayList<Vertex> vertices;
        ArrayList<Vertex> normals;
        Vertex[] collector;
        
        //Setting up data that will be scrapped from file
        Vertex v;
        Vertex n;
        int normalID;
        boolean addFace;

        //Setting up reading vars
        Scanner reader;
        String line;

        for(int i=0;i<files.length;i++){
            System.out.println(files[i].getName());
            r = new RObject();
            
            vertices = new ArrayList<>();
            normals = new ArrayList<>();
            collector = new Vertex[0];
            
            try{
                reader = new Scanner(files[i]);
                addFace = false;
                
                int num;
                while(reader.hasNextLine()){
                    v = new Vertex();
                    n = new Vertex();
                    normalID = 0;
                    
                    line = reader.nextLine();
                    if(line != ""){
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

                        if(line.substring(0,3).equals("vn ")){
                            line = line.substring(3);
                            num = line.indexOf(" ");
                            n.setX(Double.parseDouble(line.substring(0,num)));
                            line = line.substring(num+1);
                            
                            num = line.indexOf(" ");
                            n.setZ(Double.parseDouble(line.substring(0,num)));
                            line = line.substring(num+1);
                            
                            num = line.length();
                            n.setY(Double.parseDouble(line.substring(0,num)));
                            
                            normals.add(n);
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
                                    face = line;
                                    collect = false;
                                }

                                /*
                                    * There are various formats of .obj files.
                                    * Some separate the face data though '//',
                                    while some separate the face data only though
                                    an empty space.
                                    * The following conditional accounts for both
                                    data formats. If a '/' is not found, take in
                                    the entire "face" data as it exists at this
                                    point in the program.
                                */
                                int index;
                                index = face.indexOf("/");
                                normalID = Integer.parseInt(face.substring(index+2,index+3)) - 1;
                                if(index == -1){
                                    index = face.length();
                                }

                                face = face.substring(0,index);
                                index = Integer.parseInt(face);
                                index--;
                                
                                Vertex collector_input = new Vertex(
                                    vertices.get(index).getX(),
                                    vertices.get(index).getZ()*-1,
                                    vertices.get(index).getY()
                                );
                                
                                collector = append(collector, collector_input);
                                
                                addFace = true;
                            }
                        }
                        
                        if(addFace){
                            if(normals.size() <= 0){
                                normals.add(new Vertex());
                            }
                            
                            f = new Face(collector,normals.get(normalID));
                            r.addFace(f);

                            collector = new Vertex[0];
                            
                            addFace = false;
                            normalID = 0;
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            //At the end of reading the file:
            //Clear Arraylists
            vertices.clear();
            normals.clear();
            
            //That RObject gets added to the scene.
            scene.add(r);
        }
        
        return scene;
    }
}