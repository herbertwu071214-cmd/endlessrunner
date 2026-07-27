package com.herb.endlessrunner.model;

public class Coin {
    private double x;
    private int lane;
    private boolean collected;
    private double yOffset;

    public Coin(double x, int lane) {
        this.x = x;
        this.lane = lane;
        this.collected = false;
        this.yOffset = Math.random() * 30 - 15;
    }

    public void update(double deltaTime, double speed) {
        x -= speed * deltaTime;
        if (x < -50) collected = true;
    }

    public double getX() { return x; }
    public int getLane() { return lane; }
    public boolean isCollected() { return collected; }
    public void setCollected(boolean collected) { this.collected = collected; }
    public double getYOffset() { return yOffset; }
}
