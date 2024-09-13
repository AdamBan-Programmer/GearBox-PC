package org.example.GUI;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import org.example.Configs.ConfigPanel;
import org.example.Profiles.Profile;
import org.example.Utils.CalibrationStatusEnum;
import org.example.Utils.Gamepad;
import org.example.Utils.Range;
import org.example.Utils.ScaleLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalibrationGUI extends JDialog implements CreatorGUI,CloseGUI {

    static ScaleLayout scallingController = new ScaleLayout();
    static Gamepad gamepadController = new Gamepad();

    static JPanel calibrationPanel = new JPanel();

    static JButton removeBT = new JButton("REMOVE");
    static JLabel cfgName_LB = new JLabel("Set name of config");
    static JTextField cfgName_TF = new JTextField();
    static JLabel cfgValue_LB = new JLabel("Set value");
    static JTextField cfgValue_TF = new JTextField();
    static JButton saveCfg_BT = new JButton("Save");
    static JLabel detectedControl_LB = new JLabel("Detected: none");
    static JButton previousBind_BT = new JButton("<<");
    static JButton nextBind_BT = new JButton(">>");
    static JButton detectMin_BT = new JButton("Detect_MIN");
    static JButton detectMax_BT = new JButton("Detect_MAX");
    static JButton resetBind_BT = new JButton("RESET");
    static JButton stopDetecting_BT = new JButton("STOP");
    static JLabel cfgActivationValue_LB = new JLabel("Activation From: 0.0 To: 0.0");
    static JLabel cfgBindPageNumber_LB = new JLabel("Bind: 1");

    private static ConfigPanel currentConfigPanel;
    private static int bindIndex;
    private static CalibrationStatusEnum detectingStatus = CalibrationStatusEnum.STOPPED;

    public CalibrationGUI(ConfigPanel cfgPanel) {
        currentConfigPanel = cfgPanel;
        setGuiParams();
        setGuiComponentsParams();
        addGuiComponents();
        addGuiComponentsToListeners();
        setGuiIcon();

        // Window settings
        setConfigValuesIntoControls();
        this.setVisible(true);
        bindIndex =0;
    }

    @Override
    public void setGuiParams() {
        Point screenSize = scallingController.getWindowSize(20, 30);
        this.setSize(screenSize.x, screenSize.y);
        this.setResizable(false);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.setTitle("Config:");
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        calibrationPanel.setBackground(Color.decode("#C0C0C0"));
        calibrationPanel.setLayout(null);
    }

    @Override
    public void setGuiComponentsParams() {
        scallingController.setScallingParams(100,100,0,0,0,calibrationPanel,this);

        scallingController.setScallingParams(20, 6, 50, 1, 75, removeBT, calibrationPanel);
        scallingController.setScallingParams(78, 6, 50, 5, 2, cfgName_LB, calibrationPanel);
        scallingController.setScallingParams(78, 6, 70, 11, 2, cfgName_TF, calibrationPanel);
        scallingController.setScallingParams(78, 6, 50, 18, 2, cfgValue_LB, calibrationPanel);
        scallingController.setScallingParams(78, 6, 70, 24, 2, cfgValue_TF, calibrationPanel);
        scallingController.setScallingParams(50, 6, 50, 48, 10, detectedControl_LB, calibrationPanel);
        scallingController.setScallingParams(20, 7, 60, 35, 20, cfgBindPageNumber_LB, calibrationPanel);
        scallingController.setScallingParams(15, 6, 50, 35, 5, previousBind_BT, calibrationPanel);
        scallingController.setScallingParams(15, 6, 50, 35, 40, nextBind_BT, calibrationPanel);
        scallingController.setScallingParams(30, 6, 50, 44, 65, detectMin_BT, calibrationPanel);
        scallingController.setScallingParams(30, 6, 50, 51, 65, detectMax_BT, calibrationPanel);
        scallingController.setScallingParams(30, 6, 50, 58, 65, stopDetecting_BT, calibrationPanel);
        scallingController.setScallingParams(30, 6, 50, 65, 65, resetBind_BT, calibrationPanel);
        scallingController.setScallingParams(50, 6, 50, 54, 10, cfgActivationValue_LB, calibrationPanel);
        scallingController.setScallingParams(30, 10, 40, 75, 34, saveCfg_BT, calibrationPanel);
        cfgBindPageNumber_LB.setHorizontalAlignment(SwingConstants.CENTER);
        removeBT.setBackground(Color.decode("#e8354d"));
    }

    @Override
    public void addGuiComponents() {
        //adds controls into panel
        calibrationPanel.add(removeBT);
        calibrationPanel.add(cfgName_LB);
        calibrationPanel.add(cfgName_TF);
        calibrationPanel.add(cfgValue_LB);
        calibrationPanel.add(cfgValue_TF);
        calibrationPanel.add(detectedControl_LB);
        calibrationPanel.add(detectMin_BT);
        calibrationPanel.add(detectMax_BT);
        calibrationPanel.add(resetBind_BT);
        calibrationPanel.add(stopDetecting_BT);
        calibrationPanel.add(cfgActivationValue_LB);
        calibrationPanel.add(previousBind_BT);
        calibrationPanel.add(nextBind_BT);
        calibrationPanel.add(cfgBindPageNumber_LB);
        calibrationPanel.add(saveCfg_BT);

        this.add(calibrationPanel);
    }

    @Override
    public void addGuiComponentsToListeners() {
        removeBT.addActionListener(actionListener);
        previousBind_BT.addActionListener(actionListener);
        nextBind_BT.addActionListener(actionListener);
        detectMin_BT.addActionListener(actionListener);
        detectMax_BT.addActionListener(actionListener);
        stopDetecting_BT.addActionListener(actionListener);
        resetBind_BT.addActionListener(actionListener);
        saveCfg_BT.addActionListener(actionListener);
    }
    @Override
    public void setGuiIcon() {
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(this.getClass().getResourceAsStream("/images/AppImage.png")));
            this.setIconImage(icon.getImage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void closeGui()
    {
        for(java.awt.Component component : calibrationPanel.getComponents())
        {
            if(component instanceof JButton)
            {
                JButton actionBT = (JButton) component;
                actionBT.removeActionListener(actionListener);
            }
        }
        this.remove(calibrationPanel);
        this.dispose();
    }

    //default config values into conrols
    private void setConfigValuesIntoControls() {
        cfgName_TF.setText(currentConfigPanel.getName());
        cfgValue_TF.setText(String.valueOf(currentConfigPanel.getValue()));
        cfgBindPageNumber_LB.setText("Bind: 1");
        updateDetectedValues();
    }

    // sets detected values
    private void updateDetectedValues() {
        try {
            detectedControl_LB.setText("detected: " + currentConfigPanel.getBind().get(bindIndex).getControlId());
            cfgActivationValue_LB.setText("Activation From: " + currentConfigPanel.getActivationValue().get(bindIndex).getValueMin()
                    + " To: " + currentConfigPanel.getActivationValue().get(bindIndex).getValueMax());
        } catch (Exception e) {
            detectedControl_LB.setText("detected: ");
            cfgActivationValue_LB.setText("Activation_value: ");
        }
    }

    // mouse click event listener
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            java.awt.Component component = (java.awt.Component) e.getSource();

            if(component == removeBT)
            {
                CentralGUI.removeConfigPanel(currentConfigPanel);
                closeGui();
            }

            if (component == saveCfg_BT) {
                saveConfigValues(currentConfigPanel);
                CentralGUI.updateConfigPanelName();
                closeGui();
            }

            if (component == detectMin_BT) {
                if(detectingStatus == CalibrationStatusEnum.STOPPED) {
                    try {
                        detectMin_BT.setEnabled(false);
                        final int VALUE_MIN = 0;
                        setBindActivationRange(VALUE_MIN);
                    } catch (Exception ex) {
                        JOptionPane.showConfirmDialog(CalibrationGUI.this, "Gamepad not set correctly!", "Warning!", JOptionPane.DEFAULT_OPTION);
                        detectMin_BT.setEnabled(true);
                    }
                }
            }

            if (component == detectMax_BT) {
                if(detectingStatus == CalibrationStatusEnum.STOPPED) {
                    try {
                        detectMax_BT.setEnabled(false);
                        final int VALUE_MAX = 1;
                        setBindActivationRange(VALUE_MAX);
                    } catch (Exception ex) {
                        JOptionPane.showConfirmDialog(CalibrationGUI.this, "Gamepad not set correctly!", "Warning!", JOptionPane.DEFAULT_OPTION);
                        detectMax_BT.setEnabled(true);
                    }
                }
            }

            if (component == stopDetecting_BT) {
                detectingStatus = CalibrationStatusEnum.STOPPED;
                detectMin_BT.setEnabled(true);
                detectMax_BT.setEnabled(true);
            }

            if (component == resetBind_BT) {
                removeBindFromConfig(currentConfigPanel);
            }

            if (component == previousBind_BT) {
                goToPreviousBind();
            }

            if (component == nextBind_BT) {
                if (currentConfigPanel.getBind().size() > bindIndex) {
                    goToNextBind();
                }
            }
        }
    };

    private void goToPreviousBind() {
        if (bindIndex > 0) {
            bindIndex--;
            updateDetectedValues();
            cfgBindPageNumber_LB.setText("Bind: " + (bindIndex + 1));
        }
    }

    private void goToNextBind() {
        bindIndex++;
        updateDetectedValues();
        cfgBindPageNumber_LB.setText("Bind: " + (bindIndex + 1));
    }

    private void setBindActivationRange(int valueIndex) {
        detectingStatus = CalibrationStatusEnum.DETECTING;
        Controller controller = gamepadController.getGamepadByName(Profile.getInstance().getGamepadName());
        detectControl(controller);
        calibrateActivationRange(controller, valueIndex);
    }

    //detects pressed control
    private void detectControl(Controller controller) {
        Component detectedControl = gamepadController.getDetectedGamepadControl(controller);
        if (currentConfigPanel.getBind().size() <= bindIndex) {
            currentConfigPanel.getBind().add(new Gamepad());
        }
        currentConfigPanel.getBind().get(bindIndex).setControlId(detectedControl.getIdentifier().toString());
        detectedControl_LB.setText("detected: " + currentConfigPanel.getBind().get(bindIndex).getControlId());
    }

    //sets Activation range
    private void calibrateActivationRange(Controller controller,int valueIndex)
    {
        if(currentConfigPanel.getActivationValue().size() <= bindIndex)
        {
            currentConfigPanel.getActivationValue().add(new Range(0,0));
        }
        calibrateActivationValue(controller,valueIndex);
    }

    // calibrates control
    private void calibrateActivationValue(Controller controller, int valueIndex) {
        Range activationRange = currentConfigPanel.getActivationValue().get(bindIndex);
        float[] value = {activationRange.getValueMin(),activationRange.getValueMax()};
        Thread thread = new Thread() {
            public void run() {
                while (detectingStatus == CalibrationStatusEnum.DETECTING) {
                    Component gamepadControl = currentConfigPanel.getBind().get(bindIndex).getControlByName(controller, currentConfigPanel.getBind().get(bindIndex).getControlId());
                    String gamepadName = Profile.getInstance().getGamepadName();
                    Controller gamepad = gamepadController.getGamepadByName(gamepadName);
                    value[valueIndex] = gamepadController.getControlValue(gamepad, gamepadControl);
                    cfgActivationValue_LB.setText("Activation From: " + value[0] + " To: " + value[1]);
                }
                currentConfigPanel.getActivationValue().set(bindIndex,new Range(value[0],value[1]));
            }
        };

        thread.start();
    }

    //saves config
    private void saveConfigValues(ConfigPanel cfgPanel)
    {
        cfgPanel.setName(cfgName_TF.getText());
        cfgPanel.setValue(cfgValue_TF.getText());
    }

    private void removeBindFromConfig(ConfigPanel cfgPanel)
    {
        if(!cfgPanel.getBind().isEmpty()) {
            cfgPanel.getBind().remove(bindIndex);
        }
        updateDetectedValues();
    }
}
