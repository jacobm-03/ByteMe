package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.concurrent.Task;



import java.io.File;

public class SubjectDetailController {

    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;
    @FXML private VBox docBox;

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

        // Get the stage from the docBox
        Stage stage = (Stage) docBox.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            simulateUpload(file);
        }
    }



    private void simulateUpload(File file) {
        Task<Void> uploadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int steps = 100;
                for (int i = 1; i <= steps; i++) {
                    Thread.sleep(15); // Simulate time taken to process each step
                    updateProgress(i, steps);
                    updateMessage("Uploading " + file.getName() + "... " + i + "%");
                }
                return null;
            }
        };

        // Bind progress and status label to the task
        progressBar.progressProperty().bind(uploadTask.progressProperty());
        statusLabel.textProperty().bind(uploadTask.messageProperty());

        // Optional: show complete message
        uploadTask.setOnSucceeded(e -> {
            statusLabel.textProperty().unbind();
            statusLabel.setText("✅ Upload complete: " + file.getName());
        });

        uploadTask.setOnFailed(e -> {
            statusLabel.textProperty().unbind();
            statusLabel.setText("❌ Upload failed.");
        });

        // Run the task in a background thread
        Thread thread = new Thread(uploadTask);
        thread.setDaemon(true); // Daemon thread exits when the app exits
        thread.start();
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}
