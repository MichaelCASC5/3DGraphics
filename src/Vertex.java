public class Vertex{
    /*
        * Declaration and Inilization
    */

    double x,y,z;
    
    public Vertex(){
        x = 0;
        y = 0;
        z = 0;
    }
    public Vertex(double a, double b, double c){
        x = a;
        y = b;
        z = c;
    }
    public void setX(double n){
        x = n;
    }
    public double getX(){
        return x;
    }
    public void setY(double n){
        y = n;
    }
    public double getY(){
        return y;
    }
    public void setZ(double n){
        z = n;
    }
    public double getZ(){
        return z;
    }
    public String toString(){
        return "" + x + ", " + y + ", " + z;
    }
}