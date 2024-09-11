package org.example.File;

import org.example.Profiles.Profile;
import org.example.Settings.AppSettings;

import java.io.*;

public class MemoryOperations implements Serializable {

    private static final long serialVersionUID = -7889730748737034949L;

    private static final String SAVE_PATH = "C:/";
    private static final String APP_SAVE_DIR = "GearBoxBinder";
    private static final String PROFILE_SAVE_DIR = "Profiles";
    private static final String SETTINGS_SAVE_DIR = "Settings";

    public void checkAllDirectoriesExist() {
        checkDirectoryExists(SAVE_PATH, APP_SAVE_DIR);
        checkDirectoryExists(SAVE_PATH + APP_SAVE_DIR + "/", SETTINGS_SAVE_DIR);
        checkDirectoryExists(SAVE_PATH + APP_SAVE_DIR + "/", PROFILE_SAVE_DIR);
    }

    //checks the saves folder exists
    private void checkDirectoryExists(String path, String dirName) {
        String directoryPath = path + dirName;
        java.io.File settingsDirectory = new java.io.File(directoryPath);
        if (!settingsDirectory.exists()) {
            settingsDirectory.mkdirs();
        }
    }

    // Saves object into a file
    public void serializeObject(String pathToFile, Object object) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(SAVE_PATH + pathToFile);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(object);
        out.close();
        fileOut.close();
    }

    // checks the Settings File exists and Reads the settings
    public void readSerializedProfile() throws IOException, ClassNotFoundException {
        File directory = new File(SAVE_PATH + APP_SAVE_DIR + "/" + PROFILE_SAVE_DIR);
        File[] serialized_profiles = directory.listFiles();

        if (serialized_profiles.length > 0) {
            for (File file : serialized_profiles) {
                FileInputStream streamIn = new FileInputStream(file);
                ObjectInputStream object_input_stream = new ObjectInputStream(streamIn);
                Profile.getProfilesArray().add((Profile) object_input_stream.readObject());
                streamIn.close();
            }
        }
    }

    public void readSerializedSettings() throws IOException, ClassNotFoundException, NullPointerException, ArrayIndexOutOfBoundsException {
        File directory = new File(SAVE_PATH + APP_SAVE_DIR + "/" + SETTINGS_SAVE_DIR);
        File[] serialized_settings = directory.listFiles();

        if (serialized_settings.length > 0) {
            FileInputStream streamIn = new FileInputStream(serialized_settings[0]);
            ObjectInputStream objectInputStream = new ObjectInputStream(streamIn);
            AppSettings.loadSettings((AppSettings) objectInputStream.readObject());
        }
    }

    //removes file by index
    public void removeFile(int index) {
        File directory = new File(SAVE_PATH + APP_SAVE_DIR + "/" + PROFILE_SAVE_DIR);
        File[] serialized_files = directory.listFiles();

        for (int i = 0; i < serialized_files.length; i++) {
            if (i == index) {
                serialized_files[i].delete();
            }
        }
    }

    public void renameFile(String oldName, String newName) {
        File directory = new File(SAVE_PATH + APP_SAVE_DIR + "/" + PROFILE_SAVE_DIR);
        File[] serialized_settings = directory.listFiles();

        for (File file : serialized_settings) {
            String fileName = file.getName();
            if (fileName.equals(oldName + ".ser")) {
                File renamedFile = new File(SAVE_PATH + APP_SAVE_DIR + "/" + PROFILE_SAVE_DIR + "/" + newName + ".ser");
                file.renameTo(renamedFile);
                return;
            }
        }
    }

    public boolean isProfileNameAlreadyUsed(String name) {
        File directory = new File(SAVE_PATH + APP_SAVE_DIR + "/" + PROFILE_SAVE_DIR);
        File[] serialized_settings = directory.listFiles();

        if (serialized_settings.length > 0) {
            for (File file : serialized_settings) {
                String fileName = file.getName();
                if (fileName.equals(name + ".ser")) {
                    return true;
                }
            }
        }
        return false;
    }
}
