package com.herb.endlessrunner.model;

public class Coin {
    private static final double OFF_SCREEN_X = -50;
    private static final double BOB_SPEED = 6;
    private static final double BOB_SIZE = 4;

    private double x;
    private int lane;
    private boolean collected;
    private double yOffset;
    private double bobTimer;

    public Coin(double x, int lane) {
        this(x, lane, 0);
    }

    public Coin(double x, int lane, double yOffset) {
        this.x = x;
        this.lane = lane;
        this.collected = false;
        this.yOffset = yOffset;
        this.bobTimer = x / 80.0;
    }

    public void update(double deltaTime, double speed) {
        x -= speed * deltaTime;
        bobTimer += deltaTime * BOB_SPEED;

        if (x < OFF_SCREEN_X) {
            collected = true;
        }
    }

    public double getX() {
        return x;
    }

    public int getLane() {
        return lane;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public double getYOffset() {
        return yOffset + Math.sin(bobTimer) * BOB_SIZE;
    }
}
