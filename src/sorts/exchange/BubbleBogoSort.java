package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

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

final public class BubbleBogoSort extends BogoSorting {
    public BubbleBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Bubble Bogo");
        this.setRunAllSortsName("Bubble Bogo Sort");
        this.setRunSortName("Bubble Bogosort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true); //Comparisons ARE used to swap elements
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        while(!this.bogoIsSorted(array, sortLength)){
            int index1 = (int) (Math.random() * (sortLength - 1));
            
            Highlights.markArray(1, index1);
            
            if(Reads.compareValues(array[index1], array[index1 + 1]) == 1){
                Writes.swap(array, index1, index1 + 1, 1, true, false);
            }
        }
    }
}