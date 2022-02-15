package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;

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

/**
* KotaSort / EctaSort
*
* KotaSort is an in-place stable worst case O(n log n) sort with O(1) space
* and is also an implementation of "Block Merge Sort" that inherits some of
* Andrey Astrelin's GrailSort's functions and idea of a movement imitation buffer.
*
* A variant called EctaSort uses an extra 3*sqrt n space for merging in-place and tagging
* without depending on the amount of unique values in the data.
*
* Others:
* kotaSortDynamicBuf() - allocates 2*sqrt n space for merging only
* kotaSortStaticBuf()  - makes the most use out of a fixed given amount of space
*
* @author aphitorite
*/
public abstract class KotaSorting extends Sort {
	protected KotaSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

	private final int CACHE_SIZE = 32;

	private int[] tags;
	private int[] cache;

	private int bufPos;
	private int blockLen;
	private int tagLen;
	private int bufLen;
	private int effMem;

	private boolean ext;

	private void rotate(int[] array, int start, int split, int end) {
        int temp;
        while(split < end && split > start){
            if (end-split < split-start){
				if(end-split == 1) {
					Highlights.clearMark(2);
					temp = array[split];
					this.shift(array, start, split, end, true, true);
					Writes.write(array, start, temp, 1, true, false);
					return;
				}
				else {
					this.shift(array, 2*split-end, split, end, true, false);
					temp = end;
					end = split;
					split -= temp-split;
				}
            }
            else{
				if(split-start == 1) {
					Highlights.clearMark(2);
					temp = array[start];
					this.shift(array, start, split, end, false, true);
					Writes.write(array, end-1, temp, 1, true, false);
					return;
				}
				else {
					this.shift(array, start, split, 2*split-start, false, false);
					temp = start;
					start = split;
					split += split-temp;
				}
            }
        }
    }

	/*private void rotate(int[] array, int a, int m, int b) {
		Writes.reversal(array, a, b-1, 1, true, false);
		m = a+b-m;
		Writes.reversal(array, a, m-1, 1, true, false);
		Writes.reversal(array, m, b-1, 1, true, false);
	}*/

	private void kotaSwap(int[] array, int a, int b, boolean aux) {
		if(aux) {
			Highlights.markArray(2, b);
			Writes.write(array, a, array[b], 1, true, false);
		}
		else Writes.swap(array, a, b, 1, true, false);
	}

	private int binarySearch(int[] array, int start, int end, int value, boolean left) {
		int a = start, b = end;

		while(a < b) {
			int m = (a+b)/2;
			boolean comp;

			if(left) comp = Reads.compareValues(value, array[m]) <= 0;
			else     comp = Reads.compareValues(value, array[m]) < 0;

			if(comp) b = m;
			else     a = m+1;
		}

		return a;
	}

	private int findKeys(int[] array, int start, int end, int num) {
		int numKeys = 1, pos = start, posEnd = start+1;

		for(int i = start+1; i < end && numKeys < num; i++) {
			Highlights.markArray(2, i);
			Delays.sleep(1);
			int loc = this.binarySearch(array, pos, posEnd, array[i], true);
			if(i == loc || Reads.compareValues(array[i], array[loc]) != 0) {
				this.rotate(array, pos, posEnd, i);
				int inc = i-posEnd;
				loc    += inc;
				pos    += inc;
				posEnd += inc;
				this.rotate(array, loc, posEnd, posEnd+1);
				numKeys++;
				posEnd++;
			}
		}
		this.rotate(array, start, pos, posEnd);
		return numKeys;
	}

	private void swapToTags(int[] array, int a, int i, boolean aux) {
		if(aux) {
			Highlights.markArray(1, a);
			Highlights.markArray(2, i);
			int temp = this.tags[i];
			Writes.write(this.tags, i, array[a], 0, false, true);
			Writes.write(array, a, temp, 10, false, false);
		}
		else {
			this.kotaSwap(array, this.bufPos+i, a, false);
			Delays.sleep(9);
		}
	}

	/**
	Shifts values across a range determined by @param a, m, b
	@param left determines which length's order is maintained
	*/
	private void shift(int[] array, int a, int m, int b, boolean left, boolean aux) {
		if(left) {
			if(m == b) return;
			while(m > a)
				this.kotaSwap(array, --b, --m, aux);
		}
		else {
			if(m == a) return;
			while(m < b)
				this.kotaSwap(array, a++, m++, aux);
		}
	}

	//NOTE: BW = backwards version

