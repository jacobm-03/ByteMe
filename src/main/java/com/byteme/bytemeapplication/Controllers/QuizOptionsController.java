package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Utils.OllamaClient;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * QuizOptionsController is responsible for handling the quiz configuration screen.
 * Users can choose the number of questions, set the difficulty level, and trigger quiz generation
 * using previously extracted text from a PDF. It also manages UI state and error handling.
 */
public class QuizOptionsController {

    // FXML UI elements injected from the view
    @FXML private ComboBox<String> difficultyCombo;
    @FXML private Spinner<Integer> questionSpinner;
    @FXML private Button generateQuizBtn;
    @FXML private Label fileNameLabel;
    @FXML private ProgressIndicator loadingSpinner;

    /**
     * Called automatically when the FXML is loaded.
     * Initializes UI components and sets up event handlers.
     */
    @FXML
    public void initialize() {
        setupFileNameLabel();     // Show the name of the uploaded PDF
        setupDifficultyOptions(); // Populate difficulty choices
        setupQuestionSpinner();   // Configure number of questions spinner
        setupGenerateButton();    // Attach logic to "Generate Quiz" button
    }

    /**
     * Displays the uploaded file name on the UI.
     * Falls back to placeholder if no file is present.
     */
    private void setupFileNameLabel() {
        String fileName = QuizDataHolder.getFileName();
        fileNameLabel.setText(fileName != null ? fileName : "(No file uploaded)");
    }

    /**
     * Populates the difficulty ComboBox with options and selects default ("Medium").
     */
    private void setupDifficultyOptions() {
        difficultyCombo.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
        difficultyCombo.getSelectionModel().select("Medium");
    }

    /**
     * Configures the question count Spinner to range from 1 to 20 with a default of 5.
     */
    private void setupQuestionSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5);
        questionSpinner.setValueFactory(valueFactory);
    }

    /**
     * Sets the behavior of the Generate Quiz button.
     * Triggers validation, disables UI, runs quiz generation in background.
     */
    private void setupGenerateButton() {
        generateQuizBtn.setOnAction(e -> handleGenerateQuiz());
    }

    /**
     * Handles the quiz generation workflow:
     * 1. Validates extracted text
     * 2. Shows loading spinner
     * 3. Calls OllamaClient in background thread
     * 4. Switches to quiz view if successful, shows error if failed
     */
    private void handleGenerateQuiz() {
        String difficulty = difficultyCombo.getValue();             // User-selected difficulty
        int numQuestions = questionSpinner.getValue();              // User-selected number of questions
        String pdfText = QuizDataHolder.getExtractedText();         // Extracted text from PDF

        // Validation check: must have extracted text
        if (pdfText == null || pdfText.isBlank()) {
            showErrorAlert("No PDF text was uploaded or extracted.");
            return;
        }

        // Disable UI controls and show loading spinner
        setUIStateDuringGeneration(true);

        // Launch quiz generation in a separate thread (non-blocking UI)
        new Thread(() -> {
            try {
                // Call the Ollama client to generate the quiz
                String quiz = OllamaClient.generateQuiz(pdfText, difficulty, numQuestions);

                // Store the generated quiz for the next screen
                QuizDataHolder.setQuizText(quiz);

                // Switch to Quiz View screen (on JavaFX Application Thread)
                javafx.application.Platform.runLater(this::switchToQuizView);

            } catch (Exception ex) {
                ex.printStackTrace(); // Log exception for debugging
                javafx.application.Platform.runLater(() -> showErrorAlert("Quiz generation failed."));
            } finally {
                // Re-enable UI and hide spinner regardless of outcome
                javafx.application.Platform.runLater(() -> setUIStateDuringGeneration(false));
            }
        }).start();
    }

    /**
     * Loads the QuizView.fxml file into the content area (called after quiz generation).
     */
    private void switchToQuizView() {
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/QuizView.fxml");
        } catch (Exception ex) {
            ex.printStackTrace(); // Log if view loading fails
        }
    }

    /**
     * Updates the UI to show loading state or return to normal.
     * @param isLoading true = disable button and show spinner, false = enable button and hide spinner
     */
    private void setUIStateDuringGeneration(boolean isLoading) {
        generateQuizBtn.setDisable(isLoading);
        loadingSpinner.setVisible(isLoading);
    }

    /**
     * Displays an error message using a modal Alert dialog.
     * @param message the message to show to the user
     */
    private void showErrorAlert(String message) {
        System.err.println("❌ " + message);
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    /**
     * Handles the "Back" button, returning to the CourseView screen.
     */
    @FXML
    private void handleBack() {
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/CourseView.fxml");
        } catch (Exception e) {
            e.printStackTrace(); // Log if navigation fails
        }
    }
}
