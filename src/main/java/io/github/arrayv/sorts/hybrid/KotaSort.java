package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.KotaSorting;

public final class KotaSort extends KotaSorting {
    public KotaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Kota");
        //this.setRunAllID("Grail Sort (Block Merge Sort)");
        //this.setRunAllSortsName("Grail Sort [Block Merge Sort]");
        this.setRunAllSortsName("Kotasort");
        this.setRunSortName("Kotasort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        if(Delays.getSleepRatio() == 55.1) {
            Delays.setSleepRatio(1);
            //this.kotaSortDynamicBuf(array, 0, length);
        }
        else {
            this.kotaSort(array, 0, length);
        }
    }
}
