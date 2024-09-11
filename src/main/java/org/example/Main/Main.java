package org.example.Main;

import org.example.GUI.CentralGUI;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new CentralGUI();
            }
        });
    }
}
