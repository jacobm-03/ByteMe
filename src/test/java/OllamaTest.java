import static org.junit.jupiter.api.Assumptions.assumeTrue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.byteme.bytemeapplication.Utils.FileParser;
import com.byteme.bytemeapplication.Utils.OllamaClient;


import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Ollama quiz generation from a sample PDF file.
 * Verifies:
 * - PDF file exists
 * - Text extraction works
 * - Quiz is generated properly using LLM
 */
public class OllamaTest {

    @Test
    void testGenerateQuizFromPDF() throws IOException {
        assumeTrue(System.getenv("CI") == null, "Skipping test in CI environment");

        // 🔁 Make sure this file exists in the correct path
        String pdfPath = "src/main/resources/com/byteme/bytemeapplication/sampleFiles/sampleFile.pdf"
                ;
        File file = new File(pdfPath);

        assertTrue(file.exists(), "❌ PDF file not found at path: " + pdfPath);

        // Extract text
        String extractedText = FileParser.extractTextFromPDF(file);
        assertNotNull(extractedText, "❌ Extracted text was null");
        assertFalse(extractedText.trim().isEmpty(), "❌ Extracted text was empty");

        // Generate quiz
        String difficulty = "Medium";
        int questionCount = 5;
        String quiz = OllamaClient.generateQuiz(extractedText, difficulty, questionCount);

        // Validate quiz
        assertNotNull(quiz, "❌ Quiz generation returned null");
        assertTrue(quiz.contains("Q") || quiz.contains("Answer"), "❌ Quiz does not look valid");

        System.out.println("✅ Generated Quiz:\n\n" + quiz);
    }
}
