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

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import controller.TestController;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import java.util.List;
import javafx.animation.Timeline;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Language;
import model.TestResult;
import model.Word;
import util.CountdownTimer;
import util.JumpingLettersAnimation;
import util.TestResultGenerator;

public class View {
    private Stage primaryStage;
    private TestController testController;
    
    private ComboBox<Integer> durationComboBox;
    
    
    private Label timerLabel;
    
    private Timeline countdownTimeline; 


    // GUI elements
    private Text testText;
    private TextArea userInputTextArea;
    private Text statisticsText;
    
    
    // Statistics line chart
    private LineChart<String, Number> statisticsLineChart;
    private XYChart.Series<String, Number> totalTypedSeries;
    private XYChart.Series<String, Number> wpmSeries;
    private XYChart.Series<String, Number> correctSeries;
    private XYChart.Series<String, Number> incorrectSeries;
    
    
    private Boolean isEndOfParagraph;

    private VBox centerBox; // Declare centerBox as a class-level variable
    private TextFlow userInputTextFlow;
    String dummyParagraph = "This is a dummy paragraph for testing color variation.";

    public View(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.testController = new TestController();
    }

    public void initialize() throws MalformedURLException {
        primaryStage.setTitle("Monkeytype-like Application");

        BorderPane root = new BorderPane(); 
        AppConfig.setTotalTyped(0);
        AppConfig.setWPM(0.0);
         

        // Initialize the statistics line chart
        initializeStatisticsLineChart();
        
        
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
                pauseTest();
                
            } else if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                // Tab + Enter - Restart Test
                System.out.println("Restart Test");
                restartTest();
                
            } else if (event.getCode() == KeyCode.ESCAPE) {
                // Esc - End Test
                System.out.println("End Test");                
                onTimeUp();
                
                // Wait for 3 seconds before exiting the application
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                Platform.exit();
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
            //testController.generateNextParagraph();
            
            CountdownTimer.startCountdown(selectedDuration,timerLabel,this::onTimeUp);
        });
        
        root.setBottom(durationComboBox);
        
        // Language selection
        ComboBox<Language> languageComboBox = createLanguageComboBox(); 
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
        
        primaryStage.setScene(scene);
        primaryStage.show();

       
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
        
        // key events 
        userInputTextArea.setOnKeyReleased(event -> {
            String input = userInputTextArea.getText();

            // Check if the event is triggered by the space bar key
            if (event.getCode() == KeyCode.SPACE) {
                AppConfig.setSpaceCount(AppConfig.getSpaceCount() + 1);
                System.out.println("Space count: " + AppConfig.getSpaceCount());
                
                // Check if the space count is greater than 29 to determine end of paragraph
                if (AppConfig.getSpaceCount() > 29) {
                    
                    testController.generateNextParagraph();
                    AppConfig.setSpaceCount(0); // Reset the space count
                    
                    userInputTextArea.clear();
                    
                    userInputTextFlow.getChildren().clear();
                    
                }
            }

           
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
                           
                            charText.getStyleClass().add("incorrect-text");
                            animation.applyAnimation();
                                                                                   
                            
                        }
                    } else {
                        
                        charText.getStyleClass().add("not-entered-text");
                        
                        
                    }

                    // Add the Text node to the TextFlow
                    userInputTextFlow.getChildren().add(charText);
                    
                    
                }

                // Add a space after each word
                userInputTextFlow.getChildren().add(new Text(" "));
            }
           
            
            

           
        });
    }

    
    
    
    
    
    
    private void initializeStatisticsLineChart() {
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Time");

        statisticsLineChart = new LineChart<>(xAxis, yAxis);
        statisticsLineChart.setTitle("Statistics");

        totalTypedSeries = new XYChart.Series<>();
        totalTypedSeries.setName("Total Typed");

        wpmSeries = new XYChart.Series<>();
        wpmSeries.setName("WPM");

        correctSeries = new XYChart.Series<>();
        correctSeries.setName("Correct");

        incorrectSeries = new XYChart.Series<>();
        incorrectSeries.setName("Incorrect");

        statisticsLineChart.getData().addAll(totalTypedSeries, wpmSeries, correctSeries, incorrectSeries);
    }
    
    

    public void updateStatistics(TestResult testResult) {
        Platform.runLater(() -> {
            String statistics = "Correct: " + AppConfig.getcorrectCount() +
                    "  Incorrect: " + AppConfig.getIncorrectCount() +
                    "  Extra: " + testResult.getExtraCount() +
                    "  Accuracy: " + (AppConfig.getcorrectCount()*100)/AppConfig.getTotalTyped() + "%" +" "+
                    "  WPM: " + AppConfig.getWPM();
            statisticsText.setText(statistics);
        });
    }
    
    
    
    
    private ComboBox<Language> createLanguageComboBox() {
        ComboBox<Language> languageComboBox = new ComboBox<>();

        // Load available languages from the test controller
        List<Language> availableLanguages = testController.getAvailableLanguages();

        // Add available languages to the combo box
        languageComboBox.getItems().addAll(availableLanguages);

        // default selection
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

    private void onTimeUp() {
         // Display statistics
         TestResult testResult = new TestResult();
         updateStatistics(testResult);

        
        double wpm = AppConfig.getdoubleWPM();
        int correctCount = AppConfig.getcorrectCount();
        int incorrectCount = AppConfig.getIncorrectCount();
        int totalTyped = AppConfig.getTotalTyped();
        //double accuracy = (AppConfig.getcorrectCount()*100)/totalTyped;

        ChartDisplay chartDisplay = new ChartDisplay();
        chartDisplay.displayChart(wpm, correctCount, incorrectCount, totalTyped);
        
        
        //FILE GENERATOR
        String directory = "C://Users//Hp//Documents//PJAIT documents//GUI//Monkeytype//MonkeyType_GUI//src//main//java//txt";
        TestResultGenerator.generateTestResult(wpm, correctCount, incorrectCount,directory);
        


        
    }
    
    private void restartTest() {
        
        testController.generateNextParagraph();
        AppConfig.setSpaceCount(0); 
        userInputTextArea.clear();
        userInputTextFlow.getChildren().clear();
    }
    
    // Pause the test
    private void pauseTest() {
        
        CountdownTimer.pauseCountdown(); 
    }


}