package org.example.Settings;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public final class AppSettings implements Serializable {

    private static AppSettings instance;
    private String etsPath;
    private AppSettings(String etsPath) {
        this.etsPath = etsPath;
    }

    public static void loadSettings(AppSettings newAppSettings)
    {
        instance = newAppSettings;
    }
    public static AppSettings getInstance() {

        if(instance == null)
        {
            instance = new AppSettings("");
        }
        return instance;
    }
}
