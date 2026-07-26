package com.herb.macondo.endlessrunner.view;

import com.herb.macondo.endlessrunner.model.GameModel;
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

        double playerX = model.getPlayerLane() * laneWidth + laneWidth/2 - 20;
        double playerY = model.getPlayerY() - 30;
        gc.setFill(Color.BLUE);
        gc.fillRect(playerX, playerY, 40, 40);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(0, model.getPlayerY() + 10, canvas.getWidth(), model.getPlayerY() + 10);

        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + model.getScore(), 20, 30);
        gc.fillText("High: " + model.getHighScore(), 20, 60);
    }

    public double getCanvasWidth() { return canvas.getWidth(); }
    public double getCanvasHeight() { return canvas.getHeight(); }
}
