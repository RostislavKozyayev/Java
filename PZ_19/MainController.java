package com.example.app_javafx.controller;

import com.example.app_javafx.api.ApiManager;
import com.example.app_javafx.model.Character;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private ListView<Character> characterListView;
    @FXML private Button fetchButton;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label statusLabel;
    @FXML private Label detailsName;
    @FXML private Label detailsStatus;
    @FXML private Label detailsSpecies;
    @FXML private ImageView characterImage;

    private final ApiManager apiManager = new ApiManager();
    private final ObservableList<Character> characterData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Привязываем ObservableList к ListView
        characterListView.setItems(characterData);

        // Настраиваем слушатель для выбора элемента в списке
        characterListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        showCharacterDetails(newVal);
                    }
                }
        );
    }

    @FXML
    private void handleFetchCharacters() {
        // Создаем задачу для выполнения в фоновом потоке
        Task<List<Character>> fetchTask = new Task<>() {
            @Override
            protected List<Character> call() throws Exception {
                updateMessage("Загрузка данных...");
                return apiManager.fetchCharacters();
            }

            @Override
            protected void succeeded() {
                List<Character> results = getValue();
                characterData.setAll(results);

                // Обновляем UI в основном потоке
                fetchButton.setText("Загрузить снова");
                statusLabel.setText("Загружено персонажей: " + results.size());
                progressIndicator.setVisible(false);

                // Автоматически выбираем первого персонажа
                if (!characterData.isEmpty()) {
                    characterListView.getSelectionModel().selectFirst();
                }
            }

            @Override
            protected void failed() {
                Throwable exception = getException();
                fetchButton.setDisable(false);

                // Показываем ошибку пользователю
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка загрузки");
                alert.setHeaderText("Не удалось загрузить данные");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
        };

        // Блокируем кнопку и показываем индикатор загрузки
        fetchButton.setDisable(true);

        // Запускаем задачу в отдельном потоке
        Thread thread = new Thread(fetchTask);
        thread.setDaemon(true); // Поток завершится при завершении приложения
        thread.start();
    }

    private void showCharacterDetails(Character character) {
        if (character == null) return;

        // Обновляем текстовые данные
        detailsName.setText(character.getName());
        detailsStatus.setText("Статус: " + character.getStatus());
        detailsSpecies.setText("Вид: " + character.getSpecies());

        // Загружаем изображение асинхронно
        loadCharacterImage(character.getImageUrl());
    }

    private void loadCharacterImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            characterImage.setImage(null);
            return;
        }

        // Создаем задачу для загрузки изображения
        Task<Image> imageTask = new Task<>() {
            @Override
            protected Image call() {
                return new Image(imageUrl, true); // true = загрузка в фоне
            }

            @Override
            protected void succeeded() {
                characterImage.setImage(getValue());
            }

            @Override
            protected void failed() {
                characterImage.setImage(null);
                System.err.println("Не удалось загрузить изображение: " + getException().getMessage());
            }
        };

        // Запускаем загрузку изображения
        Thread imageThread = new Thread(imageTask);
        imageThread.setDaemon(true);
        imageThread.start();
    }
}