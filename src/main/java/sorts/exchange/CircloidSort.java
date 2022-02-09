package sorts.exchange;

import sorts.templates.Sort;

import main.ArrayVisualizer;

/*
 * 
MIT License

Copyright (c) 2020 yuji

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

final public class CircloidSort extends Sort {
    public CircloidSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Circloid");
        this.setRunAllSortsName("Circloid Sort");
        this.setRunSortName("Circloid Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private boolean circle(int[] array, int left, int right) {
        int a = left;
        int b = right;
        boolean swapped = false;
        while(a < b) {
        	if(Reads.compareIndices(array, a, b, 0.25, true) == 1) {
        		Writes.swap(array, a, b, 1, true, false);
        		swapped = true;
        	}
    		a++;
    		b--;
    		if(a==b) {
    			b++;
    		}
        }
        return swapped;
    }
	
	private boolean circlePass(int[] array, int left, int right) {
		if(left >= right) return false;
		int mid = (left + right) / 2;
		boolean l = this.circlePass(array, left, mid);
		boolean r = this.circlePass(array, mid+1, right);
		return this.circle(array, left, right) || l || r;
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	while(this.circlePass(array, 0, length-1));
    }
}