package org.arg.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

;

public class ScrollingBackground {
    private final AtomicInteger backgrondXPosition = new AtomicInteger(0);
    private BufferedImage backgroundImage;
    private AtomicInteger backgroundXIncrement=new AtomicInteger(-2);
    /*public ScrollingBackground(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }*/

    public ScrollingBackground(String fileName) {
        try {
            URL imageURL = getClass().getResource(fileName);

            if (imageURL != null) {
                backgroundImage = ImageIO.read(imageURL);
            } else {
                System.err.println("No se encontró la imagen en los recursos.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al leer la imagen de fondo.");
        }
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public int getWidth() {
        return backgroundImage.getWidth();
    }

    public int getHeight() {
        return backgroundImage.getHeight();
    }

    public int getXPosition() {
        return backgrondXPosition.get();
    }
    public void setBackgroundXIncrement(int increment) {
        this.backgroundXIncrement.set(increment);
    }
    public void update() {
        backgrondXPosition.accumulateAndGet(this.backgroundXIncrement.get(), Integer::sum);
        if(backgrondXPosition.get() < -this.backgroundImage.getWidth()*2) {
            backgrondXPosition.set(0);
        }

    }
}
