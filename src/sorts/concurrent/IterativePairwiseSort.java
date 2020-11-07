package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License
Copyright (c) 2019 PiotrGrochowski
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

final public class IterativePairwiseSort extends Sort {
    public IterativePairwiseSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Iterative Pairwise");
        this.setRunAllSortsName("Iterative Pairwise Sorting Network");
        this.setRunSortName("Iterative Pairwise Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void iterativepairwise(int[] array, int length, double sleep) {
        int a = 1;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        while (a < length){
            b = a;
            c = 0;
            while (b < length){
                Delays.sleep(sleep);
                Highlights.markArray(1, b - a);
                Highlights.markArray(2, b);
                if(Reads.compareValues(array[b - a], array[b]) == 1) {
                    Writes.swap(array, b - a, b, sleep, true, false);
                }
                c = (c + 1) % a;
                b++;
                if (c == 0){
                    b += a;
                }
            }
            a *= 2;
        }
        a /= 4;
        e = 1;
        while (a > 0){
            d = e;
            while (d > 0){
                b = ((d + 1) * a);
                c = 0;
                while (b < length){
                    Delays.sleep(sleep);
                    Highlights.markArray(1, b - (d * a));
                    Highlights.markArray(2, b);
                    if(Reads.compareValues(array[b - (d * a)], array[b]) == 1) {
                        Writes.swap(array, b - (d * a), b, sleep, true, false);
                    }
                    c = (c + 1) % a;
                    b++;
                    if (c == 0){
                        b += a;
                    }
                }
                d /= 2;
            }
            a /= 2;
            e = (e * 2) + 1;
        }
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.iterativepairwise(array, sortLength, 1);
    }
}