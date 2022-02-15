package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * An implementation of a tree sort using an AVL tree,
 * based on what I learned in my CSSE230 class
 *
 * @author Sam Walko (Anonymous0726)
 */
public final class AVLTreeSort extends Sort {
	public AVLTreeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("AVL Tree");
		this.setRunAllSortsName("Tree Sort (AVL Balanced)");
		this.setRunSortName("Tree sort (AVL Balanced)");
		this.setCategory("Insertion Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private Node NULL_NODE = new Node(); // (sub)tree of size 0

	/**
	 * The fundamental building block of any programming tree. Each node is the
	 * root of its own subtree. In general, a node has the data being stored at
	 * that point, up to two children nodes, and (optionally) data for keeping
	 * the tree balanced. In the case of an AVL tree, the data for balancing is
	 * an integer telling it's "balance": the height of the right subtree minus
	 * the height of the left subtree.
	 */
	private class Node {
		// main array (poor encapsulation since it's all the same but w/e)
		private int[] array;

		private int pointer; // index in main array of the element contained here
		private Node left, right; // left and right subtrees

		// height of right subtree minus height of left subtree
		// by definition of an AVL tree, must be -1, 0, or 1
		private int balance;

		// Default constructor, and constructor for NULL_NODE
		private Node() {
			// Shouldn't point to anything by default
			this.pointer = -1;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
			this.balance = 0; // A tree of size 0 is balanced
		}

		// Constructor for a node with a pointer
		private Node(int[] array, int pointer) {
			this();
			this.array = array;
			this.pointer = pointer;
		}

		/**
		 * A return container for the recursive add method containing a Node
		 * and a boolean telling whether or not the subtree changed height
		 */
		private class AddContainer {
			private Node node;
			private boolean heightChange;

			private AddContainer(Node node, boolean heightChange) {
				this.node = node;
				this.heightChange = heightChange;
			}
		}

		/**
		 * Recursively adds an element to the subtree whose root is this node
		 *
		 * @param addPointer A pointer to the array telling what element is to be inserted
		 * @return an AddContainer containing the node that is now the root of this
		 * subtree, and the boolean telling whether or not this subtree increased in height
		 */
		private AddContainer add(int addPointer) {
			// Case 1: If this is where to add the new element, create a node for it
			if(this == NULL_NODE) {
				Highlights.clearMark(2); // No longer comparing to previous leaves
				Node newNode = new Node(array, addPointer); // Create the node
				// Return the node, and the fact that the height obviously changed
				// as there was no subtree here previously
				return new AddContainer(newNode, true);
			}

			// If there's an element already here, we need to compare them to
			// determine whether it should be placed in the left or the right subtree.
			// Thus, mark the element at the pointer for comparison.
			Highlights.markArray(2, pointer);

			// Case 2: The element is smaller and thus belongs in the left subtree
			if(Reads.compareValues(array[addPointer], array[pointer]) == -1) {
				Delays.sleep(0.25);

				// Recursively get the root of the new left subtree
				AddContainer container = left.add(addPointer);

				Highlights.markArray(2, pointer);

				// Set the root of the new left subtree as such
				Writes.changeAuxWrites(1);
				Writes.startLap();
				left = container.node;
				Writes.stopLap();
				Delays.sleep(0.05);

				// This handles the case where left subtree increased in
				// height, possibly requiring rotations to keep balance
				if(container.heightChange)
					return heightChangeLeft();

				// In the case where left subtree did not increase in height,
				// return this node, and the fact that the height obviously
				// did not change as neither of it subtrees changed in height
				return new AddContainer(this, false);
				// This is the boolean that took me about a day to fix, btw.
			}

			// Case 3: The element is equal or larger and thus belongs in the right subtree
			// Note: As equality also results in right subtree, this sort is stable
			Delays.sleep(0.25);

			// Recursively get the root of the new right subtree
			AddContainer container = right.add(addPointer);

			Highlights.markArray(2, pointer);

			// Set the root of the new right subtree as such
			Writes.changeAuxWrites(1);
			Writes.startLap();
			right = container.node;
			Writes.stopLap();
			Delays.sleep(0.05);

			// This handles the case where right subtree increased in
			// height, possibly requiring rotations to keep balance
			if(container.heightChange)
				return heightChangeRight();

			// In the case where right subtree did not increase in height,
			// return this node, and the fact that the height obviously
			// did not change as neither of it subtrees changed in height
			return new AddContainer(this, false);
			// This is also the boolean that took me about a day to fix.
		}

		/**
		 * Updates balance codes and rotates if necessary when the left subtree's
		 * height has increased. Helper method for add.
		 *
		 * @return an AddContainer including the node to be placed at the position
		 * and whether or not the left subtree's height has been changed
		 */
		private AddContainer heightChangeLeft() {
			if(balance != -1) { // No rotation necessary
				balance--;
				// Trust me, this boolean works
				return new AddContainer(this, balance == -1);
			}
			// Determine which type of rotation necessary. Note that after
			// a rotation, the subtree height must not have changed.
			if(left.balance == -1)
				// Left-most subtree must be too large, so fix with single rotation
				return new AddContainer(singleRotateRight(), false);
			// Middle-left subtree must be too large, so fix with double rotation
			return new AddContainer(doubleRotateRight(), false);
		}

		/**
		 * Updates balance codes and rotates if necessary when the right subtree's
		 * height has increased. Helper method for add.
		 *
		 * @return an AddContainer including the node to be placed at the position
		 * and whether or not the right subtree's height has been changed
		 */
		private AddContainer heightChangeRight() {
			if(balance != 1) { // No rotation necessary
				balance++;
				// Trust me, this boolean works
				return new AddContainer(this, balance == 1);
			}
			// Determine which type of rotation necessary. Note that after
			// a rotation, the subtree height must not have changed.
			if(right.balance == 1)
				// Right-most subtree must be too large, so fix with single rotation
				return new AddContainer(singleRotateLeft(), false);
			// Middle-right subtree must be too large, so fix with double rotation
			return new AddContainer(doubleRotateLeft(), false);
		}

		// These rotations are kinda magic (read: too difficult to explain without
		// diagrams), so feel free to ask me (Anonymous0726#2452 on Discord) about it.
		// Alternatively, the animations on Wikipedia are pretty good.

		/**
		 * Singly rotates the subtree right about the current node,
		 * updating balance codes along the way. Helper method for
		 * heightChangeLeft and heightChangeRight.
		 *
		 * @return the subtree's new root
		 */
		private Node singleRotateRight() {
			Node b = left;

			Highlights.markArray(3, pointer);
			Highlights.markArray(4, b.pointer);

			Writes.changeAuxWrites(2);
			Writes.startLap();
			left = b.right;
			b.right = this;
			Writes.stopLap();
			Delays.sleep(0.25);

			balance = 0;
			b.balance = 0;

			Highlights.clearMark(3);
			Highlights.clearMark(4);

			return b;
		}

		/**
		 * Singly rotates the subtree left about the current node,
		 * updating balance codes along the way. Helper method for
		 * heightChangeLeft and heightChangeRight.
		 *
		 * @return the subtree's new root
		 */
		private Node singleRotateLeft() {
			Node b = right;

			Highlights.markArray(3, pointer);
			Highlights.markArray(4, b.pointer);

			Writes.changeAuxWrites(2);
			Writes.startLap();
			right = b.left;
			b.left = this;
			Writes.stopLap();
			Delays.sleep(0.25);

			balance = 0;
			b.balance = 0;

			Highlights.clearMark(3);
			Highlights.clearMark(4);

			return b;
		}

		/**
		 * Singly rotates the subtree right about the current node, updating balance
		 * codes along the way. Also known as a left-right rotation. Helper method for
		 * heightChangeLeft.
		 *
		 * @return the subtree's new root
		 */
		private Node doubleRotateRight() {
			int oldBBalance = left.right.balance;

			Node newLeft = left.singleRotateLeft();
			Writes.changeAuxWrites(1);
			Writes.startLap();
			left = newLeft;
			Writes.stopLap();
			Delays.sleep(0.25);

			Node b = singleRotateRight();

			if(oldBBalance == -1)
				b.right.balance = 1;
			if(oldBBalance == 1)
				b.left.balance = -1;

			return b;
		}

		/**
		 * Singly rotates the subtree left about the current node, updating balance
		 * codes along the way. Also known as a right-left rotation. Helper method for
		 * heightChangeRight.
		 *
		 * @return the subtree's new root
		 */
		private Node doubleRotateLeft() {
			int oldBBalance = right.left.balance;

			Node newRight = right.singleRotateRight();
			Writes.changeAuxWrites(1);
			Writes.startLap();
			right = newRight;
			Writes.stopLap();
			Delays.sleep(0.25);

			Node b = singleRotateLeft();

			if(oldBBalance == -1)
				b.right.balance = 1;
			if(oldBBalance == 1)
				b.left.balance = -1;

			return b;
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
			Highlights.markArray(1, i); // Highlights the element being added
			Node.AddContainer container = root.add(i);

			Highlights.clearMark(2);

			Writes.changeAuxWrites(1);
			Writes.startLap();
			root = container.node;
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
