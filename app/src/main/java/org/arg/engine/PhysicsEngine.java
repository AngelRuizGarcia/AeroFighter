package org.arg.engine;

import org.arg.GameBoard;

import javax.swing.*;

public class PhysicsEngine implements Runnable {
    private static final int TARGET_UPS = 60;
    private static final long OPTIMAL_TIME = 1000000000L / TARGET_UPS; // Nanosegundos por tick (aprox 16.6ms)
    private final JPanel gamePanel = new JPanel();
    private GameContext gameContext;
    private GameBoard gameBoard;

    public PhysicsEngine(GameContext gameContext, GameBoard gameBoard) {
        this.gameContext = gameContext;
        this.gameBoard = gameBoard;
    }

    public void run() {
        long lastUpdateTime = System.nanoTime();
        while (gameContext.getStatus() != GameStatus.PAUSED || gameContext.getStatus() != GameStatus.MENU) {
            long now = System.nanoTime();
            long updateLength = now - lastUpdateTime;
            lastUpdateTime = now;
            gameBoard.processInput();
            //DECIDIR EN FUNCIÓN DEL ESTADO QUE HACER, CASI
            //SIEMPRE LLAMAR AL JPANEL PARA REPINTAR
            System.out.println("Physics. " + gameContext.getStatus());
            gameBoard.update(now - updateLength);


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
}
