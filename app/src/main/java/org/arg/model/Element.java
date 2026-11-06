package org.arg.model;


import org.arg.base.Point2D;
import org.arg.base.Size;
import org.arg.base.Speed;

public class Element {
    protected static int MAXSPEEDX=3;
    protected static int MAXSPEEDY=3;
    protected  String name;
    protected  Point2D position;
    protected  Size size;
    protected  Speed speed;
    protected  final double deceleratex=0.98d,deceleratey=0.98d;
    public Element(String name,Point2D position, Size size, Speed speed) {
       this.name=name;
        this.position = position;
        this.size = size;
        this.speed = speed;
    }

    public Point2D getPosition() {
        return position;
    }
    public Size getSize() {
        return size;
    }
    public Speed getSpeed() {
        return speed;
    }
    public String getName() {
        return name;
    }
    public void update(long delta) {
        this.position=this.position.mover(this.speed.getVX(),this.speed.getVY());

    }
    public boolean collisionWidth(Element other){
        return this.position.getX() < other.position.getX() + other.size.getWidth() &&
                this.position.getX() + this.size.getWidth()> other.position.getX() &&
                this.position.getY() < other.position.getY() + (double) other.size.getHeight() &&
                this.position.getY() + this.size.getHeight() > other.position.getY();
    }
    public void accelerate(double x,double y){
        double newSpeedX=x+this.speed.getVX();
        double newSpeedY=y+this.speed.getVY();
       if(newSpeedX>MAXSPEEDX){
           newSpeedX=MAXSPEEDX;
       }else if(newSpeedX<-MAXSPEEDX){
           newSpeedX=-MAXSPEEDX;
       }
       if(newSpeedY>MAXSPEEDY){
           newSpeedY=MAXSPEEDY;
       }
       if(newSpeedY<-MAXSPEEDY){
           newSpeedY=-MAXSPEEDY;
       }
        this.speed= new Speed(newSpeedX,newSpeedY);
    }
    public void decelerate(){
        this.speed =new Speed(this.speed.getVX()*deceleratex,this.speed.getVY()*deceleratey);
    }

    public Point2D getCenter() {
        return new Point2D(this.getPosition().getX()+size.getWidth()/2, this.getPosition().getY()+size.getHeight()/2);
    }
}
