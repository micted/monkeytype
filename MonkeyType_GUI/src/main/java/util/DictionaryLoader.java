package util;

import java.io.BufferedReader;
import model.Language;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.Word;

public class DictionaryLoader {

    public static List<Language> loadAvailableLanguages(String directoryPath) {
        List<Language> availableLanguages = new ArrayList<>();

        try {
            // Get a list of all files in the directory
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();

            if (files != null) {
                // Iterate over the files and create Language objects
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        String languageName = fileName.substring(0, fileName.lastIndexOf('.'));
                        Path filePath = Paths.get(file.getAbsolutePath());

                        // Read the file content and create a Language object
                        List<String> lines = Files.readAllLines(filePath);
                        Language language = new Language(languageName, lines);
                        availableLanguages.add(language);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return availableLanguages;
    }
    
    
}
