package org.arg;

import org.arg.base.Point2D;
import org.arg.base.Size;
import org.arg.base.Speed;
import org.arg.engine.Collision;
import org.arg.engine.GameContext;
import org.arg.engine.GameStatus;
import org.arg.engine.audio.SoundEffect;
import org.arg.input.KeyBoardStatus;
import org.arg.model.Actor;
import org.arg.model.Bullet;
import org.arg.model.Element;
import org.arg.model.Player;
import org.arg.model.enemy.Enemy;
import org.arg.model.enemy.EnemyFactory;
import org.arg.model.enemy.EnemyType;
import org.arg.view.ScrollingBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class GameBoard {


    private final GameContext gameContext;
    private volatile List<Element> elements;
    private int enemyCreateProbability;
    private int playerShootProbability;
    private int enemyShootProbability;


    private Rectangle area;
    private Random rand;
    private Player player;
    private KeyBoardStatus keyboardStatus;
    private ScrollingBackground scrollingBackground;

    public GameBoard(
            KeyBoardStatus keyboardStatus,
            Player p,
            ScrollingBackground scrollingBackground,
            int enemyCreateProbability,
            int enemyShootProbability,
            Rectangle area,
            GameContext gameContext) {
        this.elements = new ArrayList<>();
        this.enemyCreateProbability = enemyCreateProbability;
        this.playerShootProbability = playerShootProbability;
        this.enemyShootProbability = enemyShootProbability;
        this.gameContext = gameContext;
        this.area = area;
        player = p;
        this.scrollingBackground = scrollingBackground;
        rand = new Random();
        this.keyboardStatus = keyboardStatus;
        this.elements = new ArrayList<Element>();

        elements.add(
                new Enemy(EnemyType.B17,new Point2D(150,100),new Speed(100,100), area)
        );
    }


    public void createEnemy() {
        Random rand = new Random();
        int probability = rand.nextInt(100);
        if (probability < this.enemyCreateProbability) {
            Enemy tempo = EnemyFactory.createRandom(area);

            this.elements = Stream.concat(this.elements.stream(), Stream.of(tempo))
                    .toList();
        }

    }

    private List<Element> removeOut() {
        List<Element> tempo = new ArrayList<Element>(elements);
        tempo.removeIf(element -> {
            if (element instanceof Bullet b) {
                if (b.getPosition().getX() <= 0 ||
                        (b.getPosition().getX() + b.getSize().getWidth() >= this.area.getMaxX()) ||
                        b.getPosition().getY() <= 0 ||
                        (b.getPosition().getY() + b.getSize().getHeight() >= this.area.getMaxY())) {
                    return true;
                }
            } else if (element instanceof Enemy e) {
                //solo por la izquierda
                if (e.getPosition().getX() + e.getSize().getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        });
        return tempo;


    }

    private void updateElements(long delta) {

        elements.stream().forEach(element -> {
            element.update(delta);

        });
        this.player.update(delta);
    }

    private ArrayList<Collision> getCollisions() {
        ArrayList<Collision> collisions = new ArrayList<>();
        List<Element[]> pairs = this.getPairs();
        for (Element[] pair : pairs) {
            if(pair[0].collisionWidth(pair[1])){
                Collision collision= new Collision(pair[0], pair[1]);
                collisions.add(collision);
            }
        }
        return collisions;
    }

    private List<Element[]> getPairs() {
        List<Element[]> pairs = new ArrayList<>();
        for (int i = 0; i < this.elements.size(); i++) {
            for (int j = i + 1; j < this.elements.size(); j++) {
                Element[] pair = new Element[2];
                Element first = this.elements.get(i);
                Element last = this.elements.get(j);
                if (
                        GameBoard.iSPairCompatible(first, last)


                ) {
                    pair[0] = this.elements.get(i);
                    pair[1] = this.elements.get(j);
                    pairs.add(pair);
                }
            }
        }
        for( int i = 0; i < this.elements.size(); i++ ) {
            if(GameBoard.iSPairCompatible(this.player, this.elements.get(i))){
                pairs.add(new Element[]{player, this.elements.get(i)});
            }
        }
        return pairs;
    }

    private static boolean iSPairCompatible(Element first, Element last) {
        return (first instanceof Player &&
                (last instanceof Enemy ||
                        (last instanceof Bullet b && b.getOwnerType() == Bullet.OwnerType.Enemy))
        ) ||
                (first instanceof Enemy &&
                        (last instanceof Player || (last instanceof Bullet b && b.getOwnerType() == Bullet.OwnerType.Player)));
    }

    public void update(long delta) {
        //se mueve el fondo
        this.scrollingBackground.update();
        //se mueven los enemigos
        this.updateElements(delta);
        //se crea un enemigo de forma aleatoria
        this.createRandomEnemy();
        //se disparan de forma aleatoria los enemigos
        this.shoot();
        //se evaluan las colisiones
        List<Collision> collision = this.getCollisions();
        this.evalCollision(collision);
        //se eliminan las balas y enemigos que estan fuera
        this.elements = this.removeOut();
    }
    private void evalCollision(List<Collision> collision) {
        for (Collision c : collision) {
            if (c.getOne() instanceof Player p) {
                this.player.decLifes();
                this.elements = this.removeElements(c.getTwo());
                try {

                    gameContext.getSoundQueue().put(SoundEffect.PLAYERCOLLISION);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(this.player.getLifes() == 0){
                    this.gameContext.setStatus(GameStatus.GAME_OVER);
                }
            } else {
                //se borran los dos
                this.elements = this.removeElements(c.getOne());
                this.elements = this.removeElements(c.getTwo());
                this.player.incScore(10);
                try {
                    gameContext.getSoundQueue().put(SoundEffect.ENEMYCOLLISION);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private List<Element> removeElements(Element e) {
        List<Element> tempo = new ArrayList<Element>(elements);
        tempo.remove(e);
        return tempo;

    }

    private void createRandomEnemy() {
        if (rand.nextInt(0, 100) <= enemyCreateProbability) {
            createEnemy();

        }
    }
    public ScrollingBackground getScrollingBackground() {
        return scrollingBackground;
    }

    private void shoot() {
        List<Bullet> nuevasBalas = new ArrayList<>();
        this.elements.forEach(element -> {
            if (element instanceof Actor && element instanceof Enemy) {

                    if (rand.nextInt(0, 100) <= enemyShootProbability) {
                        Bullet tempo = ((Enemy) element).shoot();
                        nuevasBalas.add(tempo);
                    }
            }
        });

        if (!nuevasBalas.isEmpty()) {

            List<Element> temporal = new ArrayList<>(this.elements);
            temporal.addAll(nuevasBalas);
            this.elements = temporal;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Rectangle getArea() {
        return area;
    }

    public void processInput() {
        if (keyboardStatus.isMovingDown()) {
            this.player.accelerate(0, +1);
        }
        if (keyboardStatus.isMovingUp()) {
            this.player.accelerate(0, -1);
        }
        if (keyboardStatus.isMovingRight()) {
            this.player.accelerate(1, 0);
        }
        if (keyboardStatus.isMovingLeft()) {
            this.player.accelerate(-1, 0);
        }
        if (keyboardStatus.consumeShooting()
                &&
                //maximo de balas
                this.elements.stream().filter(element -> element instanceof Bullet b &&
                        b.getOwnerType() == Bullet.OwnerType.Player).count() < this.player.getMaxBullets()) {
            Bullet b = this.player.shoot();
            this.elements.add(b);
            try {
                gameContext.getSoundQueue().put(SoundEffect.PLAYERSHOOT); // Inserta de forma segura para hilos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
        if (this.keyboardStatus.consumePauseToggle()) {
            GameStatus gameStatus = this.gameContext.getStatus();
            if (gameStatus == GameStatus.PAUSED || gameStatus == GameStatus.MENU) {
                this.gameContext.setStatus(GameStatus.PLAYING);
            } else if (gameStatus == GameStatus.PLAYING) {
                this.gameContext.setStatus(GameStatus.PAUSED);
            }
        }
        if (this.gameContext.isDebugMode() != this.keyboardStatus.isDebugging())
            this.gameContext.setDebugMode(this.keyboardStatus.isDebugging());


    }

    public List<Element> getElements() {
        return this.elements;
    }
}