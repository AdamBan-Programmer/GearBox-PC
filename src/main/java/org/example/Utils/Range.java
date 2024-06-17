package org.example.Utils;

import java.io.Serializable;

public class Range implements Serializable {

    private float valueMin;
    private float valueMax;

    public Range(float valueMin, float valueMax) {
        this.valueMin = valueMin;
        this.valueMax = valueMax;
    }

    public Range() {
    }

    public boolean isInRange(float value,Range range)
    {
        return (value >= range.getValueMin() && value <= range.getValueMax());
    }

    public float getValueMin() {
        return this.valueMin;
    }

    public float getValueMax() {
        return this.valueMax;
    }
}
