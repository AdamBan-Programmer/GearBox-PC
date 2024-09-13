package org.example.GUI;

import net.java.games.input.Controller;
import org.example.Configs.ConfigPanel;
import org.example.File.MemoryOperations;
import org.example.Profiles.Profile;
import org.example.Settings.AppSettings;
import org.example.Utils.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CentralGUI extends JFrame implements CreatorGUI{

    static MemoryOperations memoryController = new MemoryOperations();
    static ScaleLayout scallingController = new ScaleLayout();
    static Gamepad gamepadController = new Gamepad();
    static ConfigPanel cfgPanelController = new ConfigPanel();
    static CommandLine cmdController = new CommandLine();
    static RobotOperations robotController = new RobotOperations();

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
    private static LinkedHashMap<JButton, ConfigPanel> configHashMap = new LinkedHashMap<>();

    public CentralGUI() {
        setGuiParams();
        addGuiComponents();
        setGuiComponentsParams();
        addGuiComponentsToListeners();
        setGuiIcon();

        //sets all default settings and params
        try {
            memoryController.checkAllDirectoriesExist();
            memoryController.readSerializedProfile();
            memoryController.readSerializedSettings();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showConfirmDialog(this, "Error! Cannot load files.","Error!", JOptionPane.DEFAULT_OPTION);
        }
        catch (NullPointerException n)
        {
            JOptionPane.showInputDialog(this, "Set path to Euro Truck Simulator 2 in Settings!","Warning!", JOptionPane.DEFAULT_OPTION);
        }
        finally {
            loadProfilesSpinner();
            loadGamepadsSpinner();
            updateAppRunningStatus();
            addPanelsIntoFrame();
            selectFirstProfile();
        }
    }

    @Override
    public void setGuiParams() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("GearBoxBinder: ");
        Point screenSize = scallingController.getWindowSize(60, 60);
        this.setSize(screenSize.x, screenSize.y);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        layeredPane.setPreferredSize(new Dimension(screenSize.x, screenSize.y));
    }

    @Override
    public void setGuiComponentsParams() {
        scallingController.setScallingParams(100,100,0,0,0,defaultElementsView,this);
        scallingController.setScallingParams(56,69,0,9,21,configButtonsView,this);

        scallingController.setScallingParams(20, 4, 60, 0, 0, newProfile_LB, defaultElementsView);
        scallingController.setScallingParams(20, 5, 50, 3.5f, 0, newProfile_BT, defaultElementsView);
        scallingController.setScallingParams(20, 4, 60, 9.5f, 0, addConfig_LB, defaultElementsView);
        scallingController.setScallingParams(20, 5, 50, 13, 0, addConfig_BT, defaultElementsView);
        scallingController.setScallingParams(20, 4, 60, 19, 0, saveProfile_LB, defaultElementsView);
        scallingController.setScallingParams(20, 5, 50, 22.5f, 0, saveProfile_BT, defaultElementsView);
        scallingController.setScallingParams(20, 4, 60, 28.5f, 0, renameProfile_LB, defaultElementsView);
        scallingController.setScallingParams(20, 5, 50, 32, 0, renameProfile_BT, defaultElementsView);
        scallingController.setScallingParams(20, 4, 60, 38, 0, removeProfile_LB, defaultElementsView);
        scallingController.setScallingParams(20, 5, 50, 41.5f, 0, removeProfile_BT, defaultElementsView);
        scallingController.setScallingParams(56, 4, 60, 0, 42, profiles_LB, defaultElementsView);
        scallingController.setScallingParams(56, 5, 50, 3.5f, 21, profiles_CB, defaultElementsView);
        scallingController.setScallingParams(20, 5, 60, 3.5f, 78, profileName_LB, defaultElementsView);
        scallingController.setScallingParams(20, 4, 60, 9.5f, 78, gamepad_LB, defaultElementsView);
        scallingController.setScallingParams(20, 5, 50, 13, 78, gamepads_CB, defaultElementsView);
        scallingController.setScallingParams(20, 5, 50, 22.5f, 78, appRunningStatus_LB, defaultElementsView);
        scallingController.setScallingParams(20, 17.5f, 10, 33, 78, infoPanel_LB, defaultElementsView);
        scallingController.setScallingParams(20, 4, 60, 56.5f, 0, settings_LB, defaultElementsView);
        scallingController.setScallingParams(20, 5, 50, 60, 0, settings_BT, defaultElementsView);
        scallingController.setScallingParams(20, 4, 60, 66, 0, runETS_LB, defaultElementsView);
        scallingController.setScallingParams(20, 5, 40, 69.5f, 0, runETS_BT, defaultElementsView);
        scallingController.setScallingParams(40, 10, 50, 80, 9.5f, startButton_BT, defaultElementsView);
        scallingController.setScallingParams(40, 10, 50, 80, 49.5f, stopButton_BT, defaultElementsView);

        defaultElementsView.setBackground(Color.decode("#C0C0C0"));
        defaultElementsView.setLayout(null);
        defaultElementsView.setOpaque(true);

        configButtonsView.setLayout(null);
        configButtonsView.setOpaque(true);
        configButtonsView.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        removeProfile_BT.setEnabled(false);
        infoPanel_LB.setVerticalAlignment(SwingConstants.TOP);
        profileName_LB.setHorizontalAlignment(SwingConstants.CENTER);
        profileName_LB.setOpaque(true);
        profiles_CB.addItem("Profile");

        for (Component component : defaultElementsView.getComponents()) {
            component.setBackground(Color.decode("#e3e3e3"));
        }
    }

    @Override
    public void addGuiComponents() {
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

    @Override
    public void addGuiComponentsToListeners() {
        setMouseMovementListener(this);
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

    @Override
    public void setGuiIcon() {
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(this.getClass().getResourceAsStream("/images/AppImage.png")));
            this.setIconImage(icon.getImage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //adds panels into a frame
    private void addPanelsIntoFrame()
    {
        layeredPane.add(defaultElementsView, Integer.valueOf(0));
        layeredPane.add(configButtonsView, Integer.valueOf(1));
        this.getContentPane().add(layeredPane);
        this.setVisible(true);
    }

    // reloads profiles into a spinner
    private static void loadProfilesSpinner()
    {
        int itemsCount = profiles_CB.getItemCount();
        for(int i =itemsCount-1;i>0;i--)
        {
            profiles_CB.removeItemAt(i);
        }
        for(Profile profile : Profile.getProfilesArray())
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

    private void selectFirstProfile()
    {
        if(!Profile.getProfilesArray().isEmpty()) {
            selectProfile(1);
        }
        else
        {
            setDefaultLook();
        }
    }

    //clears and repaints main window
    private static void setDefaultLook()
    {
        Profile.clearCurrentProfile();
        configHashMap.clear();
        gamepads_CB.setSelectedIndex(0);
        profiles_CB.setSelectedIndex(0);
        updateCfgPanelInfo("",0,0);
        profileName_LB.setText("profile: " +"'" + "'");
        configButtonsView.removeAll();
        configButtonsView.repaint();
    }

    //spinner item selected listener
    private void setItemChangedListener(JComboBox component) {
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
                            Profile.getInstance().setGamepadName(gamepadName);
                        }
                    }
                }
            }
        });
    }

    //mouse move listener
    private void setMouseMovementListener(Component component) {
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
    private void setMouseClickListener(Component component) {
        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component component = (Component) e.getSource();

                if(component.isEnabled()) {
                    if (component == addConfig_BT) {
                        JButton btn = createNewButton(0, 0, "cfg");
                        if (isComponentColliding(btn, 0, 0)) {
                            JOptionPane.showConfirmDialog(CentralGUI.this, "To create new button, remove other buttons from (0,0) place", "Warning!", JOptionPane.DEFAULT_OPTION);
                        } else {
                            ConfigPanel panel = new ConfigPanel("", new ArrayList<Gamepad>(), new ArrayList<Range>(), "cfg", 0, 0, new Point(0, 0));
                            configHashMap.put(btn, panel);
                        }
                        return;
                    }

                    if (component == newProfile_BT) {
                        gearBoxStatus = AppStatusEnum.STOPPED;
                        setDefaultLook();
                        Profile.createNewProfile();
                        newProfile_BT.setEnabled(false);
                        return;
                    }

                    if (component == runETS_BT) {
                        String path = AppSettings.getInstance().getEtsPath();
                        try {
                            if (!path.isEmpty()) {
                                cmdController.runETS(path);
                            } else {
                                JOptionPane.showConfirmDialog(CentralGUI.this, "Cannot Open Euro Truck Simulator 2", "Warning!", JOptionPane.DEFAULT_OPTION);
                            }
                        }
                        catch (IOException ioe )
                        {
                            JOptionPane.showConfirmDialog(CentralGUI.this,"Cannot Open Euro Truck Simulator 2, path is empty","Warning!", JOptionPane.DEFAULT_OPTION);
                        }
                        return;
                    }

                    if (component == startButton_BT) {
                        int configsCount = Profile.getInstance().getConfigPanels().size();
                        if (configsCount > 0) {
                            gearBoxStatus = AppStatusEnum.STARTED;
                            startButton_BT.setEnabled(false);
                            stopButton_BT.setEnabled(true);
                            startControlsUpdater();
                            updateAppRunningStatus();
                        } else {
                            JOptionPane.showConfirmDialog(CentralGUI.this, "Cannot start! No configs created! Add new Config or select other Profile", "Warning!", JOptionPane.DEFAULT_OPTION);
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
                            if(Profile.getInstance().getName().isEmpty()) {
                                Profile.setProfileNameRequest();
                                while (!isProfilePreparedToSave()) {
                                    Profile.renameProfileRequest("This name is incorrect. Set different name!");
                                }
                            }
                            String newProfileName = Profile.getInstance().getName();
                            profileName_LB.setText("profile: " + "'" + newProfileName + "'");
                            Profile.saveProfile();
                            reloadProfiles();
                            JOptionPane.showConfirmDialog(CentralGUI.this, "Saved profile.", "Info message:", JOptionPane.DEFAULT_OPTION);
                        }
                        catch (IOException | ClassNotFoundException ex)
                        {
                            JOptionPane.showConfirmDialog(CentralGUI.this, "Cannot save profile!", "Warning", JOptionPane.DEFAULT_OPTION);
                        }
                        return;
                    }

                    if (component == renameProfile_BT) {
                        try {
                            String oldName = Profile.getInstance().getName();
                            Profile.renameProfileRequest("Rename profile: ");
                            while (!isProfilePreparedToSave()) {
                                Profile.renameProfileRequest("This name is incorrect. Set different name!");
                            }
                            String newName = Profile.getInstance().getName();
                            memoryController.renameFile(oldName, newName);
                            Profile.saveProfile();
                            profileName_LB.setText("profile: " + "'" + newName + "'");
                            reloadProfiles();
                        }
                        catch (IOException | ClassNotFoundException ex)
                        {
                            JOptionPane.showConfirmDialog(CentralGUI.this, "Cannot load profile!", "Warning", JOptionPane.DEFAULT_OPTION);
                        }
                        return;
                    }

                    if (component == removeProfile_BT) {
                        JOptionPane.showConfirmDialog(CentralGUI.this, "removed profile: " + Profile.getInstance().getName(), "Info message:", JOptionPane.DEFAULT_OPTION);
                        int index = profiles_CB.getSelectedIndex() - 1;
                        Profile.removeProfile(index);
                        profiles_CB.removeItemAt(index + 1);
                        gearBoxStatus = AppStatusEnum.STOPPED;
                        selectFirstProfile();
                        return;
                    }

                    if (component == settings_BT) {
                        EventQueue.invokeLater(new Runnable()
                        {
                            public void run()
                            {
                                new SettingsGUI();
                            }
                        });
                        return;
                    }
                    showConfigSettings((JButton) component);
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
        for (JButton btn : configHashMap.keySet()) {
            if(btn == component)
            {
                return true;
            }
        }
        return false;
    }

    public static void updateConfigPanelName()
    {
        for(JButton btn : configHashMap.keySet())
        {
            String newName = configHashMap.get(btn).getName();
            btn.setText(newName);
        }
    }

    private boolean isProfilePreparedToSave()
    {
        String selectedProfileName = (String) profiles_CB.getSelectedItem();
        String profileName = Profile.getInstance().getName();

        if (selectedProfileName.equals(profileName) || !profileName.isEmpty() && !memoryController.isProfileNameAlreadyUsed(profileName))
        {
            return true;
        }
        if(profileName.isEmpty())
        {
            JOptionPane.showConfirmDialog(this, "Profile name cannot be empty!", "Warning", JOptionPane.DEFAULT_OPTION);
            Profile.renameProfileRequest("This name is incorrect. Set different name!");
            String newProfileName = Profile.getInstance().getName();
            profileName_LB.setText("profile: " + "'" + newProfileName + "'");
        }
        return false;
    }

    private void reloadProfiles() throws IOException, ClassNotFoundException {
        int selected_index = profiles_CB.getSelectedIndex();
        Profile.getProfilesArray().clear();
        memoryController.readSerializedProfile();
        loadProfilesSpinner();
        startButton_BT.setEnabled(true);
        selectProfile(selected_index);
    }

    private void selectProfile(int index)
    {
        setSelectedGamepad();
        setSelectedProfile(index);
        newProfile_BT.setEnabled(true);
    }

    //sets current using gamepad into spinner
    private static void setSelectedGamepad()
    {
        for (int i = 0; i < gamepads_CB.getItemCount(); i++) {
            if (gamepads_CB.getItemAt(i).toString().equals(Profile.getInstance().getGamepadName())) {
                gamepads_CB.setSelectedIndex(i);
            }
        }
    }

    // sets selected profiles (creating buttons and sets their positions and texts, name and others)
    private void setSelectedProfile(int selected_index)
    {
        // it means selected New created profile
        if(selected_index == 0)
        {
            selected_index = Profile.getProfilesArray().size();
        }
        Profile SelectedProfile = Profile.getProfilesArray().get(selected_index-1);
        Profile.changeProfile(SelectedProfile);
        loadProfilesComponents();
        removeProfile_BT.setEnabled(true);
        profiles_CB.setSelectedIndex(selected_index);
    }

    // config button pressed and ready to move
    private void startMovingConfigButton(Component btn)
    {
        Point mousePos = MouseInfo.getPointerInfo().getLocation();

        // calculating selected pos from global mouse pos on ConfigButtons Panel pos
        int configButtonsPosX = (mousePos.x - scallingController.convertPercentToPixels(21, this.getWidth()))- btn.getX();
        int configButtonsPosY = (mousePos.y - scallingController.convertPercentToPixels(9, this.getHeight()))- this.getInsets().top- btn.getY();
        int x = (int) scallingController.convertPixelsToPercent(configButtonsPosX,btn.getWidth());
        int y = (int) scallingController.convertPixelsToPercent(configButtonsPosY,btn.getHeight());
        configHashMap.get(btn).setSelectedPoint(new Point(x,y));
    }

    // move config button
    private void updateCfgPanelPosition(JButton movingButton)
    {
        Point cfgButtonPos = configHashMap.get(movingButton).getSelectedPoint();
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        int newX = (mousePos.x - scallingController.convertPercentToPixels(21, this.getWidth()))- scallingController.convertPercentToPixels((float) cfgButtonPos.getX(),movingButton.getWidth());
        int newY = (mousePos.y - scallingController.convertPercentToPixels(9, this.getHeight())) - this.getInsets().top - scallingController.convertPercentToPixels((float) cfgButtonPos.getY(),movingButton.getHeight());

        if(!isComponentColliding(movingButton, newX, newY))
        {
            movingButton.setLocation(newX, newY);
            configHashMap.get(movingButton).setPosX(newX);
            configHashMap.get(movingButton).setPosY(newY);
            updateCfgPanelInfo(movingButton.getText(),newX,newY);
        }
    }

    // removes config button and all config
    private static void removeConfigFromProfile(JButton btn)
    {
        ConfigPanel cfgPanel = configHashMap.get(btn);
        configHashMap.remove(btn);
        configButtonsView.remove(btn);
        Profile.getInstance().getConfigPanels().remove(cfgPanel);
        configButtonsView.repaint();
    }

    //opens config_panel_window
    private static void showConfigSettings(JButton btn)
    {
        ConfigPanel cfgPanel = configHashMap.get(btn);
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new CalibrationGUI(cfgPanel);
            }
        });
    }

    private static void updateProfileData()
    {
        Profile.getInstance().setGamepadName(gamepads_CB.getSelectedItem().toString());
        ArrayList<ConfigPanel> newConfigPanels = new ArrayList<>();
        newConfigPanels.addAll(configHashMap.values());
        Profile.getInstance().setConfigPanels(newConfigPanels);
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
    private void loadProfilesComponents() {
        Profile profile = Profile.getInstance();
        configButtonsView.removeAll();
        profileName_LB.setText("profile: " + "'" + profile.getName() + "'");
        configHashMap.clear();

        for (int i = 1; i < gamepads_CB.getItemCount(); i++) {
            String gamepadToSelect = profile.getGamepadName();
            if (profile.getGamepadName() != null && gamepadToSelect.equals(gamepads_CB.getItemAt(i))) {
                gamepads_CB.setSelectedIndex(i);
            }
        }
        for (ConfigPanel cfgPanel : profile.getConfigPanels()) {
            int posX = (int) scallingController.convertPixelsToPercent(cfgPanel.getPosX(), configButtonsView.getWidth());
            int posY = (int) scallingController.convertPixelsToPercent(cfgPanel.getPosY(), configButtonsView.getHeight());
            JButton btn = createNewButton(posX, posY, cfgPanel.getName());
            configHashMap.put(btn, cfgPanel);
        }
        configButtonsView.repaint();
    }

    // updates activated configs
    private void startControlsUpdater() {
        sortCurrentConfigPanelsHashMap();
        Thread thread = new Thread() {
            public void run() {
                char lastPressedChar = ' ';
                while (gearBoxStatus == AppStatusEnum.STARTED) {
                    try {
                        ConfigPanel updatedCfgPanel = updateActivatedConfigPanelLook();
                        if(updatedCfgPanel != null) {
                            lastPressedChar = robotController.PressKey(updatedCfgPanel, lastPressedChar);
                        }
                        else {
                            disableAllConfigPanels();
                        }
                    }
                    catch (AWTException awtException)
                    {
                        JOptionPane.showConfirmDialog(CentralGUI.this,"Couldn't press a key!","Error!", JOptionPane.DEFAULT_OPTION);
                    }
                    catch (Exception e)
                    {
                        gearBoxStatus = AppStatusEnum.STOPPED;
                        JOptionPane.showConfirmDialog(CentralGUI.this,"Error! not set gamepad!","Error!", JOptionPane.DEFAULT_OPTION);
                    }
                }
            }
        };
        thread.start();
    }

    //sorts cfg panels in hashmap the most binds --> the lowest number of binds
    private static void sortCurrentConfigPanelsHashMap()
    {
        int configsNumber = configHashMap.size();
        ArrayList<JButton> buttons = new ArrayList<>();
        ArrayList<ConfigPanel> panels = new ArrayList<>();

        for (Map.Entry<JButton,ConfigPanel> obj : configHashMap.entrySet()) {
            buttons.add(obj.getKey());
            panels.add(obj.getValue());
        }

        boolean shouldRepeat = true;
        while(shouldRepeat) {
            shouldRepeat = false;
            for (int i = 0; i < configsNumber-1; i++) {
                ArrayList<Gamepad> currentBinds = panels.get(i).getBind();
                ArrayList<Gamepad> nextBinds = panels.get(i + 1).getBind();
                if (currentBinds.size() < nextBinds.size()) {
                    Collections.swap(buttons, i, i + 1);
                    Collections.swap(panels, i, i + 1);
                    shouldRepeat = true;
                }
            }
        }
        configHashMap.clear();
        for(int i=0;i<configsNumber;i++)
        {
            configHashMap.put(buttons.get(i),panels.get(i));
        }
    }

    private static ConfigPanel updateActivatedConfigPanelLook()
    {
        for (Map.Entry<JButton,ConfigPanel> obj : configHashMap.entrySet()) {
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
        for (Map.Entry<JButton,ConfigPanel> obj : configHashMap.entrySet()) {
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
        for (JButton btn : configHashMap.keySet()) {
            btn.setBackground(Color.decode("#e3e3e3"));
        }
    }

    public static void removeConfigPanel(ConfigPanel configPanel)
    {
        for (Map.Entry<JButton, ConfigPanel> entry : configHashMap.entrySet()) {
            JButton btn = entry.getKey();
            ConfigPanel cfgPanel = entry.getValue();

            if(cfgPanel.equals(configPanel))
            {
                removeConfigFromProfile(btn);
                JOptionPane.showConfirmDialog(null,"removed: " + btn.getText() + " from " + Profile.getInstance().getName(),"Info message", JOptionPane.DEFAULT_OPTION);
                return;
            }
        }
    }

    //creates new config button
    private JButton createNewButton(float percentFromLeft, float percentFromTop, String btnName) {
        JButton newButton = new JButton(btnName);
        scallingController.setScallingParams(11, 8, 30, percentFromTop, percentFromLeft, newButton, configButtonsView);
        newButton.setBackground(Color.decode("#e3e3e3"));
        if (!isComponentColliding(newButton, scallingController.convertPercentToPixels((int) percentFromLeft, configButtonsView.getWidth()), scallingController.convertPercentToPixels((int) percentFromTop, configButtonsView.getHeight()))) {
            configButtonsView.add(newButton);
            setMouseClickListener(newButton);
            setMouseMovementListener(newButton);

            SwingUtilities.windowForComponent(newButton).repaint();
        }
        return newButton;
    }
}
