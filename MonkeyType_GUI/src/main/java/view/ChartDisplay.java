package view;

import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

public class ChartDisplay {
    public void displayChart(double wpm, int correctCount, int incorrectCount, int totalTyped) {
        Stage stage = new Stage();
        stage.setTitle("Statistics Chart");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Data");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Statistics");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("WPM", wpm));
        series.getData().add(new XYChart.Data<>("Correct", correctCount));
        series.getData().add(new XYChart.Data<>("Incorrect", incorrectCount));
        series.getData().add(new XYChart.Data<>("Total Typed", totalTyped));

        lineChart.getData().add(series);

        Scene scene = new Scene(lineChart, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}
