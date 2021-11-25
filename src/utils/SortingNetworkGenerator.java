package utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import panes.JErrorPane;

public class SortingNetworkGenerator {
    private static final class Comparator {
        int i1, i2;

        Comparator(int i1, int i2) {
            this.i1 = i1;
            this.i2 = i2;
        }

        @Override
        public String toString() {
            return i1 + ":" + i2;
        }

        @Override
        public int hashCode() {
            return (i1 << 16) + i2;
        }

        boolean overlaps(Comparator other) {
            return (this.i1 < other.i1 && other.i1 < this.i2) ||
                   (this.i1 < other.i2 && other.i2 < this.i2) ||
                   (other.i1 < this.i1 && this.i1 < other.i2) ||
                   (other.i1 < this.i2 && this.i2 < other.i2);
        }

        boolean hasSameInput(Comparator other) {
            return this.i1 == other.i1 ||
                   this.i1 == other.i2 ||
                   this.i2 == other.i1 ||
                   this.i2 == other.i2;
        }
    }

    private static int getMaxInput(Comparator[] comparators) {
        int maxInput = 0;
        for (Comparator c : comparators) {
            if (c.i2 > maxInput) {
                maxInput = c.i2;
            }
        }
        return maxInput;
    }

    public static boolean encodeNetwork(Comparator[] comparators, File file) {
        int scale = 1;
        int xScale = scale * 35;
        int yScale = scale * 20;

        StringBuilder comparatorsSvg = new StringBuilder();
        double w = xScale;
        Map<Comparator, Double> group = new HashMap<>();
        for (Comparator c : comparators) {
            for (Comparator other : group.keySet()) {
                if (c.hasSameInput(other)) {
                    for (double pos : group.values()) {
                        if (pos > w) {
                            w = pos;
                        }
                    }
                    w += xScale;
                    group.clear();
                    break;
                }
            }

            double cx = w;
            for (Entry<Comparator, Double> entry : group.entrySet()) {
                Comparator other = entry.getKey();
                double otherPos = entry.getValue();
                if (otherPos >= cx && c.overlaps(other)) {
                    cx = otherPos + xScale / 3.0;
                }
            }

            int y0 = yScale + c.i1 * yScale;
            int y1 = yScale + c.i2 * yScale;
            comparatorsSvg.append("<circle cx='").append(cx).append("' cy='").append(y0).append("' r='3' style='stroke:black;stroke-width:1;fill=yellow'/>")
                          .append("<line x1='").append(cx).append("' y1='").append(y0).append("' x2='").append(cx).append("' y2='").append(y1).append("' style='stroke:black;stroke-width:1'/>")
                          .append("<circle cx='").append(cx).append("' cy='").append(y1).append("' r='3' style='stroke:black;stroke-width:1;fill=yellow'/>");
            group.put(c, cx);
        }

        StringBuilder linesSvg = new StringBuilder();
        w += xScale;
        int n = getMaxInput(comparators) + 1;
        for (int i = 0; i < n; i++) {
            int y = yScale + i * yScale;
            linesSvg.append("<line x1='0' y1='").append(y).append("' x2='").append(w).append("' y2='").append(y).append("' style='stroke:black;stroke-width:1'/>");
        }

        int h = (n + 1) * yScale;
        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.write("<?xml version='1.0' encoding='utf-8'?><!DOCTYPE svg>");
            writer.write("<svg width='" + w + "px' height='" + h + "px' xmlns='http://www.w3.org/2000/svg'>");
            writer.write(comparatorsSvg.toString());
            writer.write(linesSvg.toString());
            writer.write("</svg>");
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e, "Sorting Network Visualizer");
            return false;
        }
        return true;
    }

    public static String encodeNetworkAndDisplay(String name, ArrayList<Integer> indices, int arrayLength) {
        Comparator[] comparators;
        try {
            comparators = new Comparator[indices.size() / 2];
            for (int i = 0, j = 1; i < comparators.length; i++, j += 2) {
                comparators[i] = new Comparator(indices.get(j - 1), indices.get(j));
            }
        } catch (OutOfMemoryError e) {
            JErrorPane.invokeErrorMessage(e, "Sorting Network Visualizer");
            return null;
        }
        System.out.println("Length: " + arrayLength + "\tComparators: " + comparators.length / 2);
        indices.clear();
        indices.trimToSize();
        String path = "network_" + name + "_" + arrayLength + ".svg";
        File file = new File(path);
        try {
            if (!encodeNetwork(comparators, file)) {
                return null;
            }
        } catch (OutOfMemoryError e) {
            JErrorPane.invokeCustomErrorMessage(
                "ArrayV ran out of memory trying to visualize this sorting network.\n" +
                "Either run ArrayV with more memory (or a smaller maximum length) or contemplate your life choices."
            );
            return null;
        }
        JOptionPane.showMessageDialog(null, "Successfully saved output to file \"" + path + "\"",
            "Sorting Network Visualizer", JOptionPane.INFORMATION_MESSAGE);
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
