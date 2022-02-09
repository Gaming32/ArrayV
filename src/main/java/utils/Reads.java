package utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import main.ArrayVisualizer;

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

final public class Reads {
    private volatile long comparisons;
    public volatile ArrayList<Integer> networkIndices;

    private ArrayVisualizer ArrayVisualizer;

    private DecimalFormat formatter;

    private Delays Delays;
    private Highlights Highlights;
    private Timer Timer;

    public Reads(ArrayVisualizer arrayVisualizer) {
        this.ArrayVisualizer = arrayVisualizer;

        this.comparisons = 0;
        this.networkIndices = new ArrayList<>();

        this.Delays = ArrayVisualizer.getDelays();
        this.Highlights = ArrayVisualizer.getHighlights();
        this.Timer = ArrayVisualizer.getTimer();

        this.formatter = ArrayVisualizer.getNumberFormat();
    }

    public void resetStatistics() {
        this.comparisons = 0;
    }

    public void addComparison() {
        this.comparisons++;
    }

    public String getStats() {
        if (this.comparisons < 0) {
            this.comparisons = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        } else {
            if (this.comparisons == 1) return this.comparisons + " Comparison";
            else                       return this.formatter.format(this.comparisons) + " Comparisons";
        }
    }

    public long getComparisons() {
        return this.comparisons;
    }

    public void setComparisons(long value) {
        this.comparisons = value;
    }

    public int compareValues(int left, int right) {
        if (ArrayVisualizer.sortCanceled()) throw new StopSort();
        this.comparisons++;

        if (ArrayVisualizer.doingStabilityCheck()) {
            left  = ArrayVisualizer.getStabilityValue(left);
            right = ArrayVisualizer.getStabilityValue(right);
        }

        int cmpVal = 0;

        Timer.startLap("Compare");

        if (left > right)      cmpVal =  1;
        else if (left < right) cmpVal = -1;
        else                   cmpVal =  0;

        Timer.stopLap();

        if (!ArrayVisualizer.useAntiQSort()) {
            if (ArrayVisualizer.reversedComparator()) {
                return -cmpVal;
            }
            return cmpVal;
        } else {
            return ArrayVisualizer.antiqCompare(left, right);
        }
    }

    public int compareOriginalValues(int left, int right) {
        if (ArrayVisualizer.sortCanceled()) throw new StopSort();
        this.comparisons++;

        int cmpVal = 0;

        Timer.startLap("Compare");

        if (left > right)      cmpVal =  1;
        else if (left < right) cmpVal = -1;
        else                  cmpVal =  0;

        Timer.stopLap();

        return cmpVal;
    }

    public int compareIndices(int[] array, int left, int right, double sleep, boolean mark) {
        if (mark) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(sleep);
        }
        if (ArrayVisualizer.generateSortingNetworks()) {
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
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int max = 0;

        for (int i = 0; i < length; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if (ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if (val > max) max = val;

            Timer.stopLap();

            if (mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return max;
    }

    public int analyzeMin(int[] array, int length, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int min = 0;

        for (int i = 0; i < length; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if (ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if (val < min) min = val;

            Timer.stopLap();

            if (mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return min;
    }

    public int analyzeMaxLog(int[] array, int length, int base, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int max = 0;

        for (int i = 0; i < length; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if (ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if (val > max) max = val;

            Timer.stopLap();

            if (mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return (int) (Math.log(max) / Math.log(base));
    }

    public int analyzeMaxCeilingLog(int[] array, int length, int base, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int max = 0;

        for (int i = 0; i < length; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if (ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if (val > max) max = val;

            Timer.stopLap();

            if (mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return (int) (Math.log(max) / Math.log(base));
    }

    public int analyzeBit(int[] array, int length) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        // Find highest bit of highest value
        int max = 0;

        for (int i = 0; i < length; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if (ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

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

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();
        return analysis;
    }

    public int getDigit(int a, int power, int radix) {
        if (ArrayVisualizer.doingStabilityCheck())
            a = ArrayVisualizer.getStabilityValue(a);

        int digit;
        Timer.startLap();
        digit = (int) (a / Math.pow(radix, power)) % radix;
        Timer.stopLap();
        return digit;
    }

    public boolean getBit(int n, int k) {
        if (ArrayVisualizer.doingStabilityCheck())
            n = ArrayVisualizer.getStabilityValue(n);

        // Find boolean value of bit k in n
        boolean result;
        Timer.startLap();
        result = ((n >> k) & 1) == 1;
        Timer.stopLap();
        return result;
    }
}
