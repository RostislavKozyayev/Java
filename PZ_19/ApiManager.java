package com.example.app_javafx.api;

import com.example.app_javafx.model.Character;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class ApiManager {
    private static final String API_URL = "https://rickandmortyapi.com/api/character";
    private final HttpClient client;

    public ApiManager() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .sslContext(createSSLContext())
                .build();
    }

    private SSLContext createSSLContext() {
        try {
            // Создаем SSLContext, который доверяет всем сертификатам
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, getTrustManagers(), new java.security.SecureRandom());
            return sslContext;
        } catch (Exception e) {
            System.err.println("Ошибка создания SSLContext: " + e.getMessage());
            return null;
        }
    }

    private TrustManager[] getTrustManagers() {
        return new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        // Доверяем всем клиентским сертификатам
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        // Доверяем всем серверным сертификатам
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
    }

    public List<Character> fetchCharacters() throws IOException, InterruptedException {
        String jsonResponse = fetchJsonData();
        return parseCharacters(jsonResponse);
    }

    private String fetchJsonData() throws IOException, InterruptedException {
        System.out.println("Отправка запроса к: " + API_URL);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .header("Accept", "application/json")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(java.time.Duration.ofSeconds(30))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Статус ответа: " + response.statusCode());
            System.out.println("Длина ответа: " + response.body().length());

            if (response.statusCode() != 200) {
                throw new IOException("HTTP Error: " + response.statusCode());
            }

            return response.body();

        } catch (Exception e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
            throw e;
        }
    }

    // Остальные методы parseCharacters и extractValue остаются без изменений
    private List<Character> parseCharacters(String json) {
        List<Character> characters = new ArrayList<>();

        if (json == null || json.isEmpty()) {
            System.err.println("Пустой JSON ответ");
            return characters;
        }

        int resultsStart = json.indexOf("\"results\":");
        if (resultsStart == -1) {
            System.err.println("Не найдено 'results' в JSON");
            return characters;
        }

        int arrayStart = json.indexOf('[', resultsStart);
        if (arrayStart == -1) return characters;

        int arrayEnd = json.lastIndexOf(']');
        if (arrayEnd == -1 || arrayEnd < arrayStart) return characters;

        String resultsArrayString = json.substring(arrayStart + 1, arrayEnd);
        String[] characterJsons = resultsArrayString.split("},\\{");

        System.out.println("Найдено объектов для парсинга: " + characterJsons.length);

        for (String charJson : characterJsons) {
            try {
                String fullJson = "{" + charJson + "}";

                String name = extractValue(fullJson, "name");
                String status = extractValue(fullJson, "status");
                String species = extractValue(fullJson, "species");
                String imageUrl = extractValue(fullJson, "image");

                if (!name.isEmpty()) {
                    Character character = new Character(name, status, species, imageUrl);
                    characters.add(character);
                }

            } catch (Exception e) {
                System.err.println("Ошибка парсинга персонажа: " + e.getMessage());
            }
        }

        System.out.println("Успешно распарсено персонажей: " + characters.size());
        return characters;
    }

    private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);

        if (start == -1) return "";

        start += searchKey.length();

        while (start < json.length()) {
            char c = json.charAt(start);
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                start++;
            } else {
                break;
            }
        }

        if (start < json.length()) {
            if (json.charAt(start) == '"') {
                start++;
                int end = json.indexOf('"', start);
                if (end == -1) return "";
                return json.substring(start, end);
            } else {
                int end = json.indexOf(',', start);
                if (end == -1) end = json.indexOf('}', start);
                if (end == -1) return "";
                return json.substring(start, end).trim();
            }
        }

        return "";
    }
}