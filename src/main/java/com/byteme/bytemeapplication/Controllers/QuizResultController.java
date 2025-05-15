package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Models.QuizQuestion;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class QuizResultController {

    @FXML private Label scoreLabel;
    @FXML private VBox answerBox;

    @FXML
    public void initialize() {
        List<QuizQuestion> questions = QuizDataHolder.getQuestionList();
        List<String> userAnswers = QuizDataHolder.getUserAnswers();
        int score = QuizDataHolder.getFinalScore();

        scoreLabel.setText("\uD83C\uDF89 Final Score: " + score + " out of " + questions.size());

        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion q = questions.get(i);
            String correct = q.getCorrectAnswer();
            String selected = userAnswers.size() > i ? userAnswers.get(i) : "N/A";

            Label summaryLabel = new Label(String.format(
                    "Q%d:\nCorrect Answer: %s\nYour Answer: %s",
                    i + 1, correct, selected
            ));
            summaryLabel.setWrapText(true);
            summaryLabel.setMaxWidth(500);
            summaryLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: " +
                    (correct.equalsIgnoreCase(selected) ? "green;" : "red;"));

            VBox itemBox = new VBox(summaryLabel);
            itemBox.setAlignment(Pos.CENTER);
            itemBox.setStyle("-fx-padding: 10;");
            answerBox.getChildren().add(itemBox);
        }
    }

    @FXML
    private void handleBackToCourses() {
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/CourseView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
