package com.byteme.bytemeapplication.Utils;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class OllamaClient {
    private static final String API_URL = "http://localhost:11434/api/generate";

    public static String generateQuiz(String promptText) throws IOException {
        // Structured prompt
        String promptTemplate = """
        You are a helpful assistant. Your task is to generate a short 5-question multiple choice quiz based ONLY on the following content:

        %s

        Each question must follow this structure:

        Q1. [Question text]
        A. Option 1
        B. Option 2
        C. Option 3
        D. Option 4
        Answer: [A/B/C/D]

        Keep questions concise and relevant. Do not repeat or reference the original text. Ensure answers are accurate.
        """;

        String prompt = String.format(promptTemplate, promptText);
        String jsonInput = String.format("""
            {
              "model": "stablelm-zephyr:latest",
              "prompt": "%s",
              "stream": false
            }
            """, escapeJson(prompt));

        // Create connection
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Send request
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Handle error response
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            try (InputStream errorStream = conn.getErrorStream()) {
                String errorResponse = new BufferedReader(new InputStreamReader(errorStream))
                        .lines().reduce("", (a, b) -> a + b + "\n");
                System.err.println("Ollama error response: " + errorResponse);
            }
            throw new IOException("Server returned HTTP status: " + responseCode);
        }

        // Read successful response
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Parse JSON and extract only quiz response
            JSONObject jsonObject = new JSONObject(response.toString());
            String quiz = jsonObject.getString("response");

            return quiz;
        }
    }

    // Utility to escape JSON properly
    private static String escapeJson(String str) {
        return str.replace("\r", "")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }
}
