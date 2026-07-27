package com.herb.endlessrunner.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameModel {
    private int score;
    private int highScore;
    private boolean gameOver;
    private int playerLane;
    private int targetLane;
    private double playerY;
    private double velocityY;
    private boolean isJumping;
    private boolean isSliding;
    private double slideTimer;
    private double laneSwitchTimer;
    private double playerWidth = 40;
    private double playerHeight = 40;

    private List<Obstacle> obstacles;
    private List<Coin> coins;
    private double spawnTimer;
    private double coinSpawnTimer;
    private double speed;
    private double distance;

    public GameModel() {
        obstacles = new ArrayList<>();
        coins = new ArrayList<>();
        resetGame();
    }

    public void resetGame() {
        score = 0;
        gameOver = false;
        playerLane = 1;
        targetLane = 1;
        playerY = 400;
        velocityY = 0;
        isJumping = false;
        isSliding = false;
        slideTimer = 0;
        laneSwitchTimer = 0;
        obstacles.clear();
        coins.clear();
        spawnTimer = 0;
        coinSpawnTimer = 0;
        speed = 300;
        distance = 0;
    }

    public void update(double deltaTime) {
        if (gameOver) return;

        if (isJumping) {
            velocityY += 600 * deltaTime;
            playerY += velocityY * deltaTime;
            if (playerY >= 400) {
                playerY = 400;
                isJumping = false;
                velocityY = 0;
            }
        }

        if (isSliding) {
            slideTimer -= deltaTime;
            if (slideTimer <= 0) {
                isSliding = false;
                playerHeight = 40;
            }
        }

        if (playerLane != targetLane) {
            laneSwitchTimer += deltaTime;
            if (laneSwitchTimer >= 0.1) {
                laneSwitchTimer = 0;
                playerLane += (targetLane > playerLane) ? 1 : -1;
            }
        }

        distance += speed * deltaTime;
        score = (int)distance / 10;
        speed += deltaTime * 10;
        if (speed > 800) speed = 800;

        spawnTimer += deltaTime;
        double spawnInterval = Math.max(0.8, 2.0 - speed / 500);
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0;
            spawnObstacle();
        }

        coinSpawnTimer += deltaTime;
        if (coinSpawnTimer >= 1.5) {
            coinSpawnTimer = 0;
            spawnCoin();
        }

        Iterator<Obstacle> obsIt = obstacles.iterator();
        while (obsIt.hasNext()) {
            Obstacle o = obsIt.next();
            o.update(deltaTime, speed);
            if (!o.isActive()) obsIt.remove();
        }

        Iterator<Coin> coinIt = coins.iterator();
        while (coinIt.hasNext()) {
            Coin c = coinIt.next();
            c.update(deltaTime, speed);
            if (c.isCollected()) coinIt.remove();
        }

        checkCollisions();
        collectCoins();
    }

    private void spawnObstacle() {
        int lane = (int)(Math.random() * 3);
        Obstacle.ObstacleType type;
        double r = Math.random();
        if (r < 0.4) type = Obstacle.ObstacleType.GROUND;
        else if (r < 0.7) type = Obstacle.ObstacleType.LOW;
        else type = Obstacle.ObstacleType.HIGH;
        obstacles.add(new Obstacle(850, lane, type));
    }

    private void spawnCoin() {
        int lane = (int)(Math.random() * 3);
        coins.add(new Coin(850, lane));
    }

    private void checkCollisions() {
        for (Obstacle o : obstacles) {
            if (o.getLane() != playerLane) continue;

            double playerBottom = playerY + 10;
            double playerTop = playerY + 10 - playerHeight;

            double obsBottom = playerY + 10;
            double obsTop = playerY + 10 - o.getHeight();

            if (o.getType() == Obstacle.ObstacleType.LOW) {
                obsTop += 20;
            } else if (o.getType() == Obstacle.ObstacleType.HIGH) {
                obsTop -= 20;
                obsBottom -= 20;
            }

            if (playerTop < obsBottom && playerBottom > obsTop) {
                gameOver = true;
                if (score > highScore) highScore = score;
                return;
            }
        }
    }

    private void collectCoins() {
        Iterator<Coin> coinIt = coins.iterator();
        while (coinIt.hasNext()) {
            Coin c = coinIt.next();
            if (c.getLane() == playerLane) {
                double coinCenterY = playerY - 10 + c.getYOffset();
                double playerTop = playerY + 10 - playerHeight;
                double playerBottom = playerY + 10;
                double coinTop = coinCenterY - 12;
                double coinBottom = coinCenterY + 12;

                if (playerTop < coinBottom && playerBottom > coinTop) {
                    c.setCollected(true);
                    score += 10;
                    coinIt.remove();
                }
            }
        }
    }

    public void jump() {
        if (!isJumping && !isSliding && !gameOver) {
            isJumping = true;
            velocityY = -350;
        }
    }

    public void slide() {
        if (!isJumping && !isSliding && !gameOver) {
            isSliding = true;
            slideTimer = 0.5;
            playerHeight = 20;
        }
    }

    public void moveLeft() {
        if (!gameOver && playerLane > 0 && playerLane == targetLane) {
            targetLane = playerLane - 1;
            laneSwitchTimer = 0;
        }
    }

    public void moveRight() {
        if (!gameOver && playerLane < 2 && playerLane == targetLane) {
            targetLane = playerLane + 1;
            laneSwitchTimer = 0;
        }
    }

    public int getScore() { return score; }
    public int getHighScore() { return highScore; }
    public boolean isGameOver() { return gameOver; }
    public int getPlayerLane() { return playerLane; }
    public double getPlayerY() { return playerY; }
    public boolean isJumping() { return isJumping; }
    public boolean isSliding() { return isSliding; }
    public double getPlayerWidth() { return playerWidth; }
    public double getPlayerHeight() { return playerHeight; }
    public List<Obstacle> getObstacles() { return obstacles; }
    public List<Coin> getCoins() { return coins; }
    public double getSpeed() { return speed; }
    public double getDistance() { return distance; }

    public void setGameOver(boolean over) { gameOver = over; }
    public void setHighScore(int high) { highScore = high; }
}
