package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author _fluffyy
 * @author thatsOven
 * @author Gaming32
 */
public class OOPBufferedStoogeSort_backup extends Sort {
    int[] aux;

    public OOPBufferedStoogeSort_backup(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("OOP Buffered Stooge (old)");
        this.setRunAllSortsName("OOP Buffered Stooge Sort");
        this.setRunSortName("OOP Buffered Stoogesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int compare(int[] arr, int x, int y) {
        return Reads.compareIndices(arr, x, y, 0, true);
    }

	protected void mergeOOP(int[] arr, int start, int mid, int end) {
		Writes.arraycopy(arr, mid, aux, 0, end - mid, 1, true, true);
		int auxPtr = end - mid - 1;
		int left = mid - 1;
		int right = end - 1;
		Highlights.markArray(2, left);
		while (right > left && left >= start) {
			if (Reads.compareValues(arr[left], aux[auxPtr]) > 0) {
				Writes.write(arr, right--, arr[left--], 1, true, false);
				if (left >= 0) Highlights.markArray(2, left);
			} else {
				Writes.write(arr, right--, aux[auxPtr--], 1, true, false);
			}
		}
		while (right > left) {
			Writes.write(arr, right--, aux[auxPtr--], 1, true, false);
		}
	}

    public void wrapper(int[] arr, int start, int stop) {
        if (stop - start > 1) {
            if (stop - start == 2 && compare(arr, start, stop - 1) == 1) {
                Writes.swap(arr, start, stop - 1, 1, true, false);
            }
            if (stop - start > 2) {
                int third = (int) Math.ceil((stop - start) / 3.0) + start;
                int twoThird = (int) Math.ceil((stop - start) / 3.0 * 2) + start;
                if (twoThird - third < third) {
                    twoThird--;
                }
                if ((stop - start - 2) % 3 == 0) {
                    twoThird--;
                }
                wrapper(arr, third, twoThird);
                wrapper(arr, twoThird, stop);
                int left = third;
                int right = twoThird;
                int bufferStart = start;
                while (left < twoThird && right < stop) {
                    if (compare(arr, left, right) == 1) {
                        Writes.swap(arr, bufferStart, right, 1, true, false);
                        right++;
                    } else {
                        Writes.swap(arr, bufferStart, left, 1, true, false);
                        left++;
                    }
                    bufferStart++;
                }
                while (right < stop) {
                    Writes.swap(arr, bufferStart, right, 1, true, false);
                    right++;
                    bufferStart++;
                }
                wrapper(arr, twoThird, stop);
                if (stop - start > 8) {
                    if (stop > twoThird && twoThird > start) {
						mergeOOP(arr, start, twoThird, stop);
                    }
                } else {
					int temp;
                    left = twoThird - 1;
                    right = stop - 1;
                    while (right > left && left >= start) {
                        if (compare(arr, left, right) == 1) {
							temp = arr[left];
							Writes.arraycopy(arr, left + 1, arr, left, right - left, 0.5, true, false);
                            Writes.write(arr, right, temp, 0.5, true, false);
                            left--;
                        }
                        right--;
                    }
                }
				Highlights.clearMark(2);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        aux = Writes.createExternalArray((sortLength + 2) / 3);
        wrapper(array, 0, sortLength);
        Writes.deleteExternalArray(aux);
    }
}
