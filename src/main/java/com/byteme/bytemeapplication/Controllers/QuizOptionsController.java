package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Utils.OllamaClient;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class QuizOptionsController {

    @FXML private ComboBox<String> difficultyCombo;
    @FXML private Spinner<Integer> questionSpinner;
    @FXML private Button generateQuizBtn;
    @FXML private Label fileNameLabel;
    @FXML private ProgressIndicator loadingSpinner;


    @FXML
    public void initialize() {
        // Set filename label
        String fileName = QuizDataHolder.getFileName();
        fileNameLabel.setText(fileName != null ? fileName : "(No file uploaded)");

        // Set difficulty options
        difficultyCombo.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
        difficultyCombo.getSelectionModel().select("Medium");

        // Set question number spinner (1 to 20, default 5)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5);
        questionSpinner.setValueFactory(valueFactory);

        // Generate quiz button logic
        generateQuizBtn.setOnAction(e -> {
            String difficulty = difficultyCombo.getValue();
            int numQuestions = questionSpinner.getValue();
            String pdfText = QuizDataHolder.getExtractedText();

            if (pdfText == null || pdfText.isBlank()) {
                System.err.println("❌ No extracted text found.");
                Alert alert = new Alert(Alert.AlertType.ERROR, "No PDF text was uploaded or extracted.");
                alert.showAndWait();
                return;
            }

            // Disable UI and show loading
            generateQuizBtn.setDisable(true);
            loadingSpinner.setVisible(true);

            // Run quiz generation on a background thread
            new Thread(() -> {
                try {
                    String quiz = OllamaClient.generateQuiz(pdfText, difficulty, numQuestions);
                    QuizDataHolder.setQuizText(quiz);

                    // Switch to UI thread to load quiz view
                    javafx.application.Platform.runLater(() -> {
                        try {
                            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/QuizView.fxml");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                    javafx.application.Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Quiz generation failed.");
                        alert.showAndWait();
                    });
                } finally {
                    javafx.application.Platform.runLater(() -> {
                        generateQuizBtn.setDisable(false);
                        loadingSpinner.setVisible(false);
                    });
                }
            }).start();
        });

    }

    @FXML
    private void handleBack() {
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/CourseView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
