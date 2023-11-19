package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

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

public final class Reads {
    private final AtomicLong comparisons;
    private final ArrayList<Integer> networkIndices;

    private final ArrayVisualizer arrayVisualizer;

    private final DecimalFormat formatter;

    private final Delays Delays;
    private final Highlights Highlights;
    private final Timer Timer;

    public Reads(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;

        this.comparisons = new AtomicLong();
        this.networkIndices = new ArrayList<>();

        this.Delays = arrayVisualizer.getDelays();
        this.Highlights = arrayVisualizer.getHighlights();
        this.Timer = arrayVisualizer.getTimer();

        this.formatter = arrayVisualizer.getNumberFormat();
    }

    public void resetStatistics() {
        this.comparisons.set(0);
    }

    public void addComparison() {
        this.comparisons.incrementAndGet();
    }

    public String getStats() {
        if (this.comparisons.get() < 0) {
            this.comparisons.set(Long.MIN_VALUE);
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        } else {
            if (this.comparisons.get() == 1) return this.comparisons + " Comparison";
            else                             return this.formatter.format(this.comparisons) + " Comparisons";
        }
    }

    public long getComparisons() {
        return this.comparisons.get();
    }

    public void setComparisons(long value) {
        this.comparisons.set(value);
    }

    /**
     * Doesn't result in any visualizations, but counts for the number of comparisons stat.
     *
     * @param left
     * @param right
     * @return Integer.compare(left, right)
     */
    public int compareValues(int left, int right) {
        if (arrayVisualizer.sortCanceled()) throw new StopSort();
        this.comparisons.incrementAndGet();

        if (arrayVisualizer.doingStabilityCheck()) {
            left  = arrayVisualizer.getStabilityValue(left);
            right = arrayVisualizer.getStabilityValue(right);
        }

        int cmpVal;

        Timer.startLap("Compare");

        cmpVal = Integer.compare(left, right);

        Timer.stopLap();

        if (!arrayVisualizer.useAntiQSort()) {
            if (arrayVisualizer.reversedComparator()) {
                return -cmpVal;
            }
            return cmpVal;
        } else {
            return arrayVisualizer.antiqCompare(left, right);
        }
    }

    public int compareOriginalValues(int left, int right) {
        if (arrayVisualizer.sortCanceled()) throw new StopSort();
        this.comparisons.incrementAndGet();

        int cmpVal;

        Timer.startLap("Compare");

        cmpVal = Integer.compare(left, right);

        Timer.stopLap();

        return cmpVal;
    }

