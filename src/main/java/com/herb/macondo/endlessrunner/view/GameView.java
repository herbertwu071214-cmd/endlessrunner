package com.herb.endlessrunner.view;

import com.herb.endlessrunner.model.GameModel;
import com.herb.endlessrunner.model.Obstacle;
import com.herb.endlessrunner.model.Coin;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameView {
    private Canvas canvas;

    public GameView(Canvas canvas) {
        this.canvas = canvas;
    }

    public void render(GameModel model) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double laneWidth = canvas.getWidth() / 3;

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        for (int i = 1; i < 3; i++) {
            double x = i * laneWidth;
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }

        for (Obstacle o : model.getObstacles()) {
            double laneX = o.getLane() * laneWidth + laneWidth/2 - o.getWidth()/2;
            double y = model.getPlayerY() + 10 - o.getHeight();

            if (o.getType() == Obstacle.ObstacleType.GROUND) {
                gc.setFill(Color.RED);
                gc.fillRect(laneX, y, o.getWidth(), o.getHeight());
            } else if (o.getType() == Obstacle.ObstacleType.LOW) {
                gc.setFill(Color.ORANGE);
                gc.fillRect(laneX, y + 20, o.getWidth(), o.getHeight());
            } else {
                gc.setFill(Color.PURPLE);
                gc.fillRect(laneX, y - 20, o.getWidth(), o.getHeight());
            }
        }

        for (Coin c : model.getCoins()) {
            double laneX = c.getLane() * laneWidth + laneWidth/2 - 12;
            double y = model.getPlayerY() - 10 + c.getYOffset();
            gc.setFill(Color.GOLD);
            gc.fillOval(laneX, y, 24, 24);
            gc.setFill(Color.YELLOW);
            gc.fillOval(laneX + 4, y + 4, 16, 16);
        }

        double playerX = model.getPlayerLane() * laneWidth + laneWidth/2 - model.getPlayerWidth()/2;
        double playerY = model.getPlayerY() - model.getPlayerHeight();
        double w = model.getPlayerWidth();
        double h = model.getPlayerHeight();
        if (model.isSliding()) {
            h = 20;
            playerY = model.getPlayerY() - h;
        }
        gc.setFill(Color.BLUE);
        gc.fillRect(playerX, playerY, w, h);
        gc.setFill(Color.CYAN);
        gc.fillRect(playerX + 5, playerY + 5, 10, 10);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(0, model.getPlayerY() + 10, canvas.getWidth(), model.getPlayerY() + 10);

        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + model.getScore(), 20, 30);
        gc.fillText("High: " + model.getHighScore(), 20, 60);
        gc.fillText("Speed: " + (int)(model.getSpeed() / 10) + " km/h", 20, 90);

        if (model.isGameOver()) {
            gc.setFill(Color.rgb(0, 0, 0, 0.7));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            gc.setFill(Color.RED);
            gc.setFont(javafx.scene.text.Font.font("Arial", 48));
            gc.fillText("💀 GAME OVER", canvas.getWidth()/2 - 160, canvas.getHeight()/2 - 30);

            gc.setFill(Color.WHITE);
            gc.setFont(javafx.scene.text.Font.font("Arial", 24));
            gc.fillText("Score: " + model.getScore(), canvas.getWidth()/2 - 60, canvas.getHeight()/2 + 40);
            gc.fillText("High Score: " + model.getHighScore(), canvas.getWidth()/2 - 80, canvas.getHeight()/2 + 80);

            gc.setFill(Color.LIGHTGRAY);
            gc.setFont(javafx.scene.text.Font.font("Arial", 18));
            gc.fillText("Press 'R' to restart", canvas.getWidth()/2 - 80, canvas.getHeight()/2 + 130);
        }
    }

    public double getCanvasWidth() { return canvas.getWidth(); }
    public double getCanvasHeight() { return canvas.getHeight(); }
}