	/**
	Swap groups of items of blockLen at starting points @param a, b
	(BW: the starting points here would be the end points inclusive)
	*/
	private void multiSwap(int[] array, int a, int b, int len, boolean aux) {
		for(int i = 0; i < len; i++)
			this.kotaSwap(array, a+i, b+i, aux);
	}

	private void multiSwapBW(int[] array, int a, int b, int len, boolean aux) {
		for(int i = 0; i < len; i++)
			this.kotaSwap(array, a-i, b-i, aux);
	}

	/**
	Performs a selection sort on the tagged blocks
	Tag values are unique so this doesn't affect stability
	@param pos   is the position of where the first tag is closest to start or end (BW)
	@param count is the amount of them to select
	*/
	private void blockSelect(int[] array, int pos, int count) {
		for(int j = 0; j < count; j++) {
			int start = pos+j*this.blockLen, min = start;

			for(int i = j+1; i < count; i++) {
				int sel = pos+i*this.blockLen;
				if(Reads.compareValues(array[sel], array[min]) == -1)
					min = sel;
			}
			if(start != min) this.multiSwap(array, start, min, this.blockLen, false);
			this.swapToTags(array, start, j, false);
		}
		Highlights.clearMark(2);
	}

	private void blockSelectBW(int[] array, int pos, int count) {
		for(int j = 0; j < count; j++) {
			int start = pos-j*this.blockLen, min = start;

			for(int i = j+1; i < count; i++) {
				int sel = pos-i*this.blockLen;
				if(Reads.compareValues(array[sel], array[min]) == -1)
					min = sel;
			}
			if(start != min) this.multiSwapBW(array, start, min, this.blockLen, false);
			this.swapToTags(array, start, j, false);
		}
		Highlights.clearMark(2);
	}

	/**
	(EctaSort) Performs a cycle sort on the tagged blocks to make optimal # of writes
	EctaSort's [0, 1, 2, 3, 4...] generated tag values allows this to make O(n) comparisons during the merging process
	@param pos   is the position of where the first tag is closest to start or end (BW)
	@param count is the amount of them to sort
	*/
	private void blockCycle(int[] array, int pos, int count, int p) {
		for(int j = 0; j < count; j++) {
			int start = pos+j*this.blockLen;

			if(j != array[start]) {
				int first = array[start];
				int val = j;

				this.multiSwap(array, p, start, this.blockLen, true);

				while(val != first) {
					int valStart = pos+val*this.blockLen;

					int k = j+1, next = pos+k*this.blockLen;
					while(Reads.compareValues(array[next], val) != 0)
						next = pos+(++k)*this.blockLen;

					val = k;
					this.multiSwap(array, valStart, next, this.blockLen, true);
				}

				first = pos+first*this.blockLen;
				this.multiSwap(array, first, p, this.blockLen, true);
			}

			this.swapToTags(array, start, j, true);
		}
	}

	private void blockCycleBW(int[] array, int pos, int count, int p) {
		for(int j = 0; j < count; j++) {
			int start = pos-j*this.blockLen;

			if(j != array[start]) {
				int first = array[start];
				int val = j;

				this.multiSwapBW(array, p, start, this.blockLen, true);

				while(val != first) {
					int valStart = pos-val*this.blockLen;

					int k = j+1, next = pos-k*this.blockLen;
					while(Reads.compareValues(array[next], val) != 0)
						next = pos-(++k)*this.blockLen;

					val = k;
					this.multiSwapBW(array, valStart, next, this.blockLen, true);
				}

				first = pos-first*this.blockLen;
				this.multiSwapBW(array, first, p, this.blockLen, true);
			}

			this.swapToTags(array, start, j, true);
		}
	}

