package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**
 * An implementation of a tree sort using an red-black tree,
 * based on what I learned in my CSSE230 class
 *
 * @author Sam Walko (Anonymous0726)
 */
@SortMeta(listName = "Tree (Red-Black)", runName = "Red-Black-Balanced Tree Sort")
public final class RedBlackTreeSort extends Sort {
	public RedBlackTreeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private Node NULL_NODE = new Node(); // (sub)tree of size 0

	/**
	 * The fundamental building block of any programming tree. Each node is the
	 * root of its own subtree. In general, a node has the data being stored at
	 * that point, up to two children nodes, and (optionally) data for keeping
	 * the tree balanced. In the case of a red-black tree, the data for balancing
	 * is a boolean telling the node's "color": red or black.
	 */
	private class Node {
		// main array (poor encapsulation since it's all the same but w/e)
		private int[] array;

		private int pointer; // index in main array of the element contained here
		private Node left, right; // left and right subtrees
		private boolean isRed; // whether this node is red or black

		// Default constructor, and constructor for NULL_NODE
		private Node() {
			// Shouldn't point to anything by default
			this.pointer = -1;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
			this.isRed = false; // All NULL_NODEs being black will not violate black balance
		}

		// Constructor for a node with a pointer
		private Node(int[] array, int pointer) {
			this();
			this.array = array;
			this.pointer = pointer;
			this.isRed = true; // A node with data, when first created, is red
		}

		/**
		 * A return container for the recursive add method containing a Node
		 * and a boolean telling whether or not the subtree needs rebalancing
		 */
		private class AddContainer {
			private Node node;
			private boolean needsFix;

			private AddContainer(Node node, boolean needsFix) {
				this.node = node;
				this.needsFix = needsFix;
			}
		}

		/**
		 * Recursively adds an element to the subtree whose root is this node
		 *
		 * @param addPointer A pointer to the array telling what element is to be
		 *                   inserted
		 * @return an AddContainer containing the node that is now the root of this
		 *         subtree, and the boolean telling whether or not this subtree
		 *         increased in height
		 */
		private AddContainer add(int addPointer) {
			// Case 1: If this is where to add the new element, create a node for it
			if (this == NULL_NODE) {
				Highlights.clearMark(1); // No longer comparing to previous leaves
				Node newNode = new Node(array, addPointer); // Create the node
				// Return the node, and the fact that a subtree of size 1
				// obviously does not require rebalancing
				return new AddContainer(newNode, false);
			}

			// If there's an element already here, we need to compare them to
			// determine whether it should be placed in the left or the right subtree.
			// Thus, mark the element at the pointer for comparison.
			Highlights.markArray(2, pointer);

			// This type of restructuring allows us to prevent future
			// cascading rotations, without losing black balance.
			// (Note that this only occurs when there's actually a node here
			// already, hence this check is performed after the check for case 1.)
			if (!isRed && left.isRed && right.isRed) {
				Highlights.markArray(3, left.pointer);
				Highlights.markArray(4, right.pointer);

				isRed = true;
				left.isRed = false;
				right.isRed = false;

				Delays.sleep(0.25);
				Highlights.clearMark(3);
				Highlights.clearMark(4);
			}

			// Case 2: The element is smaller and thus belongs in the left subtree
			if (Reads.compareValues(array[addPointer], array[pointer]) == -1) {
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

				// If the tree is determined to need rebalancing, then first
				// determine which type of rotation is necessary, then perform it.
				if (container.needsFix) {
					if (left.left.isRed)
						return new AddContainer(singleRotateRight(), false);
					return new AddContainer(doubleRotateRight(), false);
				}

				// If no rebalancing is necessary here, return this node, and determine
				// whether or not the subtree at the parent will require rebalancing
				return new AddContainer(this, isRed && left.isRed);
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

			// If the tree is determined to need rebalancing, then first
			// determine which type of rotation is necessary, then perform it.
			if (container.needsFix) {
				if (right.right.isRed)
					return new AddContainer(singleRotateLeft(), false);
				return new AddContainer(doubleRotateLeft(), false);
			}

			// If no rebalancing is necessary here, return this node, and determine
			// whether or not the subtree at the parent will require rebalancing
			return new AddContainer(this, isRed && right.isRed);
		}

		// These rotations are kinda magic (read: too difficult to explain without
		// diagrams), so feel free to ask me (Anonymous0726#2452 on Discord) about it.
		// Alternatively, the animations on Wikipedia for AVL trees are fairly decent
		// at showing what happens during any given type of rotation, but don't really
		// show what triggers a rotation here in a red-black tree, nor do they explain
		// the recoloring.

		/**
		 * Singly rotates the subtree right about the current node,
		 * recoloring along the way. Helper method for add.
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

			b.isRed = false;
			isRed = true;

			Highlights.clearMark(3);
			Highlights.clearMark(4);

			return b;
		}

		/**
		 * Singly rotates the subtree left about the current node,
		 * recoloring along the way. Helper method for add.
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

			b.isRed = false;
			isRed = true;

			Highlights.clearMark(3);
			Highlights.clearMark(4);

			return b;
		}

		/**
		 * Doubly rotates the subtree right about the current node, recoloring along
		 * the way. Also known as a left-right rotation. Helper method for add.
		 *
		 * @return the subtree's new root
		 */
		private Node doubleRotateRight() {

			Node newLeft = left.singleRotateLeft();
			Writes.changeAuxWrites(1);
			Writes.startLap();
			left = newLeft;
			Writes.stopLap();
			Delays.sleep(0.25);

			Node b = singleRotateRight();

			return b;
		}

		/**
		 * Double rotates the subtree left about the current node, recoloring along
		 * the way. Also known as a right-left rotation. Helper method for add.
		 *
		 * @return the subtree's new root
		 */
		private Node doubleRotateLeft() {

			Node newRight = right.singleRotateRight();
			Writes.changeAuxWrites(1);
			Writes.startLap();
			right = newRight;
			Writes.stopLap();
			Delays.sleep(0.25);

			Node b = singleRotateLeft();

			return b;
		}

		/**
		 * Performs an in-order traversal of the array and writes
		 * the values of the original array to a sorted temporary array
		 *
		 * @param tempArray the temporary array to write the contents of the subtree to
		 * @param location  a pointer to the location in the temporary array to which
		 *                  the
		 *                  contents of the current subtree should be written to
		 * @return The size of subtree, used to determine where the next value should be
		 *         written to.
		 */
		private int writeToArray(int[] tempArray, int location) {
			if (this == NULL_NODE)
				return 0;

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
		for (int i = 0; i < length; i++) {
			Highlights.markArray(1, i); // Highlights the element being added
			Node.AddContainer container = root.add(i);

			Highlights.clearMark(2);

			Writes.changeAuxWrites(1);
			Writes.startLap();
			root = container.node;
			Writes.stopLap();
			Delays.sleep(0.25);

			Highlights.markArray(2, root.pointer);
			root.isRed = false; // Root of a red-black tree must always be black
			Delays.sleep(0.25);

			Highlights.clearAllMarks(); // Clearing all just in case
		}

		// Write the contents of the tree to a temporary array
		int[] tempArray = new int[length];
		root.writeToArray(tempArray, 0);
		Highlights.clearMark(1); // No more elements being transferred to temporary array

		// Write the contents of the temporary array back to the main array
		for (int i = 0; i < length; i++) {
			Writes.write(array, i, tempArray[i], 1, true, false);
		}
	}
}
