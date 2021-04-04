/**
 * 
 */
package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License
Copyright (c) 2021 mingyue12
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

/**
 * @author mingyue12
 *
 */
public final class CocktailShellSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public CocktailShellSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		// TODO Auto-generated constructor stub
        this.setSortListName("Cocktail Shell");
        this.setRunAllSortsName("Cocktail Shell Sort");
        this.setRunSortName("Cocktail Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
	}

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		// TODO Auto-generated method stub
	     int gap = sortLength / 2;
	     boolean dir = true;
	     while (gap >= 1) {
	       if (dir) {
	         for (int i = gap; i < sortLength; i++) {
	           int tmp = array[i];
	           int j = i;
	           while (j >= gap && this.Reads.compareValues(array[j - gap], tmp) == 1) {
	             this.Highlights.markArray(2, j - gap);
	             this.Writes.write(array, j, array[j - gap], 0.7D, true, false);
	             j -= gap;
	           } 
	           
	           if (j - gap >= 0) {
	             this.Highlights.markArray(2, j - gap);
	           } else {
	             this.Highlights.clearMark(2);
	           } 
	           
	           this.Writes.write(array, j, tmp, 0.7D, true, false);
	         } 
	       } else {
	         for (int i = sortLength - gap; i >= 0; i--) {
	           int tmp = array[i];
	           int j = i;
	           while (j < sortLength - gap && this.Reads.compareValues(array[j + gap], tmp) == -1) {
	             this.Highlights.markArray(2, j + gap);
	             this.Writes.write(array, j, array[j + gap], 0.7D, true, false);
	             j += gap;
	           } 
	           
	           if (j + gap < sortLength) {
	             this.Highlights.markArray(2, j + gap);
	           } else {
	             this.Highlights.clearMark(2);
	           } 
	           
	           this.Writes.write(array, j, tmp, 0.7D, true, false);
	         } 
	       } 
	       gap = (int)(gap / Math.sqrt(2.0D));
	       dir = (gap == 1) ? true : (!dir);
	     }


	}

}
