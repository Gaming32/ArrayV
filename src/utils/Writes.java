package utils;

import java.util.Arrays;
import java.util.List;
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

/**
 *
 * @author S630690
 */
final public class Writes {
    private volatile long reversals;
    private volatile long swaps;
    private volatile long auxWrites;
    private volatile long writes;
    private volatile long allocAmount;

    private DecimalFormat formatter;

    private ArrayVisualizer ArrayVisualizer;
    private Delays Delays;
    private Highlights Highlights;
    private Timer Timer;

    public Writes(ArrayVisualizer ArrayVisualizer) {
        this.reversals = 0;
        this.swaps = 0;
        this.auxWrites = 0;
        this.writes = 0;
        this.allocAmount = 0;

        this.ArrayVisualizer = ArrayVisualizer;
        this.Delays = ArrayVisualizer.getDelays();
        this.Highlights = ArrayVisualizer.getHighlights();
        this.Timer = ArrayVisualizer.getTimer();

        this.formatter = ArrayVisualizer.getNumberFormat();
    }

    public void resetStatistics() {
        this.reversals = 0;
        this.swaps = 0;
        this.auxWrites = 0;
        this.writes = 0;
        this.allocAmount = 0;
    }

    public String getReversals() {
        if(this.reversals < 0) {
            this.reversals = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        }
        else {
            if(reversals == 1) return this.reversals + " Reversal";
            else               return this.formatter.format(this.reversals) + " Reversals";
        }
    }

    public String getSwaps() {
        if(this.swaps < 0) {
            this.swaps = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        }
        else {
            if(this.swaps == 1) return this.swaps + " Swap";
            else                return this.formatter.format(this.swaps) + " Swaps";
        }
    }

    public String getAuxWrites() {
        if(this.auxWrites < 0) {
            this.auxWrites = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        }
        else {
            if(this.auxWrites == 1) return this.auxWrites + " Write to Auxiliary Array(s)";
            else                     return this.formatter.format(this.auxWrites) + " Writes to Auxiliary Array(s)";
        }
    }

    public String getMainWrites() {
        if(this.writes < 0) {
            this.writes = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        }
        else {
            if(this.writes == 1) return this.writes + " Write to Main Array";
            else                 return this.formatter.format(this.writes) + " Writes to Main Array";
        }
    }

    public String getAllocAmount() {
        if(this.allocAmount < 0) {
            this.allocAmount = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        }
        else {
            if(this.allocAmount == 1) return this.allocAmount + " Item in External Arrays";
            else                      return this.formatter.format(this.allocAmount) + " Items in External Arrays";
        }
    }

    public void changeAuxWrites(int value) {
        this.auxWrites += value;
    }

    public void changeWrites(int value) {
        this.writes += value;
    }

    public void changeAllocAmount(int value) {
        this.allocAmount += value;
    }

    public void clearAllocAmount() {
        this.allocAmount = 0;
    }

    public void changeReversals(int value) {
        this.reversals += value;
    }

    private void updateSwap(boolean auxwrite) {
        this.swaps++;
        if(auxwrite) this.auxWrites += 2;
        else            this.writes += 2;
    }

