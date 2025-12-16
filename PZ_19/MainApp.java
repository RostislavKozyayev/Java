package com.example.app_javafx.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Загружаем FXML файл
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/com/example/app_javafx/main-view.fxml")
        );

        // Создаем сцену
        Scene scene = new Scene(loader.load(), 800, 600);

        // Настраиваем главное окно
        primaryStage.setTitle("Rick and Morty API Client");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("https://rickandmortyapi.com/icons/icon-512x512.png"));
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);

        // Показываем окно
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            // Решение проблем с SSL/TLS
            setupSSL();

            // Запускаем приложение
            launch(args);

        } catch (Exception e) {
            System.err.println("Ошибка запуска приложения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void setupSSL() {
        System.out.println("Настройка SSL...");

        // 1. Отключаем проверку хоста
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname, session) -> {
                    System.out.println("Подключение к хосту: " + hostname);
                    return true; // Принимаем любой хост
                }
        );

        // 2. Устанавливаем доверяющий всем TrustManager
        try {
            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[] {
                    new javax.net.ssl.X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e) {
            System.err.println("Не удалось настроить SSL: " + e.getMessage());
        }

        // 3. Системные свойства для отключения SSL проверки
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");

        System.out.println("SSL настроен");
    }
}