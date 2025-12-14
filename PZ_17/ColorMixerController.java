package com.example.app_javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.net.URL;
import java.util.ResourceBundle;

public class ColorMixerController implements Initializable {

    @FXML private Slider redSlider;
    @FXML private Slider greenSlider;
    @FXML private Slider blueSlider;
    @FXML private Rectangle colorRect;
    @FXML private Label redValue;
    @FXML private Label greenValue;
    @FXML private Label blueValue;
    @FXML private Label rgbValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Настройка слушателей для ползунков
        redSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateColor();
            redValue.setText(String.valueOf(newValue.intValue()));
        });

        greenSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateColor();
            greenValue.setText(String.valueOf(newValue.intValue()));
        });

        blueSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateColor();
            blueValue.setText(String.valueOf(newValue.intValue()));
        });

        // Инициализация начального цвета
        updateColor();
    }

    private void updateColor() {
        int red = (int) redSlider.getValue();
        int green = (int) greenSlider.getValue();
        int blue = (int) blueSlider.getValue();

        // Создание цвета
        Color color = Color.rgb(red, green, blue);

        // Применение цвета к прямоугольнику
        colorRect.setFill(color);

        // Обновление текста с RGB значениями
        rgbValue.setText(String.format("(%d, %d, %d)", red, green, blue));

        // Изменение цвета текста для контраста
        double brightness = 0.299 * red + 0.587 * green + 0.114 * blue;
        if (brightness > 128) {
            rgbValue.setStyle("-fx-text-fill: black; -fx-font-size: 14; -fx-font-weight: bold;");
        } else {
            rgbValue.setStyle("-fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold;");
        }
    }
}