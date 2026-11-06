package org.arg.base;

public class Speed implements Cloneable {
    private final double vx;
    private final double vy;
    public Speed(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }
    public double getVX() {
        return vx;
    }
    public double getVY() {
        return vy;
    }
    @Override
    public Object clone(){
        return new Speed(this.vx, this.vy);
    }



}
