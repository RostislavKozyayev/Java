package com.example.app_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ColorMixerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Используем правильный путь к ресурсу
        FXMLLoader loader = new FXMLLoader(
                ColorMixerApp.class.getResource("color_mixer.fxml")
        );

        Scene scene = new Scene(loader.load(), 400, 400);

        primaryStage.setTitle("Смеситель цветов");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}