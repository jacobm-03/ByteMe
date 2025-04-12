    package com.byteme.bytemeapplication.Controllers;

    import com.byteme.bytemeapplication.Database.DatabaseConnection;
    import com.byteme.bytemeapplication.Models.User;
    import com.byteme.bytemeapplication.Helpers.Session;

    import javafx.animation.*;
    import javafx.fxml.FXML;
    import javafx.scene.control.Alert;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.TextField;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.util.Duration;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;
    import java.util.Random;
    import javafx.scene.control.Label;

    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.stage.Stage;
    import javafx.scene.Node;
    import javafx.event.ActionEvent;

    import javafx.scene.control.Button;


    public class LoginController {

        @FXML private HBox container;
        @FXML private HBox loginPane, signupPane;

        @FXML private VBox signupForm; // ← ✅ Add this!
        @FXML private TextField firstNameField, lastNameField, signupEmailField;
        @FXML private PasswordField signupPasswordField, confirmPasswordField;
        @FXML private VBox loginForm; // the part to fade in/out

        @FXML private TextField loginEmail;
        @FXML private PasswordField loginPassword;


        @FXML private Label lengthCheck, uppercaseCheck, numberCheck, specialCharCheck;

        @FXML private TextField visibleLoginPassword;
        @FXML private Button togglePasswordVisibility;
        private boolean isPasswordVisible = false;

        @FXML private TextField visibleSignupPasswordField;
        @FXML private TextField visibleConfirmPasswordField;
        @FXML private Button toggleSignupPassword;
        @FXML private Button toggleConfirmPassword;

        private boolean isSignupPasswordVisible = false;
        private boolean isConfirmPasswordVisible = false;



        @FXML
        private void handleLoginAndSwitch(ActionEvent event) {
            if (!loginForm.isVisible()) {
                showLogin();
                return;
            }

            String email = loginEmail.getText().trim();
            String password = loginPassword.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert("Please enter both email and password.");
                return;
            }

            Connection conn = null;
            PreparedStatement stmt = null;
            java.sql.ResultSet rs = null;

            try {
                conn = DatabaseConnection.getInstance();
                String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                stmt.setString(2, password);

                rs = stmt.executeQuery();
                if (rs.next()) {
                    // ✅ Save session info
                    int userId = rs.getInt("id");
                    String firstName = rs.getString("firstname");
                    String lastName = rs.getString("lastname");
                    String userEmail = rs.getString("email");

                    User user = new User(userId, firstName, lastName, userEmail);
                    Session.setCurrentUser(user);  // 🔥 Store in session

                    goToHome(event);  // 🔁 Navigate after session set
                } else {
                    showAlert("❌ Invalid email or password.");
                }

            } catch (SQLException e) {
                showAlert("❌ Database error: " + e.getMessage());
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                } catch (SQLException e) {
                    System.err.println("❌ Failed to close resources: " + e.getMessage());
                }
            }
        }

        private void fadeLoginForm(boolean show) {
            FadeTransition fade = new FadeTransition(Duration.millis(400), loginForm);
            fade.setFromValue(show ? 0 : 1);
            fade.setToValue(show ? 1 : 0);
            fade.setInterpolator(Interpolator.EASE_BOTH);
            fade.setOnFinished(e -> loginForm.setVisible(show));
            if (show) loginForm.setVisible(true);
            fade.play();
        }

        @FXML
        public void initialize() {
            signupPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
                validatePassword(newValue);
            });
            visibleLoginPassword.textProperty().bindBidirectional(loginPassword.textProperty());

            visibleSignupPasswordField.textProperty().bindBidirectional(signupPasswordField.textProperty());
            visibleConfirmPasswordField.textProperty().bindBidirectional(confirmPasswordField.textProperty());

        }

        private void validatePassword(String password) {
            // Requirement checks
            boolean hasLength = password.length() >= 6;
            boolean hasUppercase = password.matches(".*[A-Z].*");
            boolean hasNumber = password.matches(".*\\d.*");
            boolean hasSpecial = password.matches(".*[!@#$%^&*()].*");

            // Update visual indicators
            updateCheckLabel(lengthCheck, hasLength, "6 characters minimum");
            updateCheckLabel(uppercaseCheck, hasUppercase, "One uppercase letter");
            updateCheckLabel(numberCheck, hasNumber, "One number");
            updateCheckLabel(specialCharCheck, hasSpecial, "One special character (!@#$%^&*)");
        }

        private void updateCheckLabel(Label label, boolean condition, String text) {
            label.setText((condition ? "✔" : "✗") + " " + text);
            label.setStyle("-fx-text-fill: " + (condition ? "green" : "red"));
        }

        @FXML
        private void togglePasswordVisibility() {
            isPasswordVisible = !isPasswordVisible;
            loginPassword.setVisible(!isPasswordVisible);
            loginPassword.setManaged(!isPasswordVisible);
            visibleLoginPassword.setVisible(isPasswordVisible);
            visibleLoginPassword.setManaged(isPasswordVisible);

            togglePasswordVisibility.setText(isPasswordVisible ? "🙈" : "👁");

        }




        @FXML
        private void showSignUp() {
            animatePanes(0.33333, 0.66667);
            fadeSignupForm(true);
            fadeLoginForm(false);  // Hide login form
        }

        @FXML
        private void showLogin() {
            animatePanes(0.66667, 0.33333);
            fadeSignupForm(false);
            fadeLoginForm(true);   // Show login form
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

        private boolean isValidPassword(String password) {
            return password.length() >= 6 && password.matches(".*[^a-zA-Z0-9].*");
        }

        @FXML
        private boolean handleSignUp() {
            String id = generateSixDigitId();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = signupEmailField.getText().trim();
            String password = signupPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert("All fields must be filled.");
                return false;
            }

            if (!password.equals(confirmPassword)) {
                showAlert("❌ Passwords do not match.");
                return false;
            }

            if (!isValidPassword(password)) {
                showAlert("❌ Password must be at least 6 characters and include 1 special character.");
                return false;
            }

            try (Connection conn = DatabaseConnection.getInstance()) {
                String sql = "INSERT INTO users (id, firstname, lastname, email, password) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, id);
                stmt.setString(2, firstName);
                stmt.setString(3, lastName);
                stmt.setString(4, email);
                stmt.setString(5, password);

                stmt.executeUpdate();
                showAlert("✅ Account created successfully!");
                clearFields();
                return true;

            } catch (SQLException e) {
                showAlert("❌ Database error: " + e.getMessage());
                return false;
            }


        }


        @FXML
        private void handleSignUpAndSwitch(ActionEvent event) {
            // Check if form is currently visible
            if (!signupForm.isVisible()) {
                showSignUp();  // Animate & reveal form
            } else {
                if (handleSignUp()) {
                    showLogin(); // Only transition if sign-up successful
                    goToHome(event);
                }
            }
        }

        private String generateSixDigitId() {
            return String.valueOf(100000 + new Random().nextInt(900000));
        }

        private void showAlert(String msg) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(msg);
            alert.showAndWait();
        }

        private void clearFields() {
            firstNameField.clear();
            lastNameField.clear();
            signupEmailField.clear();
            signupPasswordField.clear();
            confirmPasswordField.clear();
        }

        private void goToHome(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/byteme/bytemeapplication/fxml/HomeView.fxml"));
                Scene homeScene = new Scene(loader.load());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(homeScene);
                stage.setTitle("Home - ByteMe");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @FXML
        private void toggleSignupPasswordVisibility() {
            isSignupPasswordVisible = !isSignupPasswordVisible;
            signupPasswordField.setVisible(!isSignupPasswordVisible);
            signupPasswordField.setManaged(!isSignupPasswordVisible);
            visibleSignupPasswordField.setVisible(isSignupPasswordVisible);
            visibleSignupPasswordField.setManaged(isSignupPasswordVisible);

            toggleSignupPassword.setText(isSignupPasswordVisible ? "🙈" : "👁");
        }

        @FXML
        private void toggleConfirmPasswordVisibility() {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            confirmPasswordField.setVisible(!isConfirmPasswordVisible);
            confirmPasswordField.setManaged(!isConfirmPasswordVisible);
            visibleConfirmPasswordField.setVisible(isConfirmPasswordVisible);
            visibleConfirmPasswordField.setManaged(isConfirmPasswordVisible);

            toggleConfirmPassword.setText(isConfirmPasswordVisible ? "🙈" : "👁");
        }

        @FXML
        private void handleGoogleLogin(ActionEvent event) {
            System.out.println("Google button clicked!");
        }

        @FXML
        private void handleFacebookLogin(ActionEvent event) {
            System.out.println("Facebook button clicked!");
        }

        @FXML
        private void handleInstagramLogin(ActionEvent event) {
            System.out.println("Instagram button clicked!");
        }




    }

