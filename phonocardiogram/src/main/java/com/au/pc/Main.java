package com.au.pc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(new Label("JavaFX working..."), 300, 100));
        primaryStage.setTitle("JavaFX Test");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
