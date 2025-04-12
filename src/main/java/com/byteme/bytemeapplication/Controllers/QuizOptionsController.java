package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;

public class QuizOptionsController {

    @FXML private ComboBox<String> difficultyCombo;
    @FXML private Spinner<Integer> questionSpinner;
    @FXML private Button generateQuizBtn;

    @FXML
    public void initialize() {
        // Setup difficulty options
        difficultyCombo.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
        difficultyCombo.getSelectionModel().selectFirst();

        // Setup spinner for number of questions
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5);
        questionSpinner.setValueFactory(valueFactory);

        // Setup button action
        generateQuizBtn.setOnAction(e -> {
            String difficulty = difficultyCombo.getValue();
            int numQuestions = questionSpinner.getValue();

            System.out.println("Generating quiz with:");
            System.out.println("- Difficulty: " + difficulty);
            System.out.println("- Questions: " + numQuestions);

            // TODO: Send these settings to your LLM request
        });
    }
}
