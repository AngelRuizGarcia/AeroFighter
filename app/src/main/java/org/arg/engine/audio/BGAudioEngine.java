package org.arg.engine.audio;

import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.arg.engine.GameContext;
import org.arg.engine.GameStatus;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

public class BGAudioEngine implements Runnable {
    private static final int TARGET_UPS = 60;
    private static final long OPTIMAL_TIME = 1000000000L / TARGET_UPS; // Nanosegundos por tick (aprox 16.6ms)
    private AdvancedPlayer player = null;

    @Override
    public void run() {
        long lastUpdateTime = System.nanoTime();
        while (true) {
            InputStream audioStream = null;
            Path ruta = Path.of("/song1.mp3");

            long now = System.nanoTime();
            long updateLength = now - lastUpdateTime;
            lastUpdateTime = now;


            try {
                audioStream = getClass().getResourceAsStream("/song1.mp3");
                if (audioStream == null) {
                    throw new FileNotFoundException("No se encontró el audio en: " + ruta);
                }
                if (player == null) {
                    player = new AdvancedPlayer(audioStream);
                    // Reproduce desde el frame guardado
                    player.play((int) updateLength, Integer.MAX_VALUE);
                }
            } catch (Exception e) {
                if (gameContext.getStatus() != GameStatus.GAME_OVER) {
                    e.printStackTrace();
                }
            } finally {
                if (audioStream != null) {
                    try {
                        audioStream.close();
                    } catch (Exception ignored) {
                    }
                }
                player = null;
            }

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

