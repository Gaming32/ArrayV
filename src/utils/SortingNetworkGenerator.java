package utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import frames.UtilFrame;
import panes.JErrorPane;
import resources.sorting_network_master.SortingNetworkFetcher;

public class SortingNetworkGenerator {
    static DecimalFormat formatter = new DecimalFormat();

    static boolean hasPython = false;
    static String pythonCommand = null;

    final static int LIMIT = 20000;

    static boolean verifyPythonVersion(String minVersion, String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command, "-c",
                String.format("import sys; print (sys.version_info >= (%s))", minVersion));
            Process p = builder.start();
            if (p.waitFor() != 0) {
                return false;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            if (r.readLine().equals("True")) {
                pythonCommand = command;
                return true;
            }
            return false;
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
            || verifyPythonVersion("3, 2, 0", "python")
            || verifyPythonVersion("3, 2, 0", "py"));
    }

    public static boolean verifyPythonVersionAndDialog() {
        boolean hasVersion = verifyPythonVersion();
        if (!hasVersion) {
            JOptionPane.showMessageDialog(null, "It appears that you do not have Python 3.2 or later installed on your computer! Please install it before using this mode.",
                "Sorting Network Visualizer", JOptionPane.WARNING_MESSAGE);
        }
        return hasVersion;
    }

    public static boolean encodeNetwork(int[] indices, String path) {
        if (indices.length < 2) {
            JOptionPane.showMessageDialog(null, "Sort does not compare indices; An empty sorting network cannot be generated.",
                "File not saved", JOptionPane.ERROR_MESSAGE);

            return false;
        }
        else if (indices.length > 2*LIMIT) {
            String[] options = {"Yes", "Cancel"};
            int choice = JOptionPane.showOptionDialog(null, "Sorting network is very large and exceeds the " + formatter.format(LIMIT) + " comparator limit. Generate anyway?",
                "Warning!", 2, JOptionPane.WARNING_MESSAGE, null, options, options[1]);

            if (choice == 1) {
                return false;
            }
        }
        String result = indices[0] + ":" + indices[1];
        for (int i = 3; i < indices.length; i += 2) {
            result += "," + indices[i - 1] + ":" + indices[i];
        }
        SortingNetworkFetcher fetcher = new SortingNetworkFetcher();
        try {
            ProcessBuilder builder = new ProcessBuilder(pythonCommand, "-c",
                new BufferedReader(new InputStreamReader(fetcher.getStream())).lines().collect(Collectors.joining("\n")),
                "--svg", path);
            builder.redirectOutput(Redirect.INHERIT);
            Process p = builder.start();
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            w.write(result);
            w.close();
            if (p.waitFor() != 0) {
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                JErrorPane.invokeCustomErrorMessage(r.lines().collect(Collectors.joining("\n")));
                return false;
            }
        }
        catch (IOException e) {
            JErrorPane.invokeErrorMessage(e, "Sorting Network Visualizer");
            return false;
        }
        catch (Exception e) {
            JErrorPane.invokeErrorMessage(e, "Sorting Network Visualizer");
            return false;
        }
        return true;
    }

    public static String encodeNetworkAndDisplay(String name, Integer[] indices, int arrayLength) {
        String path = "network_" + name + "_" + arrayLength + ".svg";
        int[] indicesInt = new int[indices.length];
        for (int i = 0; i < indices.length; i++) {
            indicesInt[i] = indices[i];
        }
        if (!encodeNetwork(indicesInt, path)) {
            return null;
        }
        JOptionPane.showMessageDialog(null, "Successfully saved output to file \"" + path + "\"",
            "Sorting Network Visualizer", JOptionPane.INFORMATION_MESSAGE);
        File file = new File(path);
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
