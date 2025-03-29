package com.byteme.bytemeapplication.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/byteme/bytemeapplication/fxml/LoginView.fxml")));
        stage.setScene(scene);
        stage.setTitle("ByteMe AI Tutor");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
