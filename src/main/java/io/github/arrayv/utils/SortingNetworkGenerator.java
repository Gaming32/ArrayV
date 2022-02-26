package io.github.arrayv.utils;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;

public final class SortingNetworkGenerator {
    private static final File SORTING_NETWORKS_DIR = new File("sorting_networks");

    static {
        SORTING_NETWORKS_DIR.mkdirs();
    }

    private static final class Comparator {
        final int i1, i2;

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
            return this.i2 > other.i1 && other.i2 > this.i1;
        }

        boolean hasSameInput(Comparator other) {
            return this.i1 == other.i1 ||
                   this.i1 == other.i2 ||
                   this.i2 == other.i1 ||
                   this.i2 == other.i2;
        }
    }

    private static final class WriterBuilderProxy {
        final PrintWriter writer;
        final StringBuilder builder;

        WriterBuilderProxy(PrintWriter writer) {
            this.writer = writer;
            this.builder = null;
        }

        WriterBuilderProxy(StringBuilder builder) {
            this.writer = null;
            this.builder = builder;
        }

        void write(String s) {
            if (builder == null)
                writer.write(s);
            else
                builder.append(s);
        }

        @Override
        public String toString() {
            return builder == null ? writer.toString() : builder.toString();
        }

        String getValue() {
            if (builder == null) {
                throw new IllegalStateException("Cannot getValue() of PrintWriter");
            }
            return builder.toString();
        }
    }

    private static final int OUT_BUFFER_SIZE = 8192; // 16 KB

    private SortingNetworkGenerator() {
    }

    private static boolean encodeNetwork0(final Comparator[] comparators, final int n, final PrintWriter out) {
        int scale = 1;
        int xScale = scale * 36;
        int yScale = scale * 20;
        boolean small = comparators.length < 500_000;

        int h = (n + 1) * yScale;
        int w = xScale;
        List<Comparator> groupComparators = new ArrayList<>();
        List<Integer> groupPositions = new ArrayList<>();

        WriterBuilderProxy writer;
        ProgressMonitor monitor;
        int progress = 0;
        out.write("<?xml version='1.0' encoding='utf-8'?><!DOCTYPE svg>");
        if (small) {
            writer = new WriterBuilderProxy(new StringBuilder());
            monitor = new ProgressMonitor(
                ArrayVisualizer.getInstance().getWindow(),
                "Visualizing sorting network...",
                "Generating SVG",
                0, comparators.length
            );
        } else {
            monitor = new ProgressMonitor(
                ArrayVisualizer.getInstance().getWindow(),
                "Visualizing sorting network...",
                "Pre-calculating image width",
                0, comparators.length * 2
            );
            for (Comparator c : comparators) {
                for (Comparator other : groupComparators) {
                    if (c.hasSameInput(other)) {
                        for (int otherX : groupPositions) {
                            if (otherX > w) {
                                w = otherX;
                            }
                        }
                        w += xScale;
                        groupComparators.clear();
                        groupPositions.clear();
                        break;
                    }
                }
                int cx = w;
                Iterator<Integer> groupPositionIterator = groupPositions.iterator();
                for (Comparator other : groupComparators) {
                    // We don't need hasNext checks because groupPositions and
                    // groupComparators have the same length
                    int otherX = groupPositionIterator.next();
                    if (otherX >= cx && c.overlaps(other)) {
                        cx = otherX + xScale / 3;
                    }
                }
                groupComparators.add(c);
                groupPositions.add(cx);

                if ((++progress & 1023) == 0) {
                    monitor.setProgress(progress);
                    if (monitor.isCanceled()) return true;
                }
            }
            groupComparators.clear();
            groupPositions.clear();
            monitor.setNote("Writing SVG");
            out.write("<svg width='" + (w + xScale) + "' height='" + h + "' xmlns='http://www.w3.org/2000/svg'>");
            w = xScale;

            writer = new WriterBuilderProxy(out);
        }

        for (Comparator c : comparators) {
            for (Comparator other : groupComparators) {
                if (c.hasSameInput(other)) {
                    for (int otherX : groupPositions) {
                        if (otherX > w) {
                            w = otherX;
                        }
                    }
                    w += xScale;
                    groupComparators.clear();
                    groupPositions.clear();
                    break;
                }
            }

            int cx = w;
            Iterator<Integer> groupPositionIterator = groupPositions.iterator();
            for (Comparator other : groupComparators) {
                // We don't need hasNext checks because groupPositions and
                // groupComparators have the same length
                int otherX = groupPositionIterator.next();
                if (otherX >= cx && c.overlaps(other)) {
                    cx = otherX + xScale / 3;
                }
            }

            int y0 = (c.i1 + 1) * yScale;
            int y1 = (c.i2 + 1) * yScale;
            writer.write("<circle cx='" + cx + "' cy='" + y0 + "' r='3' style='stroke:black;stroke-width:1;fill=yellow'/>" +
                         "<line x1='" + cx + "' y1='" + y0 + "' x2='" + cx + "' y2='" + y1 + "' style='stroke:black;stroke-width:1'/>" +
                         "<circle cx='" + cx + "' cy='" + y1 + "' r='3' style='stroke:black;stroke-width:1;fill=yellow'/>");
            groupComparators.add(c);
            groupPositions.add(cx);

            if ((++progress & 1023) == 0) {
                monitor.setProgress(progress);
                if (monitor.isCanceled()) return true;
            }
        }
        groupComparators.clear();
        groupPositions.clear();

        w += xScale;
        for (int i = 0; i < n; i++) {
            int y = yScale + i * yScale;
            writer.write("<line x1='0' y1='" + y + "' x2='" + w + "' y2='" + y + "' style='stroke:black;stroke-width:1'/>");
        }

        if (small) {
            monitor.setNote("Writing SVG to file");
            out.write("<svg width='" + w + "' height='" + h + "' xmlns='http://www.w3.org/2000/svg'>");
            out.write(writer.getValue());
        }
        out.write("</svg>");
        monitor.close();
        return false;
    }

    public static boolean encodeNetwork(Comparator[] comparators, int inputLength, File file) {
        try (PrintWriter out = new PrintWriter(
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(file), StandardCharsets.UTF_8
                    ), OUT_BUFFER_SIZE),
                false)
            ) {
            boolean cancelled = encodeNetwork0(comparators, inputLength, out);
            if (cancelled) {
                JOptionPane.showMessageDialog(null, "Sorting network visualization cancelled",
                    "Sorting Network Visualizer", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e, "Sorting Network Visualizer");
            return false;
        }
        return true;
    }

    public static File encodeNetworkAndDisplay(String name, ArrayList<Integer> indices, int arrayLength) {
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
        File file = new File(SORTING_NETWORKS_DIR, "network_" + name + "_" + arrayLength + ".svg");
        try {
            if (!encodeNetwork(comparators, arrayLength, file)) {
                return null;
            }
        } catch (OutOfMemoryError e) {
            JErrorPane.invokeCustomErrorMessage(
                "ArrayV ran out of memory trying to visualize this sorting network.\n" +
                "Either run ArrayV with more memory (or a smaller maximum length) or contemplate your life choices."
            );
            return null;
        }
        boolean shouldOpen = JOptionPane.showConfirmDialog(
            ArrayVisualizer.getInstance().getMainWindow(),
            "Successfully saved output to file \"" + file + "\"\nWould you like to open it now?",
            "Sorting Network Visualizer",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        ) == JOptionPane.YES_OPTION; // false on NO_OPION or CLOSED_OPTION
        if (shouldOpen) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (IOException e) {
                JErrorPane.invokeErrorMessage(e, "Open Sorting Network SVG");
            }
        }
        return file;
    }
}
