package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

/**
 *
 * @author S630690
 */
public final class Writes {
    private final AtomicLong reversals;
    private final AtomicLong swaps;
    private final AtomicLong auxWrites;
    private final AtomicLong writes;
    private final AtomicLong allocAmount;

    private final DecimalFormat formatter;

    private final ArrayVisualizer arrayVisualizer;
    private final Delays Delays;
    private final Highlights Highlights;
    private final Timer Timer;

    public Writes(ArrayVisualizer arrayVisualizer) {
        this.reversals = new AtomicLong();
        this.swaps = new AtomicLong();
        this.auxWrites = new AtomicLong();
        this.writes = new AtomicLong();
        this.allocAmount = new AtomicLong();

        this.arrayVisualizer = arrayVisualizer;
        this.Delays = arrayVisualizer.getDelays();
        this.Highlights = arrayVisualizer.getHighlights();
        this.Timer = arrayVisualizer.getTimer();

        this.formatter = arrayVisualizer.getNumberFormat();
    }

    public void resetStatistics() {
        this.reversals.set(0);
        this.swaps.set(0);
        this.auxWrites.set(0);
        this.writes.set(0);
        this.allocAmount.set(0);
    }

    public String getReversals() {
        if (this.reversals.get() < 0) {
            this.reversals.set(Long.MIN_VALUE);
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        } else {
            if (reversals.get() == 1) return this.reversals + " Reversal";
            else                      return this.formatter.format(this.reversals) + " Reversals";
        }
    }

    public String getSwaps() {
        if (this.swaps.get() < 0) {
            this.swaps.set(Long.MIN_VALUE);
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        } else {
            if (swaps.get() == 1) return this.swaps + " Swap";
            else                  return this.formatter.format(this.swaps) + " Swaps";
        }
    }

    public String getAuxWrites() {
        if (this.auxWrites.get() < 0) {
            this.auxWrites.set(Long.MIN_VALUE);
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        } else {
            if (auxWrites.get() == 1) return this.auxWrites + " Write to Auxiliary Array(s)";
            else                      return this.formatter.format(this.auxWrites) + " Writes to Auxiliary Array(s)";
        }
    }

    public String getMainWrites() {
        if (this.writes.get() < 0) {
            this.writes.set(Long.MIN_VALUE);
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        } else {
            if (writes.get() == 1) return this.writes + " Write to Main Array";
            else                   return this.formatter.format(this.writes) + " Writes to Main Array";
        }
    }

    public String getAllocAmount() {
        if (this.allocAmount.get() < 0) {
            this.allocAmount.set(Long.MIN_VALUE);
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        } else {
            if (allocAmount.get() == 1) return this.allocAmount + " Item in External Arrays";
            else                        return this.formatter.format(this.allocAmount) + " Items in External Arrays";
        }
    }

    public void changeAuxWrites(int value) {
        this.auxWrites.addAndGet(value);
    }

    public void changeWrites(int value) {
        this.writes.addAndGet(value);
    }

    public void changeAllocAmount(int value) {
        this.allocAmount.addAndGet(value);
    }

    public void clearAllocAmount() {
        this.allocAmount.set(0);
    }

    public void changeReversals(int value) {
        this.reversals.addAndGet(value);
    }

    private void updateSwap(boolean auxwrite) {
        this.swaps.incrementAndGet();
        if (auxwrite) this.auxWrites.addAndGet(2);
        else          this.writes.addAndGet(2);
    }