	/**
	O(n) worst case in-place merge algorithm
	@param [a, m) is the first range
	@param [m, b) is the second
	*/
	private void blockMerge(int[] array, int a, int m, int b, boolean auxTag) {
		if(b-m <= 2*this.bufLen) {
			this.dualMerge(array, a, m, b, this.bufLen);
			return;
		}

		/**
		@var i, j            are the pointers to the halves
		@var k               counts up to blockLen and resets
		@var first           keeps track of the starting point of a block
		@var left, right     keeps track of the locations of the buffer through the merge
		@var leftAD, rightAD keeps track of the left and right buffer size
		@var tagCount        increments when a block is tagged
		*/
		int i = a, j = m, k, first;
		int leftAD = this.bufLen, rightAD = 0;
		int left = i-this.bufLen, right = j;
		int tagCount = 0;

		//merge as many block sized sequences to the left
		//no block tagging happens here
		while(i < m && leftAD >= rightAD) { //j will never be >= b
			k = 0;

			while(i < m && k < this.blockLen) {
				if(Reads.compareValues(array[i], array[j]) <= 0) {
					this.kotaSwap(array, left++, i++, this.ext);
				}
				else {
					this.kotaSwap(array, left++, j++, this.ext);
					rightAD++;
					leftAD--;
				}
				k++;
			}
		}

		int selStart = left; //where to start selecting the blocks back in place

		while(i < m && j < b) {
			//merge as many block sized sequences to the right
			while(i < m && j < b && rightAD > leftAD) {
				first = right;
				k = 0;

				while(i < m && j < b && k < this.blockLen) {
					if(Reads.compareValues(array[i], array[j]) <= 0) {
						this.kotaSwap(array, right++, i++, this.ext);
						rightAD--;
						leftAD++;
					}
					else {
						this.kotaSwap(array, right++, j++, this.ext);
					}
					k++;
				}
				//move as many elements as possible to the block before breaking out
				while(i < m && k < this.blockLen) {
					this.kotaSwap(array, right++, i++, this.ext);
					rightAD--;
					leftAD++;
					k++;
				}
				while(j < b && k < this.blockLen) {
					this.kotaSwap(array, right++, j++, this.ext);
					k++;
				}

				if(k == this.blockLen) //if block is not complete don't tag
					this.swapToTags(array, first, tagCount++, auxTag);
				else {
					//if there was a leftover block shift the right buffer back
					this.shift(array, first, first+k, b, true, this.ext);
					j = b-k;
					right = first;
				}
			}

			//merge as many block sized sequences to the left
			while(i < m && j < b && leftAD >= rightAD) {
				first = left;
				k = 0;

				while(i < m && j < b && k < this.blockLen) {
					if(Reads.compareValues(array[i], array[j]) <= 0) {
						this.kotaSwap(array, left++, i++, this.ext);
					}
					else {
						this.kotaSwap(array, left++, j++, this.ext);
						rightAD++;
						leftAD--;
					}
					k++;
				}
				while(i < m && k < this.blockLen) {
					this.kotaSwap(array, left++, i++, this.ext);
					k++;
				}
				while(j < b && k < this.blockLen) {
					this.kotaSwap(array, left++, j++, this.ext);
					rightAD++;
					leftAD--;
					k++;
				}

				if(k == this.blockLen)
					this.swapToTags(array, first, tagCount++, auxTag);
				else {
					//rotates leftover block along with buffer to the end
					this.rotate(array, first, m, right);
					left += right-m;
					leftAD = 0;
				}
			}
		}
		if(i >= m && leftAD == this.blockLen && tagCount > 0) {         //if the left buffer is the same size as a block
			this.multiSwap(array, left, right-this.blockLen, this.blockLen, this.ext); //it can be swapped with the last tagged block
		}
		else {
			if(i < m) {								//if left range wasnt fully merged
				this.rotate(array, left, m, right); //rotate it to the right buffer along with the left buffer
				left += right-m;
			}
			this.shift(array, left, left+leftAD, right, false, this.ext); //shift left buffer to connect with right buffer
		}
		if(j < b) this.shift(array, j-this.bufLen, j, b, false, this.ext); //if right range wasnt fully merged
																		   //shift the full buffer to the end
		if(auxTag) this.blockCycle(array, selStart, tagCount, b-this.bufLen);
		else       this.blockSelect(array, selStart, tagCount);
	}

