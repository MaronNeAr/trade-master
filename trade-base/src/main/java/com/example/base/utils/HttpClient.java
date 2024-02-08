package com.example.base.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {

    public static String sendGetRequest(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                }
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String sendGetRequest(String url, String encoding) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                encoding = encoding == null ? StandardCharsets.UTF_8.name() : encoding;

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding))) {
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                }
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String sendPostRequest(String url, String jsonBody) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(jsonBody.getBytes("UTF-8"));
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                }
            } else {
                System.out.println("POST request failed. Response Code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

