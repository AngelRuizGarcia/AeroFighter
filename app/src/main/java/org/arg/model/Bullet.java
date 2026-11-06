package org.arg.model;

import org.arg.base.Point2D;
import org.arg.base.Size;
import org.arg.base.Speed;

public class Bullet extends Element {
    public static int WIDTH=9;
    public static int HEIGHT=3;
    public enum OwnerType {
        Player,
        Enemy
    }
    private final String owner;
    private final OwnerType ownerType;
    public Bullet(String owner,OwnerType type, String name, Point2D position, Size size, Speed speed) {
        super(name, position, size, speed);
        this.owner = owner;
        this.ownerType = type;
    }
    public OwnerType getOwnerType() {
        return ownerType;
    }
    public void move(){
        Point2D newPoint2D=this.position.mover(this.speed.getVX(),this.speed.getVY());
        Speed newSpeed=new Speed(this.speed.getVX(),this.speed.getVY()); //decelerate(0eleratex,deceleratey);
        this.position=newPoint2D;
        this.speed=newSpeed;
        //Bullet tempo= new Bullet(this.owner,this.ownerType,this.name,newPoint2D,this.size,newSpeed);
        //return tempo;
    }
}
