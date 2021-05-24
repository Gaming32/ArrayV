package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class SortingNetworkGenerator {
    static boolean hasPython = false;

    static boolean verifyPythonVersion(String minVersion, String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command, "-c",
                String.format("import sys; print (sys.version_info >= (%s))", minVersion));
            Process p = builder.start();
            if (p.waitFor() != 0) {
                return false;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            return r.readLine().equals("True");
        }
        catch (IOException e) {
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyPythonVersion() {
        return (hasPython = hasPython // This caches the result if it's true
            || verifyPythonVersion("3, 2, 0", "python3")
            || verifyPythonVersion("3, 2, 0", "python"));
    }

    public static boolean verifyPythonVersionAndDialog() {
        boolean hasVersion = verifyPythonVersion();
        if (!hasVersion) {
            JOptionPane.showMessageDialog(null, "It appears that you do not have Python 3.2 or later installed on your computer! Please install it before using this mode.",
                "Sorting Network Visualizer", JOptionPane.WARNING_MESSAGE);
        }
        return hasVersion;
    }
}
