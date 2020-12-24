package utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import main.ArrayVisualizer;

import sorts.select.PoplarHeapSort;
import sorts.select.MaxHeapSort;

/*
 * 
MIT License

Copyright (c) 2020 ArrayV 4.0 Team

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

public enum Shuffles {
    RANDOM {
        // If you want to learn why the random shuffle was changed,
        // I highly encourage you read this. It's quite fascinating:
        // http://datagenetics.com/blog/november42014/index.html
        
        public String getName() {
            return "Randomly";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
            shuffle(array, 0, currentLen, delay ? 1 : 0, Writes);
        }
    },
    REVERSE {
        public String getName() {
            return "Backwards";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
            Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
        }
    },
    ALMOST {
        public String getName() {
            return "Slight Shuffle";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			Random random = new Random();
            
            for(int i = 0; i < Math.max(currentLen / 20, 1); i++){
                Writes.swap(array, random.nextInt(currentLen), random.nextInt(currentLen), 0, true, false);
				
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(10);
            }
        }
    },
    ALREADY {
        public String getName() {
            return "No Shuffle";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for(int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    SORTED {
        public String getName() {
            return "Sorted";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            boolean delay = ArrayVisualizer.shuffleEnabled();
			this.sort(array, 0, currentLen, delay ? 1 : 0, Writes);
        }
    },
    REV_SORTED {
        public String getName() {
            return "Reverse Sorted";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            boolean delay = ArrayVisualizer.shuffleEnabled();
            this.sort(array, 0, currentLen, delay ? 1 : 0, Writes);
            Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
        }
    },
	SHUFFLED_TAIL {
        public String getName() {
            return "Scrambled Tail";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			Random random = new Random();
			
			int j = 0, k = currentLen;
			int[] temp = new int[currentLen];

			for(int i = 0; j < k; i++) {
				if(random.nextDouble() < 1/8d)
					Writes.write(temp, --k, array[i], 0, false, true);
				else
					Writes.write(temp, j++, array[i], 0, false, true);
			}

			for(int i = 0; i < currentLen; i++)
				Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);

			shuffle(array, k, currentLen, delay ? 4 : 0, Writes);
        }
    },
	SHUFFLED_HEAD {
        public String getName() {
            return "Scrambled Head";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			Random random = new Random();
			
			int j = currentLen, k = 0;
			int[] temp = new int[currentLen];

			for(int i = currentLen-1; j > k; i--) {
				if(random.nextDouble() < 1/8d)
					Writes.write(temp, k++, array[i], 0, false, true);
				else
					Writes.write(temp, --j, array[i], 0, false, true);
			}

			for(int i = 0; i < currentLen; i++)
				Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);

			shuffle(array, 0, k, delay ? 4 : 0, Writes);
        }
    },
    NOISY {
        public String getName() {
            return "Noisy";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			Random random = new Random();
			
			int i, size = Math.max(4, (int)(Math.sqrt(currentLen)/2));
			for(i = 0; i+size <= currentLen; i += random.nextInt(size-1)+1)
				shuffle(array, i, i+size, delay ? 0.5 : 0, Writes);
			shuffle(array, i, currentLen, delay ? 0.5 : 0, Writes);
		}
    },
    SHUFFLED_ODDS {
        public String getName() {
            return "Scrambled Odds";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			Random random = new Random();
            
            for(int i = 1; i < currentLen; i += 2){
                int randomIndex = (((random.nextInt(currentLen - i) / 2)) * 2) + i;
                Writes.swap(array, i, randomIndex, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(2);
            }
        }
    },
	FINAL_MERGE {
        public String getName() {
            return "Final Merge Pass";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			
            boolean delay = ArrayVisualizer.shuffleEnabled();
			int count = 2;
			
			int k = 0;
			int[] temp = new int[currentLen];

			for(int j = 0; j < count; j++)
				for(int i = j; i < currentLen; i+=count)
					Writes.write(temp, k++, array[i], 0, false, true);
				
			for(int i = 0; i < currentLen; i++)
				Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
        }
    },
	SAWTOOTH {
        public String getName() {
            return "Sawtooth";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			
            boolean delay = ArrayVisualizer.shuffleEnabled();
			int count = 4;
			
			int k = 0;
			int[] temp = new int[currentLen];

			for(int j = 0; j < count; j++)
				for(int i = j; i < currentLen; i+=count)
					Writes.write(temp, k++, array[i], 0, false, true);
				
			for(int i = 0; i < currentLen; i++)
				Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
        }
    },
	REV_FINAL_MERGE {
        public String getName() {
            return "Reversed Final Merge";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			
            boolean delay = ArrayVisualizer.shuffleEnabled();
			int count = 2;
			
			int k = 0;
			int[] temp = new int[currentLen];

			for(int j = 0; j < count; j++)
				for(int i = j; i < currentLen; i+=count)
					Writes.write(temp, k++, array[i], 0, false, true);
				
			for(int i = 0; i < currentLen; i++)
				Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
			
			Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
        }
    },
	REV_SAWTOOTH {
        public String getName() {
            return "Reversed Sawtooth";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			
            boolean delay = ArrayVisualizer.shuffleEnabled();
			int count = 4;
			
			int k = 0;
			int[] temp = new int[currentLen];

			for(int j = 0; j < count; j++)
				for(int i = j; i < currentLen; i+=count)
					Writes.write(temp, k++, array[i], 0, false, true);
				
			for(int i = 0; i < currentLen; i++)
				Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
			
			Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
        }
    },
	ORGAN {
        public String getName() {
            return "Pipe Organ";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			int[] temp = new int[currentLen];
			
            for(int i = 0, j = 0; i < currentLen; i+=2){
				temp[j++] = array[i];
            }
			for(int i = 1, j = currentLen; i < currentLen; i+=2) {
				temp[--j] = array[i];
			}
			for(int i = 0; i < currentLen; i++){
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            }
        }
	},
	FINAL_BITONIC {
        public String getName() {
            return "Final Bitonic Pass";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			int[] temp = new int[currentLen];
			
			Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
			Highlights.clearMark(2);
            for(int i = 0, j = 0; i < currentLen; i+=2){
				temp[j++] = array[i];
            }
			for(int i = 1, j = currentLen; i < currentLen; i+=2) {
				temp[--j] = array[i];
			}
			for(int i = 0; i < currentLen; i++){
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            }
        }
	},
	INTERLACED {
        public String getName() {
            return "Interlaced";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			
            int[] referenceArray = new int[currentLen];
			for(int i = 0; i < currentLen; i++) {
				referenceArray[i] = array[i];
			}

			int leftIndex = 1;
			int rightIndex = currentLen - 1;

			for(int i = 1; i < currentLen; i++) {
				if(i % 2 == 0) {
					Writes.write(array, i, referenceArray[leftIndex++], delay ? 1 : 0, true, false);
				}
				else {
					Writes.write(array, i, referenceArray[rightIndex--], delay ? 1 : 0, true, false);
				}
			}
        }
    },
	DOUBLE_LAYERED {
        public String getName() {
            return "Double Layered";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
			for(int i = 0; i < currentLen / 2; i += 2) {
				Writes.swap(array, i, currentLen - i - 1, 0, true, false);
				if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
			}
        }
    },
	FINAL_RADIX {
        public String getName() {
            return "Final Radix";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();

			currentLen -= currentLen % 2;
			int mid = currentLen/2;
			int[] temp = new int[mid];

			for(int i = 0; i < mid; i++)
				Writes.write(temp, i, array[i], 0, false, true);

			for(int i = mid, j = 0; i < currentLen; i++, j+=2) {
				Writes.write(array, j, array[i], delay ? 1 : 0, true, false);
				Writes.write(array, j+1, temp[i-mid], delay ? 1 : 0, true, false);
			}
        }
    },
	REC_RADIX {
        public String getName() {
            return "Recursive Final Radix";
        }
        @Override
		public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			
			weaveRec(array, 0, currentLen, 1, delay ? 0.5 : 0, Writes);
		}

		public void weaveRec(int[] array, int pos, int length, int gap, double delay, Writes Writes) {
			if(length < 2) return;
			
			int mod2 = length % 2;
			length -= mod2;
			int mid = length/2;
			int[] temp = new int[mid];
			
			for(int i = pos, j = 0; i < pos+gap*mid; i+=gap, j++)
				Writes.write(temp, j, array[i], 0, false, true);
			
			for(int i = pos+gap*mid, j = pos, k = 0; i < pos+gap*length; i+=gap, j+=2*gap, k++) {
				Writes.write(array, j, array[i], delay, true, false);
				Writes.write(array, j+gap, temp[k], delay, true, false);
			}
			
			weaveRec(array, pos, mid+mod2, 2*gap, delay/2, Writes);
			weaveRec(array, pos+gap, mid, 2*gap, delay/2, Writes);
		}
    },
	HALF_ROTATION {
        public String getName() {
            return "Half Rotation";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			
			int a = 0, m = (currentLen+1)/2;
			
    		if(currentLen%2 == 0)
    			while(m < currentLen) Writes.swap(array, a++, m++, delay ? 1 : 0, true, false);
    		
    		else {
    			Highlights.clearMark(2);
    			int temp = array[a];
    			while(m < currentLen) {
    				Writes.write(array, a++, array[m], delay ? 0.5 : 0, true, false);
    				Writes.write(array, m++, array[a], delay ? 0.5 : 0, true, false);
    			}
    			Writes.write(array, a, temp, delay ? 0.5 : 0, true, false);
    		}
		}
    },
	PARTIAL_REVERSE {
        public String getName() {
            return "Half Reversed";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			
			Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
			Writes.reversal(array, currentLen/4, (3*currentLen+3)/4-1, delay ? 1 : 0, true, false);
        }
    },
	BST_TRAVERSAL {
        public String getName() {
            return "BST Traversal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			int[] temp = Arrays.copyOf(array, currentLen);
			
			// credit to sam walko/anon

			class Subarray {
				private int start;
				private int end;
				Subarray(int start, int end) {
					this.start = start;
					this.end = end;
				}
			}

			Queue<Subarray> q = new LinkedList<Subarray>();
			q.add(new Subarray(0, currentLen));
			int i = 0;

			while(!q.isEmpty()) {
				Subarray sub = q.poll();
				if(sub.start != sub.end) {
					int mid = (sub.start + sub.end)/2;
					Writes.write(array, i, temp[mid], 0, true, false);
					if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
					i++;
					q.add(new Subarray(sub.start, mid));
					q.add(new Subarray(mid+1, sub.end));
				}
			}
        }
    },
	LOG_SLOPES {
        public String getName() {
            return "Logarithmic Slopes";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();

			int[] temp = new int[currentLen];
			for(int i = 0; i < currentLen; i++)
				Writes.write(temp, i, array[i], 0, false, true);

			Writes.write(array, 0, 0, delay ? 1 : 0, true, false);
			for(int i = 1; i < currentLen; i++) {
				int log = (int) (Math.log(i) / Math.log(2));
				int power = (int) Math.pow(2, log);
				int value = temp[2 * (i - power) + 1];
				Writes.write(array, i, value, delay ? 1 : 0, true, false);
			}
        }
    },
	HEAPIFIED {
        public String getName() {
            return "Heapified";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
            
			MaxHeapSort heapSort = new MaxHeapSort(ArrayVisualizer);
			heapSort.makeHeap(array, 0, currentLen, delay ? 1 : 0);
        }
    },
	REV_POPLAR {
        public String getName() {
            return "Reversed Poplarified";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
            
			Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
			PoplarHeapSort poplarHeapSort = new PoplarHeapSort(ArrayVisualizer);
			poplarHeapSort.poplarHeapify(array, 0, currentLen);
        }
    },
	CIRCLE {
        public String getName() {
            return "First Circle Pass";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
            Reads Reads = ArrayVisualizer.getReads();

			shuffle(array, 0, currentLen, delay ? 0.5 : 0, Writes);
			
			int n = 1;
			for(; n < currentLen; n*=2);
			circleSortRoutine(array, 0, n-1, currentLen, delay ? 1 : 0, Reads, Writes);
        }
	
		public void circleSortRoutine(int[] array, int lo, int hi, int end, double sleep, Reads Reads, Writes Writes) {        
			if (lo == hi) return;

			int high = hi;
			int low = lo;
			int mid = (hi - lo) / 2;

			while (lo < hi) {
				if (hi < end && Reads.compareIndices(array, lo, hi, sleep / 2, true) > 0)
					Writes.swap(array, lo, hi, sleep, true, false);
				
				lo++;
				hi--;
			}

			circleSortRoutine(array, low, low + mid, end, sleep/2, Reads, Writes);
			if(low + mid + 1 < end) circleSortRoutine(array, low + mid + 1, high, end, sleep/2, Reads, Writes);
		}
    },
	PAIRWISE {
        public String getName() {
            return "Final Pairwise Pass";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
            Reads Reads = ArrayVisualizer.getReads();

			shuffle(array, 0, currentLen, delay ? 0.5 : 0, Writes);

			//create pairs
			for(int i = 1; i < currentLen; i+=2)
				if(Reads.compareIndices(array, i - 1, i, delay ? 0.5 : 0, true) > 0)
					Writes.swap(array, i-1, i, delay ? 0.5 : 0, true, false);

			Highlights.clearMark(2);

			int[] temp = new int[currentLen];

			//sort the smaller and larger of the pairs separately with pigeonhole sort
			for(int m = 0; m < 2; m++) {
				for(int k = m; k < currentLen; k+=2)
					Writes.write(temp, array[k], temp[array[k]] + 1, 0, false, true);

				int i = 0, j = m;
				while(true) {
					while(i < currentLen && temp[i] == 0) i++;
					if(i >= currentLen) break;

					Writes.write(array, j, i, delay ? 0.5 : 0, true, false);

					j+=2;
					Writes.write(temp, i, temp[i] - 1, 0, false, true);
				}
			}
        }
    },
	REC_REV {
        public String getName() {
            return "Recursive Reversal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			
			reversalRec(array, 0, currentLen, delay ? 1 : 0, Writes);
        }
		
		public void reversalRec(int[] array, int a, int b, double sleep, Writes Writes) {
			if(b-a < 2) return;
			
			Writes.reversal(array, a, b-1, sleep, true, false);
			
			int m = (a+b)/2;
			this.reversalRec(array, a, m, sleep/2, Writes);
			this.reversalRec(array, m, b, sleep/2, Writes);
		}
    },
	GRAY_CODE {
        public String getName() {
            return "Gray Code Fractal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			
			reversalRec(array, 0, currentLen, false, delay ? 1 : 0, Writes);
        }
		
		public void reversalRec(int[] array, int a, int b, boolean bw, double sleep, Writes Writes) {
			if(b-a < 3) return;
			
			int m = (a+b)/2;
			
			if(bw) Writes.reversal(array, a, m-1, sleep, true, false);
			else   Writes.reversal(array, m, b-1, sleep, true, false);
			
			this.reversalRec(array, a, m, false, sleep/2, Writes);
			this.reversalRec(array, m, b, true, sleep/2, Writes);
		}
    },
	SIERPINSKI {
        public String getName() {
            return "Sierpinski Triangle";
        }
		@Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			int[] triangle = new int[currentLen];
			triangleRec(triangle, 0, currentLen);
			
			int[] temp = Arrays.copyOf(array, currentLen);
			for(int i = 0; i < currentLen; i++)
				Writes.write(array, i, temp[triangle[i]], 1, true, false);
		}
		
		public void triangleRec(int[] array, int a, int b) {
    		if(b-a < 2) return;
			if(b-a == 2) {
				array[a+1]++;
				return;
			}
    		
    		int h = (b-a)/3, t1 = (a+a+b)/3, t2 = (a+b+b+2)/3;
    		for(int i = a;  i < t1; i++) array[i] += h;
    		for(int i = t1; i < t2; i++) array[i] += 2*h;
    		
    		triangleRec(array, a, t1);
    		triangleRec(array, t1, t2);
    		triangleRec(array, t2, b);
    	}
    },
	TRIANGULAR {
        public String getName() {
            return "Triangular";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
            int n;
			for(n = 0; (1 << n) < currentLen; n++);

			int[] temp = new int[currentLen];
			int[] c = circleGen(n, 0, Writes);

			for(int i = 1; i <= n; i++)
				c = concat(c, circleGen(n, i, Writes), Writes);

			for(int i = 0; i < currentLen; i++)
				if(c[i] < currentLen) Writes.write(temp, c[i], array[i], 0, false, true);

			for(int i = 0; i < currentLen; i++) {
				Writes.write(array, i, temp[i], 1, true, false);
				
				if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
			}
		}

		public int[] addToAll(int[] a, int n, Writes Writes) {
			for(int i = 0; i < a.length; i++)
				Writes.write(a, i, a[i] + n, 0, false, true);

			return a;
		}

		public int[] concat(int[] a, int[] b, Writes Writes) {
			int[] c = new int[a.length + b.length];
			int j = 0;
			for(int i = 0; i < a.length; i++, j++)
				Writes.write(c, j, a[i], 0, false, true);
			for(int i = 0; i < b.length; i++, j++)
				Writes.write(c, j, b[i], 0, false, true);

			return c;
		}

		public int[] circleGen(int n, int k, Writes Writes) {
			if(n == 0) {
				int c[] = {0, 1};
				return c;
			}
			else if(k == 0) {
				int c[] = {0};
				return c;
			}
			else if(k == n) {
				int c[] = {(1 << n) - 1};
				return c;
			}
			else 
				return concat(circleGen(n-1, k, Writes), addToAll(circleGen(n-1, k-1, Writes), 1 << (n-1), Writes), Writes);
		}
	},
	QSORT_BAD {
		public String getName() {
			return "QSort Adversary";
		}
		@Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
			
			for(int j = currentLen-currentLen%2-2, i = j-1; i >= 0; i-=2, j--)
				Writes.swap(array, i, j, delay ? 1 : 0, true, false);
		}
	},
	BIT_REVERSE {
		@Override
		public String getName() {
			return "Bit Reversal";
		}
		@Override
		public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
			int len = ArrayVisualizer.getCurrentLength();
			boolean delay = ArrayVisualizer.shuffleEnabled();
            double sleep = delay ? 1 : 0;
			
			int log = (int)(Math.log(len)/Math.log(2));
			for(int i = 0; i < len; i++) {
				int j = 0;
				int k = i;
				
				for(int l = log; l > 0; l--){
					j *= 2;
					j += k % 2;
					k /= 2;
				}
				
				if(j > i && j < len)
					Writes.swap(array, i, j, sleep, true, false);
			}
		}
		
	};
	
	public void sort(int[] array, int start, int end, double sleep, Writes Writes) {
		int min = array[start], max = min;
		for(int i = start+1; i < end; i++) {
            if(array[i] < min) min = array[i];
            else if(array[i] > max) max = array[i];
        }
        
        int size = max - min + 1;
        int[] holes = new int[size];
        
        for(int i = start; i < end; i++)
            Writes.write(holes, array[i] - min, holes[array[i] - min] + 1, 0, false, true);
        
        for(int i = 0, j = start; i < size; i++) {
            while(holes[i] > 0) {
                Writes.write(holes, i, holes[i] - 1, 0, false, true);
                Writes.write(array, j, i + min, sleep, true, false);
                j++;
            }
        }
	}
	
	public void shuffle(int[] array, int start, int end, double sleep, Writes Writes) {
		Random random = new Random();
		for(int i = start; i < end; i++){
			int randomIndex = random.nextInt(end - i) + i;
            Writes.swap(array, i, randomIndex, sleep, true, false);
        }
	}
	
    public abstract String getName();
    public abstract void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes);
}