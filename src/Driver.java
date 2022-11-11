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
    
    private Keys keys;
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
        scene = Manager.read("Scene 1");
        System.out.println("Object Face Count: " + scene.get(0).getFaceCount());

        keys = new Keys();
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
        cam.compensation(keys);
        
        repaint();
    }
    /*
        * User input and key codes
    */
    public void keyPressed(KeyEvent e){
        keys.keyPressed(e);

        if(e.getKeyCode() == 27){
            System.exit(0);
        }
    }
    public void keyReleased(KeyEvent e){
        keys.keyReleased(e);
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