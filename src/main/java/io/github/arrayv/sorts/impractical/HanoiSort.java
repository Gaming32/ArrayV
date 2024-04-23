package io.github.arrayv.sorts.impractical;

import java.util.Stack;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**
 * Hanoi Sort, a sort inspired by the classic Tower of Hanoi puzzle.
 * <p>
 *
 * This sort started as a random idea I had, with a vague idea of how I could
 * approach
 * the problem. And this is, in fact, the first complete implementation that I
 * know of.
 * However, I was greatly helped by two people before me. The first was Andrei
 * "Mrrl"
 * Astrelin (rip), who wrote some pseudocode for this sort that helped solidify
 * the
 * ideas that I'd been having. However, this pseudocode failed to explain how to
 * shift
 * more than one element from one stack to another without violating the
 * traditional
 * rules of the Tower of Hanoi puzzle. Since the sort didn't know the recursion
 * depth
 * that would be required, an iterative shift would be necessary; I found a
 * simple
 * algorithm for this at Stack Overflow. There were still loose ends I had to
 * wrap up;
 * in particular, for the aforementioned iterative hanoi function to work, I had
 * to
 * ensure all consecutive identical items were moved together. But I made it
 * work, and
 * thus here I present the first implementation of this sort that I am aware of.
 *
 * @author Sam Walko (Anonymous0726)
 * @see <a href="https://sudonull.com/post/7575-Sort-Tower-of-Hanoi">
 *      Mrrl's Hanoi Sort pseudocode</a>
 * @see <a href="https://stackoverflow.com/a/12348866">
 *      Iterative algorithm for Tower of Hanoi problem</a>
 */
@SortMeta(name = "Hanoi", slowSort = true, unreasonableLimit = 32)
public final class HanoiSort extends Sort {
	// main array
	private int[] array;
	// Length of the array
	private int length;
	// Auxiliary stacks from the classic Tower of Hanoi problem
	private Stack<Integer> stack2, stack3;
	// sp is a "stack pointer" for the main array (which isn't actually a stack)
	private int sp;
	// Where the unsorted portion of the main array begins
	private int unsorted;
	// These are used in determining end conditions for the hanoi function
	private int target;
	private int targetMoves;

	public HanoiSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	/**
	 * Moves an element (or group of identical elements) from the unsorted
	 * portion of the main array into stack2 using the hanoi function.
	 */
	private void removeFromMainStack() {
		Highlights.markArray(2, sp);
		target = array[sp];

		int moves = hanoi(2, true, 1);
		int height = getHeight(moves + 1);
		targetMoves = moves;
		boolean evenHeight = height % 2 == 0;

		Highlights.clearAllMarks();
		Highlights.markArray(1, sp);

		if (evenHeight) // Move smaller elements to stack3, if necessary
			hanoi(1, true, 2);
		unsorted += moveFromMain(stack2, false); // Move next element(s) to stack2
		hanoi(3, evenHeight, 2); // Move smaller elements back to stack2

	}

	/**
	 * Moves all of the elements, currently in order on stack2,
	 * back to the main array using the hanoi function
	 */
	private void returnToMainStack() {
		int moves = hanoi(2, true, 3);
		int height = getHeight(moves + 1);
		if (height % 2 == 1) { // Odd height case: moved to stack3
			targetMoves = moves;
			hanoi(3, true, 2);
		} // In even case, it's already on main stack, and we need not do anything
	}

	/**
	 * Iteratively moves elements around the stacks according to the
	 * Tower of Hanoi problem. Recursion cannot be used because in many
	 * cases we do not know the initial recursion depth.
	 *
	 * @param startStack Which stack we wish to move a tower from
	 * @param goRight    Whether the smallest disk should go right on the pegs
	 *                   (1->2, 2->3, 3->1) or left (1->3, 2->1, 3->2)
	 * @param endCon     Used to determine when to end this function
	 * @return The number of moves performed
	 */
	private int hanoi(int startStack, boolean goRight, int endCon) {
		int moves = 0;
		int minPoleLoc = startStack;

		if (!endConMet(endCon, moves)) {
			moves++;
			switch (minPoleLoc) {
				case 1:
					if (goRight) {
						moveFromMain(stack2, true);
						minPoleLoc = 2;
					} else {
						moveFromMain(stack3, true);
						minPoleLoc = 3;
					}
					break;
				case 2:
					if (goRight) {
						moveBetweenStacks(stack2, stack3);
						minPoleLoc = 3;
					} else {
						moveToMain(stack2);
						minPoleLoc = 1;
					}
					break;
				case 3:
					if (goRight) {
						moveToMain(stack3);
						minPoleLoc = 1;
					} else {
						moveBetweenStacks(stack3, stack2);
						minPoleLoc = 2;
					}
					break;
			}
		}

		while (!endConMet(endCon, moves)) {
			moves += 2;
			switch (minPoleLoc) {
				case 1:
					if (!stack2.isEmpty() &&
							(stack3.isEmpty() || Reads.compareValues(stack2.peek(), stack3.peek()) < 0))
						moveBetweenStacks(stack2, stack3);
					else
						moveBetweenStacks(stack3, stack2);
					if (goRight) {
						moveFromMain(stack2, true);
						minPoleLoc = 2;
					} else {
						moveFromMain(stack3, true);
						minPoleLoc = 3;
					}
					break;
				case 2:
					if (stack3.isEmpty() ||
							(sp < unsorted && Reads.compareValues(array[sp], stack3.peek()) < 0))
						moveFromMain(stack3, true);
					else
						moveToMain(stack3);
					if (goRight) {
						moveBetweenStacks(stack2, stack3);
						minPoleLoc = 3;
					} else {
						moveToMain(stack2);
						minPoleLoc = 1;
					}
					break;
				case 3:
					if (stack2.isEmpty() ||
							(sp < unsorted && Reads.compareValues(array[sp], stack2.peek()) < 0))
						moveFromMain(stack2, true);
					else
						moveToMain(stack2);
					if (goRight) {
						moveToMain(stack3);
						minPoleLoc = 1;
					} else {
						moveBetweenStacks(stack3, stack2);
						minPoleLoc = 2;
					}
					break;
			}
		}

		return moves;
	}

