package com.example.pz_db.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {
    // ПРОСТАЯ версия - уберите socketFactory
    private static final String URL = "jdbc:mysql://127.0.1.27:3306/inventory_db?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL драйвер загружен");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL драйвер не найден!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("Подключение к MySQL...");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}