    private void markSwap(int a, int b) {
        Highlights.markArray(1, a);
        Highlights.markArray(2, b);
    }

    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean auxwrite) {
        if (arrayVisualizer.sortCanceled()) throw new StopSort();
        if (!auxwrite && a >= arrayVisualizer.getCurrentLength()) {
            System.err.println("Warning: write to index " + a + ", which is out of bounds for the current length (" + arrayVisualizer.getCurrentLength() + ")");
        }
        if (!auxwrite && b >= arrayVisualizer.getCurrentLength()) {
            System.err.println("Warning: write to index " + b + ", which is out of bounds for the current length (" + arrayVisualizer.getCurrentLength() + ")");
        }

        if (mark) this.markSwap(a, b);

        Timer.startLap("Swap");

        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;

        Timer.stopLap();

        this.updateSwap(auxwrite);
        arrayVisualizer.updateNow();
        Delays.sleep(pause);
    }

    public void multiSwap(int[] array, int pos, int to, double sleep, boolean mark, boolean auxwrite) {
        if (to - pos > 0) {
            for (int i = pos; i < to; i++) {
                this.swap(array, i, i + 1, 0, mark, auxwrite);
                Delays.sleep(sleep);
            }
        } else {
            for (int i = pos; i > to; i--) {
                this.swap(array, i, i - 1, 0, mark, auxwrite);
                Delays.sleep(sleep);
            }
        }
    }

    public void reversal(int[] array, int start, int length, double sleep, boolean mark, boolean auxwrite) {
        this.reversals.incrementAndGet();

        for (int i = start; i < start + ((length - start + 1) / 2); i++) {
            this.swap(array, i, start + length - i, sleep, mark, auxwrite);
        }
    }

    public void write(int[] array, int at, int equals, double pause, boolean mark, boolean auxwrite) {
        if (arrayVisualizer.sortCanceled()) throw new StopSort();
        if (!auxwrite && at >= arrayVisualizer.getCurrentLength()) {
            System.err.println("Warning: write to index " + at + ", which is out of bounds for the current length (" + arrayVisualizer.getCurrentLength() + ")");
        }

        if (mark) Highlights.markArray(1, at);

        if (auxwrite) auxWrites.incrementAndGet();
        else          writes.incrementAndGet();

        Timer.startLap("Write");

        array[at] = equals;

        Timer.stopLap();

        arrayVisualizer.updateNow();
        Delays.sleep(pause);
    }

    public <T> void write(T[] array, int at, T equals, double pause, boolean mark) {
        if (arrayVisualizer.sortCanceled()) throw new StopSort();
        if (mark) Highlights.markArray(1, at);

        auxWrites.incrementAndGet();

        Timer.startLap("Write");

        array[at] = equals;

        Timer.stopLap();

        arrayVisualizer.updateNow();
        Delays.sleep(pause);
    }

    public void visualClear(int[] array, int index) {
        visualClear(array, index, 0);
    }

    public void visualClear(int[] array, int index, double delay) {
        array[index] = -1;
    }

    public void multiDimWrite(int[][] array, int x, int y, int equals, double pause, boolean mark, boolean auxwrite) {
        if (arrayVisualizer.sortCanceled()) throw new StopSort();
        if (mark) Highlights.markArray(1, x);

        if (auxwrite) auxWrites.incrementAndGet();
        else          writes.incrementAndGet();

        Timer.startLap();

        array[x][y] = equals;

        Timer.stopLap();

        arrayVisualizer.updateNow();
        Delays.sleep(pause);
    }

    public <T> void multiDimWrite(T[][] array, int x, int y, T equals, double pause, boolean mark) {
        if (arrayVisualizer.sortCanceled()) throw new StopSort();
        if (mark) Highlights.markArray(1, x);

        auxWrites.incrementAndGet();

        Timer.startLap();

        array[x][y] = equals;

        Timer.stopLap();

        arrayVisualizer.updateNow();
        Delays.sleep(pause);
    }

    //Simulates a write in order to better estimate time for values being written to an ArrayList
    public void mockWrite(int length, int pos, int val, double pause) {
        if (arrayVisualizer.sortCanceled()) throw new StopSort();
        //noinspection MismatchedReadAndWriteOfArray
        int[] mockArray = new int[length];

        this.auxWrites.incrementAndGet();

        Timer.startLap();

        mockArray[pos] = val;

        Timer.stopLap();

        Delays.sleep(pause);
    }

    public void transcribe(int[] array, ArrayList<Integer>[] registers, int start, boolean mark, boolean auxwrite) {
        int total = start;

        for (ArrayList<Integer> register : registers) {
            for (Integer integer : register) {
                this.write(array, total++, integer, 0, mark, auxwrite);
                if (mark) Delays.sleep(1);
            }
            this.arrayListClear(register);
        }
    }

    public void transcribeMSD(int[] array, ArrayList<Integer>[] registers, int start, int min, double sleep, boolean mark, boolean auxwrite) {
        int total = start;
        int temp = 0;

        for (ArrayList<Integer> list : registers) {
            total += list.size();
        }

        for (int index = registers.length - 1; index >= 0; index--) {
            for (int i = registers[index].size() - 1; i >= 0; i--) {
                this.write(array, total + min - temp++ - 1, registers[index].get(i), 0, mark, auxwrite);
                if (mark) Delays.sleep(sleep);
            }
        }
    }

    public void fancyTranscribe(int[] array, int length, ArrayList<Integer>[] registers, double sleep) {
        int[] tempArray = this.createExternalArray(length);
        boolean[] tempWrite = new boolean[length];
        int radix = registers.length;

        this.transcribe(tempArray, registers, 0, false, true);
        auxWrites.addAndGet(-length);

        for (int i = 0; i < length; i++) {
            int register = i % radix;
            int pos = (register * (length / radix)) + (i / radix);

            if (!tempWrite[pos]) {
                this.write(array, pos, tempArray[pos], 0, false, false);
                tempWrite[pos] = true;
            }

            Highlights.markArray(register, pos);
            if (register == 0) Delays.sleep(sleep);
        }
        for (int i = 0; i < length; i++) {
            if (!tempWrite[i]){
                this.write(array, i, tempArray[i], 0, false, false);
            }
        }

        Highlights.clearAllMarks();

        this.deleteExternalArray(tempArray);
    }

    /**
     * Method to mimic {@link System#arraycopy(Object, int, Object, int, int)}
     * @see System#arraycopy(Object, int, Object, int, int)
     */
    public void arraycopy(int[] src, int srcPos, int[] dest, int destPos, int length, double sleep, boolean mark, boolean aux) {
        int start, end, dir;
        if (src != dest || destPos < srcPos) {
            start = 0;
            end = length;
            dir = 1;
        } else {
            start = length - 1;
            end = -1;
            dir = -1;
        }
        for (int i = start; i != end; i += dir) {
            if (mark) {
                if (aux) {
                    Highlights.markArray(1, srcPos + i);
                } else {
                    Highlights.markArray(1, destPos + i);
                }
            }
            write(dest, destPos + i, src[srcPos + i], sleep, false, aux);
        }
    }

    public int[] copyOfArray(int[] original, int newLength) {
        this.allocAmount.addAndGet(newLength);
        int[] result = Arrays.copyOf(original, newLength);
        arrayVisualizer.getArrays().add(result);
        arrayVisualizer.updateNow();
        return result;
    }

    public int[] copyOfRangeArray(int[] original, int from, int to) {
        this.allocAmount.addAndGet(to - from);
        int[] result = Arrays.copyOfRange(original, from, to);
        arrayVisualizer.getArrays().add(result);
        arrayVisualizer.updateNow();
        return result;
    }

    /**
     * Same as arraycopy, but used to copy in reverse
     * @deprecated Use {@link Writes#arraycopy(int[], int, int[], int, int, double, boolean, boolean)} instead
     * @see Writes#arraycopy(int[], int, int[], int, int, double, boolean, boolean)
     */
    @Deprecated
    public void reversearraycopy(int[] src, int srcPos, int[] dest, int destPos, int length, double sleep, boolean mark, boolean aux) {
        arraycopy(src, srcPos, dest, destPos, length, sleep, mark, aux);
    }

    public ArrayVList createArrayList() {
        return new ArrayVList();
    }

    public ArrayVList createArrayList(int defaultCapacity) {
        return new ArrayVList(defaultCapacity);
    }

    public int[] createExternalArray(int length) {
        this.allocAmount.addAndGet(length);
        int[] result = new int[length];
        arrayVisualizer.getArrays().add(result);
        arrayVisualizer.updateNow();
        return result;
    }

    public void deleteExternalArray(int[] array) {
        this.allocAmount.addAndGet(-array.length);
        arrayVisualizer.getArrays().remove(array);
        arrayVisualizer.updateNow();
    }

    public void deleteExternalArrays(int[]... arrays) {
        this.allocAmount.addAndGet(-Arrays.stream(arrays).reduce(0, (a, b) -> (a + b.length), Integer::sum));
        List<int[]> visArrays = arrayVisualizer.getArrays();
        Arrays.stream(arrays).forEach(visArrays::remove);
        arrayVisualizer.updateNow();
    }

    public void arrayListAdd(List<Integer> aList, int value) {
        allocAmount.incrementAndGet();
        aList.add(value);
    }

    public void arrayListAdd(List<Integer> aList, int value, boolean mockWrite, double sleep) {
        if (aList instanceof ArrayVList) {
            ((ArrayVList)aList).add(value, sleep, false);
            return;
        }
        allocAmount.incrementAndGet();
        aList.add(value);
        if (mockWrite) {
            this.mockWrite(aList.size(), aList.size() - 1, value, sleep);
        } else {
            Delays.sleep(sleep);
        }
    }

    public void arrayListRemoveAt(List<Integer> aList, int index) {
        allocAmount.decrementAndGet();
        aList.remove(index);
    }

    public void arrayListClear(List<Integer> aList) {
        if (!(aList instanceof ArrayVList))
            allocAmount.addAndGet(-aList.size());
        aList.clear();
    }

    public void deleteArrayList(List<Integer> aList) {
        if (aList instanceof ArrayVList) {
            ((ArrayVList)aList).delete();
        } else {
            allocAmount.addAndGet(-aList.size());
        }
    }

    public void deleteExternalArray(List<Integer>[] array) {
        for (List<Integer> aList : array) {
            deleteArrayList(aList);
        }
    }

    //TODO: These methods should be solely controlled by Timer class
    public void addTime(long milliseconds) {
        if (Timer.timerEnabled()) Timer.manualAddTime(milliseconds);
    }

    public void setTime(long milliseconds) {
        if (Timer.timerEnabled()) Timer.manualSetTime(milliseconds);
    }

    public void startLap() {
        if (Timer.timerEnabled()) Timer.startLap();
    }

    public void stopLap() {
        if (Timer.timerEnabled()) Timer.stopLap();
    }
}
