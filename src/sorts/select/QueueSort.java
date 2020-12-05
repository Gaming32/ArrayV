package sorts.select;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.LinkedList;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE").
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS
 * LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE.
 * TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED HERE IN
 * CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 */

// Code refactored from the Python implementation found here: https://en.wikipedia.org/wiki/Pigeonhole_sort

final public class QueueSort extends Sort {
    public QueueSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Queue");
        this.setRunAllSortsName("Queue Sort");
        this.setRunSortName("Queuesort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        PriorityQueue<LinkedList<Integer>> heap = new PriorityQueue<>(sortLength / 2, new Comparator<LinkedList<Integer>>(){
            @Override
            public int compare(LinkedList<Integer> q1, LinkedList<Integer> q2) {
                return q1.peek() - q2.peek();
            }
        });

        int currentValue = array[0];
        LinkedList<Integer> currentQueue = new LinkedList<>();
        currentQueue.add(currentValue);
        heap.add(currentQueue);
        Writes.changeAllocAmount(1);

        int qCount = 1;

        for (int i = 1; i < sortLength; i++) {
            Highlights.markArray(1, i);
            Highlights.markArray(2, qCount);
            if (Reads.compareValues(array[i], array[i - 1]) >= 0) {
                currentQueue.add(array[i]);
            }
            else {
                currentValue = array[i];
                currentQueue = new LinkedList<>();
                currentQueue.add(array[i]);
                heap.add(currentQueue);
                qCount++;
            }
            Writes.changeAuxWrites(1);
            Writes.changeAllocAmount(1);
            Delays.sleep(1);
        }

        int j = 0;
        while (qCount > 0) {
            Highlights.markArray(2, qCount);
            LinkedList<Integer> first = heap.poll();
            Writes.write(array, j++, first.pop(), 1, true, false);
            Writes.changeAllocAmount(-1);
            if (first.size() > 0) {
                heap.add(first);
            }
            else {
                qCount--;
            }
        }
    }
}