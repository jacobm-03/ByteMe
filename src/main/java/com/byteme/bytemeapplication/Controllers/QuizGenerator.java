package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Utils.FileParser;
import com.byteme.bytemeapplication.Utils.OllamaClient;

import java.io.File;
import java.io.IOException;

/**
 * QuizGenerator is a command-line tool that:
 * 1. Extracts text from a local PDF file
 * 2. Sends it to the Ollama API to generate a quiz
 * 3. Prints the resulting quiz to the terminal
 */
public class QuizGenerator {

    public static void main(String[] args) {
        // ✅ Specify the path to your input PDF file (update as needed)
        String pdfPath = "src/main/resources/sample.pdf";

        // ✅ Start the quiz generation process with default parameters
        run(pdfPath, "Medium", 5);
    }

    /**
     * Coordinates the quiz generation process:
     * - Validates the file
     * - Extracts the text
     * - Generates the quiz
     * - Displays the quiz
     */
    private static void run(String pdfPath, String difficulty, int numQuestions) {
        File file = new File(pdfPath);

        // ❌ If the file doesn't exist, display error and stop
        if (!file.exists()) {
            showFileNotFoundError(pdfPath);
            return;
        }

        try {
            // 📄 Step 1: Extract text from the PDF
            String extractedText = extractText(file);
            if (extractedText == null || extractedText.isBlank()) {
                System.err.println("❌ PDF text extraction failed or returned empty.");
                return;
            }

            // 🤖 Step 2: Generate quiz from extracted text
            String quiz = generateQuiz(extractedText, difficulty, numQuestions);

            // 📘 Step 3: Display the generated quiz in the console
            displayQuiz(quiz);

        } catch (IOException e) {
            handleIOException(e);
        }
    }

    /**
     * Extracts text content from the given PDF file.
     * @param file the PDF file to process
     * @return the extracted text
     * @throws IOException if the file cannot be read
     */
    private static String extractText(File file) throws IOException {
        System.out.println("📄 Extracting text from PDF...");
        return FileParser.extractTextFromPDF(file);
    }

    /**
     * Generates a quiz using the provided extracted text.
     * @param text the extracted PDF content
     * @param difficulty difficulty level (Easy/Medium/Hard)
     * @param numQuestions number of questions to generate
     * @return the generated quiz string
     * @throws IOException if the Ollama client fails
     */
    private static String generateQuiz(String text, String difficulty, int numQuestions) throws IOException {
        System.out.println("🤖 Generating quiz with Ollama (model: mistral)...");
        return OllamaClient.generateQuiz(text, difficulty, numQuestions);
    }

    /**
     * Displays the final quiz output to the terminal.
     * @param quiz the quiz content
     */
    private static void displayQuiz(String quiz) {
        System.out.println("\n📘 Generated Quiz:\n");
        System.out.println(quiz);
    }

    /**
     * Prints an error message if the PDF file is not found.
     * @param path the path of the file that failed to load
     */
    private static void showFileNotFoundError(String path) {
        System.err.println("❌ PDF file not found: " + path);
    }

    /**
     * Handles IOException by printing the stack trace and message.
     * @param e the exception thrown during file I/O
     */
    private static void handleIOException(IOException e) {
        System.err.println("❌ I/O Error: " + e.getMessage());
        e.printStackTrace();
    }
}
