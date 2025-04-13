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
        // Set filename label
        String fileName = QuizDataHolder.getFileName();
        fileNameLabel.setText(fileName != null ? fileName : "(No file uploaded)");

        // Set difficulty options
        difficultyCombo.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
        difficultyCombo.getSelectionModel().select("Medium");

        // Set question number spinner (1 to 20, default 5)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5);
        questionSpinner.setValueFactory(valueFactory);

        // Generate quiz button logic
        generateQuizBtn.setOnAction(e -> {
            String difficulty = difficultyCombo.getValue();
            int numQuestions = questionSpinner.getValue();
            String pdfText = QuizDataHolder.getExtractedText();

            System.out.println("📘 Generating quiz with:");
            System.out.println("- Difficulty: " + difficulty);
            System.out.println("- Questions: " + numQuestions);

            if (pdfText == null || pdfText.isBlank()) {
                System.err.println("❌ No extracted text found.");
                Alert alert = new Alert(Alert.AlertType.ERROR, "No PDF text was uploaded or extracted.");
                alert.showAndWait();
                return;
            }

            generateQuizBtn.setDisable(true);
            generateQuizBtn.setText("Generating...");

            try {
                // Generate quiz using Ollama
                String quiz = OllamaClient.generateQuiz(pdfText, difficulty, numQuestions);
                System.out.println("✅ Quiz generated:\n" + quiz);

                // Save quiz to data holder
                QuizDataHolder.setQuizText(quiz);

                // Navigate to the quiz UI screen
                HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/QuizView.fxml");

            } catch (Exception ex) {
                System.err.println("❌ Failed to generate quiz: " + ex.getMessage());
                ex.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Quiz generation failed.");
                errorAlert.showAndWait();
            } finally {
                generateQuizBtn.setDisable(false);
                generateQuizBtn.setText("Generate Quiz");
            }
        });
    }

    @FXML
    private void handleBack() {
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/CourseView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
