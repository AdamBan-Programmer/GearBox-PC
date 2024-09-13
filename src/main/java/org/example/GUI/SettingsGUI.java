package org.example.GUI;

import org.example.Settings.AppSettings;
import org.example.File.MemoryOperations;
import org.example.Utils.ScaleLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SettingsGUI extends JDialog implements CreatorGUI,CloseGUI {

    private static ScaleLayout scallingController = new ScaleLayout();
    private static MemoryOperations memoryController = new MemoryOperations();

    JPanel settingsPanel = new JPanel();

    static JLabel saveDirectoryPath_LB = new JLabel("app directory: C:/GearBoxBinder");
    static JTextField etsPath_TF = new JTextField();
    static JLabel etsPath_LB = new JLabel("path to EuroTruckSimulator 2 or shortcut:");
    static JButton openAppFiles_BT = new JButton("open app files");
    static JButton save_BT = new JButton("Save");

    public SettingsGUI() {
        setGuiParams();
        setGuiComponentsParams();
        addGuiComponents();
        addGuiComponentsToListeners();
        setValuesIntoControls();
        setGuiIcon();
        this.setVisible(true);
    }

    @Override
    public void setGuiParams() {
        Point screenSize = scallingController.getWindowSize(20, 30);
        this.setSize(screenSize.x, screenSize.y);
        this.setLocationRelativeTo(null);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setResizable(false);
        this.setTitle("appSettings:");
        this.setLayout(null);
        settingsPanel.setLayout(null);
        settingsPanel.setBackground(Color.decode("#C0C0C0"));
    }

    @Override
    public void setGuiComponentsParams() {
        scallingController.setScallingParams(100,100,0,0,0,settingsPanel,this);

        scallingController.setScallingParams(70, 5, 70, 5, 5, saveDirectoryPath_LB, settingsPanel);
        scallingController.setScallingParams(70, 5, 65, 15, 5, etsPath_LB, settingsPanel);
        scallingController.setScallingParams(70, 7, 50, 20, 5, etsPath_TF, settingsPanel);
        scallingController.setScallingParams(70, 7, 50, 30, 5, openAppFiles_BT, settingsPanel);
        scallingController.setScallingParams(30, 10, 50, 70, 35, save_BT, settingsPanel);
    }

    @Override
    public void addGuiComponents() {
        settingsPanel.add(saveDirectoryPath_LB);
        settingsPanel.add(etsPath_TF);
        settingsPanel.add(etsPath_LB);
        settingsPanel.add(openAppFiles_BT);
        settingsPanel.add(save_BT);

        this.add(settingsPanel);
    }

    @Override
    public void addGuiComponentsToListeners() {
        openAppFiles_BT.addActionListener(actionListener);
        save_BT.addActionListener(actionListener);
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
    public void closeGui() {
        for(java.awt.Component component : settingsPanel.getComponents())
        {
            if(component instanceof JButton)
            {
                JButton actionBT = (JButton) component;
                actionBT.removeActionListener(actionListener);
            }
        }
        this.remove(settingsPanel);
        this.dispose();
    }

    private void setValuesIntoControls()
    {
        etsPath_TF.setText(AppSettings.getInstance().getEtsPath());
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Component component = (Component) e.getSource();

            if (component == openAppFiles_BT) {
                try {
                    Runtime.getRuntime().exec("explorer.exe /select," + "C:\\GearBoxBinder\\Profiles");
                } catch (IOException ex) {
                    JOptionPane.showConfirmDialog(SettingsGUI.this, "Error! Cannot open app files.", "Error!", JOptionPane.DEFAULT_OPTION);
                }
            }

            if (component == save_BT) {
                try {
                    getDataFromControls();
                    memoryController.checkAllDirectoriesExist();
                    String pathToFile = "GearBoxBinder/Settings/Settings.ser";
                    memoryController.serializeObject(pathToFile, AppSettings.getInstance());
                    SettingsGUI.this.dispose();
                } catch (IOException ioe) {
                    JOptionPane.showConfirmDialog(SettingsGUI.this, "Error! Cannot save your settings.", "Error!", JOptionPane.DEFAULT_OPTION);
                }
            }
        }
    };

    private void getDataFromControls()
    {
        AppSettings.getInstance().setEtsPath(etsPath_TF.getText());
    }
}
