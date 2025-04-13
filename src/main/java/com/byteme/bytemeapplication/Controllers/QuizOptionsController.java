package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Utils.OllamaClient;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class QuizOptionsController {

    @FXML private ComboBox<String> difficultyCombo;
    @FXML private Spinner<Integer> questionSpinner;
    @FXML private Button generateQuizBtn;
    @FXML private Label fileNameLabel;

    @FXML
    public void initialize() {
        // Show uploaded file name
        String fileName = QuizDataHolder.getFileName();
        fileNameLabel.setText(fileName != null ? fileName : "(No file uploaded)");

        // Populate difficulty dropdown
        difficultyCombo.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
        difficultyCombo.getSelectionModel().selectFirst();

        // Configure number of questions spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5);
        questionSpinner.setValueFactory(valueFactory);

        // Handle quiz generation
        generateQuizBtn.setOnAction(e -> {
            String difficulty = difficultyCombo.getValue();
            int numQuestions = questionSpinner.getValue();
            String pdfText = QuizDataHolder.getExtractedText();

            System.out.println("Generating quiz with:");
            System.out.println("- Difficulty: " + difficulty);
            System.out.println("- Questions: " + numQuestions);

            if (pdfText == null || pdfText.isBlank()) {
                System.err.println("❌ No extracted text found. Make sure a PDF was uploaded and parsed.");
                return;
            }

            try {
                String quiz = OllamaClient.generateQuiz(pdfText, difficulty, numQuestions);
                System.out.println("\n✅ Generated Quiz:\n");
                System.out.println(quiz);
            } catch (Exception ex) {
                System.err.println("❌ Failed to generate quiz: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    @FXML
    private void handleBack() {
        System.out.println("⬅️ Back button clicked");
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/CourseView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
