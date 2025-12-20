package com.example.pz_db.controller;

import com.example.pz_db.model.CategorySummary;
import com.example.pz_db.service.InventoryService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AnalyticsController {
    @FXML private Label totalValueLabel;
    @FXML private Label statusLabel;
    @FXML private TableView<CategorySummary> categoryTable;
    @FXML private TableColumn<CategorySummary, String> categoryNameColumn;
    @FXML private TableColumn<CategorySummary, Integer> totalQuantityColumn;

    private final InventoryService inventoryService = new InventoryService();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadAnalyticsData();
    }

    private void setupTableColumns() {
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        totalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
    }

    private void loadAnalyticsData() {
        try {
            // Расчет общей стоимости
            BigDecimal totalValue = inventoryService.getTotalStockValue();
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));
            totalValueLabel.setText("Общая стоимость товаров на складе: " + format.format(totalValue));

            // Загрузка сводки по категориям
            List<CategorySummary> summaries = inventoryService.getCategoryQuantities();
            categoryTable.getItems().setAll(summaries);

            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Аналитические данные загружены успешно");

        } catch (SQLException e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Ошибка загрузки аналитики: " + e.getMessage());
        }
    }
}