    /**
     * Compare index to index.
     *
     * @param array
     * @param left The first index to read from the array
     * @param right The second index to read from the array
     * @param sleep How many milliseconds to sleep (subject to Delays.sleepRatio)
     * @param mark Whether to mark and delay for this comparison.
     *
     * @return Integer.compare(array[left], array[right])
     */
    public int compareIndices(int[] array, int left, int right, double sleep, boolean mark) {
        if (mark) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(sleep);
        }
        if (arrayVisualizer.generateSortingNetworks()) {
            networkIndices.add(left);
            networkIndices.add(right);
        }
        return this.compareValues(array[left], array[right]);
    }

    public int compareOriginalIndices(int[] array, int left, int right, double sleep, boolean mark) {
        if (mark) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(sleep);
        }
        return this.compareOriginalValues(array[left], array[right]);
    }

    /**
     * Compare index to value. Useful for comparing, say, the current pointer to the current minimum.
     *
     * @param array
     * @param index The index to read from the array
     * @param value A constant value
     * @param sleep How many milliseconds to sleep (subject to Delays.sleepRatio)
     * @param mark Whether to mark and delay for this comparison.
     *
     * @return Integer.compare(array[index], value)
     */
    public int compareIndexValue(int[] array, int index, int value, double sleep, boolean mark) {
        if (mark) {
            Highlights.markArray(1, index);
            Delays.sleep(sleep);
        }
        return this.compareValues(array[index], value);
    }

    public int compareOriginalIndexValue(int[] array, int index, int value, double sleep, boolean mark) {
        if (mark) {
            Highlights.markArray(1, index);
            Delays.sleep(sleep);
        }
        return this.compareOriginalValues(array[index], value);
    }

    /**
     * Compare value to index. Useful for comparing, say, the current minimum to the current pointer.
     *
     * @param array
     * @param value A constant value
     * @param index The index to read from the array
     * @param sleep How many milliseconds to sleep (subject to Delays.sleepRatio)
     * @param mark Whether to mark and delay for this comparison.
     *
     * @return Integer.compare(value, array[index])
     */
    public int compareValueIndex(int[] array, int value, int index, double sleep, boolean mark) {
        if (mark) {
            Highlights.markArray(1, index);
            Delays.sleep(sleep);
        }
        return this.compareValues(value, array[index]);
    }

    public int compareOriginalValueIndex(int[] array, int value, int index, double sleep, boolean mark) {
        if (mark) {
            Highlights.markArray(1, index);
            Delays.sleep(sleep);
        }
        return this.compareOriginalValues(value, array[index]);
    }

    public int analyzeMax(int[] array, int length, double sleep, boolean mark) {
        arrayVisualizer.toggleAnalysis(true);
        arrayVisualizer.updateNow();

        int max = 0;

        for (int i = 0; i < length; i++) {
            if (arrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if (arrayVisualizer.doingStabilityCheck())
                val = arrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if (val > max) max = val;

            Timer.stopLap();

            if (mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        arrayVisualizer.toggleAnalysis(false);
        arrayVisualizer.updateNow();

        return max;
    }

    public int analyzeMin(int[] array, int length, double sleep, boolean mark) {
        arrayVisualizer.toggleAnalysis(true);
        arrayVisualizer.updateNow();

        int min = 0;

        for (int i = 0; i < length; i++) {
            if (arrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if (arrayVisualizer.doingStabilityCheck())
                val = arrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if (val < min) min = val;

            Timer.stopLap();

            if (mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        arrayVisualizer.toggleAnalysis(false);
        arrayVisualizer.updateNow();

        return min;
    }

    public int analyzeMaxLog(int[] array, int length, int base, double sleep, boolean mark) {
        arrayVisualizer.toggleAnalysis(true);
        arrayVisualizer.updateNow();

        int max = 0;

        for (int i = 0; i < length; i++) {
            if (arrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if (arrayVisualizer.doingStabilityCheck())
                val = arrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if (val > max) max = val;

            Timer.stopLap();

            if (mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        arrayVisualizer.toggleAnalysis(false);
        arrayVisualizer.updateNow();

        return (int) (Math.log(max) / Math.log(base));
    }

    public int analyzeMaxCeilingLog(int[] array, int length, int base, double sleep, boolean mark) {
        arrayVisualizer.toggleAnalysis(true);
        arrayVisualizer.updateNow();

        int max = 0;

        for (int i = 0; i < length; i++) {
            if (arrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if (arrayVisualizer.doingStabilityCheck())
                val = arrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if (val > max) max = val;

            Timer.stopLap();

            if (mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        arrayVisualizer.toggleAnalysis(false);
        arrayVisualizer.updateNow();

        return (int) (Math.log(max) / Math.log(base));
    }

    public int analyzeBit(int[] array, int length) {
        arrayVisualizer.toggleAnalysis(true);
        arrayVisualizer.updateNow();

        // Find highest bit of highest value
        int max = 0;

        for (int i = 0; i < length; i++) {
            if (arrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if (arrayVisualizer.doingStabilityCheck())
                val = arrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if (val > max) max = val;

            Timer.stopLap();

            Highlights.markArray(1, i);
            Delays.sleep(0.75);
        }

        int analysis;

        Timer.startLap();

        analysis = 31 - Integer.numberOfLeadingZeros(max);

        Timer.stopLap();

        arrayVisualizer.toggleAnalysis(false);
        arrayVisualizer.updateNow();
        return analysis;
    }

    public int getDigit(int a, int power, int radix) {
        if (arrayVisualizer.doingStabilityCheck())
            a = arrayVisualizer.getStabilityValue(a);

        int digit;
        Timer.startLap();
        digit = (int) (a / Math.pow(radix, power)) % radix;
        Timer.stopLap();
        return digit;
    }

    public boolean getBit(int n, int k) {
        if (arrayVisualizer.doingStabilityCheck())
            n = arrayVisualizer.getStabilityValue(n);

        // Find boolean value of bit k in n
        boolean result;
        Timer.startLap();
        result = ((n >> k) & 1) == 1;
        Timer.stopLap();
        return result;
    }

    public ArrayList<Integer> getNetworkIndices() {
        return networkIndices;
    }
}
