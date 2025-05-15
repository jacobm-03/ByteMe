package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Models.QuizQuestion;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * QuizResultController handles the final score display
 * and presents a summary of correct vs. selected answers.
 */
public class QuizResultController {

    @FXML private Label scoreLabel;
    @FXML private VBox answerBox;

    /**
     * Called automatically when the result view loads.
     * Displays the user's score and individual question feedback.
     */
    @FXML
    public void initialize() {
        List<QuizQuestion> questions = QuizDataHolder.getQuestionList();
        List<String> userAnswers = QuizDataHolder.getUserAnswers();
        int score = QuizDataHolder.getFinalScore();

        showFinalScore(score, questions.size());
        populateAnswerSummaries(questions, userAnswers);
    }

    /**
     * Displays the final score in the label.
     */
    private void showFinalScore(int score, int total) {
        scoreLabel.setText("\uD83C\uDF89 Final Score: " + score + " out of " + total);
    }

    /**
     * Populates the answer summary box with each question’s correct
     * and user-selected answers, color-coded for correctness.
     */
    private void populateAnswerSummaries(List<QuizQuestion> questions, List<String> userAnswers) {
        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion question = questions.get(i);
            String correct = question.getCorrectAnswer();
            String selected = userAnswers.size() > i ? userAnswers.get(i) : "N/A";

            Label summaryLabel = buildSummaryLabel(i + 1, correct, selected);
            VBox itemBox = buildItemBox(summaryLabel);
            answerBox.getChildren().add(itemBox);
        }
    }

    /**
     * Builds a styled label displaying the correct and selected answers.
     */
    private Label buildSummaryLabel(int questionNumber, String correct, String selected) {
        Label label = new Label(String.format(
                "Q%d:\nCorrect Answer: %s\nYour Answer: %s",
                questionNumber, correct, selected
        ));
        label.setWrapText(true);
        label.setMaxWidth(500);

        // ✅ Green if correct, red if incorrect
        String color = correct.equalsIgnoreCase(selected) ? "green" : "red";
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: " + color + ";");
        return label;
    }

    /**
     * Wraps the summary label in a VBox with consistent padding.
     */
    private VBox buildItemBox(Label label) {
        VBox box = new VBox(label);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-padding: 10;");
        return box;
    }

    /**
     * Clears quiz results and reloads the quiz view for retry.
     */
    @FXML
    private void handleRetryQuiz() {
        try {
            QuizDataHolder.setQuizResults(null, null, 0); // Clear all stored quiz data
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/QuizView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates back to the Courses view screen.
     */
    @FXML
    private void handleBackToCourses() {
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/CourseView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
