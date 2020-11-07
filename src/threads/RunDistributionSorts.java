package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.distribute.AmericanFlagSort;
import sorts.distribute.CountingSort;
import sorts.distribute.FlashSort;
import sorts.distribute.GravitySort;
import sorts.distribute.InPlaceLSDRadixSort;
import sorts.distribute.IterativeBinaryQuickSort;
import sorts.distribute.LSDRadixSort;
import sorts.distribute.MSDRadixSort;
import sorts.distribute.PigeonholeSort;
import sorts.distribute.RecursiveBinaryQuickSort;
import sorts.distribute.ShatterSort;
import sorts.distribute.SimpleShatterSort;
import sorts.distribute.IndexSort;
import sorts.distribute.TimeSort;
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

final public class RunDistributionSorts extends MultipleSortThread {
    private Sort CountingSort;
    private Sort PigeonholeSort;
    private Sort GravitySort;
    private Sort AmericanFlagSort;
    private Sort LSDRadixSort;
    private Sort InPlaceLSDRadixSort;
    private Sort MSDRadixSort;
    private Sort FlashSort;
    private Sort IterativeBinaryQuickSort;
    private Sort RecursiveBinaryQuickSort;
    private Sort ShatterSort;
    private Sort SimpleShatterSort;
    private Sort IndexSort;
    private Sort TimeSort;
    
    public RunDistributionSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 13;
        this.categoryCount = this.sortCount;
        
        CountingSort             = new             CountingSort(this.arrayVisualizer);
        PigeonholeSort           = new           PigeonholeSort(this.arrayVisualizer);
        GravitySort              = new              GravitySort(this.arrayVisualizer);
        AmericanFlagSort         = new         AmericanFlagSort(this.arrayVisualizer);
        LSDRadixSort             = new             LSDRadixSort(this.arrayVisualizer);
        InPlaceLSDRadixSort      = new      InPlaceLSDRadixSort(this.arrayVisualizer);
        MSDRadixSort             = new             MSDRadixSort(this.arrayVisualizer);
        FlashSort                = new                FlashSort(this.arrayVisualizer);
        IterativeBinaryQuickSort = new IterativeBinaryQuickSort(this.arrayVisualizer);
        RecursiveBinaryQuickSort = new RecursiveBinaryQuickSort(this.arrayVisualizer);
        ShatterSort              = new              ShatterSort(this.arrayVisualizer);
        SimpleShatterSort        = new        SimpleShatterSort(this.arrayVisualizer);
        IndexSort                = new                IndexSort(this.arrayVisualizer);
        TimeSort                 = new                 TimeSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunDistributionSorts.this.runIndividualSort(CountingSort,             0, array, 2048, 1.5,  false);
        RunDistributionSorts.this.runIndividualSort(PigeonholeSort,           0, array, 2048, 1.5,  false);
        RunDistributionSorts.this.runIndividualSort(GravitySort,              0, array, 1024, 0.5,  false);
        RunDistributionSorts.this.runIndividualSort(AmericanFlagSort,       128, array, 2048, 0.75, false);
        RunDistributionSorts.this.runIndividualSort(LSDRadixSort,             4, array, 2048, 1.5,  false);
        
        Sounds.toggleSofterSounds(true);
        RunDistributionSorts.this.runIndividualSort(InPlaceLSDRadixSort,     10, array, 2048, 1,    false);
        Sounds.toggleSofterSounds(false);
        
        RunDistributionSorts.this.runIndividualSort(MSDRadixSort,             4, array, 2048, 1.25, false);
        RunDistributionSorts.this.runIndividualSort(FlashSort,                0, array, 2048, 1,    false);
        RunDistributionSorts.this.runIndividualSort(IterativeBinaryQuickSort, 0, array, 2048, 1,    false);
        RunDistributionSorts.this.runIndividualSort(RecursiveBinaryQuickSort, 0, array, 2048, 1,    false);
        RunDistributionSorts.this.runIndividualSort(ShatterSort,            128, array, 2048, 1,    false);
        RunDistributionSorts.this.runIndividualSort(SimpleShatterSort,      128, array, 2048, 1,    false);
        RunDistributionSorts.this.runIndividualSort(IndexSort,                0, array, 2048, 1,    false);
        RunDistributionSorts.this.runIndividualSort(TimeSort,                10, array,  512, 0.05, false);
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
                        RunDistributionSorts.this.sortNumber = current;
                        RunDistributionSorts.this.sortCount = total;
                    }
                    else {
                        RunDistributionSorts.this.sortNumber = 1;
                    }
                    
                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Distribution Sorts");

                    RunDistributionSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        arrayVisualizer.setCategory("Run Distribution Sorts");
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