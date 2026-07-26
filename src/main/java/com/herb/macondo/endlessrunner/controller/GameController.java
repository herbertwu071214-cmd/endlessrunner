package com.herb.macondo.endlessrunner.controller;

import com.herb.macondo.endlessrunner.model.GameModel;
import com.herb.macondo.endlessrunner.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;

public class GameController {
    private GameModel model;
    private GameView view;
    private AnimationTimer gameLoop;

    public GameController(GameModel model, GameView view, Scene scene) {
        this.model = model;
        this.view = view;
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
    }
}
