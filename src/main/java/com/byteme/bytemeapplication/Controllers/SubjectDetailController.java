package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Database.DatabaseConnection;
import com.byteme.bytemeapplication.Helpers.Session;
import com.byteme.bytemeapplication.Utils.FileParser;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;

public class SubjectDetailController {

    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;
    @FXML private VBox docBox;
    @FXML private VBox quizContainer;
    @FXML private Label avgScoreLabel;
    @FXML private Label subjectNameLabel;
    @FXML private StackPane scoreRingContainer;

    private File uploadedFile;
    private String extractedText;

    @FXML
    private void initialize() {
        progressBar.setProgress(0.0);
        statusLabel.setText("Waiting for file...");

        int subjectId = QuizDataHolder.getSubjectId();
        int userId = Session.getCurrentUser().getId();

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT AVG(score * 1.0 / total_questions) * 100 AS average_score FROM user_scores WHERE user_id = ? AND subject_id = ?")) {

            stmt.setInt(1, userId);
            stmt.setInt(2, subjectId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double avg = rs.getDouble("average_score");
                int percent = (int) Math.round(avg);

                drawScoreRing(percent);
                avgScoreLabel.setText(percent + "%");

                // Get subject name
                PreparedStatement subStmt = conn.prepareStatement("SELECT name FROM subjects WHERE id = ?");
                subStmt.setInt(1, subjectId);
                ResultSet subjectResult = subStmt.executeQuery();
                if (subjectResult.next()) {
                    subjectNameLabel.setText(subjectResult.getString("name") + " Proficiency");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void drawScoreRing(int percent) {
        double angle = percent * 3.6;

        Arc backgroundArc = new Arc(40, 40, 35, 35, 0, 360);
        backgroundArc.setType(ArcType.OPEN);
        backgroundArc.setStroke(Color.LIGHTGRAY);
        backgroundArc.setStrokeWidth(10);
        backgroundArc.setFill(Color.TRANSPARENT);

        Arc scoreArc = new Arc(40, 40, 35, 35, 90, -angle);
        scoreArc.setType(ArcType.OPEN);
        scoreArc.setStroke(Color.web("#6a5acd"));
        scoreArc.setStrokeWidth(10);
        scoreArc.setStrokeLineCap(StrokeLineCap.ROUND);
        scoreArc.setFill(Color.TRANSPARENT);

        Pane donutPane = (Pane) scoreRingContainer.lookup("#donutPane");
        if (donutPane != null) {
            donutPane.getChildren().clear();
            donutPane.getChildren().addAll(backgroundArc, scoreArc);
        } else {
            System.err.println("❌ 'donutPane' not found in scoreRingContainer.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/CourseView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDocUpload(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a document");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Document Files", "*.pdf")
        );

        Stage stage = (Stage) docBox.getScene().getWindow();
        uploadedFile = fileChooser.showOpenDialog(stage);

        if (uploadedFile != null) {
            simulateUpload(uploadedFile);
        }
    }

    private void simulateUpload(File file) {
        Task<Void> uploadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 1; i <= 100; i++) {
                    Thread.sleep(10);
                    updateProgress(i, 100);
                    updateMessage("Uploading " + file.getName() + "... " + i + "%");
                }

                extractedText = FileParser.extractTextFromPDF(file);
                if (extractedText == null || extractedText.trim().isEmpty()) {
                    throw new Exception("No text extracted.");
                }

                return null;
            }
        };

        progressBar.progressProperty().bind(uploadTask.progressProperty());
        statusLabel.textProperty().bind(uploadTask.messageProperty());

        uploadTask.setOnSucceeded(e -> {
            statusLabel.textProperty().unbind();
            statusLabel.setText("✅ Upload complete!");
            goToQuizOptions(extractedText, file.getName());
        });

        uploadTask.setOnFailed(e -> {
            statusLabel.textProperty().unbind();
            statusLabel.setText("❌ Upload failed.");
        });

        new Thread(uploadTask).start();
    }

    private void goToQuizOptions(String extractedText, String fileName) {
        try {
            QuizDataHolder.setExtractedText(extractedText);
            QuizDataHolder.setFileName(fileName);
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/QuizOptionsView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Test change to trigger Git tracking

}
