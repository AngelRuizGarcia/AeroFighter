package org.arg.view;

import org.arg.engine.GameContext;
import org.arg.model.Player;

import java.awt.*;

public class HUD {


    public void render(Graphics g, Player player, GameContext gameContext, int width) {
        // Configuración de la fuente y color por defecto para el HUD
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.RED);

        int hudY = 35;       // Altura fija desde la parte superior de la ventana
        int padding = 25;    // Margen lateral

        int score = (player != null) ? player.getScore() : 0;
        String scoreText = "SCORE: " + String.format("%06d", score);
        g.drawString(scoreText, padding, hudY);



        int lives = (player != null) ? player.getLifes() : 0;
        String livesText = "LIVES: " + lives;

        int livesWidth = g.getFontMetrics().stringWidth(livesText);
        g.drawString(livesText, width - livesWidth - padding, hudY);
    }
}