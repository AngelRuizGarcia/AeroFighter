package org.arg.view;

import org.arg.GameBoard;
import org.arg.engine.GameContext;
import org.arg.engine.GameStatus;
import org.arg.input.KeyBoardStatus;
import org.arg.model.Bullet;
import org.arg.model.Element;
import org.arg.model.Player;
import org.arg.model.enemy.Enemy;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameRenderer extends Thread {
    private static final int TARGET_UPS = 60;
    private static final long OPTIMAL_TIME = 1000000000L / TARGET_UPS; // Nanosegundos por tick (aprox 16.6ms)
    private final JPanel gamePanel;
    private final Lock lock = new ReentrantLock();
    private final HUD hud = new HUD();


    private GameContext gameContext;
    private GameBoard gameBoard;

    public GameRenderer(
            GameContext gameContext,
            GameBoard gameBoard,
            JPanel gamePanel
    ) {

        this.gameContext = gameContext;
        this.gameBoard = gameBoard;
        this.gamePanel = gamePanel;
        this.setDaemon(true);
    }


    public void run() {
        long lastUpdateTime = System.nanoTime();
        while (gameContext.getStatus() != GameStatus.GAME_OVER) {
            long now = System.nanoTime();
            long updateLength = now - lastUpdateTime;
            lastUpdateTime = now;

            gameBoard.processInput();
            //DECIDIR EN FUNCIÓN DEL ESTADO QUE HACER, CASI
            //SIEMPRE LLAMAR AL JPANEL PARA REPINTAR
            gamePanel.repaint();


            long processingTime = System.nanoTime() - lastUpdateTime;
            long sleepTime = (OPTIMAL_TIME - processingTime) / 1000000L; // Convertir a milisegundos
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void renderBackground(Graphics g, int width, int height) {
        ScrollingBackground background = gameBoard.getScrollingBackground();
        if (background != null) {
            int anchoImagen = background.getWidth() << 1;
            int altoPanel = background.getHeight() << 1;
            g.drawImage(background.getBackgroundImage(),
                    background.getXPosition(), 0,
                    anchoImagen, altoPanel,
                    null);

            g.drawImage(background.getBackgroundImage(),
                    background.getXPosition() + anchoImagen,
                    0, anchoImagen,
                    altoPanel, null
            );
        } else {
            //  por si falla la imagen
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);
        }
    }

    private void renderPlayer(Graphics g) {
        Player p = gameBoard.getPlayer();
        g.drawImage(ImageResource.PLAYER.getImage(),
                (int) p.getPosition().getX(),
                (int) p.getPosition().getY(),
                p.getSize().getWidth(), p.getSize().getHeight(), null
        );
    }

    public void render(Graphics g) {
        int width = this.gameBoard.getArea().width;
        int height = this.gameBoard.getArea().height;
        if (this.gameBoard != null) {

            this.renderBackground(g, width, height);

            switch (gameContext.getStatus()){
                case MENU -> {

                }
                case PLAYING -> {
                    //dibujar los elementos
                    this.renderPlayer(g);
                    this.renderElements(g);
                    hud.render(g, gameBoard.getPlayer(), this.gameContext,this.gameBoard.getArea().width);
                    //MIRAR SI ESTA EN MODO DEBUG Y DIBUJAR EL RECUADRO
                    if (gameContext.isDebugMode()) {
                        renderDebug(g);
                    }
                }
                case PAUSED -> {
                    renderPause(g);
                    if (gameContext.getStatus() != GameStatus.PLAYING){
                        gameContext.setStatus(GameStatus.PLAYING);
                    }
                }
                case GAME_OVER -> {

                }
            }

            //RECORRER EL RESTO DE ELEMNTOS Y DIBUJARLO



            g.setColor(Color.BLACK);

            //DIBUJAR LOS DIFERENTES TEXTOS EN FUNCIÓN DEL ESTADO
            //MENU-> PULSAR P PARA INICIAR
            //PAUSED -> TEXTO PAUSED
            //PLAYING->MOSTRAR EL HUD

        }
    }

    private void renderPause(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        String subText = "Press P to Resume";
        int textWidthSub = g.getFontMetrics().stringWidth(subText);
        g.drawString(subText, (gameBoard.getArea().width - textWidthSub) / 2, (gameBoard.getArea().height / 2) + 40);
    }

    public void renderDebug(Graphics g) {
        g.setColor(Color.GREEN);
        Player p = gameBoard.getPlayer();
        g.drawRect(
                (int) p.getPosition().getX(),
                (int) p.getPosition().getY(),
                p.getSize().getWidth(),
                p.getSize().getHeight()
        );

        for (Element e : gameBoard.getElements()) {
            g.drawRect(
                    (int) e.getPosition().getX(),
                    (int) e.getPosition().getY(),
                    e.getSize().getWidth(),
                    e.getSize().getHeight()
            );
        }
    }

    private void renderElements(Graphics g) {
        for (Element element : gameBoard.getElements()) {
            if (element instanceof Bullet) {

            }
            if (element instanceof Enemy e) {
                g.drawImage(
                        EnemySpriterMapper.getImageResource(e.getType()).getImage(),
                        (int) e.getPosition().getX(),
                        (int) e.getPosition().getY(),
                        e.getSize().getWidth(),
                        e.getSize().getHeight(),
                        null
                );
            }
        }
    }

}