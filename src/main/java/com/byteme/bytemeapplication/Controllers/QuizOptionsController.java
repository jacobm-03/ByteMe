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

    @FXML
    public void initialize() {
        difficultyCombo.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
        difficultyCombo.getSelectionModel().selectFirst();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5);
        questionSpinner.setValueFactory(valueFactory);

        generateQuizBtn.setOnAction(e -> {
            String difficulty = difficultyCombo.getValue();
            int numQuestions = questionSpinner.getValue();

            System.out.println("Generating quiz with:");
            System.out.println("- Difficulty: " + difficulty);
            System.out.println("- Questions: " + numQuestions);

            String pdfText = QuizDataHolder.getExtractedText();

            if (pdfText == null || pdfText.isBlank()) {
                System.err.println("❌ No extracted text found. Make sure a PDF was uploaded and parsed.");
                return;
            }

            try {
                String quiz = OllamaClient.generateQuiz(pdfText);
                System.out.println("\n✅ Generated Quiz:\n");
                System.out.println(quiz);
            } catch (Exception ex) {
                System.err.println("❌ Failed to generate quiz: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}
