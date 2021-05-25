package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class CompleteGraphSort extends Sort {
    public CompleteGraphSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Complete Graph");
        this.setRunAllSortsName("Complete Graph Sorting Network");
        this.setRunSortName("Complete Graph Sorting Network");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private void compSwap(int[] array, int a, int b) {
		if(Reads.compareIndices(array, a, b, 0.0125, true) > 0)
			Writes.swap(array, a, b, 0.0875, true, false);
	}
	
	private void split(int[] array, int a, int m, int b) {
		if(b-a < 2) return;
		
		int c = 0, len1 = (b-a)/2;
		boolean odd = (b-a)%2 == 1;
		
		if(odd) {
			if(m-a > b-m) c = a++;
			else          c = --b;
		}
		for(int s = 0; s < len1; s++) {
			int i = a;
			
			for(int j = s; j < len1; j++)
				this.compSwap(array, i++, m+j);
			
			for(int j = 0; j < s; j++)
				this.compSwap(array, i++, m+j);
		}
		if(odd) {
			if(c < m) 
				for(int j = 0; j < len1; j++)
					this.compSwap(array, c, m+j);
			else 
				for(int j = 0; j < len1; j++)
					this.compSwap(array, a+j, c);
		}
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
		int n = currentLength;
		int d = 2, end = 1 << (int)(Math.log(n-1)/Math.log(2) + 1);
	
		while(d <= end) {
			int i = 0, dec = 0;
			
			while(i < n) {
				int j = i;
				dec += n;
				
				while(dec >= d) {
					dec -= d;
					j++;
				}
				int k = j;
				dec += n;
				
				while(dec >= d) {
					dec -= d;
					k++;
				}
				this.split(array, i, j, k);
				i = k;
			}
			d *= 2;
		}
    }
}