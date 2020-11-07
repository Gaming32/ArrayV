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
            
            //TODO: Consider separate method
            for(int i = 0; i < currentLen; i++){
                int randomIndex = (int) (Math.random() * (currentLen - i)) + i;
                Writes.swap(array, i, randomIndex, 0, true, false);
                Writes.write(ArrayVisualizer.getShadowArray(), array[i], i, 0, false, true);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
            
            /*
            CircleSort circleSort = new CircleSort(ArrayVisualizer);
            circleSort.singleRoutine(array, currentLen);
            */
        }
    },
    REVERSE {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            Writes.changeReversals(1);
            for (int left = 0, right = currentLen - 1; left < right; left++, right--) {
                // swap the values at the left and right indices
                Writes.swap(array, left, right, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
            
            /*
            for(int i = 0; i < Math.max(currentLen / 20, 1); i++){
                Writes.swap(array, (int)(Math.random()*currentLen), (int)(Math.random()*currentLen), 0, true, false);
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(10);
            }
            */
        }
    },
    SIMILAR {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < currentLen - 8; i++) {
                array[i] = currentLen / 2;
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
            for(int i = currentLen - 8; i < currentLen; i++) {
                array[i] = (int) (Math.random() < 0.5 ? currentLen * 0.75 : currentLen * 0.25);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
            for(int i = 0; i < currentLen; i++){
                int randomIndex = (int) (Math.random() * (currentLen - i)) + i;
                Writes.swap(array, i, randomIndex, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
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
        
            /*
            int currentLen = ArrayVisualizer.getCurrentLength();
            Writes.write(array, 0, 0, 1, true, false);
            for(int i = 1; i < currentLen; i++) {
                int log = (int) (Math.log(i) / Math.log(2));
                int power = (int) Math.pow(2, log);
                int value = 2 * (i - power) + 1;
                Writes.write(array, i, value, 1, true, false);
            }
        }
        */
            
            /*
            int n;
            for(n = 0; (1 << n) < currentLen; n++);
            
            int[] temp = new int[currentLen];
            int[] c = circleGen(n, 0, Writes);
            
            for(int i = 1; i <= n; i++)
                c = concat(c, circleGen(n, i, Writes), Writes);
            
            for(int i = 0; i < currentLen; i++)
                if(c[i] < currentLen) Writes.write(temp, c[i], array[i], 0.1, true, true);
            
            for(int i = 0; i < currentLen; i++) {
                Writes.write(array, i, temp[i], 1, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
        
        public int[] addToAll(int[] a, int n, Writes Writes) {
            for(int i = 0; i < a.length; i++)
                Writes.write(a, i, a[i] + n, 0.1, true, true);
            
            return a;
        }
        
        public int[] concat(int[] a, int[] b, Writes Writes) {
            int[] c = new int[a.length + b.length];
            int j = 0;
            for(int i = 0; i < a.length; i++, j++)
                Writes.write(c, j, a[i], 0.1, true, true);
            for(int i = 0; i < b.length; i++, j++)
                Writes.write(c, j, b[i], 0.1, true, true);
            
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
        */
            /*
          //TODO: Consider separate method
            for(int i = 1; i < currentLen; i += 2){
                int randomIndex = (((int) ((Math.random() * (currentLen - i)) / 2)) * 2) + i;
                Writes.swap(array, i, randomIndex, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(2);
            }
        }
        */
            /*
            int j = 0, k = currentLen;
            int[] temp = new int[currentLen];
            
            for(int i = 0; j < k; i++) {
                Highlights.markArray(2, i);
                if(Math.random() < 1/8d)
                    Writes.write(temp, --k, array[i], 0, true, true);
                else
                    Writes.write(temp, j++, array[i], 0, true, true);
                Delays.sleep(0.5);
            }
            Highlights.clearMark(2);
            
            for(int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[i], 0.5, true, false);
            
            for(int i = k; i < currentLen; i++){
                int randomIndex = (int) (Math.random() * (currentLen - i)) + i;
                Writes.swap(array, i, randomIndex, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(4);
            }
            
            GrailSort grailSort = new GrailSort(ArrayVisualizer);
            grailSort.rotateLength(array, k, currentLen - k);
        }
            */
            
            /*
            Writes.changeReversals(1);
            int left = 0;
            int right = currentLen - 1;
            for (int i = 0; i < (currentLen / 4); i++) {
                // swap the values at the left and right indices
                Writes.swap(array, left++, right--, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
        */
            /*
            int[] perlinNoise = new int[currentLen];

            float step = 1f / currentLen;
            float randomStart = (float) (Math.random() * currentLen);
            int octave = (int) (Math.log(currentLen) / Math.log(2));

            for(int i = 0; i < currentLen; i++) {
                int value = (int) (PerlinNoise.returnFracBrownNoise(randomStart, octave) * currentLen);
                Writes.write(perlinNoise, i, value, 0.333, true, true);
                randomStart += step;
            }

            int minimum = Integer.MAX_VALUE;
            for(int i = 0; i < currentLen; i++) {
                if(perlinNoise[i] < minimum) {
                    minimum = perlinNoise[i];
                }
            }
            minimum = Math.abs(minimum);
            for(int i = 0; i < currentLen; i++) {
                Writes.write(perlinNoise, i, perlinNoise[i] + minimum, 0.333, true, true);
            }

            double maximum = Double.MIN_VALUE;
            for(int i = 0; i < currentLen; i++) {
                if(perlinNoise[i] > maximum) {
                    maximum = perlinNoise[i];
                }
            }
            double scale = currentLen / maximum;
            if(scale < 1.0 || scale > 1.8) {
                for(int i = 0; i < currentLen; i++) {
                    Writes.write(perlinNoise, i, (int) (perlinNoise[i] * scale), 0.333, true, true);
                }
            }

            for(int i = 0; i < currentLen; i++) {
                Writes.write(array, i, perlinNoise[i], 1, true, false);
            }
        }
        */
    
            /*
            for(int i = 0; i < currentLen; i++) {
                int value = 0 - (int) (PerlinNoise.returnNoise((float) i / currentLen) * currentLen);
                Writes.write(array, i, value, 1, true, false);
            }
        }
        */
            
            /*
            int[] perlinNoise = new int[currentLen];

            float step = 1f / currentLen;
            float randomStart = (float) (Math.random() * currentLen);
            int octave = (int) (Math.log(currentLen) / Math.log(2));

            for(int i = 0; i < currentLen; i++) {
                int value = (int) (PerlinNoise.returnFracBrownNoise(randomStart, octave) * currentLen);
                Writes.write(perlinNoise, i, value, 0.333, true, true);
                randomStart += step;
            }

            int minimum = Integer.MAX_VALUE;
            for(int i = 0; i < currentLen; i++) {
                if(perlinNoise[i] < minimum) {
                    minimum = perlinNoise[i];
                }
            }
            minimum = Math.abs(minimum);
            for(int i = 0; i < currentLen; i++) {
                Writes.write(perlinNoise, i, perlinNoise[i] + minimum, 0.333, true, true);
            }

            double maximum = Double.MIN_VALUE;
            for(int i = 0; i < currentLen; i++) {
                if(perlinNoise[i] > maximum) {
                    maximum = perlinNoise[i];
                }
            }
            double scale = currentLen / maximum;
            if(scale < 1.0 || scale > 1.8) {
                for(int i = 0; i < currentLen; i++) {
                    Writes.write(perlinNoise, i, (int) (perlinNoise[i] * scale), 0.333, true, true);
                }
            }

            for(int i = 0; i < currentLen; i++) {
                Writes.write(array, i, perlinNoise[i], 1, true, false);
            }
        }
        */
        
            /*
            MaxHeapSort heapSort = new MaxHeapSort(ArrayVisualizer);
            heapSort.makeHeap(array, 0, currentLen, 1);
        */
            /*
            for(int i = 0; i < currentLen / 2; i += 2) {
                Writes.swap(array, i, currentLen - i - 1, 0, true, false);
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
            */
            
            /*
            Writes.changeReversals(1);
            for (int left = 0, right = currentLen - 1; left < right; left++, right--) {
                // swap the values at the left and right indices
                Writes.swap(array, left, right, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
            PoplarHeapSort poplarHeapSort = new PoplarHeapSort(ArrayVisualizer);
            poplarHeapSort.poplarHeapify(array, 0, currentLen);
        }
        */
            
            /*
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
                    Writes.write(array, i, mid, 0, true, false);
                    if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
                    i++;
                    q.add(new Subarray(sub.start, mid));
                    q.add(new Subarray(mid+1, sub.end));
                }
            }
        */
            /*
            int offset = 0;
            for(int i = 0; i < 4; i++) {
                int value = currentLen;
                for(int j = offset; j < offset + (currentLen / 4); j++) {
                    Writes.write(array, j, value, 1, true, false);
                    value -= 4;
                }
                offset += (currentLen / 4);
            }
        }
        */
        /*
         * int value = currentLen;
            for(int i = 0; i < currentLen / 2; i++) {
                Writes.write(array, i, value, 1, true, false);
                value -= 2;
            }
            value = currentLen;
            for(int i = currentLen / 2; i < currentLen; i++) {
                Writes.write(array, i, value, 1, true, false);
                value -= 2;
            }
         */
        
            /*
            int[] referenceArray = new int[currentLen];
            for(int i = 0; i < currentLen; i++) {
                referenceArray[i] = array[i];
            }

            int leftIndex = 1;
            int rightIndex = currentLen - 1;

            for(int i = 1; i < currentLen; i++) {
                if(i % 2 == 0) {
                    Writes.write(array, i, referenceArray[leftIndex++], 1, true, false);
                }
                else {
                    Writes.write(array, i, referenceArray[rightIndex--], 1, true, false);
                }
            }
            */
            
            /*
            Reads Reads = ArrayVisualizer.getReads();
            
            //shuffle
            for(int i = 0; i < currentLen; i++){
                int randomIndex = (int) (Math.random() * (currentLen - i)) + i;
                Writes.swap(array, i, randomIndex, 0, true, false);

                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(0.5);
            }

            //create pairs
            for(int i = 1; i < currentLen; i+=2) {
                if(Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) {
                    Writes.swap(array, i-1, i, 0, true, false);

                    if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(0.5);
                }
            }

            Highlights.clearMark(2);

            int[] temp = new int[currentLen];

            //sort the smaller and larger of the pairs separately with pigeonhole sort
            for(int m = 0; m < 2; m++) {
                for(int k = m; k < currentLen; k+=2)
                    Writes.write(temp, array[k], temp[array[k]] + 1, 0.5, true, true);

                int i = 0, j = m;
                while(true) {
                    while(i < currentLen && temp[i] == 0) i++;
                    if(i >= currentLen) break;

                    Writes.write(array, j, i, 0, true, false);
                    if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(0.5);

                    j+=2;
                    Writes.write(temp, i, temp[i] - 1, 0, false, true);
                }
            */

                /*
            int currentLen = ArrayVisualizer.getCurrentLength();
            double step = 8d / currentLen;
            double position = -4;
            int constant = 1264;
            double factor = currentLen / 512d;
            
            for(int i = 0; i < currentLen; i++) {
                double square = Math.pow(position, 2);
                double negativeSquare = 0 - square;
                double halfNegSquare = negativeSquare / 2d;
                double numerator = constant * factor * Math.pow(Math.E, halfNegSquare);
                
                double doublePi = 2 * Math.PI;
                double denominator = Math.sqrt(doublePi);
                
                Writes.write(array, i, (int) (numerator / denominator), 1, true, false);
                position += step;
                
            }
            */
            
            /*
            int offset = 0;
            for(int i = 0; i < 4; i++) {
                int value = 0;
                for(int j = offset; j < offset + (currentLen / 4); j++) {
                    Writes.write(array, j, value, 1, true, false);
                    value += 4;
                }
                offset += (currentLen / 4);
            }
            */
            
            /*
            for(int i = 0; i < currentLen / 2; i++) {
                Writes.swap(array, i, i + (currentLen / 2), 2, true, false);
            }
            */
            
            /*
            int value = currentLen;
            for(int i = 0; i < currentLen / 2; i++) {
                Writes.write(array, i, value, 1, true, false);
                value -= 2;
            }
            for(int i = currentLen / 2; i < currentLen; i++) {
                Writes.write(array, i, value, 1, true, false);
                value += 2;
            }
            */
            
            /*
            int j = 0, k = currentLen;
            int[] temp = new int[currentLen];
            
            for(int i = 0; j < k; i++) {
                Highlights.markArray(2, i);
                if(Math.random() < 1/8d)
                    Writes.write(temp, --k, array[i], 0, true, true);
                else
                    Writes.write(temp, j++, array[i], 0, true, true);
                Delays.sleep(0.5);
            }
            Highlights.clearMark(2);
            
            for(int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[i], 0.5, true, false);
            
            for(int i = k; i < currentLen; i++){
                int randomIndex = (int) (Math.random() * (currentLen - i)) + i;
                Writes.swap(array, i, randomIndex, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(4);
            }
            
        }
        */
    
        /*
         * int value = currentLen;
            for(int i = 0; i < currentLen / 2; i++) {
                Writes.write(array, i, value, 1, true, false);
                value -= 2;
            }
            for(int i = currentLen / 2; i < currentLen; i++) {
                Writes.write(array, i, value, 1, true, false);
                value += 2;
            }
         */
        
        /*
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            //Arrays.sort(array);
            
            int value = 0;
            for(int i = 0; i < currentLen; i += 2) {
                Writes.write(array, i, value++, 0, false, false);
                if(ArrayVisualizer.shuffleEnabled()) {
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
            for(int i = 1; i < currentLen; i += 2) {
                Writes.write(array, i, value++, 0, false, false);
                if(ArrayVisualizer.shuffleEnabled()) {
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
        */
    };

    public abstract void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes);
}