	private void blockMergeBW(int[] array, int a, int m, int b, boolean auxTag) {
		int i = m-1, j = b-1, k, first;
		int leftAD = 0, rightAD = this.bufLen;
		int left = i, right = j+this.bufLen;
		int tagCount = 0;

		while(j >= m && rightAD >= leftAD) {
			k = 0;

			while(j >= m && k < this.blockLen) {
				if(Reads.compareValues(array[i], array[j]) == 1) {
					this.kotaSwap(array, right--, i--, this.ext);
					leftAD++;
					rightAD--;
				}
				else {
					this.kotaSwap(array, right--, j--, this.ext);
				}
				k++;
			}
		}

		int selStart = right;

		while(j >= m && i >= a) {
			while(j >= m && i >= a && leftAD > rightAD) {
				first = left;
				k = 0;

				while(j >= m && i >= a && k < this.blockLen) {
					if(Reads.compareValues(array[i], array[j]) == 1) {
						this.kotaSwap(array, left--, i--, this.ext);
					}
					else {
						this.kotaSwap(array, left--, j--, this.ext);
						rightAD++;
						leftAD--;
					}
					k++;
				}
				while(j >= m && k < this.blockLen) {
					this.kotaSwap(array, left--, j--, this.ext);
					rightAD++;
					leftAD--;
					k++;
				}
				while(i >= a && k < this.blockLen) {
					this.kotaSwap(array, left--, i--, this.ext);
					k++;
				}

				if(k == this.blockLen)
					this.swapToTags(array, first, tagCount++, auxTag);
				else {
					this.shift(array, a, first+1-k, first+1, false, this.ext);
					i = a-1+k;
					left = first;
				}
			}

			while(j >= m && i >= a && rightAD >= leftAD) {
				first = right;
				k = 0;

				while(j >= m && i >= a && k < this.blockLen) {
					if(Reads.compareValues(array[i], array[j]) == 1) {
						this.kotaSwap(array, right--, i--, this.ext);
						leftAD++;
						rightAD--;
					}
					else {
						this.kotaSwap(array, right--, j--, this.ext);
					}
					k++;
				}
				while(j >= m && k < this.blockLen) {
					this.kotaSwap(array, right--, j--, this.ext);
					k++;
				}
				while(i >= a && k < this.blockLen) {
					this.kotaSwap(array, right--, i--, this.ext);
					leftAD++;
					rightAD--;
					k++;
				}

				if(k == this.blockLen)
					this.swapToTags(array, first, tagCount++, auxTag);
				else {
					this.rotate(array, left+1, m, first+1);
					right -= m-(left+1);
					rightAD = 0;
				}
			}
		}

		if(j < m && rightAD == this.blockLen && tagCount > 0) {
			this.multiSwapBW(array, right, left+this.blockLen, this.blockLen, this.ext);
		}
		else {
			if(j >= m) {
				this.rotate(array, left+1, m, right+1);
				right -= m-(left+1);
			}
			this.shift(array, left+1, right+1-rightAD, right+1, true, this.ext);
		}
		if(i >= a) this.shift(array, a, i+1, i+1+this.bufLen, true, this.ext);

		if(auxTag) this.blockCycleBW(array, selStart, tagCount, a-1+this.bufLen);
		else       this.blockSelectBW(array, selStart, tagCount);
	}

	/**
	O(n) worst case in-place merge algorithm when merging ~sqrt(n) sized sequences
	@param [a, m) is the first range
	@param [m, b) is the second
	*/
	private void inPlaceMerge(int[] array, int a, int m, int b) {
		int i = a, j = m, k;

		while(i < j && j < b){
			if(Reads.compareValues(array[i], array[j]) == 1) {
				k = this.binarySearch(array, j, b, array[i], true);
				this.rotate(array, i, j, k);

				i += k-j;
				j = k;
			}
			else i++;
		}
	}

	private void inPlaceMergeBW(int[] array, int a, int m, int b) {
		int i = m-1, j = b-1, k;

		while(j > i && i >= a){
			if(Reads.compareValues(array[i], array[j]) >= 0) {
				k = this.binarySearch(array, a, i+1, array[j], true);
				this.rotate(array, k, i+1, j+1);

				j -= (i+1)-k;
				i = k-1;
			}
			else j--;
		}
	}

	/**
	O(n^2) worst case in-place merge algorithm
	Much faster for merging small lists and lists with very few unique values
	@param [start, mid) is the first range
	@param [mid, end)   is the second
	*/
    private void inPlaceMerge2(int[] array, int start, int mid, int end) {
		int i = start, m = mid, k = mid, q;

		while(m < end) {
			if(Reads.compareValues(array[m-1], array[m]) <= 0)
				return;

			while(i < m-1 && Reads.compareValues(array[i], array[m]) <= 0) i++;
			Writes.swap(array, i++, k++, 1, true, false);

			while(i < m) {
				Highlights.markArray(3, m);
				while(i < m && k < end && Reads.compareValues(array[m], array[k]) == 1)
					Writes.swap(array, i++, k++, 1, true, false);
				Highlights.clearMark(3);

				if(i >= m) break;

				else if(k >= end) {
					this.rotate(array, i, m, end);
					return;
				}

				else if(k-m >= m-i) {
					this.rotate(array, i, m, k);
					break;
				}

				q = m;
				while(i < m && q < k && Reads.compareValues(array[q], array[k]) <= 0)
					Writes.swap(array, i++, q++, 1, true, false);
				this.rotate(array, m, q, k);
			}

			m = k;
		}

		Highlights.clearMark(3);
    }

