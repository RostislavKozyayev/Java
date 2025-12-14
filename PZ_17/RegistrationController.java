package com.example.app_javafx.controller;

import com.example.app_javafx.AuthApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class RegistrationController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label statusLabel;
    @FXML private Button registerButton;

    @FXML
    private void onRegisterButtonClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Валидация 1: Проверка на пустые поля
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showStatus("Заполните все поля!", Color.RED);
            return;
        }

        // Валидация 2: Длина пароля
        if (password.length() < 3) {
            showStatus("Пароль должен быть не менее 3 символов!", Color.ORANGE);
            return;
        }

        // Валидация 3: Совпадение паролей
        if (!password.equals(confirmPassword)) {
            showStatus("Пароли не совпадают!", Color.RED);
            return;
        }

        // Регистрация пользователя
        if (AuthApp.registerUser(username, password)) {
            showStatus("Регистрация успешна!", Color.GREEN);
            clearFields();
            // Переход на экран входа через 1 секунду
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    javafx.application.Platform.runLater(() ->
                            AuthApp.showLoginView());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            showStatus("Имя пользователя уже занято!", Color.RED);
        }
    }

    @FXML
    private void onLoginLinkClick() {
        clearFields();
        AuthApp.showLoginView();
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(color);
        statusLabel.setVisible(true);
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        statusLabel.setVisible(false);
    }
}