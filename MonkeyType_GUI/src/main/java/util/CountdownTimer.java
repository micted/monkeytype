/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import config.AppConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;


public class CountdownTimer {

    private static Timeline countdownTimeline;
    private static int duration = AppConfig.getTimer();
    private static boolean isPaused;

    public static void startCountdown(int duration, Label timerLabel, Runnable onTimeUp) {

        AppConfig.setTimer(duration);

        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }

        countdownTimeline = new Timeline();
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            AppConfig.setTimer(AppConfig.getTimer() - 1);
            if (AppConfig.getTimer() >= 0) {
                timerLabel.setText("Time Remaining: " + AppConfig.getTimer() + "s");
            } else {
                timerLabel.setText("Time's up!");
                int totalTyped = AppConfig.getcorrectCount() + AppConfig.getIncorrectCount();
                System.out.println("here is total typed" + " " + totalTyped);
                AppConfig.setTotalTyped(totalTyped);
                AppConfig.setWPM(totalTyped*60/ duration);
                countdownTimeline.stop();
                onTimeUp.run(); // Call the onTimeUp callback
            }
        });

        countdownTimeline.getKeyFrames().add(keyFrame);
        countdownTimeline.play();
    }
    
    public static void pauseCountdown() {
        isPaused = true;
        if (countdownTimeline != null) {
            countdownTimeline.pause();
        }
    }

    public static void resumeCountdown() {
        isPaused = false;
        if (countdownTimeline != null) {
            countdownTimeline.play();
        }
    }
    
}
