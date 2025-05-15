package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Utils.FileParser;
import com.byteme.bytemeapplication.Utils.OllamaClient;

import java.io.File;
import java.io.IOException;

public class QuizGenerator {

    public static void main(String[] args) {
        // ✅ Set your actual PDF file path here
        String pdfPath = "src/main/resources/sample.pdf";  // Replace this path as needed
        File file = new File(pdfPath);

        if (!file.exists()) {
            System.err.println("❌ PDF file not found: " + pdfPath);
            return;
        }

        try {
            System.out.println("📄 Extracting text from PDF...");
            String extractedText = FileParser.extractTextFromPDF(file);

            // 🔽 Define quiz options
            String difficulty = "Medium";
            int numQuestions = 5;

            System.out.println("🤖 Sending aprompt to Ollama (model: mistral)...");
            String quiz = OllamaClient.generateQuiz(extractedText, difficulty, numQuestions);

            System.out.println("\n📘 Generated Quiz:\n");
            System.out.println(quiz);

        } catch (IOException e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
