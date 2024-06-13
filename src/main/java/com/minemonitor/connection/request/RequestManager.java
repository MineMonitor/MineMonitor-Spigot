package com.minemonitor.connection.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.minemonitor.connection.transfer.DataTransferObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class RequestManager {

    public static <U extends DataTransferObject> Promise<U> sendRequest(RequestType type, String url, DataTransferObject body, Class<U> responseType) {
        Promise<U> promise = new Promise<>();

        try {
            HttpURLConnection connection = createConnection(type, url);
            setRequestBody(connection, body);
            handleResponseAsync(connection, promise, responseType);
        } catch (IOException e) {
            promise.setError(e);
        }

        return promise;
    }

    private static HttpURLConnection createConnection(RequestType type, String url) throws IOException {
        URL finalUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) finalUrl.openConnection();
        connection.setRequestMethod(type.toString());
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        return connection;
    }

    private static void setRequestBody(HttpURLConnection connection, DataTransferObject body) throws IOException {
        if (body == null) {
            return;
        }
            try (OutputStream os = connection.getOutputStream()) {
                os.write(
                        body.toJson().getBytes(StandardCharsets.UTF_8)
                );
            }
    }


    private static <U extends DataTransferObject> void handleResponseAsync(HttpURLConnection connection, Promise<U> promise, Class<U> responseType) {
        CompletableFuture.runAsync(() -> {
            try {
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    promise.setError(new Exception("HTTP error code: " + responseCode));
                    return;
                }

                String response = readResponse(connection);
                U responseObject = DataTransferObject.fromJson(response, responseType);
                promise.setResult(responseObject);
            } catch (Exception e) {
                promise.setError(e);
            } finally {
                connection.disconnect();
            }
        });
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }



}
