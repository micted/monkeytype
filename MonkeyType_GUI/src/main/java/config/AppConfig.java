/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import view.View1;

/**
 *
 * @author Hp
 */
public class AppConfig {
    private static AppConfig instance;
    private static View1 view;

    
    public static View1 getView() {
        return view;
    }

    public static void setView(View1 view) {
        AppConfig.view = view;
    }
}

