package util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestResultGenerator {

    public static void generateTestResult(double wpm, int correctCount, int incorrectCount, String directory) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fileName = "test_result_" + now.format(formatter) + ".txt";
        Path filePath = Path.of(directory, fileName);

        try {
            String content = "Test Result\n" +
                    "WPM: " + wpm + "\n" +
                    "Correct Count: " + correctCount + "\n" +
                    "Incorrect Count: " + incorrectCount + "\n";

            Files.writeString(filePath, content, StandardOpenOption.CREATE);

            System.out.println("Test result generated successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to generate test result file: " + e.getMessage());
        }
    }
}
