package org.example.Configs;

import net.java.games.input.Controller;
import org.example.Profiles.Profile;
import org.example.Utils.Gamepad;
import org.example.Utils.Range;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class ConfigPanel extends Config implements Serializable {

    static Gamepad gamepadController = new Gamepad();
    static Range rangeController = new Range();
    static Profile profilesController = new Profile();

    private String name;
    int posX;
    int posY;
    Point selectedPoint;


    public ConfigPanel(String value, ArrayList<Gamepad> gamepadSettup, ArrayList<Range> bindActivationValue, String name, int posX, int posY, Point selectedPoint) {
        super(value, gamepadSettup, bindActivationValue);
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.selectedPoint = selectedPoint;
    }

    public ConfigPanel() {
    }

    // checks that config is activated
    public boolean isConfigActivated(ConfigPanel cfgPanel)
    {
        int bindsAmount = cfgPanel.getBindsArray().size();
        if(bindsAmount > 0) {
            for (int i = 0; i < bindsAmount; i++) {
                Controller controller = gamepadController.getGamepadByName(profilesController.getCurrentUsingProfile().getGamepadName());
                net.java.games.input.Component control = cfgPanel.getBindsArray().get(i).getControlByName(controller, cfgPanel.getBindsArray().get(i).getControlId());

                float value = gamepadController.getControlValue(controller, control);
                float valueMin = cfgPanel.getBindActivationValueArray().get(i).getValueMin();
                float valueMax = cfgPanel.getBindActivationValueArray().get(i).getValueMax();

                if (valueMin > valueMax) {
                    float min = valueMax;
                    float max = valueMin;
                    valueMax = max;
                    valueMin = min;
                }

                if (!rangeController.isInRange(value, new Range(valueMin, valueMax))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosX() {
        return this.posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Point getSelectedPoint() {return this.selectedPoint;}

    public void setSelectedPoint(Point selectedPoint) {this.selectedPoint = selectedPoint;}
}
