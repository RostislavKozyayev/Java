package com.example.pz_db;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Загружаем главное окно
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pz_db/inventory-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1000, 700);

            primaryStage.setTitle("Складской учет (CRUD)");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при запуске приложения: " + e.getMessage());
        }
    }

    static void main(String[] args) {
        launch(args);
    }
}