package org.example.Profiles;

import lombok.Getter;
import lombok.Setter;
import org.example.Configs.ConfigPanel;
import org.example.File.MemoryOperations;

import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
public final class Profile implements Serializable {

    static MemoryOperations memoryController = new MemoryOperations();
    private static final long serialVersionUID = -7889730748737034949L;

    private static Profile instance;
    private String name;
    private ArrayList<ConfigPanel> configPanels;
    private String gamepadName;

    //profiles
    private static ArrayList<Profile> profilesList = new ArrayList<>();
    private Profile(String name,ArrayList<ConfigPanel> configPanels,String gamepadName)
    {
        this.name = name;
        this.configPanels = configPanels;
        this.gamepadName = gamepadName;
    }

    //creates new profile
    public static void createNewProfile()
    {
        instance = new Profile("",new ArrayList<ConfigPanel>(),null);
    }

    public static void setProfileNameRequest() throws NullPointerException
    {
        String newProfileName = (String) JOptionPane.showInputDialog(null, "Set profile name.", "Warning!"
                ,JOptionPane.PLAIN_MESSAGE, null, null,null);
        if(newProfileName != null) {
            instance.setName(newProfileName);
        }
        else
        {
            throw new NullPointerException();
        }
    }

    public static void renameProfileRequest(String message) {
        String profileName = getInstance().getName();
        String newProfileName = (String) JOptionPane.showInputDialog(null, message, "Warning!"
                , JOptionPane.PLAIN_MESSAGE, null, null, profileName);
        if(newProfileName != null) {
            if (newProfileName.isEmpty()) {
                instance.setName("");
            } else {
                instance.setName(newProfileName);
            }
        }
    }

    //saves profile
    public static void saveProfile() throws IOException, ClassNotFoundException {
        String pathToFile = "/GearBoxBinder/Profiles/" + instance.getName() + ".ser";
        profilesList.add(instance);
        memoryController.serializeObject(pathToFile, instance);
        profilesList.clear();
    }

    //removes profile
    public static void removeProfile(int index)
    {
        if(index >= 0) {
            memoryController.removeFile(index);
        }
        profilesList.remove(index);
    }

    public static void changeProfile(Profile newProfile)
    {
        instance = newProfile;
    }

    public static void clearCurrentProfile()
    {
        instance = null;
    }

    //returns current using profile
    public static Profile getInstance()
    {
        if(instance == null)
        {
            instance = new Profile("",new ArrayList<ConfigPanel>(),null);
        }
        return instance;
    }

    public static ArrayList<Profile> getProfilesArray()
    {
        return profilesList;
    }
}