	/**
	O(n^2) worst case in-place stable sort
	Makes fewer moves than binary insertion in the worst case but can make 2x more comparisons
	Used for merging small lists and sorting lists with very few unique values in-place
	@param [start, end) is the sorting range
	*/
	protected void inPlaceMergeSort2(int[] array, int start, int end) {
		int length = end - start, j;

		for(int i = 1; i < length; i *= 2) {
			for(j = start; j + 2*i < end; j += 2*i)
				this.inPlaceMerge2(array, j, j+i, j+2*i);

			if(j + i < end)
				this.inPlaceMerge2(array, j, j+i, end);
		}
	}

	private void mergeWithBuf(int[] array, int a, int m, int b, int l) {
		int i = a, j = m, k = a-l;

		while(i < m && j < b) {
			if(Reads.compareValues(array[i], array[j]) <= 0)
				this.kotaSwap(array, k++, i++, this.ext);
			else
				this.kotaSwap(array, k++, j++, this.ext);
		}
		while(j < b)
			this.kotaSwap(array, k++, j++, this.ext);

		this.shift(array, k, i, m, false, this.ext);
	}

	private void dualMerge(int[] array, int a, int m, int b, int l) {
		if(b-m <= l) {
			this.mergeWithBuf(array, a, m, b, l);
		}
		else {
			int i = a, j = m, k = a-l;

			while(k < i && i < m) {
				if(Reads.compareValues(array[i], array[j]) <= 0)
					this.kotaSwap(array, k++, i++, this.ext);
				else
					this.kotaSwap(array, k++, j++, this.ext);
			}

			if(k < i)
				this.shift(array, j-l, j, b, false, this.ext);
			else {
				int i2 = m-1, j2 = b-1; k = (m-1)+(b-j);

				while(i2 >= i && j2 >= j) {
					if(Reads.compareValues(array[i2], array[j2]) == 1)
						this.kotaSwap(array, k--, i2--, this.ext);
					else
						this.kotaSwap(array, k--, j2--, this.ext);
				}
				while(j2 >= j)
					this.kotaSwap(array, k--, j2--, this.ext);
			}
		}
	}

	private void dualMergeBW(int[] array, int a, int m, int b, int l) {
		int i = m-1, j = b-1, k = b-1+l;

		while(k > j && j >= m) {
			if(Reads.compareValues(array[i], array[j]) == 1)
				this.kotaSwap(array, k--, i--, this.ext);
			else
				this.kotaSwap(array, k--, j--, this.ext);
		}

		if(j < m)
			this.shift(array, a, i+1, i+1+l, true, this.ext);
		else {
			int i2 = a, j2 = m;
			i++; j++; k = m-(i-a);

			while(i2 < i && j2 < j) {
				if(Reads.compareValues(array[i2], array[j2]) <= 0)
					this.kotaSwap(array, k++, i2++, this.ext);
				else
					this.kotaSwap(array, k++, j2++, this.ext);
			}
			while(i2 < i)
				this.kotaSwap(array, k++, i2++, this.ext);
		}
	}

	private void mergeWithBufStatic(int[] array, int a, int m, int b, int p, boolean bw) {
		if(m-a < 1 || b-m < 1)
			return;

		int i, j, k, q;
		if(bw) {
			i = (b-m)-1; j = m-1; k = b-1;
			while(i >= 0 && j >= a) {
				if(Reads.compareValues(array[j], array[p+i]) >= 0) {
					q = this.binarySearch(array, a, j+1, array[p+i], true);
					while(j >= q) Writes.swap(array, k--, j--, 1, true, false);
				}
				Writes.swap(array, k--, p+(i--), 1, true, false);
			}
			while(i >= 0) {
				Writes.swap(array, k--, p+(i--), 1, true, false);
			}
		}
		else {
			i = 0; j = m; k = a;
			while(i < m-a && j < b) {
				if(Reads.compareValues(array[j], array[p+i]) == -1) {
					q = this.binarySearch(array, j, b, array[p+i], true);
					while(j < q) Writes.swap(array, k++, j++, 1, true, false);
				}
				Writes.swap(array, k++, p+(i++), 1, true, false);
			}
			while(i < m-a) {
				Writes.swap(array, k++, p+(i++), 1, true, false);
			}
		}
	}

