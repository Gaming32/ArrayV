package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Simplistic Gravity")
public final class SimplisticGravitySort extends Sort {

	public SimplisticGravitySort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	int[] aux;
	int min;
	double sleep;

	private void transferFrom(int[] array, int arrayLength, int index) {
		for (int pointer = 0; pointer < arrayLength && this.aux[pointer] != 0; pointer++) {
			Highlights.markArray(2, index);
			Writes.write(array, index, ++array[index], 0, false, false);
			Writes.write(this.aux, pointer, --this.aux[pointer], this.sleep, true, true);
		}
	}

	private void transferTo(int[] array, int arrayLength, int index) {
		for (int pointer = 0; array[index] > this.min; pointer++) {
			Highlights.markArray(2, index);
			Writes.write(array, index, --array[index], 0, false, false);
			Writes.write(this.aux, pointer, ++this.aux[pointer], this.sleep, true, true);
		}
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.sleep = 10d / length;
		this.min = array[0];
		int max = min;
		for (int mainPointer = 1; mainPointer < length; mainPointer++) {
			Highlights.markArray(1, mainPointer);
			Writes.startLap();
			this.min = Math.min(array[mainPointer], min);
			max = Math.max(array[mainPointer], max);
			Writes.stopLap();
			Delays.sleep(0.5);
		}
		this.aux = Writes.createExternalArray(max - this.min);
		for (int mainPointer = 0; mainPointer < length; mainPointer++) {
			transferTo(array, length, mainPointer);
		}
		for (int mainPointer = length - 1; mainPointer >= 0; mainPointer--) {
			transferFrom(array, this.aux.length, mainPointer);
		}
		Writes.deleteExternalArray(this.aux);
	}
}