    private void markSwap(int a, int b) {
        Highlights.markArray(1, a);
        Highlights.markArray(2, b);
    }

    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean auxwrite) {
        if (ArrayVisualizer.sortCanceled()) throw new StopSort();
        if (!auxwrite && a >= ArrayVisualizer.getCurrentLength()) {
            System.err.println("Warning: write to index " + a + ", which is out of bounds for the current length (" + ArrayVisualizer.getCurrentLength() + ")");
        }
        if (!auxwrite && b >= ArrayVisualizer.getCurrentLength()) {
            System.err.println("Warning: write to index " + b + ", which is out of bounds for the current length (" + ArrayVisualizer.getCurrentLength() + ")");
        }

        if(mark) this.markSwap(a, b);

        Timer.startLap("Swap");

        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;

        Timer.stopLap();

        this.updateSwap(auxwrite);
        ArrayVisualizer.updateNow();
        Delays.sleep(pause);
    }

    public void multiSwap(int[] array, int pos, int to, double sleep, boolean mark, boolean auxwrite) {
        if(to - pos > 0) {
            for(int i = pos; i < to; i++) {
                this.swap(array, i, i + 1, 0, mark, auxwrite);
                Delays.sleep(sleep);
            }
        }
        else {
            for(int i = pos; i > to; i--) {
                this.swap(array, i, i - 1, 0, mark, auxwrite);
                Delays.sleep(sleep);
            }
        }
    }

    public void reversal(int[] array, int start, int length, double sleep, boolean mark, boolean auxwrite) {
        this.reversals++;

        for(int i = start; i < start + ((length - start + 1) / 2); i++) {
            this.swap(array, i, start + length - i, sleep, mark, auxwrite);
        }
    }

    public void write(int[] array, int at, int equals, double pause, boolean mark, boolean auxwrite) {
        if (ArrayVisualizer.sortCanceled()) throw new StopSort();
        if (!auxwrite && at >= ArrayVisualizer.getCurrentLength()) {
            System.err.println("Warning: write to index " + at + ", which is out of bounds for the current length (" + ArrayVisualizer.getCurrentLength() + ")");
        }

        if(mark) Highlights.markArray(1, at);

        if(auxwrite) auxWrites++;
        else            writes++;

        Timer.startLap("Write");

        array[at] = equals;

        Timer.stopLap();

        ArrayVisualizer.updateNow();
        Delays.sleep(pause);
    }

    public <T> void write(T[] array, int at, T equals, double pause, boolean mark) {
        if (ArrayVisualizer.sortCanceled()) throw new StopSort();
        if(mark) Highlights.markArray(1, at);

        auxWrites++;

        Timer.startLap("Write");

        array[at] = equals;

        Timer.stopLap();

        ArrayVisualizer.updateNow();
        Delays.sleep(pause);
    }

    public void visualClear(int[] array, int index) {
        visualClear(array, index, 0);
    }

    public void visualClear(int[] array, int index, double delay) {
        array[index] = -1;
    }

    public void multiDimWrite(int[][] array, int x, int y, int equals, double pause, boolean mark, boolean auxwrite) {
        if (ArrayVisualizer.sortCanceled()) throw new StopSort();
        if(mark) Highlights.markArray(1, x);

        if(auxwrite) auxWrites++;
        else            writes++;

        Timer.startLap();

        array[x][y] = equals;

        Timer.stopLap();

        ArrayVisualizer.updateNow();
        Delays.sleep(pause);
    }

    public <T> void multiDimWrite(T[][] array, int x, int y, T equals, double pause, boolean mark) {
        if (ArrayVisualizer.sortCanceled()) throw new StopSort();
        if(mark) Highlights.markArray(1, x);

        auxWrites++;

        Timer.startLap();

        array[x][y] = equals;

        Timer.stopLap();

        ArrayVisualizer.updateNow();
        Delays.sleep(pause);
    }

    //Simulates a write in order to better estimate time for values being written to an ArrayList
    public void mockWrite(int length, int pos, int val, double pause) {
        if (ArrayVisualizer.sortCanceled()) throw new StopSort();
        int[] mockArray = new int[length];

        this.auxWrites++;

        Timer.startLap();

        mockArray[pos] = val;

        Timer.stopLap();

        Delays.sleep(pause);
    }

    public void transcribe(int[] array, ArrayList<Integer>[] registers, int start, boolean mark, boolean auxwrite) {
        int total = start;

        for(int index = 0; index < registers.length; index++) {
            for(int i = 0; i < registers[index].size(); i++) {
                this.write(array, total++, registers[index].get(i), 0, mark, auxwrite);
                if(mark) Delays.sleep(1);
            }
            this.arrayListClear(registers[index]);
        }
    }

    public void transcribeMSD(int[] array, ArrayList<Integer>[] registers, int start, int min, double sleep, boolean mark, boolean auxwrite) {
        int total = start;
        int temp = 0;

        for(ArrayList<Integer> list : registers) {
            total += list.size();
        }

        for(int index = registers.length - 1; index >= 0; index--) {
            for(int i = registers[index].size() - 1; i >= 0; i--) {
                this.write(array, total + min - temp++ - 1, registers[index].get(i), 0, mark, auxwrite);
                if(mark) Delays.sleep(sleep);
            }
        }
    }

    public void fancyTranscribe(int[] array, int length, ArrayList<Integer>[] registers, double sleep) {
        int[] tempArray = this.createExternalArray(length);
        boolean[] tempWrite = new boolean[length];
        int radix = registers.length;

        this.transcribe(tempArray, registers, 0, false, true);
        auxWrites -= length;

        for(int i = 0; i < length; i++) {
            int register = i % radix;
            int pos = (register * (length / radix)) + (i / radix);

            if(!tempWrite[pos]) {
                this.write(array, pos, tempArray[pos], 0, false, false);
                tempWrite[pos] = true;
            }

            Highlights.markArray(register, pos);
            if(register == 0) Delays.sleep(sleep);
        }
        for(int i = 0; i < length; i++) {
            if(!tempWrite[i]){
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
        this.allocAmount += newLength;
        int[] result = Arrays.copyOf(original, newLength);
        ArrayVisualizer.getArrays().add(result);
        ArrayVisualizer.updateNow();
        return result;
    }

    public int[] copyOfRangeArray(int[] original, int from, int to) {
        this.allocAmount += to - from;
        int[] result = Arrays.copyOfRange(original, from, to);
        ArrayVisualizer.getArrays().add(result);
        ArrayVisualizer.updateNow();
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

    public int[] createExternalArray(int length) {
        this.allocAmount += length;
        int[] result = new int[length];
        ArrayVisualizer.getArrays().add(result);
        ArrayVisualizer.updateNow();
        return result;
    }

    public void deleteExternalArray(int[] array) {
        this.allocAmount -= array.length;
        ArrayVisualizer.getArrays().remove(array);
        ArrayVisualizer.updateNow();
    }

    public void deleteExternalArrays(int[]... arrays) {
        this.allocAmount -= Arrays.stream(arrays).reduce(0, (a, b) -> (a + b.length), (a, b) -> a + b);
        List<int[]> visArrays = ArrayVisualizer.getArrays();
        Arrays.stream(arrays).forEach(visArrays::remove);
        ArrayVisualizer.updateNow();
    }

    public void arrayListAdd(ArrayList<Integer> aList, int value) {
        allocAmount++;
        aList.add(value);
    }

    public void arrayListAdd(ArrayList<Integer> aList, int value, boolean mockWrite, double sleep) {
        allocAmount++;
        aList.add(value);
        if (mockWrite) {
            this.mockWrite(aList.size(), aList.size() - 1, value, sleep);
        }
        else {
            Delays.sleep(sleep);
        }
    }

    public void arrayListRemoveAt(ArrayList<Integer> aList, int index) {
        allocAmount--;
        aList.remove(index);
    }

    public void arrayListClear(ArrayList<Integer> aList) {
        allocAmount -= aList.size();
        aList.clear();
    }

    public void deleteArrayList(ArrayList<Integer> aList) {
        allocAmount -= aList.size();
    }

    public void deleteExternalArray(ArrayList<Integer>[] array) {
        for (ArrayList<Integer> aList : array) {
            deleteArrayList(aList);
        }
    }

    //TODO: These methods should be solely controlled by Timer class
    public void addTime(long milliseconds) {
        if(Timer.timerEnabled()) Timer.manualAddTime(milliseconds);
    }

    public void setTime(long milliseconds) {
        if(Timer.timerEnabled()) Timer.manualSetTime(milliseconds);
    }

    public void startLap() {
        if(Timer.timerEnabled()) Timer.startLap();
    }

    public void stopLap() {
        if(Timer.timerEnabled()) Timer.stopLap();
    }
}