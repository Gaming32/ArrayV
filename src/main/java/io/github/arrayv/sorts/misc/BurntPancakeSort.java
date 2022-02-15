package io.github.arrayv.sorts.misc;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class BurntPancakeSort extends Sort {
  public BurntPancakeSort(ArrayVisualizer arrayVisualizer) {
    super(arrayVisualizer);

    this.setSortListName("Burnt Pancake");
    this.setRunAllSortsName("Burnt Pancake Sorting");
    this.setRunSortName("Burnt Pancake Sort");
    this.setCategory("Miscellaneous Sorts");
    this.setBucketSort(false);
    this.setRadixSort(false);
    this.setUnreasonablySlow(false);
    this.setUnreasonableLimit(0);
    this.setBogoSort(false);
  }

  @Override
  public void runSort(int[] array, int length, int bucketCount) {
    for (int i = length - 1; i > 0; i--) {
      int max = 0;

      for (int j = max + 1; j <= i; j++)
        if (Reads.compareIndices(array, j, max, 0.025, true) >= 0)
          max = j;

      if (max != i) {
        Writes.reversal(array, 0, max, 0.025, true, false);
        Writes.reversal(array, 0, i, 0.025, true, false);
        Writes.reversal(array, 0, i - 1, 0.025, true, false);
        Writes.reversal(array, 0, max - 1, 0.025, true, false);
      }
    }
  }
}