	private void mergeExtBuf(int[] array, int a, int b, boolean bw) {
		int i, j, k, m;
		if(bw) {
			i = this.bufLen-1; j = (b-1)-this.bufLen; k = b-1;
			Highlights.markArray(2, a+i);
			while(i >= 0 && j >= a) {
				if(Reads.compareValues(array[j], this.cache[i]) >= 0) {
					m = this.binarySearch(array, a, j+1, this.cache[i], true);
					while(j >= m) Writes.write(array, k--, array[j--], 1, true, false);
				}
				Writes.write(array, k--, this.cache[i--], 1, true, false);
				if(i > 0) Highlights.markArray(2, a+i);
			}
			while(i >= 0) {
				Writes.write(array, k--, this.cache[i--], 1, true, false);
				if(i > 0) Highlights.markArray(2, a+i);
			}
		}
		else {
			i = 0; j = a+this.bufLen; k = a;
			Highlights.markArray(2, a+i);
			while(i < this.bufLen && j < b) {
				if(Reads.compareValues(array[j], this.cache[i]) == -1) {
					m = this.binarySearch(array, j, b, this.cache[i], true);
					while(j < m) Writes.write(array, k++, array[j++], 1, true, false);
				}
				Writes.write(array, k++, this.cache[i++], 1, true, false);
				Highlights.markArray(2, a+i);
			}
			while(i < this.bufLen) {
				Writes.write(array, k++, this.cache[i++], 1, true, false);
				Highlights.markArray(2, a+i);
			}
		}
	}

	private boolean kotaIterator(int[] array, int start, int end, boolean auxTag) {
		int i = 1, j, effStart = start+this.bufLen, length = end-effStart;

		if(!this.ext) {
			while(i < 16) {
				for(j = effStart; j + 2*i < end; j += 2*i)
					this.inPlaceMerge2(array, j, j+i, j+2*i);

				if(j + i < end)
					this.inPlaceMerge2(array, j, j+i, end);

				i *= 2;
			}
		}

		while(i <= this.bufLen) {
			int l = i;

			for(j = effStart; j + 2*i < end; j += 2*i)
				this.mergeWithBuf(array, j, j+i, j+2*i, l);

			if(j + i < end)
				this.mergeWithBuf(array, j, j+i, end, l);
			else
				this.shift(array, j-l, j, end, false, this.ext);

			i *= 2;

			for(j = effStart-l; j + 2*i < end-l; j += 2*i);

			if(j + i < end-l)
				this.dualMergeBW(array, j, j+i, end-l, l);
			else
				this.shift(array, j, end-l, end, true, this.ext);

			for(j -= 2*i; j >= effStart-l; j -= 2*i)
				this.dualMergeBW(array, j, j+i, j+2*i, l);

			i *= 2;

			if(this.ext && this.effMem < Math.min(i, this.bufLen)) {
				Highlights.clearMark(2);
				Writes.arraycopy(this.cache, 0, array, effStart-this.effMem, this.effMem, 1, true, false);
				this.ext = false;
			}
		}

		while(i < length) {
			for(j = effStart; j + 2*i < end; j += 2*i)
				this.blockMerge(array, j, j+i, j+2*i, auxTag);

			if(j + i < end)
				this.blockMerge(array, j, j+i, end, auxTag);
			else
				this.shift(array, j-this.bufLen, j, end, false, this.ext);

			i *= 2;
			if(i >= length) return true;

			for(j = start; j + 2*i < end-this.bufLen; j += 2*i);

			if(j + i < end-this.bufLen)
				this.blockMergeBW(array, j, j+i, end-this.bufLen, auxTag);
			else
				this.shift(array, j, end-this.bufLen, end, true, this.ext);

			for(j -= 2*i; j >= start; j -= 2*i)
				this.blockMergeBW(array, j, j+i, j+2*i, auxTag);

			i *= 2;
		}

		return false;
	}

	//@param ignoredValues
	@SuppressWarnings("unused")
	private void kotaSortLimited(int[] array, int start, int end) {
		// TODO for aphitorite: implement
	}

