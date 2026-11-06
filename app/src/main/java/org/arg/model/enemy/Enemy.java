package org.arg.model.enemy;

import org.arg.base.Point2D;
import org.arg.base.Speed;
import org.arg.model.Actor;
import org.arg.model.Bullet;

import java.awt.*;

public class Enemy  extends Actor {
    private static long bulletNumber=0;
    private static int enemyNumber=0;

    private EnemyType enemyType;
    public Enemy(EnemyType enemyType, Point2D position,  Speed speed, Rectangle area) {

        super(enemyType.name()+enemyNumber, position, enemyType.getSize(), speed,area);
        this.enemyType=enemyType;
        enemyNumber++;
    }

    @Override
    protected long incCounter() {
        bulletNumber++;
        return bulletNumber;
    }

    @Override
    public Bullet shoot() {
        return this.shoot(Bullet.OwnerType.Enemy);
    }

    public EnemyType getType() {
        return this.enemyType;
    }


    public void update(long delta){
        super.update(delta);
         Speed tempoSpeed=null;

        int changeYDirection=1;
        Point2D newPosition=this.getPosition();

        if(newPosition.getY()<area.getMinY()){
            newPosition=newPosition.setY(area.getMinY());
            changeYDirection=-1;
        }else {
            if (newPosition.getY() + this.getSize().getHeight() > area.getMaxY() - 5) {
                newPosition = newPosition.setY(area.getMaxY() - this.getSize().getHeight() - 5);
                changeYDirection = -1;
            }
        }

        tempoSpeed=new Speed(this.speed.getVX(),this.speed.getVY()*changeYDirection);
        this.position=newPosition;
        this.speed=tempoSpeed;

    }

}
