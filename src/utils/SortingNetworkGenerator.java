package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SortingNetworkGenerator {
    static boolean hasPython = false;

    static boolean verifyPythonVersion(String minVersion, String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command, "-c",
                String.format("import sys; print (sys.version_info > (%s))", minVersion));
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
        return (hasPython = hasPython
            || verifyPythonVersion("3, 2, 0", "python3")
            || verifyPythonVersion("3, 2, 0", "python"));
    }
}
