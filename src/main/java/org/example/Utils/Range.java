package org.example.Utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class Range implements Serializable {

    private float valueMin;
    private float valueMax;

    public Range(float valueMin, float valueMax) {
        this.valueMin = valueMin;
        this.valueMax = valueMax;
    }

    public boolean isInRange(float value)
    {
        return (value >= this.getValueMin() && value <= this.getValueMax());
    }
}
