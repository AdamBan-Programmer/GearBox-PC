package org.example.Windows;

import org.example.Settings.AppSettings;
import org.example.File.MemoryOperations;
import org.example.Utils.ScaleLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class AppSettingsWindow implements WindowInterface {

    private static ScaleLayout scallingController = new ScaleLayout();
    private static AppSettings appSettingsController = new AppSettings();
    private static MemoryOperations memoryController = new MemoryOperations();

    JFrame appSettingsWindowFrame = new JFrame("appSettings");
    static JLabel saveDirectoryPath_LB = new JLabel("app directory: C:/GearBoxBinder");
    static JTextField etsPath_TF = new JTextField();
    static JLabel etsPath_LB = new JLabel("path to EuroTruckSimulator 2 or shortcut:");
    static JButton openAppFiles_BT = new JButton("open app files");
    static JButton save_BT = new JButton("Save");

    public void openSettingsWindow()
    {
        createWindow();
        setWindowIcon();
        addControlsIntoPanel();
        setControlsToListeners();
        setControlsParams();
        setValuesIntoControls();
    }

    public void createWindow() {
        Point screenSize = scallingController.getWindowSize(20, 30);
        appSettingsWindowFrame.setSize(screenSize.x, screenSize.y);
        appSettingsWindowFrame.setLocationRelativeTo(null);
        appSettingsWindowFrame.setVisible(true);
        appSettingsWindowFrame.setResizable(false);
        appSettingsWindowFrame.setTitle("appSettings:");
        appSettingsWindowFrame.setLayout(null);
        appSettingsWindowFrame.getContentPane().setBackground(Color.decode("#C0C0C0"));
    }

    @Override
    public void setWindowIcon() {
        try {
            ImageIcon icon = new ImageIcon("C:/Users/Adam/IdeaProjects/GearBoxBinder/src/main/resources/images/AppImage.png");
            appSettingsWindowFrame.setIconImage(icon.getImage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addControlsIntoPanel() {
        appSettingsWindowFrame.getContentPane().add(saveDirectoryPath_LB);
        appSettingsWindowFrame.getContentPane().add(etsPath_TF);
        appSettingsWindowFrame.getContentPane().add(etsPath_LB);
        appSettingsWindowFrame.getContentPane().add(openAppFiles_BT);
        appSettingsWindowFrame.getContentPane().add(save_BT);
    }
    public void setControlsToListeners() {
        setMouseClickListener(openAppFiles_BT);
        setMouseClickListener(save_BT);
    }

    public void setControlsParams() {
        ScaleLayout scallingInViewElements = new ScaleLayout(appSettingsWindowFrame.getWidth(), appSettingsWindowFrame.getHeight());
        scallingInViewElements.setScallingParams(70, 5, 70, 5, 5, saveDirectoryPath_LB, appSettingsWindowFrame);
        scallingInViewElements.setScallingParams(70, 5, 65, 15, 5, etsPath_LB, appSettingsWindowFrame);
        scallingInViewElements.setScallingParams(70, 7, 50, 20, 5, etsPath_TF, appSettingsWindowFrame);
        scallingInViewElements.setScallingParams(70, 7, 50, 30, 5, openAppFiles_BT, appSettingsWindowFrame);
        scallingInViewElements.setScallingParams(30, 10, 50, 70, 35, save_BT, appSettingsWindowFrame);
    }

    private void setValuesIntoControls()
    {
        AppSettings settings = appSettingsController.getCurrentAppSettings();
        etsPath_TF.setText(settings.getEtsPath());
    }

    private void setMouseClickListener(Component component) {
        component.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Component component = (Component) e.getSource();

                if(component == openAppFiles_BT)
                {
                    try {
                        Runtime.getRuntime().exec("explorer.exe /select," + "C:\\GearBoxBinder\\Profiles");
                    }
                    catch (IOException ex) {
                        JOptionPane.showConfirmDialog(appSettingsWindowFrame,"Error! Cannot open app files.","Error!", JOptionPane.DEFAULT_OPTION);
                    }
                }

                 if(component == save_BT)
                 {
                     try {
                         AppSettings newAppSettings = getDataFromControls();
                         appSettingsController.getCurrentAppSettings().setCurrentAppSettings(newAppSettings);
                         memoryController.checkAllDirectoriesExist();
                         String pathToFile = "GearBoxBinder/Settings/Settings.ser";
                         memoryController.serializeObject(pathToFile, newAppSettings);
                         appSettingsWindowFrame.dispose();
                     }
                     catch (IOException ioe)
                     {
                         JOptionPane.showConfirmDialog(appSettingsWindowFrame,"Error! Cannot save your settings.","Error!", JOptionPane.DEFAULT_OPTION);
                     }
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

    private AppSettings getDataFromControls()
    {
        String etsPath = etsPath_TF.getText();
        return new AppSettings(etsPath);
    }
}
