package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

public class ProgressController {
    @FXML private ComboBox<String> subjectComboBox;

    @FXML private Label averageLabel, quizzesCompletedLabel, timePerQuestionLabel, streakLabel;

    @FXML private Text currentTopic;

    @FXML private ListView<String> strengthsList, weaknessesList;

    @FXML private LineChart<String, Number> quizPerformanceChart;


    @FXML
    public void initialize() {
        subjectComboBox.getItems().addAll("Chemistry", "Physics", "Mathematical Methods");
        subjectComboBox.setValue("Chemistry");

        subjectComboBox.setOnAction(event -> updateSubjectData(subjectComboBox.getValue()));

        updateSubjectData("Chemistry");
    }

    private void updateSubjectData(String subject){
        strengthsList.getItems().clear();
        weaknessesList.getItems().clear();
        quizPerformanceChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(subject + " Scores");

        switch (subject) {
            case "Chemistry":
                averageLabel.setText("82");
                quizzesCompletedLabel.setText("23");
                timePerQuestionLabel.setText("45 sec");
                streakLabel.setText("3 days");
                currentTopic.setText("Organic Chemistry\n • Stoichiometry");
                strengthsList.getItems().addAll("Concept recall", "Reaction balancing", "Time management");
                weaknessesList.getItems().addAll("Nomenclature", "Mechanisms", "Speed under pressure");
                series.getData().add(new XYChart.Data<>("Quiz 1", 60));
                series.getData().add(new XYChart.Data<>("Quiz 2", 65));
                series.getData().add(new XYChart.Data<>("Quiz 3", 70));
                series.getData().add(new XYChart.Data<>("Quiz 4", 75));
                break;

            case "Physics":
                averageLabel.setText("57.5");
                quizzesCompletedLabel.setText("20");
                timePerQuestionLabel.setText("40 sec");
                streakLabel.setText("3 days");
                currentTopic.setText("Electric Circuits\n • ");
                strengthsList.getItems().addAll("Concept recall", "Calculations with Voltage and current", "Time management");
                weaknessesList.getItems().addAll("Nomenclature", "Mechanisms", "Speed under pressure");
                series.getData().add(new XYChart.Data<>("Quiz 1", 50));
                series.getData().add(new XYChart.Data<>("Quiz 2", 55));
                series.getData().add(new XYChart.Data<>("Quiz 3", 60));
                series.getData().add(new XYChart.Data<>("Quiz 4", 65));
                break;

            case "Mathematical Methods":
                averageLabel.setText("57.5");
                quizzesCompletedLabel.setText("20");
                timePerQuestionLabel.setText("40 sec");
                streakLabel.setText("3 days");
                currentTopic.setText("Differentiation and introductory to calculus\n • ");
                strengthsList.getItems().addAll("Concept recall", "Differentiating using quotient rule", "Time management");
                weaknessesList.getItems().addAll("Nomenclature", "chain rule", "Speed under pressure");
                series.getData().add(new XYChart.Data<>("Quiz 1", 99));
                series.getData().add(new XYChart.Data<>("Quiz 2", 40));
                series.getData().add(new XYChart.Data<>("Quiz 3", 60));
                series.getData().add(new XYChart.Data<>("Quiz 4", 75));
                break;
        }
        quizPerformanceChart.getData().add(series);
    }
}


