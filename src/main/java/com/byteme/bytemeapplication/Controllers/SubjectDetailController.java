package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Utils.FileParser;
import com.byteme.bytemeapplication.Utils.OllamaClient;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;

public class SubjectDetailController {

    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;
    @FXML private VBox docBox;
    @FXML private VBox quizContainer;

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
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            simulateUpload(file);  // ✅ Correct method name
        }
    }

    private void simulateUpload(File file) {
        Task<Void> uploadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int steps = 100;
                for (int i = 1; i <= steps; i++) {
                    Thread.sleep(15);
                    updateProgress(i, steps);
                    updateMessage("Uploading " + file.getName() + "... " + i + "%");
                }

                try {
                    // ✅ Extract text
                    String text = FileParser.extractTextFromPDF(file);
                    if (text == null || text.trim().isEmpty()) {
                        throw new Exception("No text extracted from file.");
                    }

                    // System.out.println("📄 Extracted Text:\n" + text); // 🔒 Commented out for now

                    // ✅ Generate quiz via Ollama
                    String json = OllamaClient.generateQuiz(text);
                    System.out.println("🔁 Raw JSON:\n" + json);

                    // ✅ Parse JSON and print quiz
                    JSONObject obj = new JSONObject(json);
                    String quiz = obj.getString("response");

                    System.out.println("✅ Quiz Output:\n" + quiz);

                } catch (Exception ex) {
                    System.err.println("❌ Error generating quiz:");
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
            statusLabel.setText("✅ Quiz printed in terminal.");
        });

        uploadTask.setOnFailed(e -> {
            statusLabel.textProperty().unbind();
            statusLabel.setText("❌ Upload failed.");
        });

        new Thread(uploadTask).start();
    }
}
