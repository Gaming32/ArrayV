package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import java.awt.Color;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import java.util.Arrays;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

public final class Highlights {
    private volatile int[] highlights;
    private volatile byte[] markCounts;

    // This is in desperate need of optimization.
    private volatile Map<int[], boolean[]> colorMarks;
    private volatile Map<int[], Color[]> colorColors;

    private volatile boolean retainColorMarks = false;

    private volatile Map<String, Color> defined;
    private static int[] main;

    private volatile int maxHighlightMarked;    // IMPORTANT: This stores the index one past the farthest highlight used, so that a value
                                                // of 0 means no highlights are in use, and iteration is more convenient.

                                                // The Highlights array is huge and slows down the visualizer if all its indices are read.
                                                // In an attempt to speed up scanning through all highlights while also giving anyone room
                                                // to use the full array, this variable keeps track of the farthest highlight in use. The
                                                // Highlights array thus only needs to be scanned up to index maxHighightMarked.

                                                // If an highlight is used with markArray() that is higher than maxPossibleMarked, the
                                                // variable is updated. If the farthest highlight is removed with clearMark(), the next
                                                // farthest highlight is found and updates maxIndexMarked.

                                                // Trivially, clearAllMarks() resets maxIndexMarked to zero. This variable also serves
                                                // as a subtle design hint for anyone who wants to add an algorithm to the app to highlight
                                                // array positions at low indices which are close together.

                                                // This way, the program runs more efficiently, and looks pretty. :)

    private volatile int markCount;

    private boolean showFancyFinishes;
    private volatile boolean fancyFinish;
    private volatile int trackFinish;

    private ArrayVisualizer arrayVisualizer;
    private Delays Delays;

    public Highlights(ArrayVisualizer arrayVisualizer, int maximumLength) {
        this.arrayVisualizer = arrayVisualizer;

        try {
            defined = new HashMap<>();
            this.highlights = new int[maximumLength];
            this.markCounts = new byte[maximumLength];
            this.colorMarks = new IdentityHashMap<>();
            this.colorColors = new IdentityHashMap<>();
        } catch (OutOfMemoryError e) {
            JErrorPane.invokeCustomErrorMessage("Failed to allocate mark arrays. The program will now exit.");
            System.exit(1);
        }
        this.showFancyFinishes = true;
        this.maxHighlightMarked = 0;
        this.markCount = 0;
        this.main = arrayVisualizer.getArray();

        Arrays.fill(highlights, -1);
        Arrays.fill(markCounts, (byte)0);
        this.registerColorMarks(main);
    }

    public void postInit() {
        if (Delays != null) {
            throw new IllegalStateException();
        }
        this.Delays = arrayVisualizer.getDelays();
    }

    public boolean fancyFinishEnabled() {
        return this.showFancyFinishes;
    }
    public void toggleFancyFinishes(boolean showFancyFinishes) {
        this.showFancyFinishes = showFancyFinishes;
    }

    public boolean fancyFinishActive() {
        return this.fancyFinish;
    }
    public void toggleFancyFinish(boolean fancyFinish) {
        this.fancyFinish = fancyFinish;
    }

    public int getFancyFinishPosition() {
        return this.trackFinish;
    }
    public void incrementFancyFinishPosition() {
        this.trackFinish++;
    }
    public void resetFancyFinish() {
        this.trackFinish = -1; // Magic number that clears the green sweep animation
    }

    public void toggleAnalysis(boolean analysis) {
        this.arrayVisualizer.toggleAnalysis(analysis);
    }

    public int getMaxHighlight() {
        return this.maxHighlightMarked;
    }
    public int getMarkCount() {
        return this.markCount;
    }

    private void incrementIndexMarkCount(int i) {
        if (i >= markCounts.length) return;
        if (markCounts[i] != (byte)-1) {
            if (markCounts[i] == 0) {
                markCount++;
            }
            markCounts[i]++;
        }
    }
    private void decrementIndexMarkCount(int i) {
        if (i >= markCounts.length) return;
        if (markCounts[i] == (byte)-1) {
            int count = 0;
            for (int h = 0; h < this.maxHighlightMarked; h++) {
                if (highlights[h] == i) {
                    count++;
                    if (count > 255) {
                        return;
                    }
                }
            }
        } else if (markCounts[i] == 0) {
            markCount--;
        }
        markCounts[i]--;
    }

