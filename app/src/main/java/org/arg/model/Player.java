package org.arg.model;

import org.arg.base.Point2D;
import org.arg.base.Size;
import org.arg.base.Speed;

import java.awt.*;

public class Player extends Actor {
    private static long bulletNumber = 0;
    private int maxBullets=10;
    private int lifes = 3;
    private int score=0;

    public Player(String name, Point2D position, Size size, Speed speed, Rectangle area) {
        super(name, position, size, speed, area);
    }
    public int getMaxBullets() {
        return maxBullets;
    }
    public void setMaxBullets(int maxBullets) {
        this.maxBullets = maxBullets;
    }
    public int getScore() {
        return score;
    }
    public void incScore(int inc){
        score+=inc;
    }
    @Override
    protected long incCounter() {
        bulletNumber++;
        return bulletNumber;
    }


    public void update(long delta){
        super.update(delta);
        //Point2D newPosition=this.position.mover(this.getSpeed().getVX(),this.getSpeed().getVY());
        //this.speed= this.speed.decelerate(this.deceleratex,this.deceleratey);
        Speed tempoSpeed=null;
        int changeXDirection=1;
        int changeYDirection=1;
        Point2D newPosition=this.getPosition();
        //se evalua si se tiene que cambiar la dirección y se coloca en el borde
        if(newPosition.getX()<area.getMinX()){
            newPosition=newPosition.setX(area.getMinX());

            changeXDirection=-1;
        }else if(newPosition.getX()+this.getSize().getWidth()>area.getMaxX()){
            newPosition=newPosition.setX(area.getMaxX()-this.getSize().getWidth());
            changeXDirection=-1;
        }
        if(newPosition.getY()<area.getMinY()){
            newPosition=newPosition.setY(area.getMinY());
            changeYDirection=-1;
        }else if(newPosition.getY()+this.getSize().getHeight()>area.getMaxY()){
            newPosition=newPosition.setY(area.getMaxY()-this.getSize().getHeight());
            changeYDirection=-1;
        }
        if(!bounce){
            if(changeXDirection==-1){
                this.speed= new Speed(0,this.speed.getVY());
            }
            if(changeYDirection==-1){
                this.speed= new Speed(this.speed.getVX(),0);
            }
        }
        tempoSpeed=new Speed(this.speed.getVX()*changeXDirection,this.speed.getVY()*changeYDirection);
        this.position=newPosition;
        this.speed=tempoSpeed;
        this.decelerate();
    }

    @Override
    public Bullet shoot() {
        Point2D position= new Point2D(
                this.position.getX()+this.getSize().getWidth(),
                this.position.getY()+this.getSize().getHeight()/2);
         return this.shoot(Bullet.OwnerType.Player, position);
    }



    public void decLifes() {
         this.lifes = this.lifes - 1;

    }

    public int getLifes() {
        return lifes;
    }


}
