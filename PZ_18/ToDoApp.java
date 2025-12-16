package com.example.app_javafx.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ToDoApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                ToDoApp.class.getResource("/com/example/app_javafx/todo-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 450, 400);

        primaryStage.setTitle("Менеджер задач");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}