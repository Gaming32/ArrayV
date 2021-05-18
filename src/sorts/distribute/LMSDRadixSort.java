package sorts.distribute;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
L/MSD Radix Sort Sort 2021 Copyright (C) thatsOven
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

thanks to Tanoshi, that came up with the idea too, and made me make a Java port of the algorithm 
*/

final public class LMSDRadixSort extends Sort {

    public LMSDRadixSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("L/MSD Radix");
        this.setRunAllSortsName("L/MSD Radix Sort");
        this.setRunSortName("Least/Most Significant Digit Radix Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(true);
        this.setRadixSort(true);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int base;

    public ArrayList<Integer>[] radixSort(int[] array, int start, int end, int place) {
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] registers = new ArrayList[this.base];

        for(int i = 0; i < this.base; i++) registers[i] = new ArrayList<>();

        for (int i = start; i < end; i++) {
            Highlights.markArray(1, i);
            int digit = Reads.getDigit(array[i], place, this.base);
            Writes.arrayListAdd(registers[digit], array[i]);

            Writes.mockWrite(end - start, digit, array[i], 1);
        }

        Highlights.clearAllMarks();

        int counter = start;
        for (int i = 0; i < this.base; i++) {
            for (int j = 0; j < registers[i].size(); j++) {
                Writes.write(array, counter++, registers[i].get(j), 1, true, false);
            }
        }

        return registers;
    }
    

    public void lmsdRadixSort(int[] array, int mina, int maxa, int place, int maxPlace) {
        if (maxPlace < place || maxa - mina <= 1) return;

        ArrayList<Integer>[] registers = this.radixSort(array, mina, maxa, place);
        Writes.deleteExternalArray(registers);

        if (place != maxPlace) {
            registers = this.radixSort(array, mina, maxa, maxPlace);

            int sum = 0;

            for (int i = 0; i < registers.length; i++) {
                this.lmsdRadixSort(array, sum + mina, sum + mina + registers[i].size(), place + 1, maxPlace - 1);

                sum += registers[i].size();
                Writes.arrayListClear(registers[i]);
                Writes.changeAllocAmount(-registers[i].size());
            }

            Writes.deleteExternalArray(registers);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) throws Exception {
        this.setRunAllSortsName("Least/Most Significant Digit Radix Sort, Base " + bucketCount);
        this.base = bucketCount;

        int highestpower = Reads.analyzeMaxLog(array, currentLength, bucketCount, 0.5, true);

        this.lmsdRadixSort(array, 0, currentLength, 0, highestpower);

    }
}