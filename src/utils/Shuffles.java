package utils;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.exchange.CircleSort;
import sorts.hybrid.GrailSort;
import sorts.select.MaxHeapSort;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy

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
        
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            shuffle(array, 0, currentLen, ArrayVisualizer.shuffleEnabled(), Writes);
        }
    },
    REVERSE {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            Writes.reversal(array, 0, currentLen-1, ArrayVisualizer.shuffleEnabled() ? 1 : 0, true, false);
        }
    },
    ALMOST {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < Math.max(currentLen / 20, 1); i++){
                Writes.swap(array, (int)(Math.random()*currentLen), (int)(Math.random()*currentLen), 0, true, false);
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(2);
            }
            
            /*
            int step = (int) Math.sqrt(currentLen);
            
            //TODO: *Strongly* consider randomSwap method
            for(int i = 0; i < currentLen; i += step){
                int randomIndex = (int) (Math.random() * step);
                randomIndex = Math.max(randomIndex, 1);
                randomIndex = Math.min(randomIndex, currentLen - i - 1);
                Writes.swap(array, i, i + randomIndex, 0, true, false);

                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(2);
            }
            */
        }
    },
    ALREADY {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for(int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    SHUFFLED_ODDS {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 1; i < currentLen; i += 2){
                int randomIndex = (((int) ((Math.random() * (currentLen - i)) / 2)) * 2) + i;
                Writes.swap(array, i, randomIndex, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(2);
            }
        }
    },
	LOG_SLOPES {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int delay = ArrayVisualizer.shuffleEnabled() ? 1 : 0;
			int currentLen = ArrayVisualizer.getCurrentLength();

			int[] temp = new int[currentLen];
			for(int i = 0; i < currentLen; i++)
				Writes.write(temp, i, array[i], 0, false, true);

			Writes.write(array, 0, 0, delay, true, false);
			for(int i = 1; i < currentLen; i++) {
				int log = (int) (Math.log(i) / Math.log(2));
				int power = (int) Math.pow(2, log);
				int value = temp[2 * (i - power) + 1];
				Writes.write(array, i, value, delay, true, false);
			}
        }
    },
	TRIANGULAR {
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
    };
	
	public void sort(int[] array, int start, int end, boolean delay, Writes Writes) {
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
                Writes.write(array, j, i + min, delay ? 1 : 0, true, false);
                j++;
            }
        }
	}
	
	public void shuffle(int[] array, int start, int end, boolean delay, Writes Writes) {
		for(int i = start; i < end; i++){
            int randomIndex = (int) (Math.random() * (end - i)) + i;
            Writes.swap(array, i, randomIndex, delay ? 1 : 0, true, false);
        }
	}
	
    public abstract void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes);
}