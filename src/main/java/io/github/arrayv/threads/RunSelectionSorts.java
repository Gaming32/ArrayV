package io.github.arrayv.threads;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sorts.select.*;
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

final public class RunSelectionSorts extends MultipleSortThread {
    private Sort SelectionSort;
    private Sort DoubleSelectionSort;
    private Sort CycleSort;
    private Sort MaxHeapSort;
    private Sort MinHeapSort;
    private Sort FlippedMinHeapSort;
    private Sort BaseNMaxHeapSort;
    private Sort WeakHeapSort;
    private Sort TernaryHeapSort;
    private Sort SmoothSort;
    private Sort PoplarHeapSort;
    private Sort TournamentSort;
    private Sort AsynchronousSort;
    private Sort QueueSort;
    private Sort DequeueSort;
    private Sort StableSelectionSort;
    private Sort BingoSort;
    private Sort ClassicTournamentSort;
    private Sort ReverseSandpaperSort;
    private Sort ReverseSelectionSort;
    private Sort SandpaperSort;
    private Sort StableCycleSort;
    private Sort TriangularHeapSort;
    
    public RunSelectionSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 23;
        this.categoryCount = this.sortCount;
        
        SelectionSort         = new         SelectionSort(this.arrayVisualizer);
        StableSelectionSort   = new   StableSelectionSort(this.arrayVisualizer);
        DoubleSelectionSort   = new   DoubleSelectionSort(this.arrayVisualizer);
        CycleSort             = new             CycleSort(this.arrayVisualizer);
        MaxHeapSort           = new           MaxHeapSort(this.arrayVisualizer);
        MinHeapSort           = new           MinHeapSort(this.arrayVisualizer);
        FlippedMinHeapSort    = new    FlippedMinHeapSort(this.arrayVisualizer);
        WeakHeapSort          = new          WeakHeapSort(this.arrayVisualizer);
        TernaryHeapSort       = new       TernaryHeapSort(this.arrayVisualizer);
        BaseNMaxHeapSort      = new      BaseNMaxHeapSort(this.arrayVisualizer);
        SmoothSort            = new            SmoothSort(this.arrayVisualizer);
        PoplarHeapSort        = new        PoplarHeapSort(this.arrayVisualizer);
        TournamentSort        = new        TournamentSort(this.arrayVisualizer);
        AsynchronousSort      = new      AsynchronousSort(this.arrayVisualizer);
        QueueSort             = new             QueueSort(this.arrayVisualizer);
        DequeueSort           = new           DequeueSort(this.arrayVisualizer);
        BingoSort             = new             BingoSort(this.arrayVisualizer);
        ClassicTournamentSort = new ClassicTournamentSort(this.arrayVisualizer);
        ReverseSandpaperSort  = new  ReverseSandpaperSort(this.arrayVisualizer);
        ReverseSelectionSort  = new  ReverseSelectionSort(this.arrayVisualizer);
        SandpaperSort         = new         SandpaperSort(this.arrayVisualizer);
        StableCycleSort       = new       StableCycleSort(this.arrayVisualizer);
        TriangularHeapSort    = new    TriangularHeapSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunSelectionSorts.this.runIndividualSort(SelectionSort,         0, array,  128, 0.01, false);
        RunSelectionSorts.this.runIndividualSort(ReverseSelectionSort,  0, array,  128, 0.01, false);
        RunSelectionSorts.this.runIndividualSort(DoubleSelectionSort,   0, array,  128, 0.01, false);
        RunSelectionSorts.this.runIndividualSort(StableSelectionSort,   0, array,  128, 0.5,  false);
        RunSelectionSorts.this.runIndividualSort(SandpaperSort,         0, array,  128, 0.05, false);
        RunSelectionSorts.this.runIndividualSort(ReverseSandpaperSort,  0, array,  128, 0.05, false);
        RunSelectionSorts.this.runIndividualSort(CycleSort,             0, array,  128, 0.01, false);
        RunSelectionSorts.this.runIndividualSort(StableCycleSort,       0, array,  128, 0.01, false);
        RunSelectionSorts.this.runIndividualSort(BingoSort,             0, array,  128, 0.1,  false);
        RunSelectionSorts.this.runIndividualSort(MaxHeapSort,           0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(MinHeapSort,           0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(FlippedMinHeapSort,    0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(BaseNMaxHeapSort,      4, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(TriangularHeapSort,    0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(WeakHeapSort,          0, array, 2048, 1,    false);
        RunSelectionSorts.this.runIndividualSort(TernaryHeapSort,       0, array, 2048, 1,    false);
        RunSelectionSorts.this.runIndividualSort(SmoothSort,            0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(PoplarHeapSort,        0, array, 2048, 1,    false);
        RunSelectionSorts.this.runIndividualSort(TournamentSort,        0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(ClassicTournamentSort, 0, array, 2048, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(AsynchronousSort,      0, array, 1024, 1.5,  false);
        RunSelectionSorts.this.runIndividualSort(QueueSort,             0, array, 2048, 1,    false);
        RunSelectionSorts.this.runIndividualSort(DequeueSort,           0, array, 2048, 1,    false);
    }
    
    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if(arrayVisualizer.isActive())
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