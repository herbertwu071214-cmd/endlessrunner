package com.herb.endlessrunner.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameModel {
    public static final double PLAYER_GROUND_Y = 400;
    public static final double PLAYER_TRACK_X = 150;
    public static final double SPAWN_X = 960;

    private static final double GRAVITY = 620;
    private static final double JUMP_POWER = -430;
    private static final double LANE_MOVE_SPEED = 7.5;
    private static final double SLIDE_TIME = 0.7;
    private static final double NORMAL_PLAYER_HEIGHT = 40;
    private static final double SLIDING_PLAYER_HEIGHT = 20;
    private static final double HIT_RANGE = 38;

    private int score;
    private int highScore;
    private boolean gameOver;
    private int playerLane;
    private int targetLane;
    private double playerLanePosition;
    private double playerY;
    private double velocityY;
    private boolean isJumping;
    private boolean isSliding;
    private double slideTimer;
    private double playerWidth = 40;
    private double playerHeight = NORMAL_PLAYER_HEIGHT;

    private List<Obstacle> obstacles;
    private List<Coin> coins;
    private double spawnTimer;
    private double coinSpawnTimer;
    private double speed;
    private double distance;
    private int lastObstacleLane;
    private final Random random = new Random();

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
        playerLanePosition = 1;
        playerY = PLAYER_GROUND_Y;
        velocityY = 0;
        isJumping = false;
        isSliding = false;
        slideTimer = 0;
        obstacles.clear();
        coins.clear();
        spawnTimer = 0;
        coinSpawnTimer = 0;
        speed = 340;
        distance = 0;
        lastObstacleLane = 1;
        spawnCoinTrail(SPAWN_X + 120, 1, 5, 70, -82);
    }

    public void update(double deltaTime) {
        if (gameOver) {
            return;
        }

        if (isJumping) {
            velocityY += GRAVITY * deltaTime;
            playerY += velocityY * deltaTime;
            if (playerY >= PLAYER_GROUND_Y) {
                playerY = PLAYER_GROUND_Y;
                isJumping = false;
                velocityY = 0;
            }
        }

        if (isSliding) {
            slideTimer -= deltaTime;
            if (slideTimer <= 0) {
                isSliding = false;
                playerHeight = NORMAL_PLAYER_HEIGHT;
            }
        }

        if (Math.abs(playerLanePosition - targetLane) > 0.01) {
            double direction = Math.signum(targetLane - playerLanePosition);
            playerLanePosition += direction * deltaTime * LANE_MOVE_SPEED;
            if ((direction > 0 && playerLanePosition >= targetLane)
                    || (direction < 0 && playerLanePosition <= targetLane)) {
                playerLanePosition = targetLane;
            }
            playerLane = (int)Math.round(playerLanePosition);
        } else {
            playerLanePosition = targetLane;
            playerLane = targetLane;
        }

        distance += speed * deltaTime;
        score = (int)distance / 10;
        speed += deltaTime * 10;
        if (speed > 800) speed = 800;

        spawnTimer += deltaTime;
        double spawnInterval = Math.max(0.95, 2.15 - speed / 520);
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0;
            spawnPattern();
        }

        coinSpawnTimer += deltaTime;
        if (coinSpawnTimer >= 1.2) {
            coinSpawnTimer = 0;
            spawnCoinTrail(SPAWN_X + random.nextInt(80), random.nextInt(3), 4 + random.nextInt(3), 62, -82);
        }

        Iterator<Obstacle> obsIt = obstacles.iterator();
        while (obsIt.hasNext()) {
            Obstacle o = obsIt.next();
            o.update(deltaTime, speed);
            if (!o.isActive()) {
                obsIt.remove();
            }
        }

        Iterator<Coin> coinIt = coins.iterator();
        while (coinIt.hasNext()) {
            Coin c = coinIt.next();
            c.update(deltaTime, speed);
            if (c.isCollected()) {
                coinIt.remove();
            }
        }

        checkCollisions();
        collectCoins();
    }

    private void spawnObstacle() {
        int lane = random.nextInt(3);
        if (lane == lastObstacleLane && random.nextDouble() < 0.55) {
            lane = (lane + (random.nextBoolean() ? 1 : 2)) % 3;
        }
        lastObstacleLane = lane;
        Obstacle.ObstacleType type;
        double r = random.nextDouble();
        if (r < 0.4) {
            type = Obstacle.ObstacleType.GROUND;
        } else if (r < 0.7) {
            type = Obstacle.ObstacleType.LOW;
        } else {
            type = Obstacle.ObstacleType.HIGH;
        }
        obstacles.add(new Obstacle(SPAWN_X, lane, type));
    }

    private void spawnPattern() {
        double r = random.nextDouble();
        if (r < 0.55) {
            spawnObstacle();
        } else if (r < 0.82) {
            int blockedA = random.nextInt(3);
            int blockedB = (blockedA + 1 + random.nextInt(2)) % 3;
            obstacles.add(new Obstacle(SPAWN_X, blockedA, Obstacle.ObstacleType.GROUND));
            obstacles.add(new Obstacle(SPAWN_X + 90, blockedB, random.nextBoolean()
                    ? Obstacle.ObstacleType.LOW
                    : Obstacle.ObstacleType.HIGH));
            lastObstacleLane = blockedB;
        } else {
            int lane = random.nextInt(3);
            obstacles.add(new Obstacle(SPAWN_X, lane, Obstacle.ObstacleType.HIGH));
            spawnCoinTrail(SPAWN_X + 70, lane, 4, 62, -140);
            lastObstacleLane = lane;
        }
    }

    private void spawnCoin() {
        int lane = random.nextInt(3);
        coins.add(new Coin(SPAWN_X, lane, -82));
    }

    private void spawnCoinTrail(double startX, int lane, int count, double spacing, double yOffset) {
        for (int i = 0; i < count; i++) {
            coins.add(new Coin(startX + i * spacing, lane, yOffset));
        }
    }

    private void checkCollisions() {
        for (Obstacle o : obstacles) {
            if (o.getLane() != playerLane) {
                continue;
            }

            if (Math.abs(o.getX() - PLAYER_TRACK_X) > HIT_RANGE) {
                continue;
            }

            if (hitsPlayer(o)) {
                gameOver = true;
                if (score > highScore) highScore = score;
                return;
            }
        }
    }

    private boolean hitsPlayer(Obstacle obstacle) {
        if (obstacle.getType() == Obstacle.ObstacleType.GROUND) {
            return true;
        }

        if (obstacle.getType() == Obstacle.ObstacleType.LOW) {
            return playerY > PLAYER_GROUND_Y - 72;
        }

        return !isSliding || isJumping;
    }

    private void collectCoins() {
        Iterator<Coin> coinIt = coins.iterator();
        while (coinIt.hasNext()) {
            Coin c = coinIt.next();
            if (c.getLane() == playerLane) {
                if (Math.abs(c.getX() - PLAYER_TRACK_X) > 42) {
                    continue;
                }
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
            velocityY = JUMP_POWER;
        }
    }

    public void slide() {
        if (!isJumping && !isSliding && !gameOver) {
            isSliding = true;
            slideTimer = SLIDE_TIME;
            playerHeight = SLIDING_PLAYER_HEIGHT;
        }
    }

    public void moveLeft() {
        if (!gameOver && targetLane > 0) {
            targetLane = targetLane - 1;
        }
    }

    public void moveRight() {
        if (!gameOver && targetLane < 2) {
            targetLane = targetLane + 1;
        }
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getPlayerLane() {
        return playerLane;
    }

    public double getPlayerLanePosition() {
        return playerLanePosition;
    }

    public double getPlayerY() {
        return playerY;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isSliding() {
        return isSliding;
    }

    public double getPlayerWidth() {
        return playerWidth;
    }

    public double getPlayerHeight() {
        return playerHeight;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDistance() {
        return distance;
    }

    public void setGameOver(boolean over) {
        gameOver = over;
    }

    public void setHighScore(int high) {
        highScore = high;
    }
}
