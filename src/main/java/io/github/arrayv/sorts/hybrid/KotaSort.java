package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.KotaSorting;

@SortMeta(name = "Kota")
public final class KotaSort extends KotaSorting {
    public KotaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        if (Delays.getSleepRatio() == 55.1) {
            Delays.setSleepRatio(1);
            // this.kotaSortDynamicBuf(array, 0, length);
        } else {
            this.kotaSort(array, 0, length);
        }
    }
}
