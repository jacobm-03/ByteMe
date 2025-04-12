package com.byteme.bytemeapplication.Services;

import com.byteme.bytemeapplication.Utils.OllamaClient;
import org.json.JSONObject;

public class OllamaTest {
    public static void main(String[] args) {
        String testContent = """
            Artificial Intelligence (AI) is the simulation of human intelligence in machines.
            These machines are programmed to think and learn like humans.
            AI applications include natural language processing, speech recognition, and machine vision.
            """;

        try {
            String json = OllamaClient.generateQuiz(testContent);
            JSONObject obj = new JSONObject(json);
            String quiz = obj.getString("response");

            System.out.println("✅ Quiz Output:\n" + quiz);

        } catch (Exception e) {
            System.err.println("❌ Failed to generate quiz:");
            e.printStackTrace();
        }
    }
}