    //Consider revising highlightList().
    public int[] highlightList() {
        return this.highlights;
    }
    public boolean containsPosition(int arrayPosition) {
        if (arrayPosition >= markCounts.length) return false;
        return this.markCounts[arrayPosition] != 0;
    }
    public Set<String> getDeclaredColors() {
        return defined.keySet();
    }
    public Color getColorFromName(String color) {
        return defined.getOrDefault(color, Color.WHITE);
    }
    public synchronized void defineColor(String alias, Color col) {
        defined.put(alias, col);
    }

    public synchronized boolean[] getColorMarks(int[] array) {
        return colorMarks.get(array);
    }

    public synchronized Color[] getColorColors(int[] array) {
        return colorColors.get(array);
    }
    public synchronized void registerColorMarks(int[] array) {
        boolean[] colorMark = new boolean[array.length];
        Color[] colorColor = new Color[array.length];
        colorMarks.putIfAbsent(array, colorMark);
        colorColors.putIfAbsent(array, colorColor);
    }
    public synchronized void unregisterColors(int[] array) {
        colorMarks.remove(array);
        colorColors.remove(array);
    }
    // Ambitious function: Set the color directly
    public synchronized void setRawColor(int[] array, int position, Color color) {
        try {
            if (position < 0) {
                throw new Exception("Highlights.setRawColor(): Invalid position!");
            } else {
                boolean[] colorMark = getColorMarks(array);
                Delays.disableStepping();
                if (colorMark != null) {
                    colorMark[position] = true;
                    getColorColors(array)[position] = color;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        arrayVisualizer.updateNow();
        Delays.enableStepping();
    }

    // Convenience function: Set the color using a predefined alias
    public synchronized void setRawColor(int position, Color color) {
        setRawColor(main, position, color);
    }

    // Convenience function: Set the color using a predefined alias
    public synchronized void writeColor(int[] fromArray, int fromPosition, int[] toArray, int toPosition) {
        try {
            Delays.disableStepping();
            if (colorMarks.containsKey(fromArray) && colorMarks.containsKey(toArray)) {
                getColorMarks(toArray)[toPosition] = getColorMarks(fromArray)[fromPosition];
                getColorColors(toArray)[toPosition] = getColorColors(fromArray)[fromPosition];
            } else {
                throw new Exception("Highlights.writeColor(): One or more arrays not colorcodeable!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        arrayVisualizer.updateNow();
        Delays.enableStepping();
    }

    // Convenience function: Set the color using a predefined alias
    public synchronized void colorCode(int[] array, int position, String color) {
        try {
            if (position < 0) {
                throw new Exception("Highlights.colorCode(): Invalid position!");
            } else {
                boolean[] colorMark = getColorMarks(array);
                Delays.disableStepping();
                if (colorMark != null) {
                    colorMark[position] = true;
                    getColorColors(array)[position] = getColorFromName(color);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        arrayVisualizer.updateNow();
        Delays.enableStepping();
    }

    // Convenience function: Set the color using a predefined alias
    public synchronized void colorCode(int position, String color) {
        colorCode(main, position, color);
    }

    // Convenience function 2: Batch-colorcode a set of positions under one common name
    public synchronized void colorCode(int[] array, String color, int... positions) {
        for (int i : positions) {
            colorCode(array, i, color);
        }
    }

    // Convenience function 2: Batch-colorcode a set of positions under one common name
    public synchronized void colorCode(String color, int... positions) {
        colorCode(main, color, positions);
    }

    public synchronized void clearColor(int[] array, int position) {
        boolean[] colorMark = getColorMarks(array);
        if (colorMark == null)
            return;
        Delays.disableStepping();
        if (colorMark[position]) {
            colorMark[position] = false;
            getColorColors(array)[position] = null;
        }
        arrayVisualizer.updateNow();
        Delays.enableStepping();
    }

    public synchronized void clearColor(int position) {
        clearColor(main, position);
    }

    public synchronized boolean hasColor(int[] array, int position) {
        return colorMarks.containsKey(array) && getColorMarks(array)[position];
    }

    public synchronized boolean hasColor(int position) {
        return hasColor(main, position);
    }

    public synchronized Color colorAt(int[] array, int position) {
        return getColorColors(array)[position];
    }

    public synchronized Color colorAt(int position) {
        return colorAt(main, position);
    }

    public void swapColors(int[] array, int locA, int locB) {
        boolean[] colorMark = getColorMarks(array);
        Color[] colorColor = getColorColors(array);
        if (colorMark == null)
            return;
        boolean t0 = colorMark[locA];
        Color t1 = colorColor[locA];
        colorMark[locA] = colorMark[locB];
        colorMark[locB] = t0;
        colorColor[locA] = colorColor[locB];
        colorColor[locB] = t1;
    }

    public void swapColors(int locA, int locB) {
        swapColors(main, locA, locB);
    }

    public synchronized void markArray(int marker, int markPosition) {
        try {
            if (markPosition < 0) {
                if (markPosition == -1) throw new Exception("Highlights.markArray(): Invalid position! -1 is reserved for the clearMark method.");
                else if (markPosition == -5) throw new Exception("Highlights.markArray(): Invalid position! -5 was the constant originally used to unmark numbers in the array. Instead, use the clearMark method.");
                else throw new Exception("Highlights.markArray(): Invalid position!");
            } else {
                if (highlights[marker] == markPosition) {
                    return;
                }
                Delays.disableStepping();
                if (highlights[marker] != -1) {
                    decrementIndexMarkCount(highlights[marker]);
                }
                if (!retainColorMarks) {
                    clearColor(markPosition);
                }
                highlights[marker] = markPosition;
                incrementIndexMarkCount(markPosition);

                if (marker >= this.maxHighlightMarked) {
                    this.maxHighlightMarked = marker + 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        arrayVisualizer.updateNow();
        Delays.enableStepping();
    }

    public synchronized void clearMark(int marker) {
        if (highlights[marker] == -1) {
            return;
        }
        Delays.disableStepping();
        decrementIndexMarkCount(highlights[marker]);
        if (!retainColorMarks) {
            clearColor(highlights[marker]);
        }
        highlights[marker] = -1; // -1 is used as the magic number to unmark a position in the main array
        if (marker == this.maxHighlightMarked) {
            this.maxHighlightMarked = marker;
            while (maxHighlightMarked > 0 && highlights[maxHighlightMarked-1] == -1) {
                maxHighlightMarked--;
            }
        }
        arrayVisualizer.updateNow();
        Delays.enableStepping();
    }


    public synchronized void clearColorList() {
        defined.clear();
        retainColorMarks = false;
    }

    public synchronized void clearAllColors(int[] array) {
        Delays.disableStepping();
        Arrays.fill(getColorMarks(array), false);
        arrayVisualizer.updateNow();
        Delays.enableStepping();
    }

    public synchronized void clearAllColors() {
        clearAllColors(main);
    }

    public synchronized void clearAllColorsReferenced() {
        for (boolean[] list : colorMarks.values()) {
            Arrays.fill(list, false);
        }
    }

    public synchronized boolean isRetainingColorMarks() {
        return retainColorMarks;
    }

    public synchronized void retainColorMarks(boolean retainColorMarks) {
        this.retainColorMarks = retainColorMarks;
    }

    public synchronized void clearAllMarks() {
        Delays.disableStepping();
        for (int i = 0; i < this.maxHighlightMarked; i++) {
            if (highlights[i] != -1) {
                markCounts[highlights[i]] = 0;
            }
        }
        Arrays.fill(this.highlights, 0, this.maxHighlightMarked, -1);
        this.maxHighlightMarked = 0;
        this.markCount = 0;
        arrayVisualizer.updateNow();
        Delays.enableStepping();
    }
}
