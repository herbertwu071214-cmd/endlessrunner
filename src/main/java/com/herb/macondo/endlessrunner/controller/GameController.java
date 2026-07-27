package com.herb.endlessrunner.controller;

import com.herb.endlessrunner.input.InputHandler;
import com.herb.endlessrunner.model.GameModel;
import com.herb.endlessrunner.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;

public class GameController {
    private GameModel model;
    private GameView view;
    private AnimationTimer gameLoop;
    private InputHandler input;

    public GameController(GameModel model, GameView view, Scene scene) {
        this.model = model;
        this.view = view;
        this.input = new InputHandler();
        attachInputHandlers(scene);
    }

    private void attachInputHandlers(Scene scene) {
        scene.setOnKeyPressed(e -> {
            input.keyPressed(e);
            handleInput();
            if (e.getCode().toString().equals("R") && model.isGameOver()) {
                model.resetGame();
            }
        });
        scene.setOnKeyReleased(e -> input.keyReleased(e));
    }

    private void handleInput() {
        if (input.isLeftPressed()) model.moveLeft();
        if (input.isRightPressed()) model.moveRight();
        if (input.isUpPressed()) model.jump();
        if (input.isDownPressed()) model.slide();
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
