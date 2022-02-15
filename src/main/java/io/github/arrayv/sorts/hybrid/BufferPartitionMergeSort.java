package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2021 yuji, implemented by aphitorite

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

public final class BufferPartitionMergeSort extends Sort {
    public BufferPartitionMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Buffer Partition Merge (BPM)");
        this.setRunAllSortsName("Buffer Partition Merge Sort");
        this.setRunSortName("BPMSort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private InsertionSort insSort;
	private BinaryInsertionSort binInsSort;

	private void shiftBW(int[] array, int a, int m, int b) {
		while(m > a) Writes.swap(array, --b, --m, 1, true, false);
	}

	private void multiSwap(int[] array, int a, int b, int len) {
		for(int i = 0; i < len; i++)
			Writes.swap(array, a+i, b+i, 1, true, false);
	}

    private void rotate(int[] array, int a, int m, int b) {
        int l = m-a, r = b-m;

        while(l > 0 && r > 0) {
			if(r < l) {
				this.multiSwap(array, m-r, m, r);
				b -= r;
				m -= r;
				l -= r;
            }
            else {
				this.multiSwap(array, a, m, l);
				a += l;
				m += l;
				r -= l;
            }
        }
    }

	private void inPlaceMerge(int[] array, int a, int m, int b) {
		int i = a, j = m, k;

		while(i < j && j < b) {
			if(Reads.compareValues(array[i], array[j]) > 0) {
				k = j;
				while(++k < b && Reads.compareIndices(array, i, k, 0, false) > 0);

				this.rotate(array, i, j, k);

				i += k-j;
				j = k;
			}
			else i++;
		}
	}

	private void medianOfThree(int[] array, int a, int b) {
		int m = a+(b-1-a)/2;

		if(Reads.compareIndices(array, a, m, 1, true) == 1)
			Writes.swap(array, a, m, 1, true, false);

		if(Reads.compareIndices(array, m, b-1, 1, true) == 1) {
			Writes.swap(array, m, b-1, 1, true, false);

			if(Reads.compareIndices(array, a, m, 1, true) == 1)
				return;
		}

		Writes.swap(array, a, m, 1, true, false);
	}

	//lite version
	private void medianOfMedians(int[] array, int a, int b, int s) {
		int end = b, start = a, i, j;
		boolean ad = true;

		while(end - start > 1) {
			j = start;
			Highlights.markArray(2, j);
			for(i = start; i+2*s <= end; i+=s) {
				this.insSort.customInsertSort(array, i, i+s, 0.25, false);
				Writes.swap(array, j++, i+s/2, 1, false, false);
				Highlights.markArray(2, j);
			}
			if(i < end) {
				this.insSort.customInsertSort(array, i, end, 0.25, false);
				Writes.swap(array, j++, i+(end-(ad ? 1 : 0)-i)/2, 1, false, false);
				Highlights.markArray(2, j);
				if((end-i)%2 == 0) ad = !ad;
			}
			end = j;
		}
	}

	private int partition(int[] array, int a, int b) {
        int i = a, j = b;
		Highlights.markArray(3, a);

        while(true) {
			do {
				i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
			}
			while(i < j && Reads.compareIndices(array, i, a, 0, false) == 1);

			do {
				j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
			}
            while(j >= i && Reads.compareIndices(array, j, a, 0, false) == -1);

            if(i < j) Writes.swap(array, i, j, 1, true, false);
            else      return j;
        }
    }

	private int quickSelect(int[] array, int a, int b, int m) {
		boolean badPartition = false, mom = false;
		int m1 = (m+b+1)/2;

		while(true) {
			if(badPartition) {
				this.medianOfMedians(array, a, b, 5);
				mom = true;
			}
			else this.medianOfThree(array, a, b);

			int p = this.partition(array, a, b);
			Writes.swap(array, a, p, 1, true, false);

			int l = Math.max(1, p-a);
			int r = Math.max(1, b-(p+1));
			badPartition = !mom && (l/r >= 16 || r/l >= 16);

			if(p >= m && p < m1) return p;
			else if(p < m) a = p+1;
			else           b = p;
		}
	}

	private void merge(int[] array, int a, int m, int b, int p) {
		int i = a, j = m;

		while(i < m && j < b) {
			if(Reads.compareIndices(array, i, j, 0, false) <= 0)
				Writes.swap(array, p++, i++, 1, true, false);
			else
				Writes.swap(array, p++, j++, 1, true, false);
		}

		while(i < m) Writes.swap(array, p++, i++, 1, true, false);
		while(j < b) Writes.swap(array, p++, j++, 1, true, false);
	}

	private int mergeFW(int[] array, int p, int a, int m, int b) {
		int i = a, j = m;

		while(i < m && j < b) {
			if(Reads.compareIndices(array, i, j, 0, false) <= 0)
				Writes.swap(array, p++, i++, 1, true, false);
			else
				Writes.swap(array, p++, j++, 1, true, false);
		}

		if(i < m) return i;
		else      return j;
	}

	public static int getMinLevel(int n) {
		while(n >= 32) n = (n+3)/4;
		return n;
	}

	private void mergeSort(int[] array, int a, int b, int p) {
		int len = b-a;
		if(len < 2) return;

		int i, pos, j = getMinLevel(len);

		for(i = a; i+j <= b; i+=j)
			this.binInsSort.customBinaryInsert(array, i, i+j, 0.25);
		this.binInsSort.customBinaryInsert(array, i, b, 0.25);

		while(j < len) {
			pos = p;
			for(i = a; i+2*j <= b; i+=2*j, pos+=2*j)
				this.merge(array, i, i+j, i+2*j, pos);
			if(i + j < b)
				this.merge(array, i, i+j, b, pos);
			else
				while(i < b) Writes.swap(array, i++, pos++, 1, true, false);

			j *= 2;

			pos = a;
			for(i = p; i+2*j <= p+len; i+=2*j, pos+=2*j)
				this.merge(array, i, i+j, i+2*j, pos);
			if(i + j < p+len)
				this.merge(array, i, i+j, p+len, pos);
			else
				while(i < p+len) Writes.swap(array, i++, pos++, 1, true, false);

			j *= 2;
		}
	}

	private void sort(int[] array, int a, int b) {
		int minLvl = (int)Math.sqrt(b-a);

		int m = (a+b+1)/2;
		this.mergeSort(array, m, b, a);

		while(m-a > minLvl) {
			int m1 = (a+m+1)/2;

			m1 = this.quickSelect(array, a, m, m1);
			this.mergeSort(array, m1, m, a);

			int bSize = m1-a;
			int m2 = Math.min(m1+bSize, b);
			m1 = this.mergeFW(array, a, m1, m, m2);

			while(m1 < m) {
				this.shiftBW(array, m1, m, m2);
				m1 = m2-(m-m1);
				a  = m1-bSize;
				m  = m2;

				if(m == b) break;

				m2 = Math.min(m2+bSize, b);
				m1 = this.mergeFW(array, a, m1, m, m2);
			}
			m = m1;
			a = m1-bSize;
		}
		this.binInsSort.customBinaryInsert(array, a, m, 0.25);
		this.inPlaceMerge(array, a, m, b);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.insSort = new InsertionSort(this.arrayVisualizer);
		this.binInsSort = new BinaryInsertionSort(this.arrayVisualizer);
		this.sort(array, 0, length);
    }
}
