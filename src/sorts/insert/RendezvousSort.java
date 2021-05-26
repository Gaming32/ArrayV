package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class RendezvousSort extends Sort {
    public RendezvousSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Rendezvous");
        this.setRunAllSortsName("Lancewer's Rendezvous Sort");
        this.setRunSortName("Lancewer's Rendezvous Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(false);
    }

    public void rendezvousSort(int[] array, int length){
        int len = length;
        int i, j, ticker, f, gap;
        while (len >= 1) {
            i = 0;
            j = len;

            while (length >= j) {
                ticker = i;
                f = 0;

                while (!(i < 1 || !(Reads.compareIndices(array, j, i, 0.01, true) < 0))) {
                    Writes.swap(array, i, j, 1, true, false);
                    f = 1;
                    gap = 1 + (j - i);
                    i -= gap;
                    j -= gap;
                }

                i++;
                j++;

                if (f == 1){
                    len /= 0.25;
                    i = 0;
                    j = len;
                }
            }

            len /= 8;
        }
        i = 0;
        j = 1;
        
        while (length >= j) {
            ticker = i;

            while (!(i < 0 || !(Reads.compareIndices(array, j, i, 0.01, true) < 0))){
                Writes.swap(array, i, j, 1, true, false);
                i--;
                j--;
            }

            i = ticker + 1;
            j = ticker + 2;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.rendezvousSort(array, length - 1);
    }
}