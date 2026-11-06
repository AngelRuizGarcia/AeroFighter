package org.arg.model.enemy;

import org.arg.base.Point2D;
import org.arg.base.Speed;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class EnemyFactory {

    public static Enemy createRandom(Rectangle area) {
        EnemyType[] values=EnemyType.values();
        int index= ThreadLocalRandom.current().nextInt(values.length);
        EnemyType  type=values[index];

        int xPosition= area.width;
        int yPosition= ThreadLocalRandom.current().nextInt(area.height- (type.getHeight() << 1));
        yPosition+= type.getHeight();
        int ySpeed= ThreadLocalRandom.current().nextInt(2)+1;
         Enemy enemy=new Enemy(type,new Point2D(xPosition,yPosition), new Speed(
                type.getXSpeed(),ySpeed),area);

        return enemy;

    }
}
