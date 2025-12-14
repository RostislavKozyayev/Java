package com.example.app_javafx.controller;

import com.example.app_javafx.AuthApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML private Label welcomeLabel;

    // Метод для установки приветственного сообщения
    public void setWelcomeMessage(String username) {
        welcomeLabel.setText("Добро пожаловать, " + username + "!");
    }

    @FXML
    private void onLogoutButtonClick() {
        AuthApp.showLoginView();
    }
}