package com.byteme.bytemeapplication.Controllers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuizGenerator {

    public static void main(String[] args) {
        // ✅ Replace with actual path to your PDF file
        String pdfPath = "C:/Users/1320t/Documents/QUT/ByteMe/sample.pdf";
        String ollamaUrl = "http://localhost:11434/api/generate";
        String model = "llama3.2:latest";

        try {
            // 1. Load PDF and extract text
            PDDocument document = PDDocument.load(new File(pdfPath));
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String extractedText = pdfStripper.getText(document);
            document.close();

            // 2. Prepare prompt to generate multiple-choice quiz
            String prompt = """
                Create a 5-question multiple choice quiz based on the following content.
                Each question should have:
                - A clear question statement
                - Four answer options labeled A to D
                - Indicate the correct option at the end

                Content:
                """ + extractedText;

            // 3. Create JSON payload
            String jsonPayload = String.format(
                    "{\"model\": \"%s\", \"prompt\": \"%s\", \"stream\": false}",
                    model,
                    prompt.replace("\"", "\\\"")
            );

            // 4. Send request to Ollama
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ollamaUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 5. Output to terminal
            System.out.println("\n📘 Generated Multiple Choice Quiz:\n");
            System.out.println(response.body());

        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
