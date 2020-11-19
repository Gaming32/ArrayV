package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class IterativeBoseNelsonSort extends Sort {
    public IterativeBoseNelsonSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Iter. Bose-Nelson");
        this.setRunAllSortsName("Iterative Bose-Nelson Sorting Network");
        this.setRunSortName("Iterative Bose-Nelson Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private int end;
	
	private void compSwap(int[] array, int a, int b) {
		if(b >= this.end) return;
		
		if(Reads.compareIndices(array, a, b, 0.5, true) == 1)
			Writes.swap(array, a, b, 1, false, false);
	}
	
	private void rangeComp(int[] array, int a, int b, int offset) {
		int half = (b-a)/2, m = a+half; 
		a += offset;
		
		for(int i = 0; i < half - offset; i++)
			if((i & ~offset) == i) this.compSwap(array, a+i, m+i);
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
		this.end = currentLength;
		currentLength = 1 << (int)(Math.ceil(Math.log(currentLength)/Math.log(2)));
		
		for(int k = 2; k <= currentLength; k*=2)
			for(int j = 0; j < k/2; j++)
				for(int i = 0; i+j < this.end; i+=k)
					this.rangeComp(array, i, i+k, j);
    }
}