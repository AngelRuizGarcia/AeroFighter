package org.arg.engine.audio;

import org.arg.engine.GameContext;
import org.arg.engine.GameStatus;

import javazoom.jl.player.Player;

public class SoundEffectEngine implements Runnable {
    private GameContext gameContext;

    public SoundEffectEngine(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void run() {
        while (gameContext.getStatus() != GameStatus.GAME_OVER) {
            try {
                SoundEffect effect = gameContext.getSoundQueue().take();
                playSound(effect);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void playSound(SoundEffect effect) {
        Thread playbackThread = new Thread(() -> {
            try {
                Player player = new Player(effect.getInputStream());
                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        playbackThread.start();
    }
}