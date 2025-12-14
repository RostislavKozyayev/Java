package com.example.app_javafx.controller;

import com.example.app_javafx.AuthApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private Button loginButton;

    @FXML
    private void onLoginButtonClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Проверка на пустые поля
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Заполните все поля!", Color.RED);
            return;
        }

        // Попытка аутентификации
        if (AuthApp.authenticate(username, password)) {
            showStatus("Вход выполнен успешно!", Color.GREEN);
            clearFields();
            AuthApp.showMainView(username);
        } else {
            showStatus("Неверный логин или пароль!", Color.RED);
        }
    }

    @FXML
    private void onRegisterLinkClick() {
        clearFields();
        AuthApp.showRegistrationView();
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(color);
        statusLabel.setVisible(true);
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        statusLabel.setVisible(false);
    }
}