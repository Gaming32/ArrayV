package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class SimplisticGravitySort extends Sort {

    public SimplisticGravitySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Simplistic Gravity");
        this.setRunAllSortsName("Simplistic Gravity Sort (By McDude_73)");
        this.setRunSortName("Simplistic Gravity Sort");
        this.setCategory("Distributive Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
	}

    int[] aux;
    int min;
	double sleep;

    private void transferFrom(int[] array, int arrayLength, int index) {
    	for(int pointer = 0;pointer < arrayLength && this.aux[pointer] != 0;pointer++) {
    		Highlights.markArray(2, index);
    		Writes.write(array, index, ++array[index], 0, false, false);
    		Writes.write(this.aux, pointer, --this.aux[pointer], this.sleep, true, true);
    	}
    }

    private void transferTo(int[] array, int arrayLength, int index) {
    	for(int pointer = 0;array[index] > this.min;pointer++) {
    		Highlights.markArray(2, index);
    		Writes.write(array, index, --array[index], 0, false, false);
    		Writes.write(this.aux, pointer, ++this.aux[pointer], this.sleep, true, true);
    	}
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.sleep = 10d/length;
    	this.min = array[0];
		int max  = min;
		for(int mainPointer = 1;mainPointer < length;mainPointer++) {
			Highlights.markArray(1, mainPointer);
			Writes.startLap();
			this.min = Math.min(array[mainPointer], min);
			max = Math.max(array[mainPointer], max);
			Writes.stopLap();
			Delays.sleep(0.5);
		}
    	this.aux = Writes.createExternalArray(max-this.min);
    	for(int mainPointer = 0;mainPointer < length;mainPointer++) {
    		transferTo(array, length, mainPointer);
    	}
    	for(int mainPointer = length - 1;mainPointer >= 0;mainPointer--) {
    		transferFrom(array, this.aux.length, mainPointer);
    	}
    	Writes.deleteExternalArray(this.aux);
    }
}