	/**
	 * Determines whether or not the hanoi function should end now
	 *
	 * @param endCon Which ending condition is required for the hanoi function to
	 *               end
	 * @param moves  the moves completed by the hanoi function so far
	 * @return Whether or not the end condition has been met
	 */
	private boolean endConMet(int endCon, int moves) {
		if (!validNumberMoves(moves))
			return false;
		return switch (endCon) {
			case 1 -> (stack2.isEmpty() || Reads.compareValues(target, stack2.peek()) <= 0);
			case 2 -> moves == targetMoves;
			case 3 -> stack2.isEmpty();
			default -> throw new IllegalArgumentException();
		};
	}

	/**
	 * @param moves The number of moves completed so far
	 * @return If the moves is of the form (2^n)-1
	 */
	private boolean validNumberMoves(int moves) {
		if (moves == 0)
			return true;
		if (moves % 2 == 0)
			return false;
		return validNumberMoves(moves / 2);
	}

	/**
	 * Figures out the height of the pyramid moved based on number of moves
	 *
	 * @param movesPlus1 The number of moves performed, plus one
	 * @return The height of the pyramid moved (equal to log_2(moves + 1))
	 */
	private int getHeight(int movesPlus1) {
		if (movesPlus1 == 1)
			return 0;
		return getHeight(movesPlus1 / 2) + 1;
	}

	/**
	 * Moves an element from the main array to another stack,
	 * then moves any consecutive duplicates of that element with it
	 *
	 * @param stack         The stack to move the element(s) from the main array to
	 * @param checkUnsorted Whether or not it is safe to remove elements in
	 *                      the unsorted portion of the main array
	 * @return duplicates The number of consecutive identical elements
	 *         that were popped off the main array in the current "move"
	 */
	private int moveFromMain(Stack<Integer> stack, boolean checkUnsorted) {
		int duplicates = 1;
		// Move element
		Writes.changeAuxWrites(1);
		Writes.startLap();
		stack.push(array[sp]);
		Writes.stopLap();
		sp++;
		Highlights.markArray(1, sp);
		Delays.sleep(0.25);
		// Move any duplicates (endOnLength indicates the relevant portion of
		// the main stack is "empty")
		boolean endOnLength = (sp >= length) || (checkUnsorted && sp >= unsorted);
		while (!endOnLength && Reads.compareValues(array[sp], stack.peek()) == 0) {
			duplicates++;
			Writes.changeAuxWrites(1);
			Writes.startLap();
			stack.push(array[sp]);
			Writes.stopLap();
			sp++;
			Highlights.markArray(1, sp);
			Delays.sleep(0.25);
			endOnLength = (sp >= length) || (checkUnsorted && sp >= unsorted);
		}
		return duplicates;
	}

	/**
	 * Moves an element to the main array from another stack,
	 * then moves any consecutive duplicates of that element with it
	 *
	 * @param stack The stack to move the element(s) to the main array from
	 */
	private void moveToMain(Stack<Integer> stack) {
		// Move element
		sp--;
		Highlights.markArray(1, sp);
		Writes.write(array, sp, stack.pop(), 0.25, false, false);
		// Move any duplicates
		while (!stack.isEmpty() && Reads.compareValues(stack.peek(), array[sp]) == 0) {
			sp--;
			Highlights.markArray(1, sp);
			Writes.write(array, sp, stack.pop(), 0.25, false, false);
		}
	}

	/**
	 * Moves an element from one stack to another stack,
	 * then moves any consecutive duplicates of that element with it
	 *
	 * @param from the stack to move the element(s) from
	 * @param to   the stack to move the element(s) to
	 */
	private void moveBetweenStacks(Stack<Integer> from, Stack<Integer> to) {
		// Move element
		Writes.changeAuxWrites(1);
		Writes.startLap();
		to.push(from.pop());
		Writes.stopLap();
		Delays.sleep(0.25);
		// Move any duplicates
		while (!from.isEmpty() && Reads.compareValues(from.peek(), to.peek()) == 0) {
			Writes.changeAuxWrites(1);
			Writes.startLap();
			to.push(from.pop());
			Writes.stopLap();
			Delays.sleep(0.25);
		}
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		// Initialize local variables
		this.array = array;
		this.length = length;
		stack2 = new Stack<Integer>();
		stack3 = new Stack<Integer>();
		sp = 0;
		unsorted = 0;

		while (unsorted < length)
			removeFromMainStack();

		returnToMainStack();
	}
}
