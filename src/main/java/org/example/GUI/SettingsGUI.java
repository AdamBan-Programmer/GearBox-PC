package org.example.GUI;

import org.example.Settings.AppSettings;
import org.example.File.MemoryOperations;
import org.example.Utils.ScaleLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SettingsGUI extends JDialog implements CreatorGUI {

    private static ScaleLayout scallingController = new ScaleLayout();
    private static MemoryOperations memoryController = new MemoryOperations();

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
        this.getContentPane().setBackground(Color.decode("#C0C0C0"));
    }

    @Override
    public void setGuiComponentsParams() {
        scallingController.setScallingParams(70, 5, 70, 5, 5, saveDirectoryPath_LB, this);
        scallingController.setScallingParams(70, 5, 65, 15, 5, etsPath_LB, this);
        scallingController.setScallingParams(70, 7, 50, 20, 5, etsPath_TF, this);
        scallingController.setScallingParams(70, 7, 50, 30, 5, openAppFiles_BT, this);
        scallingController.setScallingParams(30, 10, 50, 70, 35, save_BT, this);
    }

    @Override
    public void addGuiComponents() {
        this.getContentPane().add(saveDirectoryPath_LB);
        this.getContentPane().add(etsPath_TF);
        this.getContentPane().add(etsPath_LB);
        this.getContentPane().add(openAppFiles_BT);
        this.getContentPane().add(save_BT);
    }

    @Override
    public void addGuiComponentsToListeners() {
        setMouseClickListener(openAppFiles_BT);
        setMouseClickListener(save_BT);
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

    private void setValuesIntoControls()
    {
        etsPath_TF.setText(AppSettings.getInstance().getEtsPath());
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
                        JOptionPane.showConfirmDialog(SettingsGUI.this,"Error! Cannot open app files.","Error!", JOptionPane.DEFAULT_OPTION);
                    }
                }

                 if(component == save_BT)
                 {
                     try {
                         getDataFromControls();
                         memoryController.checkAllDirectoriesExist();
                         String pathToFile = "GearBoxBinder/Settings/Settings.ser";
                         memoryController.serializeObject(pathToFile, AppSettings.getInstance());
                         SettingsGUI.this.dispose();
                     }
                     catch (IOException ioe)
                     {
                         JOptionPane.showConfirmDialog(SettingsGUI.this,"Error! Cannot save your settings.","Error!", JOptionPane.DEFAULT_OPTION);
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

    private void getDataFromControls()
    {
        AppSettings.getInstance().setEtsPath(etsPath_TF.getText());
    }
}
