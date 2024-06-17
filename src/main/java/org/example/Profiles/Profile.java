package org.example.Profiles;

import org.example.Configs.ConfigPanel;
import org.example.File.MemoryOperations;

import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable {

    static MemoryOperations memoryController = new MemoryOperations();
    private static final long serialVersionUID = -7889730748737034949L;
    private String name;
    private ArrayList<ConfigPanel> configPanels;
    private String gamepadName;

    //profiles
    private static ArrayList<Profile> profilesList = new ArrayList<>();

    //current profile
    private static Profile currentUsingProfile;

    public Profile(String name,ArrayList<ConfigPanel> configPanels,String gamepadName)
    {
        this.name = name;
        this.configPanels = configPanels;
        this.gamepadName = gamepadName;
    }

    public Profile()
    {
    }

    //creates new profile
    public void createNewProfile()
    {
        Profile newProfile = new Profile("",new ArrayList<ConfigPanel>(),null);
        setCurrentUsingProfile(newProfile);
    }

    public void setProfileNameRequest() throws NullPointerException
    {
        String newProfileName = (String) JOptionPane.showInputDialog(null, "Set profile name.", "Warning!"
                ,JOptionPane.PLAIN_MESSAGE, null, null,null);
        if(newProfileName != null) {
            getCurrentUsingProfile().setName(newProfileName);
        }
        else
        {
            throw new NullPointerException();
        }
    }

    public void renameProfileRequest(String message)
    {
        String profileName = getCurrentUsingProfile().getName();
        String newProfileName = (String) JOptionPane.showInputDialog(null, message, "Warning!"
                ,JOptionPane.PLAIN_MESSAGE, null, null,profileName );
        if(newProfileName.isEmpty() || newProfileName == null)
        {
            getCurrentUsingProfile().setName("");
        }
        else {
            getCurrentUsingProfile().setName(newProfileName);
        }
    }

    //saves profile
    public void saveProfile() throws IOException, ClassNotFoundException {
        String pathToFile = "/GearBoxBinder/Profiles/" + currentUsingProfile.getName() + ".ser";
        profilesList.add(currentUsingProfile);
        memoryController.serializeObject(pathToFile,currentUsingProfile);
        profilesList.clear();
    }

    //removes profile
    public void removeProfile(int index)
    {
        if(index >= 0) {
            memoryController.removeFile(index);
        }
        getProfilesArray().remove(index);
    }

    //returns current using profile
    public Profile getCurrentUsingProfile()
    {
        if(currentUsingProfile == null)
        {
            currentUsingProfile = new Profile("",new ArrayList<ConfigPanel>(),null);
        }
        return currentUsingProfile;
    }

    //sets current using profile
    public void setCurrentUsingProfile(Profile profile)
    {
        currentUsingProfile = profile;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Profile> getProfilesArray() {
        return profilesList;
    }

    public ArrayList<ConfigPanel> getConfigPanels() {
        return this.configPanels;
    }

    public void setConfigPanels(ArrayList<ConfigPanel> configPanels) {
        this.configPanels = configPanels;
    }

    public String getGamepadName() {
        return this.gamepadName;
    }

    public void setGamepadName(String gamepad) {
        this.gamepadName = gamepad;
    }
}
