package com.example.app_javafx.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VisualizerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                VisualizerApp.class.getResource("/com/example/app_javafx/visualizer-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 650, 550);

        primaryStage.setTitle("Визуализатор числовых данных");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}