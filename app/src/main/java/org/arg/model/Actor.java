package org.arg.model;

import org.arg.base.Point2D;
import org.arg.base.Size;
import org.arg.base.Speed;

import java.awt.*;

public abstract class Actor extends Element {
    protected final Rectangle area;
protected boolean bounce=false;
    public Actor(String name,
                 Point2D position,
                 Size size,
                 Speed speed,
                 Rectangle area) {
        super(name, position, size, speed);
        this.area=area;
    }

    protected abstract  long incCounter();

    protected Bullet shoot(Bullet.OwnerType type, Point2D position){

        Bullet bullet= new Bullet(this.name,
                type,
                type== Bullet.OwnerType.Enemy?"enemyBullet":"playerBullet"+incCounter(),
                position, //new Point2D(this.getPosition().getX()+this.size.getRadius()/2, this.getPosition().getY()+this.size.getRadius()/2),
                new Size(Bullet.WIDTH,Bullet.HEIGHT),

                new Speed(type== Bullet.OwnerType.Enemy?-4:4,0)//this.getSpeed().getVX()-1:this.getSpeed().getVX()+1,0)
                       // this.getSpeed().getVY()>0?this.getSpeed().getVY()+1:this.getSpeed().getVY()-1)

        );

        return bullet;
    }
    public abstract Bullet shoot();
    protected Bullet shoot(Bullet.OwnerType type){
        Point2D center=this.getCenter();
        if(type== Bullet.OwnerType.Enemy){
            center.mover(-this.getSize().getWidth()/2,this.getSize().getHeight()/2);
        }else{
            center.mover(this.getSize().getWidth()/2,this.getSize().getHeight()/2);
        }

        return this.shoot(type, center);
    }


}
