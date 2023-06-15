/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;



import config.AppConfig;
import javafx.application.Application;
import javafx.stage.Stage;

import controller.TestController;
import controller.TestController_1;
import java.net.MalformedURLException;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        
        //TestController_1 testController = new TestController_1();
        View1 view1 = new View1(primaryStage);
        AppConfig.setView(view1);
        view1.initialize();
        System.out.println(view1);
    }

}

