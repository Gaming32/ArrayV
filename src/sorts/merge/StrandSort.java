package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import java.util.ArrayList;

/*
 * 
MIT License

Copyright (c) 2020 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */
 
public final class StrandSort extends Sort {
    public StrandSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Strand");
        this.setRunAllSortsName("Strand Sort");
        this.setRunSortName("Strandsort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    final class Node {
    	int val;
    	Node next;
    	
    	public Node(int val, Node next) {
    		this.val  = val;
    		this.next = next;
    	}
    }
    
    final class LinkedList {
    	int size;
    	Node head;
    	
    	public LinkedList() {
    		this.size = 0;
    		this.head = new Node(0, null);
    	}
    	
    	void addNext(int e, Node n) {
    		Node next = new Node(e, n.next);
    		n.next = next;
    		
    		this.size++;
    	}
    	
    	int removeNext(Node n) {
    		Node next = n.next;
    		n.next = next.next;
    		
    		this.size--;
    		
    		return next.val;
    	}
    }
    
    private LinkedList list;
    private ArrayList<Integer> subList;
    
    private void listAddNext(int e, Node n) {
		Writes.startLap();
		list.addNext(e, n);
		Writes.stopLap();
		
		Writes.changeAuxWrites(1);
		Writes.changeAllocAmount(1);
    }
    
    private int listRemoveNext(Node n) {
    	int e;
		Writes.startLap();
		e = list.removeNext(n);
		Writes.stopLap();
		
		Writes.changeAuxWrites(1);
		Writes.changeAllocAmount(-1);
		return e;
    }
    
    //reverses equal items order
    private void mergeTo(int[] array, int a, int m, int b) {
    	int i = 0;
    	
    	while(i < subList.size() && m < b) {
        	if(Reads.compareValues(subList.get(i), array[m]) < 0)
        		Writes.write(array, a++, subList.get(i++), 0.5, true, false);
        	
        	else Writes.write(array, a++, array[m++], 0.5, true, false);
    	}
    	
    	while(i < subList.size())
    		Writes.write(array, a++, subList.get(i++), 0.5, true, false);
    	
    	Writes.arrayListClear(subList);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	list = new LinkedList();
    	subList = new ArrayList<Integer>();
    	
    	Node j = list.head;
    	for(int i = 0; i < length; i++) {
    		Highlights.markArray(1, i);
    		listAddNext(array[i], j);
    		j = j.next;
    		Delays.sleep(1);
    	}
    	
    	int k = length, m = k;
    	
    	while(list.size > 0) {
    		j = list.head;
			Writes.arrayListAdd(subList, listRemoveNext(j), true, 0);
        	k--;
        	
        	while(j.next != null) {
        		if(Reads.compareValues(j.next.val, subList.get(subList.size()-1)) >= 0) {
        			Writes.arrayListAdd(subList, listRemoveNext(j), true, 0);
        			k--;
        		}
        		else j = j.next;
        	}
        	
        	mergeTo(array, k, m, length);
        	m = k;
    	}
    }
}