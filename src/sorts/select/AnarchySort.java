package sorts.select;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class AnarchySort extends Sort {

    public AnarchySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Anarchy");
        setRunAllSortsName("Anarchy Sort (By Lancewer & McDude_73)");
        setRunSortName("Anarchy Sort");
        setCategory("Selection Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(1024);
        setBogoSort(false);

    }

    private boolean containsValue(ArrayList<Integer> list, int value) {
        for (int at = 0; at < list.size(); at++) {
            this.Delays.sleep(0.001D);
            this.Highlights.markArray(1, at);

            this.Writes.startLap();
            boolean comp = (((Integer) list.get(at)).intValue() == value);
            this.Writes.stopLap();

            if (comp)
                return true;
        }
        return false;
    }

    private void swapper(int[] normalArray, int[] auxiliaryArray, int pos1, int pos2) {
        this.Writes.swap(auxiliaryArray, pos1, pos2, 1.75D, true, true);
        this.Writes.swap(normalArray, pos1, pos2, 1.75D, true, false);
    }

    private void convert(int[] array, int[] aux, int length) {
        int i = 0;
        int j = 0;

        while (i < length) {
            if (this.Reads.compareIndices(array, j, i, 0.001D, true) < 0) {
                j = i;
            }
            i++;
        }

        this.Writes.swap(array, 0, j, 1.0D, true, true);

        for (int init = 0; init < length; init++) {
            this.Writes.write(aux, init, array[init], 0.001D, true, true);
        }

        ArrayList<Integer> t2 = new ArrayList<>();
        int m = 0;

        while (m < length) {
            i = 0;
            j = 0;
            while (i < length) {
                if (this.Reads.compareIndices(array, i, j, 0.001D, true) <= 0 && !containsValue(t2, i)) {
                    j = i;
                }
                i++;
            }
            this.Writes.changeAuxWrites(1);
            t2.add(Integer.valueOf(j));
            this.Highlights.markArray(1, j);
            this.Delays.sleep(1.0D);

            this.Writes.write(array, j, m, 1.0D, true, false);
            m++;
        }
    }

    private void sortMainAndAux(int[] array, int[] aux, int length) {
        for (int i = 0; i < length; i++) {
            while (array[i] != i) {
                swapper(array, aux, i, array[i]);
                this.Highlights.markArray(3, array[i]);
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] aux =Writes.createExternalArray(length);

        convert(array, aux, length);
        sortMainAndAux(array, aux, length);

        for (int i = 0; i < length; i++)
            this.Writes.write(array, i, aux[i], 1.0D, true, false);
        Writes.deleteExternalArray(aux);

    }

}
