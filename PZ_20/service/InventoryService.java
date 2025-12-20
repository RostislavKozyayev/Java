package com.example.pz_db.service;

import com.example.pz_db.config.DBConfig;
import com.example.pz_db.model.CategorySummary;
import com.example.pz_db.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {

    // Загрузка всех товаров с JOIN
    public ObservableList<Product> loadAllProducts() throws SQLException {
        ObservableList<Product> products = FXCollections.observableArrayList();

        // Сначала проверим подключение
        System.out.println("Загрузка продуктов из БД...");

        String sql = "SELECT " +
                "p.product_id, p.name, p.price, p.quantity, " +
                "p.category_id, p.supplier_id, " +
                "c.category_name, s.supplier_name " +
                "FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.category_id " +
                "LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("SQL запрос выполнен: " + sql);

            int count = 0;
            while (rs.next()) {
                count++;
                Product product = new Product();

                // Основные поля
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setSupplierId(rs.getInt("supplier_id"));

                // Имена из связанных таблиц
                product.setCategoryName(rs.getString("category_name"));
                product.setSupplierName(rs.getString("supplier_name"));

                products.add(product);

                System.out.println("Загружен товар #" + count + ": " +
                        product.getName() + " (ID: " + product.getProductId() + ")");
            }

            System.out.println("Всего загружено товаров: " + count);

        } catch (SQLException e) {
            System.err.println("Ошибка загрузки продуктов: " + e.getMessage());
            throw e;
        }

        return products;
    }

    // Создание товара
    public boolean createProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Products (name, price, quantity, category_id, supplier_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());

            if (product.getCategoryId() > 0) {
                stmt.setInt(4, product.getCategoryId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            if (product.getSupplierId() > 0) {
                stmt.setInt(5, product.getSupplierId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;
        }
    }

    // Обновление товара
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE Products SET name = ?, price = ?, quantity = ?, " +
                "category_id = ?, supplier_id = ? " +
                "WHERE product_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());

            if (product.getCategoryId() > 0) {
                stmt.setInt(4, product.getCategoryId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            if (product.getSupplierId() > 0) {
                stmt.setInt(5, product.getSupplierId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setInt(6, product.getProductId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Удаление товара
    public boolean deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM Products WHERE product_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Загрузка имен категорий для ComboBox
    public ObservableList<String> loadCategoryNames() throws SQLException {
        ObservableList<String> categories = FXCollections.observableArrayList();
        String sql = "SELECT category_name FROM Categories ORDER BY category_name";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        }
        return categories;
    }

    // Загрузка имен поставщиков для ComboBox
    public ObservableList<String> loadSupplierNames() throws SQLException {
        ObservableList<String> suppliers = FXCollections.observableArrayList();
        String sql = "SELECT supplier_name FROM Suppliers ORDER BY supplier_name";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers.add(rs.getString("supplier_name"));
            }
        }
        return suppliers;
    }

    // Получение ID категории по имени
    public int getCategoryIdByName(String name) throws SQLException {
        if (name == null || name.isEmpty()) return 0;

        String sql = "SELECT category_id FROM Categories WHERE category_name = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? rs.getInt("category_id") : 0;
        }
    }

    // Получение ID поставщика по имени
    public int getSupplierIdByName(String name) throws SQLException {
        if (name == null || name.isEmpty()) return 0;

        String sql = "SELECT supplier_id FROM Suppliers WHERE supplier_name = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? rs.getInt("supplier_id") : 0;
        }
    }

    // Расчет общей стоимости товаров
    public BigDecimal getTotalStockValue() throws SQLException {
        String sql = "SELECT SUM(price * quantity) as total_value FROM Products";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return rs.next() ? rs.getBigDecimal("total_value") : BigDecimal.ZERO;
        }
    }

    // Сводка по категориям
    public List<CategorySummary> getCategoryQuantities() throws SQLException {
        List<CategorySummary> summaries = new ArrayList<>();
        String sql = "SELECT c.category_name, COALESCE(SUM(p.quantity), 0) as total_quantity " +
                "FROM Categories c " +
                "LEFT JOIN Products p ON c.category_id = p.category_id " +
                "GROUP BY c.category_id, c.category_name " +
                "ORDER BY total_quantity DESC";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                summaries.add(new CategorySummary(
                        rs.getString("category_name"),
                        rs.getInt("total_quantity")
                ));
            }
        }
        return summaries;
    }
}