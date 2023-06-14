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
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Language;
import model.TestResult1;
import model.Word;
import util.CountdownTimer;
import util.JumpingLettersAnimation;

public class View1 {
    private Stage primaryStage;
    private TestController_1 testController;
    
    private ComboBox<Integer> durationComboBox;
    
    
    private Label timerLabel;
    
    private Timeline countdownTimeline; 


    // GUI elements
    private Text testText;
    private TextArea userInputTextArea;
    private Text statisticsText;
    
    
    private Boolean isEndOfParagraph;

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
        
        
            // Keyboard shortcuts footer
        
        Label restartLabel = new Label(" - Restart Test");
        Label pauseLabel = new Label("Ctrl + Shift + P");
        Label pause = new Label(" -Pause");
        pauseLabel.getStyleClass().add("keyboard-label");
        pauseLabel.setWrapText(true);
        pauseLabel.setMaxWidth(60);
        
        
        Label endTestLabel = new Label("Esc");
        endTestLabel.getStyleClass().add("keyboard-label");
        endTestLabel.setWrapText(true);
        endTestLabel.setMaxWidth(60);
        
        
        Label tabLabel = new Label("Tab");
        tabLabel.getStyleClass().add("keyboard-label");
        tabLabel.setWrapText(true);
        tabLabel.setMaxWidth(60);

        Label enterLabel = new Label("Enter");
        enterLabel.getStyleClass().add("keyboard-label");
        enterLabel.setWrapText(true);
        enterLabel.setMaxWidth(60);

        HBox shortcutsFooter = new HBox(tabLabel,enterLabel, restartLabel, pauseLabel, pause, endTestLabel);
        shortcutsFooter.setSpacing(10);
        shortcutsFooter.setAlignment(Pos.CENTER_RIGHT);
        centerBox.getChildren().add(shortcutsFooter);

        // ...

        Scene scene = new Scene(root, 800, 600);

        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.P) {
                // Ctrl + Shift + P - Pause
                System.out.println("Pause");
                // Call the pause method or perform the pause action here
            } else if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                // Tab + Enter - Restart Test
                System.out.println("Restart Test");
                // Call the restart test method or perform the restart action here
            } else if (event.getCode() == KeyCode.ESCAPE) {
                // Esc - End Test
                System.out.println("End Test");
                // Call the end test method or perform the end test action here
            }
        });

        
        //TIME SELECTION
        
        ComboBox<Integer> durationComboBox = createDurationComboBox();
        
        
         // Timer label
        Label timerLabel = new Label();
        timerLabel.setStyle("-fx-font-size: 18; -fx-text-fill: #000080;");
        HBox timerBox = new HBox(timerLabel);
        timerBox.setAlignment(Pos.TOP_RIGHT);
        centerBox.getChildren().add(timerBox);
        
        durationComboBox.setOnAction(event -> {
            int selectedDuration = durationComboBox.getValue();
            testController.setTestDuration(selectedDuration);
            //testController.generateNextParagraph(); // Generate a new paragraph when duration is changed
            
            CountdownTimer.startCountdown(selectedDuration,timerLabel);
        });
        
        root.setBottom(durationComboBox);
        
        // Language selection
        ComboBox<Language> languageComboBox = createLanguageComboBox(); // Implement this method to create the language selection component
        languageComboBox.setOnAction(event -> {
            Language selectedLanguage = languageComboBox.getValue();
            AppConfig.setSelectedLanguage(selectedLanguage);
            testController.selectLanguage(selectedLanguage);
            
            isEndOfParagraph = false;
        });
        root.setTop(languageComboBox);

       
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
        
        // Listen for key events in the user input text area
        userInputTextArea.setOnKeyReleased(event -> {
            String input = userInputTextArea.getText();

            // Check if the event is triggered by the space bar key
            if (event.getCode() == KeyCode.SPACE) {
                AppConfig.setSpaceCount(AppConfig.getSpaceCount() + 1);
                System.out.println("Space count: " + AppConfig.getSpaceCount());
                
                // Check if the space count is greater than 29
                if (AppConfig.getSpaceCount() > 29) {
                    testController.generateNextParagraph();
                    AppConfig.setSpaceCount(0); // Reset the space count
                    
                    userInputTextArea.clear();
                    
                    userInputTextFlow.getChildren().clear();
                    
                }
            }

            // Call the handleUserInput method with the updated input
            testController.handleUserInput(input);
        });

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
            
            AppConfig.setIncorrectCount(0);
            AppConfig.setcorrectCount(0);

            // Clear the existing content in the TextFlow
            userInputTextFlow.getChildren().clear();

            // Split the input into words
            String[] inputWords = input.split(" ");
            System.out.println();            
            String[] paragraphWords = AppConfig.getCurrentParagraph().split(" ");
            
            
            
            
           for (int i = 0; i < paragraphWords.length; i++) {
                
                String paragraphWord = paragraphWords[i];
                

                for (int j = 0; j < paragraphWord.length(); j++) {
                    char paragraphChar = paragraphWord.charAt(j);

                    // Create a Text node for each character
                    Text charText = new Text(String.valueOf(paragraphChar));
                    JumpingLettersAnimation animation = new JumpingLettersAnimation(charText);

                    // Apply the default style class
                    charText.getStyleClass().add("default-text");

                    // Check if the character has been entered correctly
                    if (i < inputWords.length && j < inputWords[i].length()) {
                        char userChar = inputWords[i].charAt(j);
                        if (userChar == paragraphChar) {
                            // Apply the "correct" style class
                            
                            charText.getStyleClass().add("correct-text");
                            AppConfig.setcorrectCount(AppConfig.getcorrectCount()+1);
                            System.out.println("this is correct count" + " " + AppConfig.getcorrectCount());
                        }                        
                        
                        else {
                            AppConfig.setIncorrectCount(AppConfig.getIncorrectCount()+1);
                            System.out.println("this is incorrect count" + " " + AppConfig.getIncorrectCount());
                            // Apply the "incorrect" style class
                            charText.getStyleClass().add("incorrect-text");
                            animation.applyAnimation();
                                                                                   
                            
                        }
                    } else {
                        // Character has not been entered yet, apply the "not-entered" style class
                        charText.getStyleClass().add("not-entered-text");
                        
                        
                    }

                    // Add the Text node to the TextFlow
                    userInputTextFlow.getChildren().add(charText);
                    
                    
                }

                // Add a space after each word
                userInputTextFlow.getChildren().add(new Text(" "));
            }
            
            
            

            
            /*
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
                            
                        } else {
                            // Apply the "incorrect" style class
                            wordText.getStyleClass().add("incorrect-text");
                            animation.applyAnimation();
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
            */
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
    
    
    private ComboBox<Integer> createDurationComboBox() {
        ComboBox<Integer> durationComboBox = new ComboBox<>();
        durationComboBox.getItems().addAll(15, 20, 45, 60, 90, 120, 300);
        durationComboBox.setValue(60); // Set a default duration
        durationComboBox.setStyle("-fx-font-size: 14px; -fx-background-color: #808080; -fx-padding: 5px;");
         
        return durationComboBox;
    }


}