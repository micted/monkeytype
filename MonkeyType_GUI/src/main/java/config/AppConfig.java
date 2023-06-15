/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import model.Language;
import view.View;

/**
 *
 * @author Hp
 */
public class AppConfig {
    private static AppConfig instance;
    private static View view;
    private static String paragraphText;
    private static int duration;
    private static Language selectedLanguage;
    private static int spaceCount = 0;
    private static int incorrectCount = 0;
    private static int correctCount = 0;
    private static int missedCount = 0;
    private static int totalTyped = 0;
    private static double wpm = 0;

    
    public static View getView() {
        return view;
    }

    public static void setView(View view) {
        AppConfig.view = view;
    }
    
    public static void setCurrentParagraph(String paragraphText) {
        
        AppConfig.paragraphText = paragraphText;
        
    }
    public static String getCurrentParagraph() {
        
        return paragraphText;
        
    }
    
    public static void setTimer(int duration) {
        
        AppConfig.duration = duration;
    
    }
    
    public static int getTimer() {
    
        return duration;
    }
    
    public static void setSpaceCount(int spaceCount) {
        
        AppConfig.spaceCount = spaceCount;
    
    }
    
    public static int getSpaceCount() {
    
        return spaceCount;
    }
    
    public static void setSelectedLanguage(Language language) {
        AppConfig.selectedLanguage = language;
}

    public static Language getSelectedLanguage() {
        return selectedLanguage;
    }
    
    public static void setIncorrectCount(int incorrectCount) {
        AppConfig.incorrectCount = incorrectCount;
    }
    
    public static int getIncorrectCount() {
        return incorrectCount;
    }
    
    public static void setcorrectCount(int correctCount) {
        AppConfig.correctCount = correctCount;
    }
    
    
    public static int getcorrectCount() {
        return correctCount;
    }
    
    public static void setTotalTyped(int totalTyped) {
        AppConfig.totalTyped = totalTyped;
    }
    
    public static int getTotalTyped() {
        
        return totalTyped;
    
    }
    
    public static void setWPM(double wpm) {
        AppConfig.wpm = wpm;
    }
   

    public static String getWPM() {
        return String.valueOf(wpm);
    }
    
    public static double getdoubleWPM() {
        return wpm;
    }


   
}

