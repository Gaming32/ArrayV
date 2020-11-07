package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.merge.BottomUpMergeSort;
import sorts.merge.InPlaceMergeSort;
import sorts.merge.LazyStableSort;
import sorts.merge.MergeSort;
import sorts.merge.RotateMergeSort;
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

final public class RunMergeSorts extends MultipleSortThread {
    private Sort MergeSort;
    private Sort BottomUpMergeSort;
    private Sort InPlaceMergeSort;
    private Sort LazyStableSort;
    private Sort RotateMergeSort;
    
    public RunMergeSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 5;
        this.categoryCount = this.sortCount;
        
        MergeSort         = new         MergeSort(this.arrayVisualizer);
        BottomUpMergeSort = new BottomUpMergeSort(this.arrayVisualizer);
        InPlaceMergeSort  = new  InPlaceMergeSort(this.arrayVisualizer);
        LazyStableSort    = new    LazyStableSort(this.arrayVisualizer);
        RotateMergeSort   = new   RotateMergeSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunMergeSorts.this.runIndividualSort(MergeSort,         0, array, 2048, 1.5,  false);
        RunMergeSorts.this.runIndividualSort(BottomUpMergeSort, 0, array, 2048, 1.5,  false);
        RunMergeSorts.this.runIndividualSort(InPlaceMergeSort,  0, array, 2048, 1.5,  false);
        RunMergeSorts.this.runIndividualSort(LazyStableSort,    0, array,  256, 0.2,  false);
        RunMergeSorts.this.runIndividualSort(RotateMergeSort,   0, array,  512, 0.2,  false);
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
                        RunMergeSorts.this.sortNumber = current;
                        RunMergeSorts.this.sortCount = total;
                    }
                    else {
                        RunMergeSorts.this.sortNumber = 1;
                    }
                    
                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Merge Sorts");

                    RunMergeSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        arrayVisualizer.setCategory("Run Merge Sorts");
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