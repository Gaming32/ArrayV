package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class OutOfPlaceWeaveMergeSort extends Sort {

    public OutOfPlaceWeaveMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Out-of-Place Weave Merge");
        this.setRunAllSortsName("Out-of-Place Weave Merge (by Control)");
        this.setRunSortName("Out-of-Place Weave Merge");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    
    //Implemented by Control
    //Looks like weave merge, but it really is a normal merge.
    //Why? you might ask...


    //stats:
    //best case:    θ(n*log(n))
    //average case: θ(n*log(n))
    //worst case:   θ(n*log(n))
    //extra memory: θ(n)
    //Stable:       Yes


    private void weave(int[] array, int[] aux, int start, int end) {  

        for(int i = 0; i<=end-start; i+=2){
            Writes.write(array, start+i, aux[start+i/2], 0.25, true, false);
        }

        for(int i = 1; i<=end-start; i+=2){
            Writes.write(array, start+i, aux[start/2+i/2+end/2+1], 0.25, true, false);
        }

    }
    
    private void merge(int[] array, int[] aux, int start, int end) {
        
        int i = start;
        int j = start+1;
        int pointer = start;

        while(i <= end && j <= end){

            if(Reads.compareValues(array[i],array[j])>0){
                Writes.write(aux, pointer, array[j], 0.05, true, true);
                j += 2;
            }else{
                Writes.write(aux, pointer, array[i], 0.05, true, true);
                i += 2;
            }
            pointer++;
        }

        int k;

        if(i < j)  {k = i;}
        else       {k = j;}

        while(k<=end){
            Writes.write(aux, pointer, array[k], 0.05, true, true);
            k += 2;
            pointer++;
        }
    }


    public void controller(int[] array, int[] aux, int start, int end){
        if(end-start>=2){
            controller(array, aux, start, (end+start-1)/2);
            controller(array, aux, (end+start+1)/2, end);
        }
        weave(array, aux, start, end);
        merge(array, aux, start, end); 
    }
        

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        
        
        int [] swaparray = Writes.createExternalArray(currentLength);
        for(int i = 0; i<currentLength; i++){
            Writes.write(swaparray, i, array[i], 0.05, true, false);
        }

        controller(array, swaparray, 0, currentLength-1);

        for(int i = 0; i<currentLength; i++){
            Writes.write(array, i, swaparray[i], 0.05, true, true);
        }

        Delays.sleep(0.5);
        Writes.deleteExternalArray(swaparray);
    }
}

