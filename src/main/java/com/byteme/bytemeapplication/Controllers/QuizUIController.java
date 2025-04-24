package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Database.DatabaseConnection;
import com.byteme.bytemeapplication.Helpers.Session;
import com.byteme.bytemeapplication.Models.QuizQuestion;
import com.byteme.bytemeapplication.Models.User;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import com.byteme.bytemeapplication.Utils.QuizParser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class QuizUIController {

    @FXML private VBox container;
    @FXML private TextFlow questionTextFlow;
    @FXML private Text questionText;
    @FXML private Button optionA;
    @FXML private Button optionB;
    @FXML private Button optionC;
    @FXML private Button optionD;
    @FXML private Button nextButton;
    @FXML private Button finishButton;
    @FXML private Label resultLabel;

    private List<QuizQuestion> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String selectedAnswer = null;

    @FXML
    public void initialize() {
        questionText.wrappingWidthProperty().bind(container.widthProperty().subtract(200));
        questions = QuizParser.parse(QuizDataHolder.getQuizText());

        if (questions == null || questions.isEmpty()) {
            questionText.setText("⚠️ No quiz questions found.");
            nextButton.setDisable(true);
        } else {
            showQuestion();
        }
    }

    private void showQuestion() {
        QuizQuestion q = questions.get(currentIndex);
        questionText.setText("Q" + (currentIndex + 1) + ". " + q.getQuestion());
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
        selectedAnswer = clicked.getText().substring(0, 1);
    }

    @FXML
    private void handleNext() {
        if (selectedAnswer == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an answer.").showAndWait();
            return;
        }

        QuizQuestion q = questions.get(currentIndex);
        if (selectedAnswer.equalsIgnoreCase(q.getCorrectAnswer())) {
            score++;
        }

        currentIndex++;
        if (currentIndex >= questions.size()) {
            questionTextFlow.setVisible(false);
            optionA.setVisible(false);
            optionB.setVisible(false);
            optionC.setVisible(false);
            optionD.setVisible(false);
            nextButton.setVisible(false);
            resultLabel.setVisible(true);
            resultLabel.setText("🎉 Final Score: " + score + " out of " + questions.size());
            finishButton.setVisible(true);
            saveScoreToDatabase();
            printAverageScoreForSubject(); // 👈 Add this
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

    private void saveScoreToDatabase() {
        User user = Session.getCurrentUser();
        int subjectId = QuizDataHolder.getSubjectId();

        if (user == null || subjectId <= 0) {
            System.err.println("❌ Cannot save score. Missing user or subject.");
            return;
        }

        String sql = "INSERT INTO user_scores (user_id, subject_id, score, total_questions) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getId());
            stmt.setInt(2, subjectId);
            stmt.setInt(3, score);
            stmt.setInt(4, questions.size());
            stmt.executeUpdate();
            System.out.println("✅ Score saved to database.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to save score: " + e.getMessage());
        }
    }

    private void printAverageScoreForSubject() {
        int userId = Session.getCurrentUser().getId();
        int subjectId = QuizDataHolder.getSubjectId();

        String sql = """
        SELECT u.firstname || ' ' || u.lastname AS user_name,
               s.name AS subject_name,
               AVG(us.score * 1.0 / us.total_questions) AS average
        FROM user_scores us
        JOIN subjects s ON us.subject_id = s.id
        JOIN users u ON us.user_id = u.id
        WHERE us.user_id = ? AND us.subject_id = ?
    """;

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, subjectId);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                String userName = rs.getString("user_name");
                String subjectName = rs.getString("subject_name");
                double avg = rs.getDouble("average");

                System.out.printf("📊 %s's average score for \"%s\": %.2f%%%n", userName, subjectName, avg * 100);
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to compute average score: " + e.getMessage());
        }
    }



}
