package com.byteme.bytemeapplication.Controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LoginController {

    @FXML private HBox container;
    @FXML private HBox loginPane, signupPane;

    @FXML private VBox signupForm;
    @FXML private TextField firstNameField, lastNameField, signupEmailField;
    @FXML private PasswordField signupPasswordField, confirmPasswordField;

    @FXML
    private void handleLogin() {
        System.out.println("Login with credentials.");
    }

    @FXML
    private void handleSignup() {
        System.out.println("Signup with credentials.");
    }

    @FXML
    private void showSignUp() {
        animatePanes(0.25, 0.75);
        fadeSignupForm(true);
    }

    @FXML
    private void showLogin() {
        animatePanes(0.75, 0.25);
        fadeSignupForm(false);
    }

    private void animatePanes(double loginPercent, double signupPercent) {
        double totalWidth = container.getPrefWidth();

        KeyValue loginWidth = new KeyValue(loginPane.prefWidthProperty(), totalWidth * loginPercent);
        KeyValue signupWidth = new KeyValue(signupPane.prefWidthProperty(), totalWidth * signupPercent);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(400), loginWidth, signupWidth)
        );
        timeline.play();
    }

    private void fadeSignupForm(boolean show) {
        FadeTransition fade = new FadeTransition(Duration.millis(400), signupForm);
        fade.setFromValue(show ? 0 : 1);
        fade.setToValue(show ? 1 : 0);
        fade.setInterpolator(Interpolator.EASE_BOTH);
        fade.setOnFinished(e -> signupForm.setVisible(show));
        if (show) signupForm.setVisible(true);
        fade.play();
    }
}
