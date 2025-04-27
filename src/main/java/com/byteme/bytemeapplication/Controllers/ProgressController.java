package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Database.DatabaseConnection;
import com.byteme.bytemeapplication.Helpers.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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
    @FXML private LineChart<String, Number> quizPerformanceChart;

    private final HashMap<String, Integer> subjectNameToId = new HashMap<>();

    @FXML
    public void initialize() {
        loadUserSubjects();

        subjectComboBox.setOnAction(event -> {
            String selectedSubject = subjectComboBox.getValue();
            if (selectedSubject != null) {
                updateSubjectData(selectedSubject);
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
                subjectComboBox.setValue(subjectComboBox.getItems().get(0)); // auto-select first subject
                updateSubjectData(subjectComboBox.getValue());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSubjectData(String subjectName) {
        if (!subjectNameToId.containsKey(subjectName)) return;

        int subjectId = subjectNameToId.get(subjectName);
        int userId = Session.getCurrentUser().getId();

        strengthsList.getItems().clear();
        weaknessesList.getItems().clear();
        quizPerformanceChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(subjectName + " Scores");

        // Updated query: Order by score instead of created_at
        String sql = "SELECT score FROM user_scores WHERE user_id = ? AND subject_id = ? ORDER BY score"; // Sorting by score

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

                series.getData().add(new XYChart.Data<>("Quiz " + quizCount, score));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update UI with calculated values
        averageLabel.setText(quizCount > 0 ? String.format("%.1f", totalScore / quizCount) : "N/A");
        quizzesCompletedLabel.setText(String.valueOf(quizCount));
        timePerQuestionLabel.setText("N/A");
        streakLabel.setText("3 days"); // Placeholder or implement actual streak logic

        currentTopic.setText("Topic coverage not yet implemented");

        strengthsList.getItems().addAll("", "");
        weaknessesList.getItems().addAll("");

        quizPerformanceChart.getData().add(series);
    }
}
