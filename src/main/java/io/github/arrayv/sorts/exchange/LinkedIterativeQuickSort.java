package io.github.arrayv.sorts.exchange;

import java.util.LinkedList;
import java.util.Queue;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author aphitorite
 *
 */
public final class LinkedIterativeQuickSort extends Sort {

    public LinkedIterativeQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Linked Iterative Quick");
        setRunAllSortsName("Linked Iterative Quick Sort, Left/Right Pointers");
        setRunSortName("Linked Iterative Left/Right Quicksort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);

    }

    private class Task {
        int p;
        int r;

        public Task(int p, int r) {
            this.p = p;
            this.r = r;
        }
    }

    private void quickSort(int[] a, int p, int r) {
        Queue<Task> tasks = new LinkedList<>();
        tasks.add(new Task(p, r));

        while (!tasks.isEmpty()) {
            Task task = tasks.remove();

            int i = task.p;
            int j = task.r;
            int pivot = task.p + (task.r - task.p) / 2;
            int x = a[pivot];

            this.Highlights.markArray(3, pivot);

            while (i <= j) {
                while (this.Reads.compareValues(a[i], x) == -1) {
                    i++;
                    this.Highlights.markArray(1, i);
                    this.Delays.sleep(0.5D);
                }
                while (this.Reads.compareValues(a[j], x) == 1) {
                    j--;
                    this.Highlights.markArray(2, j);
                    this.Delays.sleep(0.5D);
                }

                if (i <= j) {

                    if (i == pivot) {
                        this.Highlights.markArray(3, j);
                    }
                    if (j == pivot) {
                        this.Highlights.markArray(3, i);
                    }

                    this.Writes.swap(a, i, j, 1.0D, true, false);

                    i++;
                    j--;
                }
            }

            if (task.p < j) {
                tasks.add(new Task(task.p, j));
            }
            if (i < task.r) {
                tasks.add(new Task(i, task.r));
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength - 1);

    }

}
