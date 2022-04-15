package io.github.arrayv.sorts.misc;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * <b>IDeserve <br>
 * <a href="<a class="vglnk" href="https://www.youtube.com/c/IDeserve" rel="nofollow"><span>https</span><span>://</span><span>www</span><span>.</span><span>youtube</span><span>.</span><span>com</span><span>/</span><span>c</span><span>/</span><span>IDeserve</span></a>"><a class="vglnk" href="https://www.youtube.com/c/IDeserve" rel="nofollow"><span>https</span><span>://</span><span>www</span><span>.</span><span>youtube</span><span>.</span><span>com</span><span>/</span><span>c</span><span>/</span><span>IDeserve</span></a></a>
 * Given an array, sort the array using Pancake sort.
 *
 * @author Saurabh
 * https://www.ideserve.co.in/learn/pancake-sorting
 */

@SortMeta(
    name = "Pancake",
    runName = "Pancake Sort"
)
public final class PancakeSort extends Sort {
    public PancakeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private boolean sorted(int[] array, int length) {
        for(int i = 0; i < length; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.025);

            if(Reads.compareValues(array[i], array[i + 1]) > 0) return false;
        }
        return true;
    }

    private int findMax(int[] arr, int end) {
        int index = 0, max = Integer.MIN_VALUE;
        for (int i = 0; i <= end; i++) {
            Highlights.markArray(1, i);

            if (Reads.compareValues(arr[i], max) == 1) {
                max = arr[i];
                index = i;
                Highlights.markArray(2, i);
            }

            Delays.sleep(0.025);
            Highlights.clearMark(1);
        }
        return index;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = length - 1; i >= 0; i--) {
            if(!this.sorted(array, i)) {
                int index = this.findMax(array, i);

                if(index == 0) {
                    Writes.reversal(array, 0, i, 0.05, true, false);
                }
                else if(index != i) {
                    Writes.reversal(array, 0, index, 0.05, true, false);
                    Writes.reversal(array, 0, i, 0.05, true, false);
                }
            }
            else break;
        }
    }
}
