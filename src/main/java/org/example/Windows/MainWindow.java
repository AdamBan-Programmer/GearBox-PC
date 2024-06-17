package org.example.Windows;

import net.java.games.input.Controller;
import org.example.Configs.ConfigPanel;
import org.example.File.MemoryOperations;
import org.example.Profiles.Profile;
import org.example.Settings.AppSettings;
import org.example.Utils.AppStatusEnum;
import org.example.Utils.Gamepad;
import org.example.Utils.Range;
import org.example.Utils.ScaleLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainWindow implements WindowInterface {

    static MemoryOperations memoryController = new MemoryOperations();
    static ScaleLayout scallingController = new ScaleLayout();
    static Profile profilesController = new Profile();
    static CalibrationWindow calibrationPanelController = new CalibrationWindow();
    static Gamepad gamepadController = new Gamepad();
    static AppSettingsWindow appSettingsWindowController = new AppSettingsWindow();
    static AppSettings appSettingsController = new AppSettings();
    static ConfigPanel cfgPanelController = new ConfigPanel();

    static JFrame mainWindowFrame = new JFrame();
    static JPanel defaultElementsView = new JPanel();
    static JPanel configButtonsView = new JPanel();
    static JLayeredPane layeredPane = new JLayeredPane();

    static JLabel newProfile_LB = new JLabel("Create New Profile:");
    static JButton newProfile_BT = new JButton("New Profile");
    static JLabel addConfig_LB = new JLabel("Create New Config:");
    static JButton addConfig_BT = new JButton("New Config");
    static JLabel saveProfile_LB = new JLabel("Save Your Profile:");
    static JButton saveProfile_BT = new JButton("Save Profile");
    static JLabel renameProfile_LB = new JLabel("Rename Your Profile:");
    static JButton renameProfile_BT = new JButton("Rename Profile");
    static JLabel removeProfile_LB = new JLabel("Remove Your Profile:");
    static JButton removeProfile_BT = new JButton("Remove Profile");
    static JLabel gamepad_LB = new JLabel("Select your Gamepad:");
    static JComboBox gamepads_CB = new JComboBox();
    static JLabel profiles_LB = new JLabel("Select your Profile:");
    static JComboBox profiles_CB = new JComboBox();
    static JLabel profileName_LB = new JLabel("Set Profile Name:");
    static JButton startButton_BT = new JButton("START");
    static JButton stopButton_BT = new JButton("STOP");
    static JLabel infoPanel_LB = new JLabel("Parameters:");
    static JButton runETS_BT = new JButton("run: Euro Truck Simulator 2");
    static JLabel runETS_LB = new JLabel("run: ETS 2");
    static JButton settings_BT = new JButton("Settings");
    static JLabel settings_LB = new JLabel("Go to Settings");
    static JLabel appRunningStatus_LB = new JLabel("Status: STOPPED");

    static AppStatusEnum gearBoxStatus = AppStatusEnum.STOPPED;
    private static LinkedHashMap<JButton, ConfigPanel> currentConfigPanelsHashMap = new LinkedHashMap<>();

    public static void main(String[] args) {
        MainWindow mainWindowController = new MainWindow();
        mainWindowController.createWindow();
        mainWindowController.setWindowIcon();
        mainWindowController.addControlsIntoPanel();
        mainWindowController.setControlsToListeners();
        mainWindowController.setControlsParams();

        //sets all default settings and params
        try {
            memoryController.checkAllDirectoriesExist();
            memoryController.readSerializedProfile();
            memoryController.readSerializedSettings();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showConfirmDialog(mainWindowFrame, "Error! Cannot load files.","Error!", JOptionPane.DEFAULT_OPTION);
        }
        catch (NullPointerException n)
        {
            JOptionPane.showInputDialog(mainWindowFrame, "Set path to Euro Truck Simulator 2 in Settings!","Warning!", JOptionPane.DEFAULT_OPTION);
        }

        loadProfilesSpinner();
        mainWindowController.loadGamepadsSpinner();
        updateAppRunningStatus();

        addPanelsIntoFrame();
        selectFirstProfile();
    }

    //Window Default settups
    @Override
    public void createWindow() {
        mainWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindowFrame.setTitle("GearBoxBinder: ");
        Point screenSize = scallingController.getWindowSize(60, 60);
        mainWindowFrame.setSize(screenSize.x, screenSize.y);
        mainWindowFrame.setResizable(false);
        mainWindowFrame.setLocationRelativeTo(null);
        layeredPane.setPreferredSize(new Dimension(screenSize.x, screenSize.y));

        setDefaultElementsView(screenSize);
        setConfigButtonsView(screenSize);
    }

    @Override
    public void setWindowIcon() {
        try {
            ImageIcon icon = new ImageIcon("C:/Users/Adam/IdeaProjects/GearBoxBinder/src/main/resources/images/AppImage.png");
            mainWindowFrame.setIconImage(icon.getImage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // adding controls into a panel
    @Override
    public void addControlsIntoPanel()
    {
        defaultElementsView.add(newProfile_LB);
        defaultElementsView.add(newProfile_BT);
        defaultElementsView.add(addConfig_LB);
        defaultElementsView.add(addConfig_BT);
        defaultElementsView.add(saveProfile_LB);
        defaultElementsView.add(saveProfile_BT);
        defaultElementsView.add(renameProfile_LB);
        defaultElementsView.add(renameProfile_BT);
        defaultElementsView.add(removeProfile_LB);
        defaultElementsView.add(removeProfile_BT);
        defaultElementsView.add(profiles_LB);
        defaultElementsView.add(profiles_CB);
        defaultElementsView.add(profileName_LB);
        defaultElementsView.add(gamepad_LB);
        defaultElementsView.add(gamepads_CB);
        defaultElementsView.add(infoPanel_LB);
        defaultElementsView.add(runETS_BT);
        defaultElementsView.add(runETS_LB);
        defaultElementsView.add(startButton_BT);
        defaultElementsView.add(stopButton_BT);
        defaultElementsView.add(settings_BT);
        defaultElementsView.add(settings_LB);
        defaultElementsView.add(appRunningStatus_LB);
    }

    //setting listener
    @Override
    public void setControlsToListeners()
    {
        //sets listeners for controls
        setMouseMovementListener(mainWindowFrame);
        setMouseClickListener(newProfile_BT);
        setMouseClickListener(addConfig_BT);
        setMouseClickListener(startButton_BT);
        setMouseClickListener(saveProfile_BT);
        setMouseClickListener(renameProfile_BT);
        setMouseClickListener(removeProfile_BT);
        setMouseClickListener(stopButton_BT);
        setMouseClickListener(runETS_BT);
        setMouseClickListener(settings_BT);
        setItemChangedListener(profiles_CB);
        setItemChangedListener(gamepads_CB);
    }

    // setting ViewElements controls params
    @Override
    public void setControlsParams() {
        ScaleLayout scallingInViewElements = new ScaleLayout(defaultElementsView.getWidth(), defaultElementsView.getHeight());
        scallingInViewElements.setScallingParams(20, 4, 60, 0, 0, newProfile_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 5, 50, 3.5f, 0, newProfile_BT, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 4, 60, 9.5f, 0, addConfig_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 5, 50, 13, 0, addConfig_BT, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 4, 60, 19, 0, saveProfile_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 5, 50, 22.5f, 0, saveProfile_BT, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 4, 60, 28.5f, 0, renameProfile_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 5, 50, 32, 0, renameProfile_BT, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 4, 60, 38, 0, removeProfile_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 5, 50, 41.5f, 0, removeProfile_BT, defaultElementsView);
        scallingInViewElements.setScallingParams(56, 4, 60, 0, 42, profiles_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(56, 5, 50, 3.5f, 21, profiles_CB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 5, 60, 3.5f, 78, profileName_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 4, 60, 9.5f, 78, gamepad_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 5, 50, 13, 78, gamepads_CB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 5, 50, 22.5f, 78, appRunningStatus_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 17.5f, 10, 33, 78, infoPanel_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 4, 60, 56.5f, 0, settings_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 5, 50, 60, 0, settings_BT, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 4, 60, 66, 0, runETS_LB, defaultElementsView);
        scallingInViewElements.setScallingParams(20, 5, 40, 69.5f, 0, runETS_BT, defaultElementsView);
        scallingInViewElements.setScallingParams(40, 10, 50, 80, 9.5f, startButton_BT, defaultElementsView);
        scallingInViewElements.setScallingParams(40, 10, 50, 80, 49.5f, stopButton_BT, defaultElementsView);
        removeProfile_BT.setEnabled(false);
        infoPanel_LB.setVerticalAlignment(SwingConstants.TOP);
        profileName_LB.setHorizontalAlignment(SwingConstants.CENTER);
        profileName_LB.setOpaque(true);
        profiles_CB.addItem("Profile");

        for (Component component : defaultElementsView.getComponents()) {
            component.setBackground(Color.decode("#e3e3e3"));
        }
    }

    //adds panels into a frame
    private static void addPanelsIntoFrame()
    {
        layeredPane.add(defaultElementsView, Integer.valueOf(0));
        layeredPane.add(configButtonsView, Integer.valueOf(1));
        mainWindowFrame.getContentPane().add(layeredPane);
        mainWindowFrame.setVisible(true);
    }

    // reloads profiles into a spinner
    private static void loadProfilesSpinner()
    {
        int itemsCount = profiles_CB.getItemCount();
        for(int i =itemsCount-1;i>0;i--)
        {
            profiles_CB.removeItemAt(i);
        }
        for(Profile profile : profilesController.getProfilesArray())
        {
            profiles_CB.addItem(profile.getName());
        }
    }

    // loads gamepads into spinner
    private void loadGamepadsSpinner()
    {
        gamepads_CB.addItem("Gamepads");
        for(Controller controller : gamepadController.getAllControllers())
        {
            gamepads_CB.addItem(controller.getName());
        }
    }

    private static void selectFirstProfile()
    {
        if(!profilesController.getProfilesArray().isEmpty()) {
            selectProfile(1);
        }
        else
        {
            setDefaultMainWindow();
        }
    }

    //clears and repaints main window
    private static void setDefaultMainWindow()
    {
        profilesController.getCurrentUsingProfile().setCurrentUsingProfile(null);
        currentConfigPanelsHashMap.clear();
        gamepads_CB.setSelectedIndex(0);
        profiles_CB.setSelectedIndex(0);
        updateCfgPanelInfo("",0,0);
        profileName_LB.setText("profile: " +"'" + "'");
        configButtonsView.removeAll();
        configButtonsView.repaint();
    }

    // View elements panel settings
    private void setDefaultElementsView(Point screenSize)
    {
        defaultElementsView.setBackground(Color.decode("#C0C0C0"));
        defaultElementsView.setLayout(null);
        defaultElementsView.setOpaque(true);
        defaultElementsView.setBounds(0,0,screenSize.x, screenSize.y);
    }

    //dynamicly created config button panel settings
    private void setConfigButtonsView(Point screenSize)
    {
        ScaleLayout scallingInConfigButtons = new ScaleLayout(defaultElementsView.getWidth(), defaultElementsView.getHeight());
        configButtonsView.setLayout(null);
        configButtonsView.setOpaque(true);
        configButtonsView.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        int x = scallingInConfigButtons.convertPercentToPixels(21, screenSize.x);
        int y = scallingInConfigButtons.convertPercentToPixels(9, screenSize.y);
        int width = scallingInConfigButtons.getScaledWidth(56);
        int height = scallingInConfigButtons.getScaledHeight(69);
        configButtonsView.setBounds(x,y,width, height);
    }

    //spinner item selected listener
    private static void setItemChangedListener(JComboBox component) {
        component.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int selected_index = component.getSelectedIndex();
                int TITLE_INDEX = 0;

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (component == profiles_CB && selected_index != TITLE_INDEX)  //spinner selected saved profile
                    {
                        selectProfile(selected_index);
                        gearBoxStatus = AppStatusEnum.STOPPED;
                        startButton_BT.setEnabled(true);
                    }
                    if (component == gamepads_CB)   //gamepad spinner
                    {
                        if (gamepads_CB.getSelectedIndex() > 0) {
                            String gamepadName = gamepads_CB.getSelectedItem().toString();
                            profilesController.getCurrentUsingProfile().setGamepadName(gamepadName);
                        }
                    }
                }
            }
        });
    }

    //mouse move listener
    private static void setMouseMovementListener(Component component) {
        component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(e.getSource() instanceof JButton && isAnyConfigSelected((JButton) e.getSource())) {
                    updateCfgPanelPosition((JButton) e.getSource());
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    // mouse click listener
    private static void setMouseClickListener(Component component) {
        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component component = (Component) e.getSource();

                if(component.isEnabled()) {
                    if (component == addConfig_BT) {
                        JButton btn = createNewButton(0, 0, "cfg");
                        if (isComponentColliding(btn, 0, 0)) {
                            JOptionPane.showConfirmDialog(mainWindowFrame, "To create new button, remove other buttons from (0,0) place", "Warning!", JOptionPane.DEFAULT_OPTION);
                        } else {
                            ConfigPanel panel = new ConfigPanel("", new ArrayList<Gamepad>(), new ArrayList<Range>(), "cfg", 0, 0, new Point(0, 0));
                            currentConfigPanelsHashMap.put(btn, panel);
                        }
                        return;
                    }

                    if (component == newProfile_BT) {
                        gearBoxStatus = AppStatusEnum.STOPPED;
                        setDefaultMainWindow();
                        profilesController.createNewProfile();
                        newProfile_BT.setEnabled(false);
                        return;
                    }

                    if (component == runETS_BT) {
                        String path = appSettingsController.getCurrentAppSettings().getEtsPath();
                        if (!path.isEmpty()) {
                            runETS(path);
                        } else {
                            JOptionPane.showConfirmDialog(mainWindowFrame, "Cannot Open Euro Truck Simulator 2", "Warning!", JOptionPane.DEFAULT_OPTION);
                        }
                        return;
                    }

                    if (component == startButton_BT) {
                        int configsCount = profilesController.getCurrentUsingProfile().getConfigPanels().size();
                        if (configsCount > 0) {
                            gearBoxStatus = AppStatusEnum.STARTED;
                            startButton_BT.setEnabled(false);
                            stopButton_BT.setEnabled(true);
                            startControlsUpdater();
                            updateAppRunningStatus();
                        } else {
                            JOptionPane.showConfirmDialog(mainWindowFrame, "Cannot start! No configs created! Add new Config or select other Profile", "Warning!", JOptionPane.DEFAULT_OPTION);
                        }
                        return;
                    }

                    if (component == stopButton_BT) {
                        gearBoxStatus = AppStatusEnum.STOPPED;
                        stopButton_BT.setEnabled(false);
                        startButton_BT.setEnabled(true);
                        disableAllConfigPanels();
                        updateAppRunningStatus();
                        return;
                    }

                    if (component == saveProfile_BT) {
                        try {
                            gearBoxStatus = AppStatusEnum.STOPPED;
                            updateProfileData();
                            profilesController.setProfileNameRequest();
                            while(!isProfilePreparedToSave()) {
                                profilesController.renameProfileRequest("This name is incorrect. Set different name!");
                            }
                            String newProfileName = profilesController.getCurrentUsingProfile().getName();
                            profileName_LB.setText("profile: " + "'" + newProfileName + "'");
                            profilesController.saveProfile();
                            reloadProfiles();
                            JOptionPane.showConfirmDialog(mainWindowFrame, "Saved profile.", "Info message:", JOptionPane.DEFAULT_OPTION);
                        }
                        catch (IOException | ClassNotFoundException ex)
                        {
                            JOptionPane.showConfirmDialog(mainWindowFrame, "Cannot save profile!", "Warning", JOptionPane.DEFAULT_OPTION);
                        }
                        return;
                    }

                    if (component == renameProfile_BT) {
                        try {
                            String oldName = profilesController.getCurrentUsingProfile().getName();
                            profilesController.renameProfileRequest("Rename profile: ");
                            while (!isProfilePreparedToSave()) {
                                profilesController.renameProfileRequest("This name is incorrect. Set different name!");
                            }
                            String newName = profilesController.getCurrentUsingProfile().getName();
                            memoryController.renameFile(oldName, newName);
                            profilesController.saveProfile();
                            profileName_LB.setText("profile: " + "'" + newName + "'");
                            reloadProfiles();
                        }
                        catch (IOException | ClassNotFoundException ex)
                        {
                            JOptionPane.showConfirmDialog(mainWindowFrame, "Cannot load profile!", "Warning", JOptionPane.DEFAULT_OPTION);
                        }
                        return;
                    }

                    if (component == removeProfile_BT) {
                        JOptionPane.showConfirmDialog(mainWindowFrame, "removed profile: " + profilesController.getCurrentUsingProfile().getName(), "Info message:", JOptionPane.DEFAULT_OPTION);
                        int index = profiles_CB.getSelectedIndex() - 1;
                        profilesController.removeProfile(index);
                        profiles_CB.removeItemAt(index + 1);
                        gearBoxStatus = AppStatusEnum.STOPPED;
                        selectFirstProfile();
                        return;
                    }

                    if (component == settings_BT) {
                        appSettingsWindowController.openSettingsWindow();
                        return;
                    }

                    if (e.getSource() instanceof JButton) {

                        if (e.getClickCount() == 2) {
                            removeConfigFromProfile((JButton) e.getSource());
                        } else {
                            showConfigSettings((JButton) e.getSource());
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Component btn = (Component) e.getSource();
                if (e.getSource() instanceof JButton && isAnyConfigSelected(btn)) {
                    startMovingConfigButton(btn);
                }
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

    private static boolean isAnyConfigSelected(Component component)
    {
        for (JButton btn : currentConfigPanelsHashMap.keySet()) {
            if(btn == component)
            {
                return true;
            }
        }
        return false;
    }

    public void updateConfigPanelName()
    {
        for(JButton btn : currentConfigPanelsHashMap.keySet())
        {
            String newName = currentConfigPanelsHashMap.get(btn).getName();
            btn.setText(newName);
        }
    }

    private static boolean isProfilePreparedToSave()
    {
        String selectedProfileName = (String) profiles_CB.getSelectedItem();
        String profileName = profilesController.getCurrentUsingProfile().getName();

        if (selectedProfileName.equals(profileName) || !profileName.isEmpty() && !memoryController.isProfileNameAlreadyUsed(profileName))
        {
            return true;
        }
        if(profileName.isEmpty())
        {
            JOptionPane.showConfirmDialog(mainWindowFrame, "Profile name cannot be empty!", "Warning", JOptionPane.DEFAULT_OPTION);
            profilesController.renameProfileRequest("This name is incorrect. Set different name!");
            String newProfileName = profilesController.getCurrentUsingProfile().getName();
            profileName_LB.setText("profile: " + "'" + newProfileName + "'");
        }
        return false;
    }

    private static void reloadProfiles() throws IOException, ClassNotFoundException {
            int selected_index = profiles_CB.getSelectedIndex();
            profilesController.getProfilesArray().clear();
            memoryController.readSerializedProfile();
            loadProfilesSpinner();
            startButton_BT.setEnabled(true);
            selectProfile(selected_index);
    }

    private static void selectProfile(int index)
    {
        setSelectedGamepad();
        setSelectedProfile(index);
        newProfile_BT.setEnabled(true);
    }

    //sets current using gamepad into spinner
    private static void setSelectedGamepad()
    {
        for (int i = 0; i < gamepads_CB.getItemCount(); i++) {
            if (gamepads_CB.getItemAt(i).toString().equals(profilesController.getCurrentUsingProfile().getGamepadName())) {
                gamepads_CB.setSelectedIndex(i);
            }
        }
    }

    // sets selected profiles (creating buttons and sets their positions and texts, name and others)
    private static void setSelectedProfile(int selected_index)
    {
        // it means selected New created profile
        if(selected_index == 0)
        {
            selected_index = profilesController.getProfilesArray().size();
        }
        Profile SelectedProfile = profilesController.getProfilesArray().get(selected_index-1);
        profilesController.setCurrentUsingProfile(SelectedProfile);
        loadProfilesComponents();
        removeProfile_BT.setEnabled(true);
        profiles_CB.setSelectedIndex(selected_index);
    }

    // config button pressed and ready to move
    private static void startMovingConfigButton(Component btn)
    {
        Point mousePos = MouseInfo.getPointerInfo().getLocation();

        // calculating selected pos from global mouse pos on ConfigButtons Panel pos
        int configButtonsPosX = (mousePos.x - scallingController.convertPercentToPixels(21, mainWindowFrame.getWidth()))- btn.getX();
        int configButtonsPosY = (mousePos.y - scallingController.convertPercentToPixels(9, mainWindowFrame.getHeight()))- mainWindowFrame.getInsets().top- btn.getY();
        int x = (int) scallingController.convertPixelsToPercent(configButtonsPosX,btn.getWidth());
        int y = (int) scallingController.convertPixelsToPercent(configButtonsPosY,btn.getHeight());
        currentConfigPanelsHashMap.get(btn).setSelectedPoint(new Point(x,y));
    }

    // move config button
    private static void updateCfgPanelPosition(JButton movingButton)
    {
        Point cfgButtonPos = currentConfigPanelsHashMap.get(movingButton).getSelectedPoint();
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        int newX = (mousePos.x - scallingController.convertPercentToPixels(21, mainWindowFrame.getWidth()))- scallingController.convertPercentToPixels((float) cfgButtonPos.getX(),movingButton.getWidth());
        int newY = (mousePos.y - scallingController.convertPercentToPixels(9, mainWindowFrame.getHeight())) - mainWindowFrame.getInsets().top - scallingController.convertPercentToPixels((float) cfgButtonPos.getY(),movingButton.getHeight());

        if(!isComponentColliding(movingButton, newX, newY))
        {
            movingButton.setLocation(newX, newY);
            currentConfigPanelsHashMap.get(movingButton).setPosX(newX);
            currentConfigPanelsHashMap.get(movingButton).setPosY(newY);
            updateCfgPanelInfo(movingButton.getText(),newX,newY);
        }
    }

    // removes config button and all config
    private static void removeConfigFromProfile(JButton btn)
    {
        ConfigPanel cfgPanel = currentConfigPanelsHashMap.get(btn);
        currentConfigPanelsHashMap.remove(btn);
        configButtonsView.remove(btn);
        profilesController.getCurrentUsingProfile().getConfigPanels().remove(cfgPanel);
        configButtonsView.repaint();
        JOptionPane.showConfirmDialog(mainWindowFrame,"removed: " + btn.getText() + " from " + profilesController.getCurrentUsingProfile().getName(),"Info message", JOptionPane.DEFAULT_OPTION);
    }

    //opens config_panel_window
    private static void showConfigSettings(JButton btn)
    {
        ConfigPanel cfgPanel = currentConfigPanelsHashMap.get(btn);
        calibrationPanelController.openConfigPanelWindow(cfgPanel);
    }

    private static void updateProfileData()
    {
        profilesController.getCurrentUsingProfile().setGamepadName(gamepads_CB.getSelectedItem().toString());
        ArrayList<ConfigPanel> newConfigPanels = new ArrayList<>();
        newConfigPanels.addAll(currentConfigPanelsHashMap.values());
        profilesController.getCurrentUsingProfile().setConfigPanels(newConfigPanels);
    }

    //updates info panel
    private static void updateCfgPanelInfo(String btnName, int x, int y)
    {
        infoPanel_LB.setText("<html>Parameters:<br><br><br>Name: "+btnName + "<br><br>x = "+x+"<br><br>y = "+y+"</html>");
    }

    private static void updateAppRunningStatus()
    {
        String status = gearBoxStatus == AppStatusEnum.STARTED?"Started":"Stopped";
        appRunningStatus_LB.setText("Status: " + status);
    }


    // checks that cmponent is touching the other one. calculation in px
    private static boolean isComponentColliding(JButton movingButton, int newX, int newY)
    {
        int movingButtonWidth = movingButton.getWidth();
        int movingButtonHeight = movingButton.getHeight();

        for(Component component: configButtonsView.getComponents()) {
            int componentX = component.getX();
            int componentY = component.getY();
            int componentWidth = component.getWidth();
            int componentHeight = component.getHeight();

            //checking collision with border
            if (newX < 0 || newX + movingButtonWidth > configButtonsView.getWidth() || newY < 0 || newY + movingButtonHeight > configButtonsView.getHeight()) {
                return true;
            }

            //checking collision with other config-btn
            if (component != movingButton) {
                if ((newX >= componentX && newX <= componentX + componentWidth) || (newX >= componentX - movingButtonWidth && newX <= componentX + componentWidth)) {
                    if ((newY >= componentY && newY <= componentY + componentHeight) || (newY >= componentY - movingButtonHeight && newY <= componentY + componentHeight)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //sets components saved in profile
    private static void loadProfilesComponents() {
        configButtonsView.removeAll();
        profileName_LB.setText("profile: " + "'" + profilesController.getCurrentUsingProfile().getName() + "'");
        currentConfigPanelsHashMap.clear();

        for (int i = 1; i < gamepads_CB.getItemCount(); i++) {
            String gamepadToSelect = profilesController.getCurrentUsingProfile().getGamepadName();
            if (profilesController.getCurrentUsingProfile().getGamepadName() != null && gamepadToSelect.equals(gamepads_CB.getItemAt(i))) {
                gamepads_CB.setSelectedIndex(i);
            }
        }
        for (ConfigPanel cfgPanel : profilesController.getCurrentUsingProfile().getConfigPanels()) {
            int posX = (int) scallingController.convertPixelsToPercent(cfgPanel.getPosX(), configButtonsView.getWidth());
            int posY = (int) scallingController.convertPixelsToPercent(cfgPanel.getPosY(), configButtonsView.getHeight());
            JButton btn = createNewButton(posX, posY, cfgPanel.getName());
            currentConfigPanelsHashMap.put(btn, cfgPanel);
        }
        configButtonsView.repaint();
    }

    // updates activated configs
    private static void startControlsUpdater() {
        sortCurrentConfigPanelsHashMap();
        Thread thread = new Thread() {
            public void run() {
                char lastPressedChar = ' ';
                while (gearBoxStatus == AppStatusEnum.STARTED) {
                    try {
                        ConfigPanel updatedCfgPanel = updateActivatedConfigPanelLook();
                        if(updatedCfgPanel != null) {
                            lastPressedChar = updatePressedKey(updatedCfgPanel, lastPressedChar);
                        }
                        else {
                            disableAllConfigPanels();
                        }
                    }
                    catch (Exception e)
                    {
                        gearBoxStatus = AppStatusEnum.STOPPED;
                        JOptionPane.showConfirmDialog(mainWindowFrame,"Error! not set gamepad!","Error!", JOptionPane.DEFAULT_OPTION);
                    }
                }
            }
        };
        thread.start();
    }

    //sorts cfg panels in hashmap the most binds --> the lowest number of binds
    private static void sortCurrentConfigPanelsHashMap()
    {
        int configsNumber = currentConfigPanelsHashMap.size();
        ArrayList<JButton> buttons = new ArrayList<>();
        ArrayList<ConfigPanel> panels = new ArrayList<>();

        for (Map.Entry<JButton,ConfigPanel> obj : currentConfigPanelsHashMap.entrySet()) {
            buttons.add(obj.getKey());
            panels.add(obj.getValue());
        }

        boolean shouldRepeat = true;
        while(shouldRepeat) {
            shouldRepeat = false;
            for (int i = 0; i < configsNumber-1; i++) {
                ArrayList<Gamepad> currentBinds = panels.get(i).getBindsArray();
                ArrayList<Gamepad> nextBinds = panels.get(i + 1).getBindsArray();
                if (currentBinds.size() < nextBinds.size()) {
                    Collections.swap(buttons, i, i + 1);
                    Collections.swap(panels, i, i + 1);
                    shouldRepeat = true;
                }
            }
        }

        currentConfigPanelsHashMap.clear();
        for(int i=0;i<configsNumber;i++)
        {
            currentConfigPanelsHashMap.put(buttons.get(i),panels.get(i));
        }
    }

    private static char updatePressedKey(ConfigPanel updatedCfgPanel,char lastPressed)
    {
        try {
            Robot robot = new Robot();
            if (updatedCfgPanel.getValue().toCharArray().length > 0) {
                char charToPress = updatedCfgPanel.getValue().toCharArray()[0];
                if (lastPressed != charToPress) {
                    robot.keyRelease(lastPressed);
                    robot.keyPress(charToPress);
                    return charToPress;
                }
            }
        }
        catch (AWTException e)
        {
            JOptionPane.showConfirmDialog(mainWindowFrame,"Error! Cannot change gear!","Error!", JOptionPane.DEFAULT_OPTION);
        }
        return lastPressed;
    }

    private static ConfigPanel updateActivatedConfigPanelLook()
    {
        for (Map.Entry<JButton,ConfigPanel> obj : currentConfigPanelsHashMap.entrySet()) {
            JButton btn = obj.getKey();
            ConfigPanel cfgPanel = obj.getValue();

            if(cfgPanelController.isConfigActivated(cfgPanel))
            {
                btn.setBackground(Color.YELLOW);
                disableOtherConfigPanels(cfgPanel);
                return cfgPanel;
            }
        }
        return null;
    }

    //disables all config panels different from current activated
    private static void disableOtherConfigPanels(ConfigPanel activatedCfgPanel)
    {
        for (Map.Entry<JButton,ConfigPanel> obj : currentConfigPanelsHashMap.entrySet()) {
            JButton btn = obj.getKey();
            ConfigPanel cfgPanel = obj.getValue();
            if(cfgPanel != activatedCfgPanel)
            {
                btn.setBackground(Color.decode("#e3e3e3"));
            }
        }
    }

    private static void disableAllConfigPanels()
    {
        for (JButton btn : currentConfigPanelsHashMap.keySet()) {
            btn.setBackground(Color.decode("#e3e3e3"));
        }
    }

    //creates new config button
    private static JButton createNewButton(float percentFromLeft, float percentFromTop, String btnName) {
        JButton newButton = new JButton(btnName);
        scallingController.setScallingParams(11, 8, 30, percentFromTop, percentFromLeft, newButton, configButtonsView);
        newButton.setBackground(Color.decode("#e3e3e3"));
        if(!isComponentColliding(newButton,scallingController.convertPercentToPixels((int)percentFromLeft,configButtonsView.getWidth()),scallingController.convertPercentToPixels((int)percentFromTop,configButtonsView.getHeight()))) {
            configButtonsView.add(newButton);
            setMouseClickListener(newButton);
            setMouseMovementListener(newButton);
            SwingUtilities.windowForComponent(newButton).repaint();
        }
        return newButton;
    }

    private static void runETS(String path)
    {
        try {
            Runtime runtime = Runtime.getRuntime();
            String command = "cmd /c start " + path;
            runtime.exec(command);
        }
        catch (Exception e )
        {
            JOptionPane.showConfirmDialog(mainWindowFrame,"Cannot Open Euro Truck Simulator 2","Warning!", JOptionPane.DEFAULT_OPTION);
        }
    }
}
