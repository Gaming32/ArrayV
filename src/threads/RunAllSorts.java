package threads;

import java.util.ArrayList;

import main.ArrayVisualizer;
import panes.JErrorPane;

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

final public class RunAllSorts {
    private ArrayVisualizer arrayVisualizer;
    private ArrayList<MultipleSortThread> allSortThreads;
    
    public RunAllSorts(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;
        this.allSortThreads = new ArrayList<>();
        this.allSortThreads.add(new RunExchangeSorts(arrayVisualizer));
        this.allSortThreads.add(new RunSelectionSorts(arrayVisualizer));
        this.allSortThreads.add(new RunInsertionSorts(arrayVisualizer));
        this.allSortThreads.add(new RunMergeSorts(arrayVisualizer));
        this.allSortThreads.add(new RunDistributionSorts(arrayVisualizer));
        this.allSortThreads.add(new RunConcurrentSorts(arrayVisualizer));
        this.allSortThreads.add(new RunHybridSorts(arrayVisualizer));
        this.allSortThreads.add(new RunMiscellaneousSorts(arrayVisualizer));
        this.allSortThreads.add(new RunImpracticalSorts(arrayVisualizer));
    }

    public void reportAllSorts(int[] array) {
        int totalSortCount = 0;
        for(MultipleSortThread category : this.allSortThreads) {
            totalSortCount += category.getSortCount();
        }
        
        try {
            int currentSort = 1;
            for(MultipleSortThread thread : this.allSortThreads) {
                thread.reportAllSorts(array, currentSort, totalSortCount);
                this.arrayVisualizer.getSortingThread().join();
                currentSort += thread.getCategoryCount();
            }
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }
        
        this.arrayVisualizer.setCategory("Run All Sorts");
        this.arrayVisualizer.setHeading("Finished!!");
    }
}