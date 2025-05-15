package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Database.DatabaseConnection;
import com.byteme.bytemeapplication.Helpers.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ProgressController {

    @FXML private ComboBox<String> subjectComboBox;
    @FXML private Label averageLabel, quizzesCompletedLabel, timePerQuestionLabel, streakLabel;
    @FXML private Text currentTopic;
    @FXML private ListView<String> strengthsList, weaknessesList;
    @FXML private VBox chartVBox;  // New VBox container for progress bars

    private final HashMap<String, Integer> subjectNameToId = new HashMap<>();

    @FXML
    public void initialize() {
        loadUserSubjects();
        loadProgressBars();

        subjectComboBox.setOnAction(event -> {
            String selectedSubject = subjectComboBox.getValue();
            if (selectedSubject != null) {
                updateSubjectStats(selectedSubject);
            }
        });
    }

    private void loadUserSubjects() {
        subjectComboBox.getItems().clear();
        subjectNameToId.clear();

        if (Session.getCurrentUser() == null) return;
        int userId = Session.getCurrentUser().getId();

        String sql = "SELECT id, name FROM subjects WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int subjectId = rs.getInt("id");
                String name = rs.getString("name");

                subjectNameToId.put(name, subjectId);
                subjectComboBox.getItems().add(name);
            }

            if (!subjectNameToId.isEmpty()) {
                subjectComboBox.setValue(subjectComboBox.getItems().get(0));
                updateSubjectStats(subjectComboBox.getValue());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSubjectStats(String subjectName) {
        if (!subjectNameToId.containsKey(subjectName)) return;

        int subjectId = subjectNameToId.get(subjectName);
        int userId = Session.getCurrentUser().getId();

        strengthsList.getItems().clear();
        weaknessesList.getItems().clear();

        String sql = "SELECT score FROM user_scores WHERE user_id = ? AND subject_id = ?";

        double totalScore = 0;
        int quizCount = 0;

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, subjectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double score = rs.getDouble("score");
                totalScore += score;
                quizCount++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        averageLabel.setText(quizCount > 0 ? String.format("%.1f", totalScore / quizCount) : "N/A");
        quizzesCompletedLabel.setText(String.valueOf(quizCount));
        timePerQuestionLabel.setText("N/A");
        streakLabel.setText("3 days"); // Placeholder
        currentTopic.setText("Topic coverage not yet implemented");

        strengthsList.getItems().addAll("", "");
        weaknessesList.getItems().addAll("");
    }

    private void loadProgressBars() {
        chartVBox.getChildren().clear();
        int userId = Session.getCurrentUser().getId();

        String sql = """
        SELECT s.name, AVG(us.score * 1.0 / us.total_questions) AS avg_score
        FROM user_scores us
        JOIN subjects s ON s.id = us.subject_id
        WHERE us.user_id = ?
        GROUP BY s.name
    """;

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String subjectName = rs.getString("name");
                double avgScore = rs.getDouble("avg_score");

                Label label = new Label(subjectName + ":");
                label.setStyle("-fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: #444;");

                ProgressBar progressBar = new ProgressBar(avgScore);
                progressBar.setPrefWidth(300);
                progressBar.setStyle("""
                -fx-accent: #6a5acd;
                -fx-control-inner-background: #eeeeee;
                -fx-background-insets: 0;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
            """);

                Label percent = new Label(String.format("%.0f%%", avgScore * 100));
                percent.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #6a5acd;");

                HBox row = new HBox(12, label, progressBar, percent);
                row.setStyle("""
                -fx-padding: 10;
                -fx-background-color: #ffffff;
                -fx-background-radius: 10;
                -fx-border-color: #dddddd;
                -fx-border-radius: 10;
            """);
                chartVBox.getChildren().add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
