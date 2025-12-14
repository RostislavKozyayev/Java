package com.example.app_javafx;

import com.example.app_javafx.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.Properties;

public class AuthApp extends Application {

    private static final String USERS_FILE = "users.properties";
    private static Properties users = new Properties();
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        loadUsers();
        showLoginView();
    }

    // Загрузка пользователей из файла
    private static void loadUsers() {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                users.load(fis);
                System.out.println("Пользователи загружены: " + users.size());
            } catch (IOException e) {
                System.err.println("Ошибка загрузки пользователей: " + e.getMessage());
            }
        }
    }

    // Сохранение пользователей в файл
    private static void saveUsers() {
        try (FileOutputStream fos = new FileOutputStream(USERS_FILE)) {
            users.store(fos, "Список пользователей");
            System.out.println("Пользователи сохранены");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения пользователей: " + e.getMessage());
        }
    }

    // Аутентификация пользователя
    public static boolean authenticate(String username, String password) {
        String storedPassword = users.getProperty(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    // Регистрация нового пользователя
    public static boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // Пользователь уже существует
        }
        users.setProperty(username, password);
        saveUsers();
        return true;
    }

    // Показать экран входа
    public static void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    AuthApp.class.getResource("login.fxml")
            );
            Scene scene = new Scene(loader.load(), 400, 300);
            primaryStage.setTitle("Вход в систему");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Показать экран регистрации
    public static void showRegistrationView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    AuthApp.class.getResource("registration.fxml")
            );
            Scene scene = new Scene(loader.load(), 400, 350);
            primaryStage.setTitle("Регистрация");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Показать главный экран
    public static void showMainView(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    AuthApp.class.getResource("main.fxml")
            );
            Scene scene = new Scene(loader.load(), 400, 300);

            // Получаем контроллер и передаем имя пользователя
            MainController controller = loader.getController();
            controller.setWelcomeMessage(username);

            primaryStage.setTitle("Главный экран");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}