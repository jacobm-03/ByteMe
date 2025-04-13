package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Utils.OllamaClient;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import com.byteme.bytemeapplication.Utils.TerminalQuizRunner;
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
        String fileName = QuizDataHolder.getFileName();
        fileNameLabel.setText(fileName != null ? fileName : "(No file uploaded)");

        difficultyCombo.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
        difficultyCombo.getSelectionModel().selectFirst();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5);
        questionSpinner.setValueFactory(valueFactory);

        generateQuizBtn.setOnAction(e -> {
            String difficulty = difficultyCombo.getValue();
            int numQuestions = questionSpinner.getValue();
            String pdfText = QuizDataHolder.getExtractedText();

            System.out.println("Generating quiz with:");
            System.out.println("- Difficulty: " + difficulty);
            System.out.println("- Questions: " + numQuestions);

            if (pdfText == null || pdfText.isBlank()) {
                System.err.println("❌ No extracted text found.");
                return;
            }

            try {
                String quiz = OllamaClient.generateQuiz(pdfText, difficulty, numQuestions);
                System.out.println("\n✅ Generated Quiz:\n" + quiz);

                // 🧠 Run the interactive terminal quiz immediately
                TerminalQuizRunner.run(quiz);

            } catch (Exception ex) {
                System.err.println("❌ Failed to generate quiz: " + ex.getMessage());
                ex.printStackTrace();
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
