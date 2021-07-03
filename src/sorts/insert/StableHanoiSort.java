package sorts.insert;

import java.util.Stack;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * A stable variant of Hanoi Sort.
 * For more information on this type of sort, see Javadoc on {@link HanoiSort}. 
 * 
 * @author Sam Walko (Anonymous0726)
 */
final public class StableHanoiSort extends Sort {
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

	public StableHanoiSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Stable Hanoi");
		this.setRunAllSortsName("Stable Hanoi Sort");
		this.setRunSortName("Stable Hanoi sort");
		this.setCategory("Impractical Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(true);
		this.setUnreasonableLimit(32);
		this.setBogoSort(false);
	}

	/**
	 * Moves an element (or group of identical elements) from the unsorted portion
	 * of the main array into stack2, without disturbing previously-moved elements'
	 * reverse-stable pattern, using the hanoi function.
	 */
	private void removeFromMainStack() {
		Highlights.markArray(1, sp);
		target = array[sp];
		
		int moves = hanoi(2, true, 1);
		int height = getHeight(moves + 1);
		targetMoves = moves;
		
		Highlights.clearAllMarks();
		Highlights.markArray(1, sp);
		
		if(height % 2 == 1) { // Odd height case: moved to stack3
			unsorted += moveFromMain(stack2, false);
			hanoi(3, false, 2);
		} else { // Even height case: moved to main stack
			// Moving to stack3 in this manner allows us to keep stability
			hanoi(1, false, 2);
			hanoi(2, false, 2);
			unsorted += moveFromMain(stack2, false);
			hanoi(3, true, 2);
		}
	}

	/**
	 * Stably moves all of the elements, currently in order on stack2, back to
	 * the main array using the hanoi function. Stability achieved by moving
	 * each disk, which is initially reverse-stable, an odd number of times.
	 */
	private void returnToMainStack() {
		int moves = hanoi(2, true, 3);
		int height = getHeight(moves + 1);
		boolean oddHeight = height % 2 == 1;
		targetMoves = moves;
		
		if(oddHeight) { // Odd height case: moved to stack3
			hanoi(3, false, 2); // All disks have moved an even number of times
		} else { // Even height case: moved to main stack
			targetMoves /= 2;
			hanoi(1, false, 2);
			hanoi(3, false, 2);
			oddHeight = true;
			// At this point, all disks but the greatest has moved a multiple of four times
			// (an even number), and the greatest disk has moved once (an odd number).
		}
		// In both cases, there is now an odd number of disks,
		// each moved an even number of times, on stack2
		while(!stack2.isEmpty()) {
			targetMoves /= 2; // Rounds down to correct integer
			hanoi(2, !oddHeight, 2); // Move all but the new largest element to stack3
			moveToMain(stack2); // Move the new smallest element to the main stack
			hanoi(3, oddHeight, 2); // Move stack3 back to stack2
			oddHeight = !oddHeight; // Change height's odd parity
		}
	}
	
