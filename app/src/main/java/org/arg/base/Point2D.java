package org.arg.base;

public class Point2D implements Cloneable{
    private final double x;
    private final double y;
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public Point2D mover(double x, double y) {
        Point2D tempo= new Point2D(this.x+x,this.y+y);
        return tempo;
    }
    public Point2D setX(double x) {
        Point2D tempo= new Point2D(x,y);
        return tempo;
    }
    public Point2D setY(double y) {
        Point2D tempo= new Point2D(x,y);
        return tempo;
    }
    @Override
    public Object clone(){
        return new Point2D(this.x, this.y);
    }
}
