package model;

import config.AppConfig;

public class TestResult {
    private int correctCount;
    private int incorrectCount;
    private int extraCount;
    private int missedCount;
    private int accuracy;

    public TestResult() {
        // Initialize the counts to zero
        correctCount = AppConfig.getcorrectCount();
        incorrectCount = AppConfig.getIncorrectCount();
        extraCount = 0;
        missedCount = 0;
        accuracy = 0;
    }

    // Getters and setters for the fields

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getIncorrectCount() {
        return incorrectCount;
    }

    public void setIncorrectCount(int incorrectCount) {
        this.incorrectCount = incorrectCount;
    }

    public int getExtraCount() {
        return extraCount;
    }

    public void setExtraCount(int extraCount) {
        this.extraCount = extraCount;
    }

    public int getMissedCount() {
        return missedCount;
    }

    public void setMissedCount(int missedCount) {
        this.missedCount = missedCount;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
}
