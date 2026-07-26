package com.herb.macondo.endlessrunner.model;

public class GameModel {
    private int score;
    private int highScore;
    private boolean gameOver;
    private int playerLane;
    private double playerY;
    private boolean isJumping;
    private boolean isSliding;

    public GameModel() {
        resetGame();
    }

    public void resetGame() {
        score = 0;
        gameOver = false;
        playerLane = 1;
        playerY = 400;
        isJumping = false;
        isSliding = false;
    }

    public int getScore() { return score; }
    public int getHighScore() { return highScore; }
    public boolean isGameOver() { return gameOver; }
    public int getPlayerLane() { return playerLane; }
    public double getPlayerY() { return playerY; }
    public boolean isJumping() { return isJumping; }
    public boolean isSliding() { return isSliding; }

    public void setPlayerLane(int lane) { playerLane = lane; }
    public void setPlayerY(double y) { playerY = y; }
    public void setJumping(boolean jumping) { isJumping = jumping; }
    public void setSliding(boolean sliding) { isSliding = sliding; }
    public void setGameOver(boolean over) { gameOver = over; }
    public void addScore(int points) { score += points; }
    public void setHighScore(int high) { highScore = high; }
}
