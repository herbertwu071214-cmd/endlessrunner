package com.herb.endlessrunner.view;

import com.herb.endlessrunner.model.Coin;
import com.herb.endlessrunner.model.GameModel;
import com.herb.endlessrunner.model.Obstacle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameView {
    private Canvas canvas;

    public GameView(Canvas canvas) {
        this.canvas = canvas;
    }

    public void render(GameModel model) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        double horizonY = 95;
        double playerGroundY = model.getPlayerY() + 10;

        drawWorld(gc, width, height, horizonY, playerGroundY, model);

        for (Coin c : model.getCoins()) {
            drawCoin(gc, c, width, horizonY, playerGroundY);
        }

        for (Obstacle o : model.getObstacles()) {
            drawObstacle(gc, o, width, horizonY, playerGroundY);
        }

        double laneWidth = width / 3;
        double playerX = model.getPlayerLanePosition() * laneWidth + laneWidth / 2 - model.getPlayerWidth() / 2;
        double playerY = model.getPlayerY() - model.getPlayerHeight();
        double w = model.getPlayerWidth();
        double h = model.getPlayerHeight();
        if (model.isSliding()) {
            h = 20;
            playerY = model.getPlayerY() - h;
        }
        drawPlayer(gc, playerX, playerY, w, h, model.isSliding());

        drawHud(gc, model);

        if (model.isGameOver()) {
            gc.setFill(Color.rgb(0, 0, 0, 0.7));
            gc.fillRect(0, 0, width, height);

            gc.setFill(Color.rgb(255, 80, 70));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            gc.fillText("GAME OVER", width / 2 - 145, height / 2 - 30);

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", 24));
            gc.fillText("Score: " + model.getScore(), width / 2 - 60, height / 2 + 40);
            gc.fillText("High Score: " + model.getHighScore(), width / 2 - 80, height / 2 + 80);

            gc.setFill(Color.LIGHTGRAY);
            gc.setFont(Font.font("Arial", 18));
            gc.fillText("Press R to restart", width / 2 - 74, height / 2 + 130);
        }
    }

    private void drawWorld(GraphicsContext gc, double width, double height, double horizonY,
                           double playerGroundY, GameModel model) {
        gc.setFill(Color.rgb(18, 24, 34));
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.rgb(32, 43, 55));
        gc.fillRect(0, horizonY, width, height - horizonY);

        double centerX = width / 2;
        double roadTopHalf = 70;
        double roadBottomHalf = width * 0.48;
        gc.setFill(Color.rgb(54, 58, 66));
        gc.fillPolygon(
                new double[]{
                        centerX - roadTopHalf,
                        centerX + roadTopHalf,
                        centerX + roadBottomHalf,
                        centerX - roadBottomHalf
                },
                new double[]{horizonY, horizonY, playerGroundY + 95, playerGroundY + 95},
                4
        );

        gc.setStroke(Color.rgb(235, 238, 224, 0.75));
        gc.setLineWidth(3);
        for (int boundary = 1; boundary < 3; boundary++) {
            double laneRatio = boundary / 3.0;
            gc.strokeLine(centerX - roadTopHalf + roadTopHalf * 2 * laneRatio, horizonY,
                    centerX - roadBottomHalf + roadBottomHalf * 2 * laneRatio, playerGroundY + 95);
        }

        gc.setStroke(Color.rgb(255, 255, 255, 0.18));
        gc.setLineWidth(2);
        double stripeOffset = (model.getDistance() % 115) / 115;
        for (int i = -1; i < 9; i++) {
            double t = (i + stripeOffset) / 8.0;
            if (t < 0 || t > 1) {
                continue;
            }

            double y = horizonY + (playerGroundY + 95 - horizonY) * t;
            double half = roadTopHalf + (roadBottomHalf - roadTopHalf) * t;
            gc.strokeLine(centerX - half, y, centerX + half, y);
        }
    }

    private void drawObstacle(GraphicsContext gc, Obstacle o, double width, double horizonY, double playerGroundY) {
        double depth = depth(o.getX());
        if (depth <= 0 || depth > 1.15) {
            return;
        }

        double scale = 0.35 + depth * 1.25;
        double x = laneCenter(o.getLane(), width, depth);
        double groundY = trackY(horizonY, playerGroundY, depth);
        double obstacleWidth = o.getWidth() * scale;
        double obstacleHeight = o.getHeight() * scale;
        double y = groundY - obstacleHeight;

        if (o.getType() == Obstacle.ObstacleType.LOW) {
            y = groundY - obstacleHeight * 0.55;
        } else if (o.getType() == Obstacle.ObstacleType.HIGH) {
            y = groundY - obstacleHeight - 42 * scale;
        }

        if (depth > 0.2 && depth < 0.82) {
            double pulse = 0.35 + Math.abs(Math.sin(o.getWarningPulse())) * 0.35;
            gc.setFill(Color.rgb(255, 78, 64, pulse));
            gc.fillOval(x - obstacleWidth * 0.75, groundY - 8 * scale, obstacleWidth * 1.5, 18 * scale);
        }

        Color body = obstacleColor(o);

        gc.setFill(Color.rgb(0, 0, 0, 0.28));
        gc.fillRoundRect(x - obstacleWidth / 2 + 5 * scale, y + 6 * scale,
                obstacleWidth, obstacleHeight, 10, 10);
        gc.setFill(body);
        gc.fillRoundRect(x - obstacleWidth / 2, y, obstacleWidth, obstacleHeight, 8, 8);
        gc.setFill(Color.rgb(255, 255, 255, 0.24));
        gc.fillRoundRect(x - obstacleWidth / 2 + 7 * scale, y + 6 * scale,
                obstacleWidth * 0.28, obstacleHeight - 12 * scale, 4, 4);

        drawObstacleSymbol(gc, o, x, y, obstacleWidth, obstacleHeight, scale);
    }

    private void drawCoin(GraphicsContext gc, Coin c, double width, double horizonY, double playerGroundY) {
        double depth = depth(c.getX());
        if (depth <= 0 || depth > 1.15) {
            return;
        }

        double scale = 0.45 + depth;
        double size = 20 * scale;
        double x = laneCenter(c.getLane(), width, depth);
        double y = trackY(horizonY, playerGroundY, depth) + c.getYOffset() * scale;
        gc.setFill(Color.rgb(0, 0, 0, 0.25));
        gc.fillOval(x - size / 2 + 3, y - size / 2 + 4, size, size * 0.6);
        gc.setFill(Color.GOLD);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
        gc.setFill(Color.rgb(255, 245, 145));
        gc.fillOval(x - size * 0.24, y - size * 0.24, size * 0.48, size * 0.48);
    }

    private void drawPlayer(GraphicsContext gc, double x, double y, double w, double h, boolean sliding) {
        gc.setFill(Color.rgb(0, 0, 0, 0.32));
        gc.fillOval(x - 7, y + h - 3, w + 14, 14);
        gc.setFill(Color.rgb(31, 122, 255));
        gc.fillRoundRect(x, y, w, h, 9, 9);
        gc.setFill(Color.rgb(103, 218, 255));
        gc.fillRoundRect(x + 7, y + 7, sliding ? 18 : 12, sliding ? 6 : 13, 5, 5);
        gc.setFill(Color.rgb(12, 35, 75));
        gc.fillRect(x + 6, y + h - 5, w - 12, 5);
    }

    private void drawHud(GraphicsContext gc, GameModel model) {
        gc.setFill(Color.rgb(0, 0, 0, 0.35));
        gc.fillRoundRect(14, 13, 188, 110, 8, 8);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gc.fillText("Score: " + model.getScore(), 26, 38);
        gc.setFont(Font.font("Arial", 15));
        gc.fillText("High: " + model.getHighScore(), 26, 63);
        gc.fillText("Speed: " + (int)(model.getSpeed() / 10) + " km/h", 26, 87);
        gc.fillText("Gets harder over time", 26, 111);
    }

    private Color obstacleColor(Obstacle obstacle) {
        if (obstacle.getType() == Obstacle.ObstacleType.GROUND) {
            return Color.rgb(226, 55, 48);
        }

        if (obstacle.getType() == Obstacle.ObstacleType.LOW) {
            return Color.rgb(255, 149, 39);
        }

        return Color.rgb(148, 83, 255);
    }

    private void drawObstacleSymbol(GraphicsContext gc, Obstacle obstacle, double x, double y,
                                    double obstacleWidth, double obstacleHeight, double scale) {
        String symbol = obstacleSymbol(obstacle);
        double badgeSize = Math.max(24, 24 * scale);
        double badgeX = x - badgeSize / 2;
        double badgeY = y + obstacleHeight / 2 - badgeSize / 2;

        if (obstacle.getType() == Obstacle.ObstacleType.HIGH) {
            badgeY = y + obstacleHeight - badgeSize * 0.86;
        }

        gc.setFill(Color.rgb(255, 255, 255, 0.92));
        gc.fillOval(badgeX, badgeY, badgeSize, badgeSize);
        gc.setStroke(Color.rgb(22, 26, 32, 0.55));
        gc.setLineWidth(Math.max(1.0, 1.5 * scale));
        gc.strokeOval(badgeX, badgeY, badgeSize, badgeSize);

        gc.setFill(Color.rgb(20, 24, 30));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, Math.max(18, 18 * scale)));
        double textX = x - (symbol.equals("X") ? 6.2 : 5.2) * scale;
        double textY = badgeY + badgeSize * 0.72;
        gc.fillText(symbol, textX, textY);
    }

    private String obstacleSymbol(Obstacle obstacle) {
        if (obstacle.getType() == Obstacle.ObstacleType.GROUND) {
            return "X";
        }

        if (obstacle.getType() == Obstacle.ObstacleType.LOW) {
            return "^";
        }

        return "v";
    }

    private double depth(double objectX) {
        return 1.0 - objectX / GameModel.SPAWN_X;
    }

    private double trackY(double horizonY, double playerGroundY, double depth) {
        return horizonY + (playerGroundY - horizonY) * Math.pow(depth, 0.82);
    }

    private double laneCenter(int lane, double width, double depth) {
        double flatLaneWidth = width / 3;
        double flatCenter = lane * flatLaneWidth + flatLaneWidth / 2;
        double perspectivePull = 1.0 - depth;
        double laneSpread = (0.25 + depth * 0.75) * (1.0 - perspectivePull * 0.08);

        return width / 2 + (flatCenter - width / 2) * laneSpread;
    }

    public double getCanvasWidth() {
        return canvas.getWidth();
    }

    public double getCanvasHeight() {
        return canvas.getHeight();
    }
}
