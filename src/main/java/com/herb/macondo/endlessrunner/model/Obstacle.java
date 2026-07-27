package com.herb.endlessrunner.model;

public class Obstacle {
    public enum ObstacleType {
        GROUND,
        LOW,
        HIGH
    }

    private double x;
    private int lane;
    private ObstacleType type;
    private boolean active;
    private double width;
    private double height;

    public Obstacle(double x, int lane, ObstacleType type) {
        this.x = x;
        this.lane = lane;
        this.type = type;
        this.active = true;
        this.width = 30;
        switch(type) {
            case GROUND:
                this.height = 40;
                break;
            case LOW:
                this.height = 20;
                break;
            case HIGH:
                this.height = 40;
                break;
        }
    }

    public void update(double deltaTime, double speed) {
        x -= speed * deltaTime;
        if (x < -50) active = false;
    }

    public double getX() { return x; }
    public int getLane() { return lane; }
    public ObstacleType getType() { return type; }
    public boolean isActive() { return active; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public void setActive(boolean active) { this.active = active; }
}