	protected void kotaSort(int[] array, int start, int end) {
		int length = end - start;
		if(length <= 128) {
			this.inPlaceMergeSort2(array, start, end);
			return;
		}

		this.ext = false;
		this.bufPos = start;
		for(this.blockLen = 1; this.blockLen*this.blockLen < length; this.blockLen*=2); //ceiling power of 2 sqrt

		int ideal = this.blockLen*2;
		this.bufLen = this.findKeys(array, start, end, ideal);

		if(this.bufLen < ideal) {
			if(this.bufLen == 1) {
				return;
			}
			else if(this.bufLen <= 16) {
				this.inPlaceMergeSort2(array, start, end);
				return;
			}
			else {
				//phase 2
				System.out.println("phase 2");
				this.inPlaceMergeSort2(array, start, end);
				return;
			}
		}

		ideal = length/this.blockLen;
		this.tagLen = this.findKeys(array, start+this.bufLen, end, ideal);

		if(this.tagLen < ideal) {
			if(this.tagLen <= 16) {
				this.inPlaceMergeSort2(array, start, end);
				return;
			}
			else {
				//phase 2
				System.out.println("phase 2");
				this.inPlaceMergeSort2(array, start, end);
				return;
			}
		}

		int bufStart = start+this.tagLen;
		int effStart = bufStart+this.bufLen;
		int bufEnd = start+this.bufLen;
		this.shift(array, start, bufEnd, effStart, false, false);

		boolean bw = this.kotaIterator(array, bufStart, end, false);

		if(bw) {
			int endStart = end-this.bufLen;
			this.multiSwap(array, start, endStart, this.tagLen, false);
			this.mergeWithBufStatic(array, start, bufStart, endStart, endStart, false);
			this.inPlaceMergeSort2(array, endStart, end);

			int mid = endStart+this.blockLen;
			int pos = this.binarySearch(array, start, endStart, array[mid-1], true);
			this.rotate(array, pos, endStart, mid);
			pos += this.blockLen;

			this.multiSwapBW(array, end-1, pos-1, this.blockLen, false);
			this.mergeWithBufStatic(array, start, pos-this.blockLen, pos, mid, true);
			this.inPlaceMergeSort2(array, mid, end);
			this.inPlaceMergeBW(array, pos, mid, end);
		}
		else {
			this.mergeWithBufStatic(array, bufEnd, effStart, end, start, false);
			this.inPlaceMergeSort2(array, start, bufEnd);

			int mid = start+this.blockLen;
			int pos = this.binarySearch(array, bufEnd, end, array[mid], true);
			this.rotate(array, mid, bufEnd, pos);
			pos -= this.blockLen;

			this.multiSwap(array, start, pos, this.blockLen, false);
			this.mergeWithBufStatic(array, pos, pos+this.blockLen, end, start, false);
			this.inPlaceMergeSort2(array, start, mid);
			this.inPlaceMerge(array, start, mid, pos);
		}
	}

	protected void ectaSort(int[] array, int start, int end) {
		int length = end - start;
		if(length <= 16) {
			this.inPlaceMergeSort2(array, start, end);
			return;
		}

		this.ext = true;
		this.bufPos = start;
		for(this.blockLen = 1; this.blockLen*this.blockLen < length; this.blockLen*=2);
		this.bufLen = this.blockLen*2;
		this.effMem = this.bufLen;
		this.cache = Writes.createExternalArray(this.bufLen);

		int effStart = start+this.bufLen;
		this.inPlaceMergeSort2(array, start, effStart);
		Highlights.clearMark(2);
		Writes.arraycopy(array, start, this.cache, 0, this.bufLen, 1, true, true);

		if(this.bufLen < length/4) {
			this.tagLen = length/this.blockLen;

			this.tags = Writes.createExternalArray(this.tagLen);
			for(int i = 0; i < this.tagLen; i++)
				Writes.write(this.tags, i, i, 0, false, true);
		}

		boolean bw = this.kotaIterator(array, start, end, true);
		this.mergeExtBuf(array, start, end, bw);

		Writes.deleteExternalArray(this.cache);
		Writes.deleteExternalArray(this.tags);
	}

