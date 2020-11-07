package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.concurrent.IterativeBitonicSort;
import sorts.concurrent.IterativeOddEvenMergeSort;
import sorts.concurrent.IterativePairwiseSort;
import sorts.concurrent.RecursiveBitonicSort;
import sorts.concurrent.RecursiveOddEvenMergeSort;
import sorts.concurrent.RecursivePairwiseSort;
import sorts.templates.Sort;

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

final public class RunConcurrentSorts extends MultipleSortThread {
    private Sort RecursiveBitonicSort;
    private Sort RecursiveOddEvenMergeSort;
    private Sort RecursivePairwiseSort;
    private Sort IterativeBitonicSort;
    private Sort IterativeOddEvenMergeSort;
    private Sort IterativePairwiseSort;
    
    public RunConcurrentSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 6;
        this.categoryCount = this.sortCount;
        
        RecursiveBitonicSort      = new      RecursiveBitonicSort(this.arrayVisualizer);
        RecursiveOddEvenMergeSort = new RecursiveOddEvenMergeSort(this.arrayVisualizer);
        RecursivePairwiseSort     = new     RecursivePairwiseSort(this.arrayVisualizer);
        IterativeBitonicSort      = new      IterativeBitonicSort(this.arrayVisualizer);
        IterativeOddEvenMergeSort = new IterativeOddEvenMergeSort(this.arrayVisualizer);
        IterativePairwiseSort     = new     IterativePairwiseSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunConcurrentSorts.this.runIndividualSort(RecursiveBitonicSort,      0, array, 1024, 0.6667, false);
        RunConcurrentSorts.this.runIndividualSort(RecursiveOddEvenMergeSort, 0, array, 1024, 1,      false);
        RunConcurrentSorts.this.runIndividualSort(RecursivePairwiseSort,     0, array, 1024, 1,      false);
        RunConcurrentSorts.this.runIndividualSort(IterativeBitonicSort,      0, array, 1024, 0.3333, false);
        RunConcurrentSorts.this.runIndividualSort(IterativeOddEvenMergeSort, 0, array, 1024, 1,      false);
        RunConcurrentSorts.this.runIndividualSort(IterativePairwiseSort,     0, array, 1024, 0.8,    false);
    }
    
    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if(arrayVisualizer.getSortingThread() != null && arrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    if(runAllActive) {
                        RunConcurrentSorts.this.sortNumber = current;
                        RunConcurrentSorts.this.sortCount = total;
                    }
                    else {
                        RunConcurrentSorts.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Concurrent Sorts");

                    RunConcurrentSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        arrayVisualizer.setCategory("Run Concurrent Sorts");
                        arrayVisualizer.setHeading("Done");
                    }
                    
                    arrayManager.toggleMutableLength(true);
                }
                catch (Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                Sounds.toggleSound(false);
                arrayVisualizer.setSortingThread(null);
            }
        });

        arrayVisualizer.runSortingThread();
    }
}