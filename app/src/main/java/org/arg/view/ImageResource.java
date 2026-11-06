package org.arg.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public enum ImageResource {
    PLAYER("/player1.png"),
    BOMBARDERO("/bombardero.png"),
    B17("/b17.png"),
    F16("/f16.png"),
    XWIN("/xwin.png"),
    BALAPLAYER("/balaplayer.png"),
    BALAENEMY("/balaenemy.png");
    private String path;
    private BufferedImage image;

    ImageResource(String path) {
        this.path = path;
        try {
            URL imageURL = getClass().getResource(path);

            if (imageURL != null) {
                this.image = ImageIO.read(imageURL);
            } else {
                System.err.println("No se encontró la imagen en los recursos.");
            }


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al leer la imagen de fondo.");
        }
    }

    public String getPath() {
        return this.path;
    }

    public BufferedImage getImage() {
        return this.image;
    }
}

