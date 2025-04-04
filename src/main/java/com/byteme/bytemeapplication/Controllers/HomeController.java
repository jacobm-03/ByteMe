package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label; // ✅ You need this import

public class HomeController {

    @FXML private Label navHome, navCourses, navProgress, navProfile;

    @FXML
    private void handleNavClick() {
        System.out.println("Navigation label clicked.");
    }




}
