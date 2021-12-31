package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020 aphitorite

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

final public class QuickBinaryRadixSort extends Sort {
    public QuickBinaryRadixSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Quick Binary Radix");
        this.setRunAllSortsName("Quick Binary Radix Sort");
        this.setRunSortName("Quick Binary Radix Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	public int partition(int[] array, int a, int b, int bit) {
        int i = a - 1;
        int j = b;

        while(true) {
            i++;
            while(i < b && !Reads.getBit(array[i], bit)) {
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
                i++;
            }

            j--;
            while(j >= a && Reads.getBit(array[j], bit)) {
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
                j--;
            }

            if(i < j) Writes.swap(array, i, j, 1, true, false);
            else      return i;
        }
    }

	private void radixLSD(int[] array, int a, int b, int p, int bit) {
		int m = p;

		for(int i = a; i < b; i++) {
			if(!Reads.getBit(array[i], bit)) m++;
			Highlights.markArray(1, i);
			Highlights.markArray(2, m);
			Delays.sleep(0.5);
		}

		for(int i = a; i < b; i++) {
			if(Reads.getBit(array[i], bit))
				Writes.swap(array, i, m++, 0.5, true, false);
			else
				Writes.swap(array, i, p++, 0.5, true, false);
		}
	}

	private void radixLSDSort(int[] array, int a, int b, int p, int maxBit) {
		int pow = 0, length = b-a;

		while(pow < maxBit) {
			this.radixLSD(array, a, b, p, pow++);

			if(pow >= maxBit) {
				for(int i = 0; i < length; i++)
					Writes.swap(array, a+i, p+i, 0.5, true, false);
				return;
			}
			else this.radixLSD(array, p, p+length, a, pow++);
		}
	}

	private void quickRadixSort(int[] array, int a, int b, int maxBit) {
		int start = a, end = b;
		while(maxBit >= 0) {
			int p = this.partition(array, start, end, maxBit);

			int left  = p-start;
			int right = end-p;

			if(left < right) {
				this.radixLSDSort(array, start, p, p, maxBit);
				start = p;
			}
			else {
				this.radixLSDSort(array, p, end, p-right, maxBit);
				end = p;
			}
			Highlights.clearMark(2);

			maxBit--;
		}
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int maxBit = Reads.analyzeBit(array, length);
		this.quickRadixSort(array, 0, length, maxBit);
    }
}