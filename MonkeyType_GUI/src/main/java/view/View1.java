package view;

import config.AppConfig;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;

import controller.TestController_1;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.List;
import javafx.stage.Stage;
import model.Language;
import model.TestResult1;
import model.Word;
import util.JumpingLettersAnimation;

public class View1 {
    private Stage primaryStage;
    private TestController_1 testController;

    // GUI elements
    private Text testText;
    private TextArea userInputTextArea;
    private Text statisticsText;

    private VBox centerBox; // Declare centerBox as a class-level variable
    private TextFlow userInputTextFlow;
    String dummyParagraph = "This is a dummy paragraph for testing color variation.";

    public View1(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.testController = new TestController_1();
    }

    public void initialize() throws MalformedURLException {
        primaryStage.setTitle("Monkeytype-like Application");

        BorderPane root = new BorderPane();

        // Center area for test text and user input
        centerBox = createCenterBox();
        root.setCenter(centerBox);
        
        
        // Language selection
        ComboBox<Language> languageComboBox = createLanguageComboBox(); // Implement this method to create the language selection component
        languageComboBox.setOnAction(event -> {
            Language selectedLanguage = languageComboBox.getValue();
            testController.selectLanguage(selectedLanguage);
        });
        root.setTop(languageComboBox);

        Scene scene = new Scene(root, 800, 600);
        File cssFile = new File("C://Users//Hp//Documents//PJAIT documents//GUI//Monkeytype//MonkeyType_GUI//src//main//java//styles/styles.css");
        URL cssUrl = cssFile.toURI().toURL();
        
        scene.getStylesheets().add(cssUrl.toExternalForm());
        //scene.getStylesheets().add(view.View1.class.getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        // Generate and display test text
        //String dummyParagraph = "This is a dummy paragraph for testing color variation.";
        //displayTestText(dummyParagraph);
    }


    private VBox createCenterBox() {
        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(20);
        centerBox.setPadding(new Insets(10));
        
        centerBox.widthProperty().addListener((obs, oldWidth, newWidth) -> {
        if (testText != null) {
            testText.setWrappingWidth(newWidth.doubleValue() - 20);
        }        
        });


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

        // User input text flow
        userInputTextFlow = new TextFlow();  // Initialize userInputTextFlow
        userInputTextFlow.setStyle("-fx-font-size: 18");
        centerBox.getChildren().add(userInputTextFlow);

        // Statistics text
        statisticsText = new Text();
        centerBox.getChildren().add(statisticsText);

        return centerBox;
}


    public void displayTestText(String text) {
        Platform.runLater(() -> testText.setText(text));
        testText.setWrappingWidth(centerBox.getWidth() - 20);
    }
    
    public void updateInputTextArea(String input) {
        Platform.runLater(() -> {
            //userInputTextArea.setText(input);

            // Clear the existing content in the TextFlow
            userInputTextFlow.getChildren().clear();

            // Split the input into words
            String[] inputWords = input.split(" ");
            System.out.println();            
            String[] paragraphWords = AppConfig.getCurrentParagraph().split(" ");
            
            

            for (int i = 0; i < paragraphWords.length; i++) {
                String paragraphWord = paragraphWords[i];

                // Create a Text node for each word
                Text wordText = new Text(paragraphWord);
                JumpingLettersAnimation animation = new JumpingLettersAnimation(wordText);
                

                // Apply the default style class
                wordText.getStyleClass().add("default-text");

                // Check if the word has been entered by the user
                if (i < inputWords.length) {
                    String userWord = inputWords[i];

                    // Check each character in the word
                    for (int j = 0; j < paragraphWord.length(); j++) {
                        char paragraphChar = paragraphWord.charAt(j);

                        // Check if the character is correctly entered
                        if (j < userWord.length() && userWord.charAt(j) == paragraphChar) {
                            // Apply the "correct" style class
                            wordText.getStyleClass().add("correct-text");
                            animation.applyAnimation();
                        } else {
                            // Apply the "incorrect" style class
                            wordText.getStyleClass().add("incorrect-text");
                        }
                    }
                } else {
                    // Word has not been entered yet, apply the "not-entered" style class
                    wordText.getStyleClass().add("not-entered-text");
                }

                // Add the Text node to the TextFlow
                userInputTextFlow.getChildren().add(wordText);

                // Add a space after each word
                userInputTextFlow.getChildren().add(new Text(" "));
            }
        });
    }



   

    public void updateStatistics(TestResult1 testResult) {
        Platform.runLater(() -> {
            String statistics = "Correct: " + testResult.getCorrectCount() +
                    "  Incorrect: " + testResult.getIncorrectCount() +
                    "  Extra: " + testResult.getExtraCount() +
                    "  Missed: " + testResult.getMissedCount() +
                    "  Accuracy: " + testResult.getAccuracy() + "%";
            statisticsText.setText(statistics);
        });
    }
    
    
    private ComboBox<Language> createLanguageComboBox() {
        ComboBox<Language> languageComboBox = new ComboBox<>();

        // Load available languages from the test controller
        List<Language> availableLanguages = testController.getAvailableLanguages();

        // Add available languages to the combo box
        languageComboBox.getItems().addAll(availableLanguages);

        // Set a default selection
        if (!availableLanguages.isEmpty()) {
            languageComboBox.setValue(availableLanguages.get(0));
        }

        return languageComboBox;
    }

}