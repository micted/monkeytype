package controller;

import config.AppConfig;
import model.Language;
import model.TestResult1;
import model.Word;
import util.DictionaryLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class TestController_1 {
    private static final int DEFAULT_TEST_DURATION = 60; // seconds
    private static final int PARAGRAPH_LENGTH = 30; // words

    private List<Language> availableLanguages;
    private Language selectedLanguage;
    private int testDuration;
    private List<Word> paragraph;
    private StringBuilder userInput;
    private TestResult1 testResult;
    private Timer timer;    
    String directoryPath = "dictionary";

    public TestController_1() {
        
        this.availableLanguages = DictionaryLoader.loadAvailableLanguages(directoryPath);
        this.testDuration = DEFAULT_TEST_DURATION;
    }

    public List<Language> getAvailableLanguages() {
        return availableLanguages;
    }

    public void selectLanguage(Language language) {
        this.selectedLanguage = language;
        generateParagraph();
    }

    public void setTestDuration(int duration) {
        this.testDuration = duration;
    }
    
    public int checkControl() {
        return 1;
    }

    public void generateParagraph() {
        if (selectedLanguage != null) {
            List<String> dictionary = loadLanguageDictionary(selectedLanguage);
            int totalWords = dictionary.size();
            paragraph = new ArrayList<>();

            Random random = new Random();
            for (int i = 0; i < PARAGRAPH_LENGTH; i++) {
                int randomIndex = random.nextInt(totalWords);
                String wordText = dictionary.get(randomIndex);
                Word word = new Word(wordText);
                paragraph.add(word);
            }

            //AppConfig.getView().displayTestText(buildParagraphText());
            AppConfig.getView().displayTestText("hello man this is test text to check text coloring while typing");
            System.out.println(AppConfig.getView());
            startTimer();
        }
    }
    
    
    public List<Word> getParagraph() {
        return paragraph;
    }

    private List<String> loadLanguageDictionary(Language language) {
        // Assuming you have a dictionary file associated with each language
        String dictionaryFileName = language.getName() + ".txt";
        // Implement the logic to load the dictionary from the file
        // You can use file reading techniques or any other approach that fits your project structure
        // Return the list of dictionary words
        // Example implementation using Files.readAllLines:
        try {
            Path dictionaryPath = Paths.get(dictionaryFileName);
            return Files.readAllLines(dictionaryPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList(); // Return an empty list if there was an error loading the dictionary
    }

    private String buildParagraphText() {
        StringBuilder paragraphText = new StringBuilder();
        for (int i = 0; i < paragraph.size(); i++) {
            paragraphText.append(paragraph.get(i).getText());
            if (i < paragraph.size() - 1) {
                paragraphText.append(" ");
            }
        }
        return paragraphText.toString();
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                endTest();
            }
        }, testDuration * 1000); // Convert seconds to milliseconds
    }

    public void handleUserInput(String input) {
        System.out.println("handler called");
        System.out.println("This is the user input so far"+ " " + input);
        userInput = new StringBuilder(input);
        updateView();
        /*
        if (paragraph != null && !paragraph.isEmpty()) {
            
            userInput = new StringBuilder(input);
            updateView();
        }
        */

    }

    private void updateView() {
        if (userInput != null) {
            System.out.println("updating");
            AppConfig.getView().updateInputTextArea(userInput.toString());
            
           // testResult = calculateTestResult();
           // AppConfig.getView().updateStatistics(testResult);
        }
        else {
            System.out.println("user input is null");
        }
        
        
    }

    private TestResult1 calculateTestResult() {
        TestResult1 result = new TestResult1();
        int correctCount = 0;
        int incorrectCount = 0;
        int extraCount = 0;
        int missedCount = 0;

        int userInputLength = userInput.length();
        int paragraphLength = paragraph.size();

        int minSize = Math.min(userInputLength, paragraphLength);
        for (int i = 0; i < minSize; i++) {
            Word word = paragraph.get(i);
            String userWord = userInput.substring(i * (word.getText().length() + 1), (i + 1) * (word.getText().length() + 1) - 1);

            if (userWord.equals(word.getText())) {
                correctCount++;
            } else {
                incorrectCount++;
            }
        }

        extraCount = userInputLength > paragraphLength ? (userInputLength - paragraphLength) : 0;
        missedCount = paragraphLength > userInputLength ? (paragraphLength - userInputLength) : 0;

        result.setCorrectCount(correctCount);
        result.setIncorrectCount(incorrectCount);
        result.setExtraCount(extraCount);
        result.setMissedCount(missedCount);
        result.setAccuracy((int) (((double) correctCount / paragraphLength) * 100));

        return result;
    }

    private void endTest() {
        if (timer != null) {
            timer.cancel();
        }

        // Generate test result file
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String fileName = "TestResult_" + timestamp + ".txt";

        // Generate file content
        StringBuilder fileContent = new StringBuilder();
        fileContent.append("Test Result:\n");
        fileContent.append("Test Duration: ").append(testDuration).append(" seconds\n");
        fileContent.append("Paragraph:\n");
        for (Word word : paragraph) {
            fileContent.append(word.getText()).append(" ");
        }
        fileContent.append("\n\nUser Input:\n");
        fileContent.append(userInput.toString());
        fileContent.append("\n\nStatistics:\n");
        fileContent.append("Correct: ").append(testResult.getCorrectCount()).append("\n");
        fileContent.append("Incorrect: ").append(testResult.getIncorrectCount()).append("\n");
        fileContent.append("Extra: ").append(testResult.getExtraCount()).append("\n");
        fileContent.append("Missed: ").append(testResult.getMissedCount()).append("\n");
        fileContent.append("Accuracy: ").append(testResult.getAccuracy()).append("%");

        // Save the file
        //FileUtil.saveFile(fileName, fileContent.toString());
    }

    
}
