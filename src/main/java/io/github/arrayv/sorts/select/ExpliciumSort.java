package io.github.arrayv.sorts.select;

import java.awt.Color;
import java.util.Arrays;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;


final public class ExpliciumSort extends Sort {  
    public ExpliciumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Explicium");
        this.setRunAllSortsName("Explicium Sort");
        this.setRunSortName("Expliciumsort");
        this.setCategory("Selection Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    // Expliciumsort: A variant of Classic Tournament Sort that runs faster.
    // *Made by Distray*

    private void make(int[] array, int[] tree, int start, int end, int index) {
    	if(end <= start) {
    		Highlights.markArray(1, start);
    		Writes.write(tree, index, start, 1, true, true);
    		return;
    	}
    	int next = 2*index+1, // Location of left child in implicit binary tree,
    		mid = start + (end - start) / 2; // and the middle number.
    	
    	make(array, tree, start, mid, next); // Build the two children.
    	make(array, tree, mid+1, end, next+1);
    	
    	// Get the winning child, and set the parent index to that value.
    	if(Reads.compareIndices(array, tree[next], tree[next+1], 0.5, true) <= 0) {
    		Writes.write(tree, index, tree[next], 1, true, true);
    	} else {
    		Writes.write(tree, index, tree[next+1], 1, true, true);
    	}
    	// Colorcode the winner using "build".
		Highlights.colorCode(tree[index], "build");
    }
    private void makeTree(int[] array, int[] tree, int start, int end) {
    	make(array, tree, start, end, 0); // wrapper function
    }
    private void removeWinner(int[] array, int[] tree) {
    	int now = 0, neg;
    	int l = (tree.length - 1) / 2;
    	// Note: We can't take Classic Tournament's approach at finding the winner value,
    	// because this sort doesn't handle trees exactly like Classic Tournament does.
    	while(now < l) {
    		// Negation value to skip past empty children
    		neg = (2*(tree[2*now+1] >> 31)) + (tree[2*now+2] >> 31);
    		if(neg < 0) {
    			if(neg == -3) // -3 is the special number that indicates that we should stop.
    				break;
    			// When the left child is empty, this gives us the index for the right child,
    			// and when the right child is empty, it gives the index for the left child.
    			now = 2*now-neg;
    		} else if(tree[now] == tree[2*now+1]) {
    			// Traverse the child that it pulled the winner from.
        		now = 2*now+1;
        	} else {
        		now = 2*now+2;
        	}
    	}
		Highlights.colorCode(tree[now], "eliminate"); // Mark the winner as eliminated,
    	Writes.write(tree, now, -1, 1, true, true);  // snap the winner out of existence,
    	while(now > 0) {
    		now = (now - 1) / 2; // go up a level,
    		neg = (2*(tree[2*now+1] >> 31)) + (tree[2*now+2] >> 31); // get the negation value,
    		if(neg < 0) { // and check.
        		if(neg == -3) { // -3 in the rebuilding stage is the special number that deletes the parent.
        			Writes.write(tree, now, -1, 1, true, true);
        			continue;
        		} else // And just like before, this gives the non-empty child whenever used.
        			Writes.write(tree, now, tree[2*now-neg], 1, true, true);
    		} else if(Reads.compareIndices(array, tree[2*now+1], tree[2*now+2], 0.5, true) <= 0) {
    			// Rebuild the tree with the lower child of the two, if both are non-empty.
        		Writes.write(tree, now, tree[2*now+1], 1, true, true);
        	} else {
        		Writes.write(tree, now, tree[2*now+2], 1, true, true);
        	}
			// Colorcode the winning child with the rebuild alias.
			Highlights.colorCode(tree[now], "rebuild");
    	}
    }
    public void Explic(int[] array, int start, int end) {
    	int treeLen = 2*(end-start)-1;
    	
    	// Convert the tree length into a 2^x-1 number.
    	treeLen |= treeLen>>1;
    	treeLen |= treeLen>>2;
    	treeLen |= treeLen>>4;
    	treeLen |= treeLen>>8;
    	treeLen |= treeLen>>16;

    	// Define the colors we'll be using.
    	Highlights.retainColorMarks = true;
    	Highlights.defineColor("build", new Color(0, 192, 192));
    	Highlights.defineColor("eliminate", Color.DARK_GRAY);
    	Highlights.defineColor("rebuild", Color.BLUE);
    	
    	int[] tree = Writes.createExternalArray(treeLen), // Tree of indices
		out = Writes.createExternalArray(end-start); // Sorted result
	Arrays.fill(tree, -1);
	Writes.changeAuxWrites(treeLen);
    	makeTree(array, tree, start, end-1);
    	int t = 0;
    	while(true) {
    		// Push the winner value stored in the root of the tree to the output.
    		Writes.write(out, t++, array[tree[0]], 1, true, true);
    		if(t < end-start) // Remove the winner and rebuild the tree.
    			removeWinner(array, tree);
    		else
    			break;
    	}
    	// Put the sorted output back into the main array, and wipe the elimination colors off in the process.
    	Highlights.retainColorMarks = false;
    	Writes.arraycopy(out, 0, array, start, end-start, 1, true, false);
    	Writes.deleteExternalArrays(out, tree);
    }
    @Override 
    public void runSort(int[] array, int length, int bucketCount) {
    	Explic(array, 0, length);
    }
}