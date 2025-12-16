package com.example.app_javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class VisualizerController implements Initializable {

    @FXML private TextField valueInput;
    @FXML private Button addButton;
    @FXML private Button sortAscButton;
    @FXML private Button sortDescButton;
    @FXML private LineChart<String, Number> lineChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    private ObservableList<XYChart.Data<String, Number>> rawData;
    private XYChart.Series<String, Number> series;
    private int dataCounter = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Инициализация списка данных
        rawData = FXCollections.observableArrayList();

        // Создание серии данных
        series = new XYChart.Series<>();
        series.setName("Числовые данные");
        series.setData(rawData);

        // Добавление серии на график
        lineChart.getData().add(series);

        // Настройка осей
        xAxis.setLabel("Порядковый номер");
        yAxis.setLabel("Значение");

        // Настройка графика
        lineChart.setTitle("Визуализация числовых данных");
        lineChart.setCreateSymbols(true);
        lineChart.setAnimated(false);

        // Обработка нажатия Enter в поле ввода
        valueInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addValue();
            }
        });

        // Инициализация начальными данными
        addInitialData();
    }

    private void addInitialData() {
        // Добавляем несколько начальных точек для демонстрации
        addDataPoint(10.0);
        addDataPoint(25.0);
        addDataPoint(15.0);
        addDataPoint(30.0);
        addDataPoint(20.0);
    }

    private void addDataPoint(double value) {
        String xLabel = "N" + dataCounter;
        XYChart.Data<String, Number> newData = new XYChart.Data<>(xLabel, value);
        rawData.add(newData);
        dataCounter++;
    }

    @FXML
    private void addValue() {
        try {
            double value = Double.parseDouble(valueInput.getText().trim());
            addDataPoint(value);
            valueInput.clear();
            valueInput.requestFocus();
        } catch (NumberFormatException e) {
            valueInput.setText("Ошибка! Введите число");
            valueInput.selectAll();
        }
    }

    @FXML
    private void sortAscending() {
        // Создаем компаратор для сортировки по возрастанию
        Comparator<XYChart.Data<String, Number>> comparator =
                Comparator.comparingDouble(data -> data.getYValue().doubleValue());

        // Сортируем данные
        FXCollections.sort(rawData, comparator);

        // Переиндексация X-меток
        reindexXLabels();

        System.out.println("Данные отсортированы по возрастанию");
    }

    @FXML
    private void sortDescending() {
        // Создаем компаратор для сортировки по убыванию
        Comparator<XYChart.Data<String, Number>> comparator =
                Comparator.comparingDouble(data -> -data.getYValue().doubleValue());

        // Сортируем данные
        FXCollections.sort(rawData, comparator);

        // Переиндексация X-меток
        reindexXLabels();

        System.out.println("Данные отсортированы по убыванию");
    }

    private void reindexXLabels() {
        for (int i = 0; i < rawData.size(); i++) {
            rawData.get(i).setXValue("N" + (i + 1));
        }
    }

    @FXML
    private void clearChart() {
        rawData.clear();
        dataCounter = 1;
        System.out.println("График очищен");
    }
}