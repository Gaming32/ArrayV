package io.github.arrayv.sorts.misc;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Burnt Pancake")
public final class BurntPancakeSort extends Sort {
  public BurntPancakeSort(ArrayVisualizer arrayVisualizer) {
    super(arrayVisualizer);
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
