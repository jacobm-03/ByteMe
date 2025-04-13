package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Utils.FileParser;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SubjectDetailController {

    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;
    @FXML private VBox docBox;
    @FXML private VBox quizContainer;

    private File uploadedFile;
    private String extractedText;

    @FXML
    private void initialize() {
        progressBar.setProgress(0.0);
        statusLabel.setText("Waiting for file...");
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
                new FileChooser.ExtensionFilter("Document Files", "*.pdf", "*.docx", "*.txt")
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
                int steps = 100;
                for (int i = 1; i <= steps; i++) {
                    Thread.sleep(10);
                    updateProgress(i, steps);
                    updateMessage("Uploading " + file.getName() + "... " + i + "%");
                }

                try {
                    extractedText = FileParser.extractTextFromPDF(file);
                    if (extractedText == null || extractedText.trim().isEmpty()) {
                        throw new Exception("No text extracted from file.");
                    }
                } catch (Exception ex) {
                    System.err.println("❌ Error extracting text:");
                    ex.printStackTrace();
                    throw ex;
                }

                return null;
            }
        };

        progressBar.progressProperty().bind(uploadTask.progressProperty());
        statusLabel.textProperty().bind(uploadTask.messageProperty());

        uploadTask.setOnSucceeded(e -> {
            statusLabel.textProperty().unbind();
            statusLabel.setText("✅ Upload complete!");
            goToQuizOptions(extractedText, file.getName()); // ⬅️ send file name too
        });

        uploadTask.setOnFailed(e -> {
            statusLabel.textProperty().unbind();
            statusLabel.setText("❌ Upload failed.");
        });

        new Thread(uploadTask).start();
    }

    private void goToQuizOptions(String extractedText, String fileName) {
        try {
            // ✅ Save both extracted text and file name globally
            QuizDataHolder.setExtractedText(extractedText);
            QuizDataHolder.setFileName(fileName); // ⬅️ save file name here

            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/QuizOptionsView.fxml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
