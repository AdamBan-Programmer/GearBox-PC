package org.example.Utils;

import javax.swing.*;
import java.awt.*;

public class ScaleLayout extends JFrame {
    public ScaleLayout() {
        //empty
    }

    // Gets Values and sets parameters
    public void setScallingParams(float percentOfWidth, float percentOfHeight, float percentOfControlHeight, float percentOfTopMargin, float percentOfLeftMargin, Component component, Container frame) {
        Point screenSize = new Point(getScreenWidth(frame), getScreenHeight(frame));
        setScaledSize(percentOfWidth, percentOfHeight, percentOfControlHeight, component, screenSize);
        setScaledMargins(percentOfTopMargin, percentOfLeftMargin, component, screenSize);
    }

    // Gets width
    public int getScreenWidth(Container frame) {
        return frame.getWidth();
    }

    // Gets height
    public int getScreenHeight(Container frame) {
        return frame.getHeight();
    }

    //converts pixels into percent
    public float convertPixelsToPercent(int px, int fullPx) {
        double finalPercent = (((double) px / (double) fullPx) * 100);
        return (float) finalPercent;
    }

    //converts percent on pixels
    public int convertPercentToPixels(float percent, int fullPx) {
        return (int) (fullPx - fullPx * ((double) (100 - percent) / 100));
    }

    // Setting Size
    private void setScaledSize(float percentOfWidth, float percentOfHeight, float percentOfControlHeight, Component component, Point screenSize) {
        if (percentOfWidth != 0 && percentOfHeight != 0) {
            component.setSize(getScaledWidth(percentOfWidth, screenSize.x), getScaledHeight(percentOfHeight, screenSize.y));
            setTextSizeOfControl(component.getHeight(), percentOfControlHeight, component);
        }
    }

    // Setting Place on View
    private void setScaledMargins(float percentOfTopMargin, float percentOfLeftMargin, Component component, Point screenSize) {
        int leftMargin = (int) (screenSize.x - screenSize.x * ((100 - percentOfLeftMargin) / 100));
        int topMargin = (int) (screenSize.y - screenSize.y * ((100 - percentOfTopMargin) / 100));

        component.setBounds(leftMargin, topMargin, component.getWidth(), component.getHeight());
    }

    //Scalling text on controlls
    private void setTextSizeOfControl(float componentHeight, float percentOfComponentHeight, Component component) {
        component.setFont(new Font("Arial", Font.BOLD, (int) (componentHeight * percentOfComponentHeight / 100)));
    }

    // returns scaled width by %
    public int getScaledWidth(float percentOfWidth, int screenWidth) {
        return (int) (screenWidth - screenWidth * ((100 - percentOfWidth) / 100));
    }

    // returns scaled height by %
    public int getScaledHeight(float percentOfHeight, int screenHeight) {
        return (int) (screenHeight - screenHeight * ((100 - percentOfHeight) / 100));
    }

    //returns window size
    public Point getWindowSize(int percentOfWidth, int percentOfHeight) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        width = (((float) percentOfWidth / 100) * width);
        height = (((float) percentOfHeight / 100) * height);

        return new Point((int) width, (int) height);
    }

}