package com.example.pz_db.controller;

import com.example.pz_db.model.Product;
import com.example.pz_db.service.InventoryService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

public class InventoryController {
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, String> supplierColumn;

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> supplierComboBox;

    @FXML private Label statusLabel;
    @FXML private Label totalValueLabel;

    private final InventoryService inventoryService = new InventoryService();

    @FXML
    public void initialize() {
        setupTableColumns();
        setupTableSelectionListener();
        loadComboBoxData();
        loadProducts();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));

        // Форматирование цены
        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));
                    setText(format.format(price));
                }
            }
        });
    }

    private void setupTableSelectionListener() {
        productTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        fillFormForEdit(newValue);
                    }
                });
    }

    private void loadComboBoxData() {
        try {
            ObservableList<String> categories = inventoryService.loadCategoryNames();
            categoryComboBox.setItems(categories);

            ObservableList<String> suppliers = inventoryService.loadSupplierNames();
            supplierComboBox.setItems(suppliers);
        } catch (SQLException e) {
            showError("Ошибка загрузки данных: " + e.getMessage());
        }
    }

    @FXML
    private void loadProducts() {
        try {
            ObservableList<Product> products = inventoryService.loadAllProducts();
            productTable.setItems(products);
            updateTotalValue();
            showSuccess("Данные загружены успешно");
        } catch (SQLException e) {
            showError("Ошибка загрузки товаров: " + e.getMessage());
        }
    }

    @FXML
    private void fillFormForEdit(Product product) {
        idField.setText(String.valueOf(product.getProductId()));
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
        categoryComboBox.setValue(product.getCategoryName());
        supplierComboBox.setValue(product.getSupplierName());
    }

    @FXML
    private void handleCreate() {
        try {
            Product product = collectProductFromForm(true);
            if (product != null) {
                if (inventoryService.createProduct(product)) {
                    showSuccess("Товар успешно создан!");
                    loadProducts();
                    clearForm();
                } else {
                    showError("Ошибка при создании товара");
                }
            }
        } catch (SQLException e) {
            showError("Ошибка создания товара: " + e.getMessage());
        } catch (NumberFormatException e) {
            showError("Ошибка формата чисел: проверьте цену и количество");
        }
    }

    @FXML
    private void handleUpdate() {
        if (idField.getText().isEmpty()) {
            showError("Выберите товар для обновления!");
            return;
        }

        try {
            Product product = collectProductFromForm(false);
            if (product != null) {
                if (inventoryService.updateProduct(product)) {
                    showSuccess("Товар успешно обновлен!");
                    loadProducts();
                    clearForm();
                } else {
                    showError("Ошибка при обновлении товара");
                }
            }
        } catch (SQLException e) {
            showError("Ошибка обновления товара: " + e.getMessage());
        } catch (NumberFormatException e) {
            showError("Ошибка формата чисел: проверьте цену и количество");
        }
    }

    @FXML
    private void handleDelete() {
        if (idField.getText().isEmpty()) {
            showError("Выберите товар для удаления!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Удалить товар?");
        alert.setContentText("Вы уверены, что хотите удалить выбранный товар?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                int productId = Integer.parseInt(idField.getText());
                if (inventoryService.deleteProduct(productId)) {
                    showSuccess("Товар успешно удален!");
                    loadProducts();
                    clearForm();
                } else {
                    showError("Ошибка при удалении товара");
                }
            } catch (SQLException e) {
                showError("Ошибка удаления товара: " + e.getMessage());
            } catch (NumberFormatException e) {
                showError("Неверный формат ID!");
            }
        }
    }

    private Product collectProductFromForm(boolean isNew) throws SQLException {
        // Валидация
        if (nameField.getText().trim().isEmpty()) {
            showError("Введите название товара!");
            return null;
        }

        if (categoryComboBox.getValue() == null) {
            showError("Выберите категорию!");
            return null;
        }

        try {
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            if (price <= 0) {
                showError("Цена должна быть больше 0!");
                return null;
            }

            if (quantity < 0) {
                showError("Количество не может быть отрицательным!");
                return null;
            }

            Product product = new Product();

            if (!isNew) {
                product.setProductId(Integer.parseInt(idField.getText()));
            }

            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);

            // Получаем ID категории и поставщика
            String categoryName = categoryComboBox.getValue();
            int categoryId = inventoryService.getCategoryIdByName(categoryName);
            product.setCategoryId(categoryId);
            product.setCategoryName(categoryName);

            if (supplierComboBox.getValue() != null && !supplierComboBox.getValue().isEmpty()) {
                String supplierName = supplierComboBox.getValue();
                int supplierId = inventoryService.getSupplierIdByName(supplierName);
                product.setSupplierId(supplierId);
                product.setSupplierName(supplierName);
            } else {
                product.setSupplierId(0);
                product.setSupplierName("");
            }

            return product;

        } catch (NumberFormatException e) {
            throw new NumberFormatException("Проверьте правильность ввода чисел!");
        }
    }

    @FXML
    private void clearForm() {
        idField.clear();
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        supplierComboBox.getSelectionModel().clearSelection();
        statusLabel.setText("Форма очищена");
        statusLabel.setStyle("");
    }

    private void updateTotalValue() {
        try {
            BigDecimal total = inventoryService.getTotalStockValue();

            // ВАЖНО: Проверяем, что total не null
            if (total != null) {
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));
                totalValueLabel.setText("Общая стоимость: " + format.format(total));
            } else {
                totalValueLabel.setText("Общая стоимость: 0,00 ₽");
            }
        } catch (SQLException e) {
            totalValueLabel.setText("Общая стоимость: ошибка расчета");
            System.err.println("Ошибка расчета общей стоимости: " + e.getMessage());
        }
    }

    @FXML
    private void handleShowAnalytics() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pz_db/analytics-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Сводная аналитика");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            showError("Ошибка загрузки аналитики: " + e.getMessage());
        }
    }

    private void showSuccess(String message) {
        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setText(message);
    }

    private void showError(String message) {
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setText(message);
    }
}