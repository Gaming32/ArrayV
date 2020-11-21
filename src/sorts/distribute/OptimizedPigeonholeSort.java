package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2020 Gaming32

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

final public class OptimizedPigeonholeSort extends Sort {
    public OptimizedPigeonholeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Optimized Pigeonhole");
        this.setRunAllSortsName("Optimized Pigeonhole Sort");
        this.setRunSortName("Optimized Pigeonhole Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        for(int i = 0; i < sortLength; i++) {
            if(array[i] < min) {
                min = array[i];
            }
            if(array[i] > max) {
                max = array[i];
            }
        }
        
        int mi = min;
        int size = max - mi + 1;
        int[] holes = Writes.createExternalArray(size);
        
        for(int x = 0; x < sortLength; x++) {
            Writes.write(holes, array[x] - mi, holes[array[x] - mi] + 1, 1, false, true);
            Highlights.markArray(1, x);
        }
        
        int j = 0;
        
        for(int count = 0; count < size; count++) {
            for (int i = 0; i < holes[count]; i++) {
                Writes.write(array, j, count + mi, 1, false, false);
                
                Highlights.markArray(1, j);
                j++;
            }
        }

        Writes.deleteExternalArray(holes);
    }
}