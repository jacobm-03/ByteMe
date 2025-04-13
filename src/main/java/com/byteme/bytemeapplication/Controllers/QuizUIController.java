package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Models.QuizQuestion;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import com.byteme.bytemeapplication.Utils.QuizParser;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;

public class QuizUIController {

    @FXML private Label questionLabel;
    @FXML private Button optionA;
    @FXML private Button optionB;
    @FXML private Button optionC;
    @FXML private Button optionD;
    @FXML private Button nextButton;
    @FXML private Label resultLabel;

    private List<QuizQuestion> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String selectedAnswer = null;

    @FXML
    public void initialize() {
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
        selectedAnswer = null;
        resetButtonStyles();
    }

    @FXML
    private void handleOptionClick(javafx.event.ActionEvent event) {
        resetButtonStyles();
        Button clicked = (Button) event.getSource();
        clicked.setStyle(clicked.getStyle() + "; -fx-background-color: #d9d1ff; -fx-border-color: #6a5acd;");
        selectedAnswer = clicked.getText().substring(0, 1); // Extract A/B/C/D
    }
    @FXML private Button finishButton;
    @FXML
    private void handleNext() {
        if (selectedAnswer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an answer.");
            alert.showAndWait();
            return;
        }

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
            finishButton.setVisible(true);
        } else {
            showQuestion();
        }
    }

    @FXML
    private void handleFinish() {
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/CourseView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetButtonStyles() {
        String baseStyle = "-fx-background-color: white; -fx-border-color: #cccccc; -fx-text-fill: #333; -fx-border-radius: 10; -fx-background-radius: 10;";
        optionA.setStyle(baseStyle);
        optionB.setStyle(baseStyle);
        optionC.setStyle(baseStyle);
        optionD.setStyle(baseStyle);
    }
}
