package org.example.Utils;

import lombok.Getter;
import lombok.Setter;
import net.java.games.input.*;


import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

@Getter
@Setter
public class Gamepad implements Serializable{

    private static final long serialVersionUID = -5784725879521831210L;

    private String controlId;
    public Gamepad()
    {
    }

    //returns all recognized controllers
    public Controller[] getAllControllers()
    {
        return ControllerEnvironment.getDefaultEnvironment().getControllers();
    }

    //returns controller found by name
    public Controller getGamepadByName(String gamepadName) {
        Controller[] controllers = getAllControllers();
        for(Controller controller : controllers)
        {
            if(controller.getName().equals(gamepadName))
            {
                return controller;
            }
        }
        return null;
    }

    //returns last used config component
    public Component getDetectedGamepadControl(Controller controller)
    {
        float[] oldGamepadState = getActualGamepadState(controller);
        while (true) {
            controller.poll();
            float[] newGamePadState = getActualGamepadState(controller);
            Component[] components = controller.getComponents();
            Component detectedControl = getChangedComponent(oldGamepadState,newGamePadState,components);
            if(detectedControl != null)
            {
                return detectedControl;
            }
        }
    }

    //returns Component by name
    public Component getControlByName(Controller controller, String name)
    {
        Component[] components = controller.getComponents();
        for(Component component : components)
        {
            if(component.getIdentifier().toString().equals(name))
            {
                return component;
            }
        }
        return null;
    }

    //returns control value
    public float getControlValue(Controller controller, Component control) {
        float[] controls_values = getActualGamepadState(controller);
        Component[] controls = controller.getComponents();

        for(int i =0;i<controls_values.length;i++)
        {
            if(controls[i] == control)
            {
                return controls_values[i];
            }
        }
        return 0;
    }

    // returns all controller buttons values
    private float[] getActualGamepadState(Controller controller)
    {
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        format.setMaximumFractionDigits(1);
        controller.poll();
        Component[] components = controller.getComponents();
        float[] values = new float[components.length];
        for(int i =0;i<components.length;i++) {
            Component component = components[i];
            values[i] = Float.parseFloat(format.format(component.getPollData()));
        }
        return values;
    }

    //returns component that changed value
    private Component getChangedComponent(float[] oldValues, float[] newValues, Component[] components)
    {
        int length = oldValues.length;
        for(int i =0;i<length;i++)
        {
            if(oldValues[i] != newValues[i])
            {
                return components[i];
            }
        }
        return null;
    }
}
