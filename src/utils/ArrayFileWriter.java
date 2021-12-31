package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import panes.JErrorPane;

public final class ArrayFileWriter {
    public static boolean writeArray(String fileName, int[] array, int length) {
        try {
            FileWriter writer = new FileWriter(fileName);
            write(writer, array, length);
        } catch (IOException e) {
            JErrorPane.invokeErrorMessage(e);
            return false;
        }
        return true;
    }

    public static boolean writeArray(File file, int[] array, int length) {
        try {
            FileWriter writer = new FileWriter(file);
            write(writer, array, length);
        } catch (IOException e) {
            JErrorPane.invokeErrorMessage(e);
            return false;
        }
        return true;
    }

    private static void write(FileWriter writer, int[] array, int length) throws IOException {
        // FileWriter writer = new FileWriter(outName);
        for (int i = 0; i < length - 1; i++) {
            writer.write(array[i] + " ");
        }
        writer.write("" + array[length - 1]);
        writer.close();
    }
}
