package org.arg.base;
public class Size implements Cloneable {
    private final int width;
    private final int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Size incSize(int widthDelta, int heightDelta) {
        return new Size(this.width + widthDelta, this.height + heightDelta);
    }

    @Override
    public Object clone() {
        return new Size(this.width, this.height);
    }
}