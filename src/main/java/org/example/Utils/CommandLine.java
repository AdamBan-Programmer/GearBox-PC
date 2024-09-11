package org.example.Utils;

import javax.swing.*;
import java.io.IOException;

public class CommandLine {

    public CommandLine() {
    }

    public void runETS(String path) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String command = "cmd /c start " + path;
        runtime.exec(command);
    }
}