	protected void kotaSortDynamicBuf(int[] array, int start, int end) {
		int length = end - start;
		if(length <= 16) {
			this.inPlaceMergeSort2(array, start, end);
			return;
		}

		this.ext = true;
		this.bufPos = start;
		for(this.blockLen = 1; this.blockLen*this.blockLen < length; this.blockLen*=2);
		this.bufLen = this.blockLen*2;
		this.effMem = this.bufLen;

		if(this.bufLen < length/4) {
			int ideal = length/this.blockLen;
			this.tagLen = this.findKeys(array, start, end, ideal);

			if(this.tagLen < ideal) {
				if(this.tagLen <= 16) {
					this.inPlaceMergeSort2(array, start, end);
					return;
				}
				else {
					//phase 2
					System.out.println("phase 2");
					this.inPlaceMergeSort2(array, start, end);
					return;
				}
			}
		} else this.tagLen = 0;

		this.cache = Writes.createExternalArray(this.bufLen);

		int bufStart = start+this.tagLen;
		int effStart = bufStart+this.bufLen;

		this.inPlaceMergeSort2(array, bufStart, effStart);
		Highlights.clearMark(2);
		Writes.arraycopy(array, bufStart, this.cache, 0, this.bufLen, 1, true, true);

		boolean bw = this.kotaIterator(array, bufStart, end, false);
		this.mergeExtBuf(array, bufStart, end, bw);
		this.inPlaceMerge(array, start, bufStart, end);

		Writes.deleteExternalArray(this.cache);
	}

	protected void kotaSortStaticBuf(int[] array, int start, int end) {
		if(this.CACHE_SIZE < 4) {
			this.kotaSort(array, start, end);
			return;
		}

		int length = end - start;
		if(length <= 16) {
			this.inPlaceMergeSort2(array, start, end);
			return;
		}

		this.ext = true;
		this.bufPos = start;
		for(this.blockLen = 1; this.blockLen*this.blockLen < length; this.blockLen*=2);
		this.bufLen = this.blockLen*2;
		this.effMem = Math.min(CACHE_SIZE, this.bufLen);

		if(this.effMem == this.bufLen) {
			this.kotaSortDynamicBuf(array, start, end);
			return;
		}

		int ideal = this.blockLen*2;
		this.bufLen = this.findKeys(array, start, end, ideal);

		if(this.bufLen < ideal) {
			if(this.bufLen == 1) {
				return;
			}
			else if(this.bufLen <= 16) {
				this.inPlaceMergeSort2(array, start, end);
				return;
			}
			else {
				//phase 2
				System.out.println("phase 2");
				this.inPlaceMergeSort2(array, start, end);
				return;
			}
		}

		if(this.bufLen < length/4) {
			ideal = length/this.blockLen;
			this.tagLen = this.findKeys(array, start+this.bufLen, end, ideal);

			if(this.tagLen < ideal) {
				if(this.tagLen <= 16) {
					this.inPlaceMergeSort2(array, start, end);
					return;
				}
				else {
					//phase 2
					System.out.println("phase 2");
					this.inPlaceMergeSort2(array, start, end);
					return;
				}
			}
		} else this.tagLen = 0;

		int bufStart = start+this.tagLen;
		int effStart = bufStart+this.bufLen;
		int bufEnd = start+this.bufLen;
		this.shift(array, start, bufEnd, effStart, false, false);

		this.cache = Writes.createExternalArray(this.effMem);
		Highlights.clearMark(2);
		Writes.arraycopy(array, effStart-this.effMem, this.cache, 0, this.effMem, 1, true, true);

		boolean bw = this.kotaIterator(array, bufStart, end, false);

		if(bw) {
			int endStart = end-this.bufLen;
			this.multiSwap(array, start, endStart, this.tagLen, false);
			this.mergeWithBufStatic(array, start, bufStart, endStart, endStart, false);
			this.inPlaceMergeSort2(array, endStart, end);

			int mid = endStart+this.blockLen;
			int pos = this.binarySearch(array, start, endStart, array[mid-1], true);
			this.rotate(array, pos, endStart, mid);
			pos += this.blockLen;

			this.multiSwapBW(array, end-1, pos-1, this.blockLen, false);
			this.mergeWithBufStatic(array, start, pos-this.blockLen, pos, mid, true);
			this.inPlaceMergeSort2(array, mid, end);
			this.inPlaceMergeBW(array, pos, mid, end);
		}
		else {
			this.mergeWithBufStatic(array, bufEnd, effStart, end, start, false);
			this.inPlaceMergeSort2(array, start, bufEnd);

			int mid = start+this.blockLen;
			int pos = this.binarySearch(array, bufEnd, end, array[mid], true);
			this.rotate(array, mid, bufEnd, pos);
			pos -= this.blockLen;

			this.multiSwap(array, start, pos, this.blockLen, false);
			this.mergeWithBufStatic(array, pos, pos+this.blockLen, end, start, false);
			this.inPlaceMergeSort2(array, start, mid);
			this.inPlaceMerge(array, start, mid, pos);
		}

		Writes.deleteExternalArray(this.cache);
	}
}
