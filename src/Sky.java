import java.awt.Color;
import java.awt.Graphics;

public class Sky{
    /*
        * Declaration and Initlization
    */
    private int HEIGHT,WIDTH;
    private int horizon;
    
    public Sky(int h, int w){
        horizon = 0;
        
        HEIGHT = h;
        WIDTH = w;
    }
    /*
        * Camera logic
    */
    public void actions(Camera c){
        horizon = (int)c.getPitch();
        if(horizon < 360 && horizon > 90){
            horizon-=360;
        }
        horizon*=-17.5555;
        horizon+=HEIGHT/2;
    }
    /*
        * Draws sky in the background
    */
    public void draw(Graphics g){
        //Background colors
        g.setColor(new Color(90,90,90));
        g.fillRect(0, horizon, WIDTH, HEIGHT-horizon);
        
        //Background colors
        g.setColor(new Color(100,100,100));
        g.fillRect(0, 0, WIDTH, horizon);
        
        //Dynamic Horizon
        g.setColor(new Color(90,90,90));
        g.fillRect(0, horizon, WIDTH, HEIGHT*2);
        
        g.setColor(new Color(100,100,100));
        g.fillRect(0, horizon-1000, WIDTH, HEIGHT*2);
    }
}