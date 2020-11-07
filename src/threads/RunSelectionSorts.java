package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.select.CycleSort;
import sorts.select.DoubleSelectionSort;
import sorts.select.FlippedMinHeapSort;
import sorts.select.MaxHeapSort;
import sorts.select.MinHeapSort;
import sorts.select.PoplarHeapSort;
import sorts.select.SelectionSort;
import sorts.select.SmoothSort;
import sorts.select.TernaryHeapSort;
import sorts.select.TournamentSort;
import sorts.select.WeakHeapSort;
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

final public class RunSelectionSorts extends MultipleSortThread {
    private Sort SelectionSort;
    private Sort DoubleSelectionSort;
    private Sort CycleSort;
    private Sort MaxHeapSort;
    private Sort MinHeapSort;
    private Sort FlippedMinHeapSort;
    private Sort WeakHeapSort;
    private Sort TernaryHeapSort;
    private Sort SmoothSort;
    private Sort PoplarHeapSort;
    private Sort TournamentSort;
    
    public RunSelectionSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 11;
        this.categoryCount = this.sortCount;
        
        SelectionSort       = new       SelectionSort(this.arrayVisualizer);
        DoubleSelectionSort = new DoubleSelectionSort(this.arrayVisualizer);
        CycleSort           = new           CycleSort(this.arrayVisualizer);
        MaxHeapSort         = new         MaxHeapSort(this.arrayVisualizer);
        MinHeapSort         = new         MinHeapSort(this.arrayVisualizer);
        FlippedMinHeapSort  = new  FlippedMinHeapSort(this.arrayVisualizer);
        WeakHeapSort        = new        WeakHeapSort(this.arrayVisualizer);
        TernaryHeapSort     = new     TernaryHeapSort(this.arrayVisualizer);
        SmoothSort          = new          SmoothSort(this.arrayVisualizer);
        PoplarHeapSort      = new      PoplarHeapSort(this.arrayVisualizer);
        TournamentSort      = new      TournamentSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunSelectionSorts.this.runIndividualSort(SelectionSort,       0, array,  128, 0.01, false);
        RunSelectionSorts.this.runIndividualSort(DoubleSelectionSort, 0, array,  128, 0.01, false);
        RunSelectionSorts.this.runIndividualSort(CycleSort,           0, array,  128, 0.01, false);
        RunSelectionSorts.this.runIndividualSort(MaxHeapSort,         0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(MinHeapSort,         0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(FlippedMinHeapSort,  0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(WeakHeapSort,        0, array, 2048, 1,    false);
        RunSelectionSorts.this.runIndividualSort(TernaryHeapSort,     0, array, 2048, 1,    false);
        RunSelectionSorts.this.runIndividualSort(SmoothSort,          0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(PoplarHeapSort,      0, array, 2048, 1,    false);
        RunSelectionSorts.this.runIndividualSort(TournamentSort,      0, array, 2048, 1.5,  false);
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
                        RunSelectionSorts.this.sortNumber = current;
                        RunSelectionSorts.this.sortCount = total;
                    }
                    else {
                        RunSelectionSorts.this.sortNumber = 1;
                    }
                    
                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Selection Sorts");

                    RunSelectionSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        arrayVisualizer.setCategory("Run Selection Sorts");
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