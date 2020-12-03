package main;

import panes.JErrorPane;
import utils.Delays;
import utils.Highlights;
import utils.Shuffles;
import utils.Statistics;
import utils.Writes;

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

final public class ArrayManager {
    private int[] presortedArray;
    private utils.Shuffles[] shuffleTypes;
    private String[] shuffleIDs = { "Randomly", "Shuffled Odds", "Backwards", "Recursively Reversed",
            "Few Unique", "Almost Sorted", "Decreasing Random", "Logarithmic Slopes", "Triangular",
            "Scrambled Head", "Scrambled Tail", "Half Reversed", "Perlin Noise", "Perlin Noise Curve",
            "Heapified", "Interlaced", "Poplar Heapified", "Binary Search Tree", "Reversed Sawtooth",
            "Reversed Final Merge", "Double Layered", "Final Pairwise Pass", "Bell Curve", "Sawtooth",
            "Final Reversed Merge", "Final Bitonic Pass", "Pipe Organ", "Final Radix Pass", "Already Sorted" };
    
    private volatile boolean MUTABLE;

    private ArrayVisualizer ArrayVisualizer;
    private Delays Delays;
    private Highlights Highlights;
    private Shuffles Shuffles;
    private Writes Writes;
    
    public ArrayManager(ArrayVisualizer arrayVisualizer) {
        this.ArrayVisualizer = arrayVisualizer;
        this.presortedArray = new int[ArrayVisualizer.getMaximumLength()];
        
        this.Shuffles = utils.Shuffles.RANDOM;
        this.shuffleTypes = utils.Shuffles.values();
        
        this.Delays = ArrayVisualizer.getDelays();
        this.Highlights = ArrayVisualizer.getHighlights();
        this.Writes = ArrayVisualizer.getWrites();
        
        this.MUTABLE = true;
    }
    
    public boolean isLengthMutable() {
        return this.MUTABLE;
    }
    public void toggleMutableLength(boolean Bool) {
        this.MUTABLE = Bool;
    }
 
    //TODO: Fix minimum to zero
    public void initializeArray(int[] array) {
        int power = ArrayVisualizer.getPower();
        int currentLen = ArrayVisualizer.getCurrentLength();
		
        double uniqueFactor = (double)currentLen/ArrayVisualizer.getUniqueItems();
		for(int i = 0; i < currentLen; i++)
			array[i] = (int)(uniqueFactor*(int)(i/uniqueFactor))+(int)uniqueFactor/2;
		
        if (power > 1) {
            double mid = (currentLen-1)/2d;
			
			for(int i = 0; i < currentLen; i++)
				array[i] = (int)(Math.pow(array[i] - mid, power)/Math.pow(mid, power-1) + mid);
        }
    }
    
    public void initializePresortedArray() {
        for (int i = 0; i < this.presortedArray.length; i++) {
            this.presortedArray[i] = i;
        }
        
        for(int i = 0; i < Math.max((this.presortedArray.length / 10), 1); i++){
            Writes.swap(this.presortedArray, (int) (Math.random() * this.presortedArray.length), (int) (Math.random() * this.presortedArray.length), 0, true, false);
        }
    }
    
    public String[] getShuffleIDs() {
        return this.shuffleIDs;
    }
    public Shuffles[] getShuffles() {
        return this.shuffleTypes;
    }
    public Shuffles getShuffle() {
        return this.Shuffles;
    }
    public void setShuffle(Shuffles choice) {
        this.Shuffles = choice;
    }
    
    public void shuffleArray(int[] array, int currentLen, ArrayVisualizer ArrayVisualizer) {
        this.initializeArray(array);

        String tmp = ArrayVisualizer.getHeading();
        ArrayVisualizer.setHeading("Shuffling...");
        
        double speed = Delays.getSleepRatio();
        
        if(ArrayVisualizer.isActive()) {
            double sleepRatio;
            
            //TODO: Replace with continuous function
            switch(ArrayVisualizer.getLogBaseTwoOfLength()) {
            case 15: sleepRatio = 32d;  break;
            case 14: sleepRatio = 16d;  break;
            case 13: sleepRatio = 8d;   break;
            case 12: sleepRatio = 4d;   break;
            case 11: sleepRatio = 2d;   break;
            case 10: sleepRatio = 3/2d; break;
            case 9:  sleepRatio = 1d;   break;
            case 8:  sleepRatio = 3/4d; break;
            case 7:  sleepRatio = 1/2d; break;
            case 6:  sleepRatio = 1/3d; break;
            case 5:
            case 4:  sleepRatio = 1/4d; break;
            case 3:
            case 2:
            default: sleepRatio = 1/8d;
            }
            
            Delays.setSleepRatio(sleepRatio);
        }
        
        Shuffles.shuffleArray(array, this.ArrayVisualizer, Delays, Highlights, Writes);
        this.ArrayVisualizer.setShadowArray();
        
        Delays.setSleepRatio(speed);
        
        Highlights.clearAllMarks();
        ArrayVisualizer.setHeading(tmp);
    }
    
    public void refreshArray(int[] array, int currentLen, ArrayVisualizer ArrayVisualizer) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            JErrorPane.invokeErrorMessage(e);
        }
        
        ArrayVisualizer.resetAllStatistics();
        Highlights.clearAllMarks();
        
        ArrayVisualizer.setHeading("");
        this.shuffleArray(array, currentLen, ArrayVisualizer);
        
        Highlights.clearAllMarks();
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            JErrorPane.invokeErrorMessage(e);
        }
        
        ArrayVisualizer.resetAllStatistics();
    }
}