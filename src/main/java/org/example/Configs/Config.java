package org.example.Configs;

import org.example.Utils.Gamepad;
import org.example.Utils.Range;

import java.io.Serializable;
import java.util.ArrayList;

public class Config implements Serializable {
    private String value;
    private ArrayList<Gamepad> bind;
    private ArrayList<Range> bindActivationValue;

    Config (String value,ArrayList<Gamepad> bind,ArrayList<Range> bindActivationValue)
    {
        this.value = value;
        this.bind = bind;
        this.bindActivationValue = bindActivationValue;
    }

    Config()
    {

    }
    // Generated setters
    public void setValue(String value) {
        this.value = value;
    }


    // Generated getters
    public String getValue() {return this.value;}

    public ArrayList<Gamepad> getBindsArray() {
        return this.bind;
    }

    public ArrayList<Range> getBindActivationValueArray() {
        return this.bindActivationValue;
    }

}
