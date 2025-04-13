package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Models.QuizQuestion;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import com.byteme.bytemeapplication.Utils.QuizParser;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class QuizUIController {

    @FXML private Label questionLabel;
    @FXML private RadioButton optionA, optionB, optionC, optionD;
    @FXML private Button nextButton;
    @FXML private Label resultLabel;

    private ToggleGroup optionsGroup;
    private List<QuizQuestion> questions;
    private int currentIndex = 0;
    private int score = 0;

    @FXML
    public void initialize() {
        optionsGroup = new ToggleGroup();
        optionA.setToggleGroup(optionsGroup);
        optionB.setToggleGroup(optionsGroup);
        optionC.setToggleGroup(optionsGroup);
        optionD.setToggleGroup(optionsGroup);

        questions = QuizParser.parse(QuizDataHolder.getQuizText());

        if (questions == null || questions.isEmpty()) {
            questionLabel.setText("⚠️ No quiz questions found.");
            nextButton.setDisable(true);
        } else {
            showQuestion();
        }
    }

    private void showQuestion() {
        QuizQuestion q = questions.get(currentIndex);
        questionLabel.setText("Q" + (currentIndex + 1) + ". " + q.getQuestion());
        optionA.setText("A. " + q.getOptionA());
        optionB.setText("B. " + q.getOptionB());
        optionC.setText("C. " + q.getOptionC());
        optionD.setText("D. " + q.getOptionD());
        optionsGroup.selectToggle(null); // Clear selection
    }

    @FXML
    private void handleNext() {
        RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an answer.");
            alert.showAndWait();
            return;
        }

        String selectedAnswer = selected.getText().substring(0, 1); // A, B, C, or D
        QuizQuestion q = questions.get(currentIndex);
        if (selectedAnswer.equalsIgnoreCase(q.getCorrectAnswer())) {
            score++;
        }

        currentIndex++;
        if (currentIndex >= questions.size()) {
            // End of quiz
            questionLabel.setVisible(false);
            optionA.setVisible(false);
            optionB.setVisible(false);
            optionC.setVisible(false);
            optionD.setVisible(false);
            nextButton.setVisible(false);
            resultLabel.setVisible(true);
            resultLabel.setText("🎉 Final Score: " + score + " out of " + questions.size());
        } else {
            showQuestion();
        }
    }
}
