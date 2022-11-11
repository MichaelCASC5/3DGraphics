import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keys{
    private boolean w,a,s,d,e,m,i,u,o,arL,arR,arU,arD,space,ctrl,shiftL;
    /*
        * Event listeners
    */
    public void keyPressed(KeyEvent e){
        int k = e.getKeyCode();

        if(k == 38){
            arU = true;
        }else if(k == 37){
            arL = true;
        }else if(k == 40){
            arD = true;
        }else if(k == 39){
            arR = true;
        }else if(k == 87){
            w = true;
        }else if(k == 65){
            a = true;
        }else if(k == 83){
            s = true;
        }else if(k == 68){
            d = true;
        }else if(k == 32){
            space = true;
        }else if(k == 17){
            ctrl = true;
        }
    }
    public void keyReleased(KeyEvent e){
        int k = e.getKeyCode();

        if(k == 38){
            arU = false;
        }else if(k == 37){
            arL = false;
        }else if(k == 40){
            arD = false;
        }else if(k == 39){
            arR = false;
        }else if(k == 87){
            w = false;
        }else if(k == 65){
            a = false;
        }else if(k == 83){
            s = false;
        }else if(k == 68){
            d = false;
        }else if(k == 32){
            space = false;
        }else if(k == 17){
            ctrl = false;
        }
    }
    /*
        * Typed letters
    */
    public void setA(boolean b){
        a = b;
    }
    public boolean getA(){
        return a;
    }
    public void setE(boolean b){
        e = b;
    }
    public boolean getE(){
        return e;
    }
    public void setD(boolean b){
        d = b;
    }
    public boolean getD(){
        return d;
    }
    public void setI(boolean b){
        i = b;
    }
    public boolean getI(){
        return i;
    }
    public void setM(boolean b){
        m = b;
    }
    public boolean getM(){
        return m;
    }
    public void setO(boolean b){
        o = b;
    }
    public boolean getO(){
        return o;
    }
    public void setS(boolean b){
        s = b;
    }
    public boolean getS(){
        return s;
    }
    public void setU(boolean b){
        u = b;
    }
    public boolean getU(){
        return u;
    }
    public void setW(boolean b){
        w = b;
    }
    public boolean getW(){
        return w;
    }
    
    /*
        Keyboard buttons
    */
    public void setSpace(boolean b){
        space = b;
    }
    public boolean getSpace(){
        return space;
    }
    public void setShiftL(boolean b){
        shiftL = b;
    }
    public boolean getShiftL(){
        return shiftL;
    }
    public void setCtrl(boolean b){
        ctrl = b;
    }
    public boolean getCtrl(){
        return ctrl;
    }
    
    /*
        * Arrow Keys
    */
    public void setArD(boolean b){
        arD = b;
    }
    public boolean getArD(){
        return arD;
    }
    public void setArL(boolean b){
        arL = b;
    }
    public boolean getArL(){
        return arL;
    }
    public void setArR(boolean b){
        arR = b;
    }
    public boolean getArR(){
        return arR;
    }
    public void setArU(boolean b){
        arU = b;
    }
    public boolean getArU(){
        return arU;
    }
}