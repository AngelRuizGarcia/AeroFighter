package org.arg;

import org.arg.base.Point2D;
import org.arg.base.Size;
import org.arg.base.Speed;
import org.arg.engine.GameContext;
import org.arg.engine.GameStatus;

import org.arg.engine.PhysicsEngine;
import org.arg.engine.audio.BGAudioEngine;
import org.arg.input.KeyBoardStatus;
import org.arg.model.Player;
import org.arg.view.GameRenderer;
import org.arg.view.ScrollingBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Game extends JPanel {
    private static final int TARGET_UPS = 120;
    private static final long OPTIMAL_TIME = 1000000000L / TARGET_UPS; // Nanosegundos por tick (aprox 16.6ms)
    public static int WIDTH = 480 * 2;
    public static int HEIGHT = 240 * 2;

    private final GameBoard gameBoard;
    private final GameRenderer gameRenderer;
    private volatile Player player;


    private JFrame windows;

    private GameContext gameContext = new GameContext(GameStatus.MENU, false);
    private KeyBoardStatus keyboardStatus;

    public Game(int enemyCreateProbability,

                int enemyShootProbability,
                Rectangle area) {

        this.configWindows();
        this.keyboardStatus = new KeyBoardStatus();
        Player tempo = new Player(
                "player",
                new Point2D(area.getCenterX(), area.getCenterY()),
                new Size(68, 38),
                new Speed(0, 0),
                area
        );
        //CREAR EL TABLERO, CONTIENE LOS ELEMENTOS DEL JUEGO
        this.gameBoard = new GameBoard(keyboardStatus, tempo,
                new ScrollingBackground("/bg4.png"),
                enemyCreateProbability,
                enemyShootProbability,
                area,
                this.gameContext);
        //CREAR E INICIAR LOS HILOS
        //HILO PARA PINTAR
        //HILO MUSICA DE FONDO
        Thread bgAudioEngine = new Thread(
                new BGAudioEngine()
        );
        //HILO EFECTOS SONIDO
        //HILO DE LA FISICA
        Thread physicsEngineThread = new Thread(
                new PhysicsEngine(gameContext, gameBoard)
        );

        this.gameRenderer = new GameRenderer(
                this.gameContext, this.gameBoard, this);

        // Iniciar los hilos


        this.gameRenderer.start();
        physicsEngineThread.start();
        bgAudioEngine.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.gameRenderer != null) {
            this.gameRenderer.render(g);
        }
    }


    private void configWindows() {
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.windows = new JFrame("Air Fight");
        windows.add(this);
        windows.setSize(WIDTH, HEIGHT + 36);
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windows.setVisible(true);
        this.windows.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyboardStatus.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyboardStatus.keyReleased(e);
            }
        });
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void exit() {
        this.gameContext.setStatus(GameStatus.GAME_OVER);
        try {

            if (gameRenderer != null)
                gameRenderer.join();
            System.out.println("fin de juego");
        System.exit(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Game game = new Game(7,  2, new Rectangle(0, 0,
                WIDTH,
                HEIGHT));
    }
}