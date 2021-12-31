package sorts.category;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class MySort extends Sort {
    public MySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("My");
        this.setRunAllSortsName("My Sort");
        this.setRunSortName("Mysort");
        this.setCategory("Category Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {

    }
}
