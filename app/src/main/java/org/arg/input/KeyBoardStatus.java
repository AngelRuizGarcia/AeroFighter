package org.arg.input;

import java.awt.event.KeyEvent;

public class KeyBoardStatus {
    private volatile boolean movingLeft = false;
    private volatile boolean movingRight = false;
    private volatile boolean movingUp = false;
    private volatile boolean movingDown = false;
    private volatile boolean shooting = false;
    private volatile boolean debugging = false;
    private volatile boolean isPaused = false;

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                movingLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                movingRight = true;
                break;
            case KeyEvent.VK_SPACE:
                shooting = true;
                break;
            case KeyEvent.VK_UP:
                movingUp = true;
                break;
            case KeyEvent.VK_DOWN:
                movingDown = true;
                break;
            case KeyEvent.VK_D:
                debugging = !debugging;
                break;
                case KeyEvent.VK_P:
                    isPaused = true;
                    break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                movingLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                movingRight = false;
                break;
            case KeyEvent.VK_SPACE:
                shooting = false;
                break;
            case KeyEvent.VK_UP:
                movingUp = false;
                break;
            case KeyEvent.VK_DOWN:
                movingDown = false;
                break;


        }
    }

    // Métodos sincronizados o atómicos para que el hilo del Jugador consulte el estado
    public synchronized boolean isMovingLeft() {
        return movingLeft;
    }

    public synchronized boolean isMovingRight() {
        return movingRight;
    }

    public synchronized boolean isMovingUp() {
        return movingUp;
    }

    public synchronized boolean isMovingDown() {
        return movingDown;
    }

    public synchronized boolean isShooting() {
        return shooting;
    }
public synchronized boolean isDebugging() {
        return debugging;
}
    public synchronized boolean consumeShooting() {
        if (shooting) {
            shooting = false; // Se resetea inmediatamente al leerlo
            return true;      // Devuelve true solo la primera vez
        }
        return false;
    }


    public boolean consumePauseToggle() {
        if (this.isPaused) {
            this.isPaused = false;
            return true;
        }
        return false;
    }

}