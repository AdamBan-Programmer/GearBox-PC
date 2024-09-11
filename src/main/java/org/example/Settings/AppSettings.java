package org.example.Settings;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public final class AppSettings implements Serializable {

    private String etsPath;

    private static AppSettings currentAppSettings = null;

    private AppSettings(String etsPath) {
        this.etsPath = etsPath;
    }

    public static void loadSettings(AppSettings newAppSettings)
    {
        currentAppSettings = newAppSettings;
    }
    public static AppSettings getInstance() {

        if(currentAppSettings == null)
        {
            currentAppSettings = new AppSettings("");
        }
        return currentAppSettings;
    }
}
