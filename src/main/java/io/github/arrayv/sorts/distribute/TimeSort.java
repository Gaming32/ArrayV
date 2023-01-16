package io.github.arrayv.sorts.distribute;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.templates.Sort;

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

@SortMeta(
    name = "Time",
    category = "Distribution Sorts",
    showcaseName = "Time Sort, Mul 10",
    question = "Enter delay per number in milliseconds:",
    defaultAnswer = 10
)
public final class TimeSort extends Sort {
    private InsertionSort insertSorter;

    private volatile int next = 0;

    public TimeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private synchronized void report(int[] array, int a){
        Writes.write(array, next, a, 0, true, false);
        next++;
    }

    @Override
    public void runSort(int[] array, int sortLength, int magnitude) throws Exception {
        insertSorter = new InsertionSort(this.arrayVisualizer);

        final int A = magnitude;
        next = 0;

        ArrayList<Thread> threads = new ArrayList<>();

        final int[] tmp = Writes.createExternalArray(sortLength);

        for(int i = 0; i < sortLength; i++) {
            Writes.write(tmp, i, array[i], 0.25, true, true);
        }

        double temp = Delays.getDisplayedDelay();
        Delays.updateDelayForTimeSort(magnitude);

        for(int i = 0; i < sortLength; i++){
            final int index = i;
            threads.add(new Thread("TimeSort-" + i) {
                @Override
                public void run() {
                    int a = tmp[index];

                    try {
                        Thread.sleep(a*A);
                        Writes.addTime(A);
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (IllegalArgumentException ex) {
                        JErrorPane.invokeErrorMessage(ex);
                    }
                    TimeSort.this.report(array, a);
                }
            });
        }

        for(Thread t : threads)
            t.start();

        try {
            Thread.sleep(sortLength * A);
        }
        catch (InterruptedException e) {
            Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (IllegalArgumentException ex) {
            JErrorPane.invokeErrorMessage(ex);
        }

        Delays.setCurrentDelay(temp);
        Writes.setTime(sortLength * A);

        insertSorter.customInsertSort(array, 0, sortLength, 0.2, false);

        Writes.deleteExternalArray(tmp);
    }
}
