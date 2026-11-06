package org.arg.model.enemy;

import org.arg.base.Size;

public enum EnemyType{
    B17(75,48,-2),
    BOMBARDERO(121,70,-1),
    F16(57,29,-3),
    XWIN(70,46,-5);
    private Size size;
    private int xSpeed;
    EnemyType(int width, int height,int xSpeed){
        this.size = new Size(width, height);
        this.xSpeed = xSpeed;
    }
    public Size getSize(){
        return this.size;
    }
    public int getWidth(){
        return this.size.getWidth();
    }
    public int getHeight(){
        return this.size.getHeight();
    }
    public int getXSpeed(){
        return this.xSpeed;
    }

}