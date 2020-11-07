package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

final public class ExchangeBogoSort extends BogoSorting {
    public ExchangeBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Exchange Bogo");
        this.setRunAllSortsName("Exchange Bogo Sort");
        this.setRunSortName("Exchange Bogosort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true); //Comparisons ARE used to swap elements
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        while(!bogoIsSorted(array, currentLen)) {
            int index1 = (int) (Math.random() * currentLen),
                index2 = (int) (Math.random() * currentLen);
            
            Highlights.markArray(1, index1);
            Highlights.markArray(2, index2);
            
            if(index1 < index2) {
                if(Reads.compareValues(array[index1], array[index2]) == 1){
                    Writes.swap(array, index1, index2, 1, true, false);
                }
            }
            else {
                if(Reads.compareValues(array[index1], array[index2]) == -1){
                    Writes.swap(array, index1, index2, 1, true, false);
                }
            }
        }
    }
}