public class Camera{
    private int WIDTH, HEIGHT;
    private double yaw, pitch;
    private double x,y,z;

    private int scale;
    
    public Camera(){
        yaw = 0;
        pitch = 0;
        
        x = 0;
        y = 0;
        z = 0;

        WIDTH = 0;
        HEIGHT = 0;

        scale = 0;
    }
    public double getX(){
        return x;
    }
    public void setX(double n){
        x = n;
    }
    public double getY(){
        return y;
    }
    public void setY(double n){
        y = n;
    }
    public double getZ(){
        return z;
    }
    public void setZ(double n){
        z = n;
    }
    public double getYaw(){
        return yaw;
    }
    public void setYaw(double n){
        n+=360;
        n%=360;
        yaw = n;
    }
    public double getPitch(){
        return pitch;
    }
    public void setPitch(double n){
        n+=360;
        n%=360;
        
        if(n > 90 && n < 180){
            n = 90;
        }else if(n < 270 && n > 180){
            n = 270;
        }
        
        pitch = n;
    }
    public void setRes(int a, int b, int s){
        WIDTH = a;
        HEIGHT = b;

        scale = s;
    }
    public void setWidth(int n){
        WIDTH = n;
    }
    public int getWidth(){
        return WIDTH;
    }
    public void setHeight(int n){
        HEIGHT = n;
    }
    public int getHeight(){
        return HEIGHT;
    }
    public void setScale(int n){
        scale = n;
    }
    public int getScale(){
        return scale;
    }
    public void compensation(boolean w, boolean a, boolean s, boolean d){
        double yaw;
        yaw = getYaw();
        
        double userX,userY;
        userX = getX();
        userY = getY();
        
        double comp;
        comp = (yaw%90)/90.0;
        
        //Speed
        comp*=0.2;
        
        //Add
        double add = 0.2;
        
        //Key Movements
        if(w){//FORWARD
            if(yaw >= 0 && yaw < 90){//Top right
                userX = (userX)+comp;
                userY = (userY+add)-comp;
            }else if(yaw >= 180 && yaw < 270){//Bottom left
                userX = (userX)-comp;
                userY = (userY-add)+comp;
            }else if(yaw >= 270 && yaw < 360){//Top left
                userX = (userX)-(add-comp);
                userY = (userY+add)-(add-comp);
            }else{//Bottom right
                userX = (userX)+(add-comp);
                userY = (userY-add)+(add-comp);
            }
        }
        if(s){//BACKWARD
            if(yaw >= 0 && yaw < 90){//Top right
                userX = (userX)-comp;
                userY = (userY-add)+comp;
            }else if(yaw >= 180 && yaw < 270){//Bottom left
                userX = (userX)+comp;
                userY = (userY+add)-comp;
            }else if(yaw >= 270 && yaw < 360){//Top left
                userX = (userX)+(add-comp);
                userY = (userY-add)+(add-comp);
            }else{//Bottom right
                userX = (userX)-(add-comp);
                userY = (userY+add)-(add-comp);
            }
        }
        if(a){//LEFT
            if(yaw >= 0 && yaw < 90){//Top right
                userX = (userX-add)+comp;
                userY = (userY)+comp;
            }else if(yaw >= 180 && yaw < 270){//Bottom left
                userX = (userX+add)-comp;
                userY = (userY)-comp;
            }else if(yaw >= 270 && yaw < 360){//Top left
                userX = (userX)-comp;
                userY = (userY-add)+comp;
            }else{//Bottom right
                userX = (userX)+comp;
                userY = (userY+add)-comp;
            }
        }
        if(d){//RIGHT
            if(yaw >= 0 && yaw < 90){//Top right
                userX = (userX+add)-comp;
                userY = (userY)-comp;
            }else if(yaw >= 180 && yaw < 270){//Bottom left
                userX = (userX-add)+comp;
                userY = (userY)+comp;
            }else if(yaw >= 270 && yaw < 360){//Top left
                userX = (userX)+comp;
                userY = (userY+add)-comp;
            }else{//Bottom right
                userX = (userX)-comp;
                userY = (userY-add)+comp;
            }
        }
        setY(userY);
        setX(userX);
    }
}
