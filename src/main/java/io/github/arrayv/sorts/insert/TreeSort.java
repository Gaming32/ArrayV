package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * An implementation of a tree sort using an unbalanced
 * (aka naive) binary search tree (BST)
 *
 * @author Sam Walko (Anonymous0726)
 */
public final class TreeSort extends Sort {
	public TreeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Tree");
		this.setRunAllSortsName("Tree Sort (Unbalanced)");
		this.setRunSortName("Tree sort (Unbalanced)");
		this.setCategory("Insertion Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private Node NULL_NODE = new Node();

	/**
	 * The fundamental building block of any programming tree. Each node is the
	 * root of its own subtree. In general, a node has the data being stored at
	 * that point, up to two children nodes, and (optionally) data for keeping
	 * the tree balanced. In an unbalanced (naive) tree, there is no balance data.
	 */
	private class Node {
		// main array (poor encapsulation since it's all the same but w/e)
		private int[] array;

		private int pointer; // index in main array of the element contained here
		private Node left, right; // left and right subtrees

		// Default constructor, and constructor for NULL_NODE
		private Node() {
			// Shouldn't point to anything by default
			this.pointer = -1;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		}

		// Constructor for a node with a pointer
		private Node(int[] array, int pointer) {
			this();
			this.array = array;
			this.pointer = pointer;
		}

		/**
		 * Recursively adds an element to the subtree whose root is this node
		 *
		 * @param addPointer A pointer to the array telling what element is to be inserted
		 * @return the Node to be added at this point
		 */
		private Node add(int addPointer) {
			// Case 1: If this is where to add the new element, create a node for it
			if(this == NULL_NODE) {
				Highlights.clearMark(1); // No longer comparing to previous leaves
				return new Node(array, addPointer); // Create the new node and return it
			}

			// If there's an element already here, we need to compare them to
			// determine whether it should be placed in the left or the right subtree.
			// Thus, mark the element at the pointer for comparison.
			Highlights.markArray(2, pointer);

			// Case 2: The element is smaller and thus belongs in the left subtree
			if(Reads.compareValues(array[addPointer], array[pointer]) == -1) {
				Delays.sleep(0.25);

				// Recursively get the root of the new left subtree
				Node newLeaf = left.add(addPointer);

				Highlights.markArray(2, pointer);

				// Set the root of the new left subtree as such
				Writes.changeAuxWrites(1);
				Writes.startLap();
				left = newLeaf;
				Writes.stopLap();

				Delays.sleep(0.05);
				return this; // return this node
			}

			// Case 3: The element is equal or larger and thus belongs in the right subtree
			// Note: As equality also results in right subtree, this sort is stable
			Delays.sleep(0.25);

			// Recursively get the root of the new right subtree
			Node newLeaf = right.add(addPointer);

			// Set the root of the new right subtree as such
			Writes.changeAuxWrites(1);
			Writes.startLap();
			right = newLeaf;
			Writes.stopLap();

			Delays.sleep(0.05);
			return this; // return this node
		}

		/**
		 * Performs an in-order traversal of the array and writes
		 * the values of the original array to a sorted temporary array
		 *
		 * @param tempArray the temporary array to write the contents of the subtree to
		 * @param location a pointer to the location in the temporary array to which the
		 * contents of the current subtree should be written to
		 * @return The size of subtree, used to determine where the next value should be
		 * written to.
		 */
		private int writeToArray(int[] tempArray, int location) {
			if(this == NULL_NODE) return 0;

			int leftTreeSize = left.writeToArray(tempArray, location);
			int newLocation = location + leftTreeSize;

			Highlights.markArray(1, pointer);
			Writes.write(tempArray, newLocation, array[pointer], 0.1, false, true);

			int rightTreeSize = right.writeToArray(tempArray, newLocation + 1);
			return leftTreeSize + rightTreeSize + 1;
		}
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		// Tells the tree what array is to be sorted
		NULL_NODE.array = array;
		// Creates a tree of size 0, to which all elements will be added
		Node root = NULL_NODE;

	 // This loop adds every element of the array to be sorted into the tree
		for(int i = 0; i < length; i++) {
			Highlights.markArray(1, i);
			Node newRoot = root.add(i);

			Highlights.clearMark(2);

			Writes.changeAuxWrites(1);
			Writes.startLap();
			root = newRoot;
			Writes.stopLap();
			Delays.sleep(0.25);

			Highlights.clearAllMarks(); // Clearing all just in case
		}

		// Write the contents of the tree to a temporary array
		int[] tempArray = new int[length];
		root.writeToArray(tempArray, 0);
		Highlights.clearMark(1); // No more elements being transferred to temporary array

		// Write the contents of the temporary array back to the main array
		for(int i = 0; i < length; i++) {
			Writes.write(array, i, tempArray[i], 1, true, false);
		}
	}
}
