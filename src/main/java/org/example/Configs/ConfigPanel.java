package org.example.Configs;

import lombok.Getter;
import lombok.Setter;
import net.java.games.input.Controller;
import org.example.Profiles.Profile;
import org.example.Utils.Gamepad;
import org.example.Utils.Range;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
public class ConfigPanel extends Config implements Serializable {

    static Gamepad gamepadController = new Gamepad();

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
        int bindsAmount = cfgPanel.getBind().size();
        if(bindsAmount > 0) {
            for (int i = 0; i < bindsAmount; i++) {
                Controller controller = gamepadController.getGamepadByName(Profile.getInstance().getGamepadName());
                net.java.games.input.Component control = cfgPanel.getBind().get(i).getControlByName(controller, cfgPanel.getBind().get(i).getControlId());

                Range activation = cfgPanel.getActivationValue().get(i);
                float value = gamepadController.getControlValue(controller, control);
                float valueMin = activation.getValueMin();
                float valueMax = activation.getValueMax();

                if (valueMin > valueMax) {
                    float min = valueMax;
                    float max = valueMin;
                    valueMax = max;
                    valueMin = min;
                }

                Range range = new Range(valueMin, valueMax);
                if (!range.isInRange(value)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
