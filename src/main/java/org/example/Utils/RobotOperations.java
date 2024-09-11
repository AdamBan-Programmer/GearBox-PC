package org.example.Utils;

import org.example.Configs.ConfigPanel;

import javax.swing.*;
import java.awt.*;

public class RobotOperations {

    public RobotOperations() {
    }

    public char PressKey(ConfigPanel updatedCfgPanel, char lastPressed) throws AWTException {
        Robot robot = new Robot();
        if (updatedCfgPanel.getValue().toCharArray().length > 0) {
            char charToPress = updatedCfgPanel.getValue().toCharArray()[0];
            if (lastPressed != charToPress) {
                robot.keyRelease(lastPressed);
                robot.keyPress(charToPress);
                return charToPress;
            }
        }
        return lastPressed;
    }
}
