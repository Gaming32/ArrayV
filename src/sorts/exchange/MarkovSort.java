package sorts.exchange;

import java.util.Random;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * Markov Sort, which is in a sense a gnome bogo sort.
 * 
 * @author invented by Blasterfreund
 * @author implemented in Java by Sam Walko (Anonymous0726)
 */
final public class MarkovSort extends Sort {
    public MarkovSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Markov");
        this.setRunAllSortsName("Markov Sort");
        this.setRunSortName("Markov sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	Random random = new Random();
    	
    	for(int s = 0; s < length-1; s++) {
    		int i = s+1;
    		
    		boolean isSorted = Reads.compareIndices(array, i-1, i, 0.25, true) <= 0;
    		
    		while(!isSorted) {
    			Highlights.clearAllMarks();
    			
    			if(i == 0) {
    				Writes.swap(array, i, i+1, 0.25, true, false);
    				i++;
    			} else if(i >= s) {
    				Writes.swap(array, i, i-1, 0.25, true, false);
    				i--;
    			} else {
    				int c = 1;
    				if(random.nextBoolean())
    					c = -1;
    				Writes.swap(array, i, i+c, 0.25, true, false);
    				i += c;
    			}
    			
    			if(i == 0) {
    				isSorted = Reads.compareIndices(array, i-1, i, 0.25, true) <= 0;
    			} else if(i >= s) {
    				isSorted = Reads.compareIndices(array, i-1, i, 0.25, true) <= 0;
    			} else {
    				boolean leftSort = Reads.compareIndices(array, i-1, i, 0.25, true) <= 0;
    				boolean rightSort = Reads.compareIndices(array, i, i+1, 0.25, true) <= 0;
    				
    				isSorted = leftSort && rightSort;
    			}
    		}
    		Highlights.clearAllMarks();
    	}
    }
}
