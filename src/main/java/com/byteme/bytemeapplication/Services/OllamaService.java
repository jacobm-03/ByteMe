package com.byteme.bytemeapplication.Services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OllamaService {

    private static final String API_URL = "http://localhost:11434/api/generate";

    public static String generateQuiz(String promptText) throws Exception {
        String requestBody = String.format("""
            {
                "model": "llama2",
                "prompt": "%s",
                "stream": false
            }
        """, promptText.replace("\"", "\\\""));

        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes());
        }

        String response = new String(conn.getInputStream().readAllBytes());
        conn.disconnect();

        return response;
    }
}
