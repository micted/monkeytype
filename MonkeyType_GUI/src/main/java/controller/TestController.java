/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;



import model.TestResult;

public class TestController {
    private TestResult testResult;

    public TestController() {
        // Initialize the test result with dummy data
        testResult = new TestResult(10, 2, 1, 3);
    }

    public void handleUserInput(String userInput) {
        // Perform any necessary logic based on user input
    }

    public TestResult getTestResult() {
        return testResult;
    }
}
