package org.example.Configs;

import lombok.Getter;
import lombok.Setter;
import org.example.Utils.Gamepad;
import org.example.Utils.Range;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
public class Config implements Serializable {
    private String value;
    private ArrayList<Gamepad> bind;
    private ArrayList<Range> activationValue;

    Config (String value,ArrayList<Gamepad> bind,ArrayList<Range> activationValue)
    {
        this.value = value;
        this.bind = bind;
        this.activationValue = activationValue;
    }

    Config()
    {
    }
}
