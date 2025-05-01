package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;

public class HomeContentController {
    @FXML
    private void handleCoursesClick(MouseEvent event) {
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/CourseView.fxml");
        } catch (IOException e) {
            System.out.println("❌ Error loading Courses content");
            e.printStackTrace();
        }
    }
}
