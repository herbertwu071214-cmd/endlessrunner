package com.herb.endlessrunner.controller;

import com.herb.endlessrunner.input.InputHandler;
import com.herb.endlessrunner.model.GameModel;
import com.herb.endlessrunner.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class GameController {
    private final GameModel model;
    private final GameView view;
    private final InputHandler input;
    private AnimationTimer gameLoop;

    public GameController(GameModel model, GameView view, Scene scene) {
        this.model = model;
        this.view = view;
        this.input = new InputHandler();
        attachInputHandlers(scene);
    }

    private void attachInputHandlers(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (input.keyPressed(e)) {
                handleKey(e.getCode());
            }

            if (e.getCode() == KeyCode.R && model.isGameOver()) {
                model.resetGame();
            }
        });
        scene.setOnKeyReleased(e -> input.keyReleased(e));
    }

    private void handleKey(KeyCode code) {
        if (code == KeyCode.A || code == KeyCode.LEFT) {
            model.moveLeft();
        } else if (code == KeyCode.D || code == KeyCode.RIGHT) {
            model.moveRight();
        } else if (code == KeyCode.W || code == KeyCode.UP || code == KeyCode.SPACE) {
            model.jump();
        } else if (code == KeyCode.S || code == KeyCode.DOWN) {
            model.slide();
        }
    }

    public void start() {
        gameLoop = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                update(deltaTime);
                view.render(model);
                lastUpdate = now;
            }
        };
        gameLoop.start();
    }

    private void update(double deltaTime) {
        if (model.isGameOver()) {
            return;
        }
        model.update(deltaTime);
    }
}
