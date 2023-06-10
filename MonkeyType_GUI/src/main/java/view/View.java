/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import controller.TestController;
import model.TestResult;

public class View {
    private Stage primaryStage;
    private TestController testController;

    // GUI elements
    private Text testText;
    private TextArea userInputTextArea;
    private Text statisticsText;

    public View(Stage primaryStage, TestController testController) {
        this.primaryStage = primaryStage;
        this.testController = testController;
    }

    public void initialize() {
        primaryStage.setTitle("Monkeytype-like Application");

        BorderPane root = new BorderPane();

        // Center area for test text and user input
        VBox centerBox = createCenterBox();
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set dummy test text
        displayTestText("This is a test text. Type it as quickly as possible!");

        // Set dummy statistics
        TestResult dummyResult = new TestResult(50, 5, 2, 3);
        updateStatistics(dummyResult);
    }

    private VBox createCenterBox() {
        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(20);
        centerBox.setPadding(new Insets(10));

        // Test text
        testText = new Text();
        testText.setStyle("-fx-font-size: 20");
        centerBox.getChildren().add(testText);

        // User input text area
        userInputTextArea = new TextArea();
        userInputTextArea.setWrapText(true);
        userInputTextArea.setStyle("-fx-font-size: 18");
        userInputTextArea.setPrefRowCount(5);
        userInputTextArea.setOnKeyReleased(e -> testController.handleUserInput(userInputTextArea.getText()));
        centerBox.getChildren().add(userInputTextArea);

        // Statistics text
        statisticsText = new Text();
        centerBox.getChildren().add(statisticsText);

        return centerBox;
    }

    public void displayTestText(String text) {
        Platform.runLater(() -> testText.setText(text));
    }

    public void updateInputTextArea(String input) {
        Platform.runLater(() -> userInputTextArea.setText(input));
    }

    public void updateStatistics(TestResult testResult) {
        Platform.runLater(() -> {
            String statistics = "Correct: " + testResult.getCorrectCount() +
                    "  Incorrect: " + testResult.getIncorrectCount() +
                    "  Extra: " + testResult.getExtraCount() +
                    "  Missed: " + testResult.getMissedCount() +
                    "  Accuracy: " + testResult.getAccuracy() + "%";
            statisticsText.setText(statistics);
        });
    }
}
