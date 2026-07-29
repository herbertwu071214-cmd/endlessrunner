package com.herb.endlessrunner;

import com.herb.endlessrunner.controller.GameController;
import com.herb.endlessrunner.model.GameModel;
import com.herb.endlessrunner.view.GameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);

        GameModel model = new GameModel();
        GameView view = new GameView(canvas);
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setTitle("Endless Runner");
        primaryStage.setScene(scene);
        primaryStage.show();

        GameController controller = new GameController(model, view, scene);
        controller.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
