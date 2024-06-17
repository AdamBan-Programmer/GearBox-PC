package org.example.Settings;


import java.io.Serializable;

public class AppSettings implements Serializable {

    private String etsPath;

    private static AppSettings currentAppSettings = null;

    public AppSettings(String etsPath) {
        this.etsPath = etsPath;
    }

    public AppSettings() {
    }

    public AppSettings getCurrentAppSettings() {

        if(currentAppSettings == null)
        {
            currentAppSettings = new AppSettings("");
        }
        return currentAppSettings;
    }

    public void setCurrentAppSettings(AppSettings newAppSettings) {
        currentAppSettings = newAppSettings;
    }


    public String getEtsPath() {
        return this.etsPath;
    }

}
