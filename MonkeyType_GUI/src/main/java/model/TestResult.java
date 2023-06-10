/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class TestResult {
    private int correctCount;
    private int incorrectCount;
    private int extraCount;
    private int missedCount;

    public TestResult(int correctCount, int incorrectCount, int extraCount, int missedCount) {
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
        this.extraCount = extraCount;
        this.missedCount = missedCount;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getIncorrectCount() {
        return incorrectCount;
    }

    public int getExtraCount() {
        return extraCount;
    }

    public int getMissedCount() {
        return missedCount;
    }

    public double getAccuracy() {
        int totalCount = correctCount + incorrectCount + extraCount + missedCount;
        if (totalCount > 0) {
            return (double) correctCount / totalCount * 100;
        } else {
            return 0;
        }
    }
}

