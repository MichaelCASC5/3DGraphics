/*
========================= ========================= 

        * R A D I U M  3 D
        * MICHAEL CALLE, 2022

        * Main Driver.java

        * 3D Graphics Framework Software. Eventually,
        this will provide the backbone of a 3D program
        that can be easily repurposed for any other use.

========================= ========================= 
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Driver extends JComponent implements KeyListener, MouseListener, MouseMotionListener{
    /*
        * Declaring variables and objects
    */
    private int WIDTH;
    private int HEIGHT;
    
    private Keys k;
    private Camera cam;
    
    private Sky sky;
    
    private ArrayList<RObject> scene;
    
    public Driver(){
        /*
            * Instantiating Objects, initilizing vars
        */

        //Canvas Resolution
        WIDTH = 1000;
        HEIGHT = 500;

        scene = new ArrayList<>();
        
        //Read Scene
        read("Scene 1");
        System.out.println("Object Face Count: " + scene.get(0).getFaceCount());

        k = new Keys();
        cam = new Camera();
        cam.setRes(WIDTH,HEIGHT,5);
        
        sky = new Sky(HEIGHT,WIDTH);
        
        //Setting up the GUI
        JFrame gui = new JFrame();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setTitle("Radium Engine");
        gui.setPreferredSize(new Dimension(WIDTH + 5, HEIGHT + 30));
        gui.setResizable(false);
        gui.getContentPane().add(this);
        
        gui.pack();
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.addKeyListener(this);
        gui.addMouseListener(this);
        gui.addMouseMotionListener(this);
    }
    /*
        * Paint to Canvas
    */
    public void paintComponent(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0,0,WIDTH,HEIGHT);
        
        for(int i=0;i<scene.size();i++){
            scene.get(i).actions(cam);
            scene.get(i).draw(g,cam);
        }
    }
    /*
        * Game Logic
    */
    public void loop(){
        /*
            * Camerica compensation logic
        */
        cam.compensation(k.getW(), k.getA(), k.getS(), k.getD());
        
        /*
            * Horizontal movement
        */
        if(k.getArL()){ 
            cam.setYaw(cam.getYaw()+2);
        }
        if(k.getArR()){
            cam.setYaw(cam.getYaw()-2);
        }
        if(k.getArU()){
            cam.setPitch(cam.getPitch()-2);
        }
        if(k.getArD()){
            cam.setPitch(cam.getPitch()+2);
        }
        
        /*
            * Vertical movement
        */
        if(k.getSpace()){
            cam.setZ(cam.getZ()+0.2);
        }
        if(k.getCtrl()){
            cam.setZ(cam.getZ()-0.2);
        }
        
        repaint();
    }
    /*
        * User input and key codes
    */
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        
        if(key == 39){              //LEFT ARROW
            k.setArL(true);
        }else if(key == 37){        //RIGHT ARROW
            k.setArR(true);
        }else if(key == 87){        //W
            k.setW(true);
        }else if(key == 65){        //A
            k.setA(true);
        }else if(key == 83){        //S
            k.setS(true);
        }else if(key == 68){        //D
            k.setD(true);
        }else if(key == 32){        //D
            k.setSpace(true);
        }else if(key == 17){        //D
            k.setCtrl(true);
        }else if(key == 38){        //UP ARROW
            k.setArU(true);
        }else if(key == 40){        //DOWN ARROW
            k.setArD(true);
        }
    }
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        
        if(key == 39){              //LEFT ARROW
            k.setArL(false);
        }else if(key == 37){        //RIGHT ARROW
            k.setArR(false);
        }else if(key == 87){        //W
            k.setW(false);
        }else if(key == 65){        //A
            k.setA(false);
        }else if(key == 83){        //S
            k.setS(false);
        }else if(key == 68){        //D
            k.setD(false);
        }else if(key == 32){        //D
            k.setSpace(false);
        }else if(key == 17){        //D
            k.setCtrl(false);
        }else if(key == 38){        //UP ARROW
            k.setArU(false);
        }else if(key == 40){        //DOWN ARROW
            k.setArD(false);
        }
    }
    public void keyTyped(KeyEvent e){
    }
    public void mousePressed(MouseEvent e){
    }
    public void mouseReleased(MouseEvent e){
    }
    public void mouseClicked(MouseEvent e){
    }
    public void mouseEntered(MouseEvent e){
    }
    public void mouseExited(MouseEvent e){
    }
    public void mouseMoved(MouseEvent e){
    }
    public void mouseDragged(MouseEvent e){
    }
    /*
        * Reading Files
        * Helper method to the read() method.
        * Append vertices to new face generated
    */
    public double[] append(double[] a, double n){
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
    public void read(String d){
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
    }
    /*
        * Helper method to the main method; starts the game thread
    */
    public void start(final int ticks){
        Thread gameThread = new Thread(){
            public void run(){
                while(true){
                    loop();
                    try{
                        Thread.sleep(1000 / ticks);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };	
        gameThread.start();
    }
    /*
        * Main method, instantiates the driver and starts the game thread
    */
    public static void main(String[] args){
        Driver g = new Driver();
        g.start(60);
    }
}