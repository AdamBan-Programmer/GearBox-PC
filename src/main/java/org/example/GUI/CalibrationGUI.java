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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CalibrationGUI extends JDialog implements CreatorGUI {

    static ScaleLayout scallingController = new ScaleLayout();
    static Gamepad gamepadController = new Gamepad();

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
        this.getContentPane().setBackground(Color.decode("#C0C0C0"));
    }

    @Override
    public void setGuiComponentsParams() {
        scallingController.setScallingParams(78, 6, 50, 5, 2, cfgName_LB, this);
        scallingController.setScallingParams(78, 6, 70, 11, 2, cfgName_TF, this);
        scallingController.setScallingParams(78, 6, 50, 18, 2, cfgValue_LB, this);
        scallingController.setScallingParams(78, 6, 70, 24, 2, cfgValue_TF, this);
        scallingController.setScallingParams(50, 6, 50, 48, 10, detectedControl_LB, this);
        scallingController.setScallingParams(20, 7, 60, 35, 20, cfgBindPageNumber_LB, this);
        scallingController.setScallingParams(15, 6, 50, 35, 5, previousBind_BT, this);
        scallingController.setScallingParams(15, 6, 50, 35, 40, nextBind_BT, this);
        scallingController.setScallingParams(30, 6, 50, 44, 65, detectMin_BT, this);
        scallingController.setScallingParams(30, 6, 50, 51, 65, detectMax_BT, this);
        scallingController.setScallingParams(30, 6, 50, 58, 65, stopDetecting_BT, this);
        scallingController.setScallingParams(30, 6, 50, 65, 65, resetBind_BT, this);
        scallingController.setScallingParams(50, 6, 50, 54, 10, cfgActivationValue_LB, this);
        scallingController.setScallingParams(30, 10, 40, 75, 34, saveCfg_BT, this);
        cfgBindPageNumber_LB.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public void addGuiComponents() {
        //adds controls into panel
        this.add(cfgName_LB);
        this.add(cfgName_TF);
        this.add(cfgValue_LB);
        this.add(cfgValue_TF);
        this.add(detectedControl_LB);
        this.add(detectMin_BT);
        this.add(detectMax_BT);
        this.add(resetBind_BT);
        this.add(stopDetecting_BT);
        this.add(cfgActivationValue_LB);
        this.add(previousBind_BT);
        this.add(nextBind_BT);
        this.add(cfgBindPageNumber_LB);
        this.add(saveCfg_BT);
    }

    @Override
    public void addGuiComponentsToListeners() {
        setMouseClickListener(previousBind_BT);
        setMouseClickListener(nextBind_BT);
        setMouseClickListener(detectMin_BT);
        setMouseClickListener(detectMax_BT);
        setMouseClickListener(stopDetecting_BT);
        setMouseClickListener(resetBind_BT);
        setMouseClickListener(saveCfg_BT);
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
    private void setMouseClickListener(java.awt.Component component) {
        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                java.awt.Component component = (java.awt.Component) e.getSource();
                if (e.getClickCount() == 1 && !e.isConsumed() && component.isEnabled()) {

                    if (component == saveCfg_BT) {
                        saveConfigValues(currentConfigPanel);
                        CalibrationGUI.this.dispose();
                        CentralGUI.updateConfigPanelName();
                    }

                    if (component == detectMin_BT) {
                        try {
                            detectMin_BT.setEnabled(false);
                            final int VALUE_MIN = 0;
                            setBindActivationRange(VALUE_MIN);
                        }
                        catch (Exception ex)
                        {
                            JOptionPane.showConfirmDialog(CalibrationGUI.this,"Gamepad not set correctly!","Warning!", JOptionPane.DEFAULT_OPTION);
                            detectMin_BT.setEnabled(true);
                        }
                    }

                    if (component == detectMax_BT) {
                        try {
                            detectMax_BT.setEnabled(false);
                            final int VALUE_MAX = 1;
                            setBindActivationRange(VALUE_MAX);
                        } catch (Exception ex) {
                            JOptionPane.showConfirmDialog(CalibrationGUI.this, "Gamepad not set correctly!", "Warning!", JOptionPane.DEFAULT_OPTION);
                            detectMax_BT.setEnabled(true);
                        }
                    }

                    if (component == stopDetecting_BT) {
                        detectingStatus = CalibrationStatusEnum.STOPPED;
                        detectMin_BT.setEnabled(true);
                        detectMax_BT.setEnabled(true);
                    }

                    if(component == resetBind_BT)
                    {
                        removeBindFromConfig(currentConfigPanel);
                    }

                    if (component == previousBind_BT) {
                        goToPreviousBind();
                    }

                    if (component == nextBind_BT) {
                        if(currentConfigPanel.getBind().size() > bindIndex) {
                            goToNextBind();
                        }
                    }
                    e.consume();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

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
