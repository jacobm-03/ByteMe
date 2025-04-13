package com.byteme.bytemeapplication.Services;

import com.byteme.bytemeapplication.Utils.FileParser;
import com.byteme.bytemeapplication.Utils.OllamaClient;

import java.io.File;
import java.io.IOException;

public class OllamaTest {

    public static void main(String[] args) {
        // ✅ Set your actual PDF file path here
        String pdfPath = "src/main/resources/sample.pdf";  // Replace with your actual path
        File file = new File(pdfPath);

        if (!file.exists()) {
            System.err.println("❌ PDF file not found: " + pdfPath);
            return;
        }

        try {
            System.out.println("📄 Extracting text from PDF...");
            String extractedText = FileParser.extractTextFromPDF(file);

            // ✅ Set difficulty and question count for the test
            String difficulty = "Medium";
            int questionCount = 5;

            System.out.println("🤖 Sending prompt to Ollama (model: mistral)...");
            String quiz = OllamaClient.generateQuiz(extractedText, difficulty, questionCount);

            System.out.println("\n📘 Generated Quiz:\n");
            System.out.println(quiz);

        } catch (IOException e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
