package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Yuri-chan2007
 *
 */
public final class ForcedStableHeapSort extends Sort {

    public ForcedStableHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Forced Stable Heap");
        this.setRunAllSortsName("Forced Stable Heap Sort");
        this.setRunSortName("Forced Stable Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private boolean stableComp(int[] array, int[] key, int a, int b) {
        int comp = Reads.compareIndices(array, a, b, 0.0, true);
        
        return comp > 0 || (comp == 0 && Reads.compareOriginalIndices(key, a, b, 0.0, false) > 0);
    }
    
    private void stableSwap(int[] array, int[] key, int a, int b) {
        Writes.swap(array, a, b, 0.0, true, false);
        Writes.swap(key,   a, b, 1.0, false, true);
    }
    
    private void siftDown(int[] array, int[] key, int root, int dist, int start) {
        while (root <= dist / 2) {
            int leaf = 2 * root;
            if (leaf < dist && this.stableComp(array, key, start + leaf, start + leaf - 1)) {
                leaf++;
            }
            if(this.stableComp(array, key, start + leaf - 1, start + root - 1)) {
                this.stableSwap(array, key, start + leaf - 1, start + root - 1);
                root = leaf;
            }
            else break;
        }
    }
    
    protected void heapify(int[] array, int[] key, int low, int high) {
        int length = high - low;
        for (int i = length / 2; i >= 1; i--) {
            siftDown(array, key, i, length, low);
        }
    }
    
    protected void heapSort(int[] array, int[] key, int start, int length) {
        heapify(array, key, start, length);
        for (int i = length - start; i > 1; i--) {
            this.stableSwap(array, key, start, start + i - 1);
            siftDown(array, key, 1, i - 1, start);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int[] key = Writes.createExternalArray(sortLength);
        for(int i = 0; i < sortLength; i++)
            Writes.write(key, i, i, 0.5, true, true);
        heapSort(array, key, 0, sortLength);
        Writes.deleteExternalArray(key);

    }

}