	/**
	 * Iteratively moves elements around the stacks according to the
	 * Tower of Hanoi problem. Recursion cannot be used because in many
	 * cases we do not know the initial recursion depth.
	 * 
	 * @param startStack Which stack we wish to move a tower from
	 * @param goRight Whether the smallest disk should go right on the pegs
	 * (1->2, 2->3, 3->1) or left (1->3, 2->1, 3->2)
	 * @param endCon Used to determine when to end this function
	 * @return The number of moves performed
	 */
	private int hanoi(int startStack, boolean goRight, int endCon) {
		int moves = 0;
		int minPoleLoc = startStack;
		
		if(!endConMet(endCon, moves)) {
			moves++;
			switch(minPoleLoc) {
			case 1:
				if(goRight) {
					moveFromMain(stack2, true);
					minPoleLoc = 2;
				} else {
					moveFromMain(stack3, true);
					minPoleLoc = 3;
				} break;
			case 2:
				if(goRight) {
					moveBetweenStacks(stack2, stack3);
					minPoleLoc = 3;
				} else {
					moveToMain(stack2);
					minPoleLoc = 1;
				} break;
			case 3:
				if(goRight) {
					moveToMain(stack3);
					minPoleLoc = 1;
				} else {
					moveBetweenStacks(stack3, stack2);
					minPoleLoc = 2;
				} break;
			}
		}
		
		while(!endConMet(endCon, moves)) {
			moves += 2;
			switch(minPoleLoc) {
			case 1:
				if(!stack2.isEmpty() &&
						(stack3.isEmpty() || Reads.compareValues(stack2.peek(), stack3.peek()) < 0))
					moveBetweenStacks(stack2, stack3);
				else
					moveBetweenStacks(stack3, stack2);
				if(goRight) {
					moveFromMain(stack2, true);
					minPoleLoc = 2;
				} else {
					moveFromMain(stack3, true);
					minPoleLoc = 3;
				} break;
			case 2:
				if(stack3.isEmpty() ||
						(sp < unsorted && Reads.compareValues(array[sp], stack3.peek()) < 0))
					moveFromMain(stack3, true);
				else
					moveToMain(stack3);
				if(goRight) {
					moveBetweenStacks(stack2, stack3);
					minPoleLoc = 3;
				} else {
					moveToMain(stack2);
					minPoleLoc = 1;
				} break;
			case 3:
				if(stack2.isEmpty() ||
						(sp < unsorted && Reads.compareValues(array[sp], stack2.peek()) < 0))
					moveFromMain(stack2, true);
				else
					moveToMain(stack2);
				if(goRight) {
					moveToMain(stack3);
					minPoleLoc = 1;
				} else {
					moveBetweenStacks(stack3, stack2);
					minPoleLoc = 2;
				} break;
			}
		}
		
		return moves;
	}
	
	/**
	 * Determines whether or not the hanoi function should end now
	 * 
	 * @param endCon Which ending condition is required for the hanoi function to end
	 * @param moves the moves completed by the hanoi function so far
	 * @return Whether or not the end condition has been met
	 */
	private boolean endConMet(int endCon, int moves) {
		if(!validNumberMoves(moves))
			return false;
		switch (endCon) {
		case 1: return (stack2.isEmpty() || Reads.compareValues(target, stack2.peek()) <= 0);
		case 2: return moves == targetMoves;
		case 3: return stack2.isEmpty();
		default: throw new IllegalArgumentException();
		}
	}

	/**
	 * @param moves The number of moves completed so far
	 * @return If the moves is of the form (2^n)-1
	 */
	private boolean validNumberMoves(int moves) {
		if(moves == 0)
			return true;
		if(moves % 2 == 0)
			return false;
		return validNumberMoves(moves/2);
	}

	/**
	 * Figures out the height of the pyramid moved based on number of moves
	 * 
	 * @param movesPlus1 The number of moves performed, plus one
	 * @return The height of the pyramid moved (equal to log_2(moves + 1))
	 */
	private int getHeight(int movesPlus1) {
		if(movesPlus1 == 1)
			return 0;
		return getHeight(movesPlus1 / 2) + 1;
	}
	
	/**
	 * Moves an element from the main array to another stack,
	 * then moves any consecutive duplicates of that element with it
	 * 
	 * @param stack The stack to move the element(s) from the main array to
	 * @param checkUnsorted Whether or not it is safe to remove elements in
	 * the unsorted portion of the main array
	 * @return duplicates The number of consecutive identical elements
	 * that were popped off the main array in the current "move"
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
		while(!endOnLength && Reads.compareValues(array[sp], stack.peek()) == 0) {
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
		while(!stack.isEmpty() && Reads.compareValues(stack.peek(), array[sp]) == 0) {
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
	 * @param to the stack to move the element(s) to
	 */
	private void moveBetweenStacks(Stack<Integer> from, Stack<Integer> to) {
		// Move element
		Writes.changeAuxWrites(1);
		Writes.startLap();
		to.push(from.pop());
		Writes.stopLap();
		Delays.sleep(0.25);
		// Move any duplicates
		while(!from.isEmpty() && Reads.compareValues(from.peek(), to.peek()) == 0) {
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
		
		while(unsorted < length)
			removeFromMainStack();
		
		returnToMainStack();
	}
}
