package main;

import panes.JErrorPane;
import utils.Delays;
import utils.Highlights;
import utils.Shuffles;
import utils.Distributions;
import utils.Statistics;
import utils.Writes;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy
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

final public class ArrayManager {
    private int[] presortedArray;
    private utils.Shuffles[] shuffleTypes;
	private utils.Distributions[] distributionTypes;
    private String[] shuffleIDs/* = { 
			"Randomly", "Backwards", "Slight Shuffle", "No Shuffle", "Sorted", "Reverse Sorted", 
            "Scrambled Tail", "Scrambled Head", "Noisy", "Scrambled Odds",
			"Final Merge Pass", "Sawtooth", "Reversed Final Merge", "Reversed Sawtooth", "Pipe Organ", "Final Bitonic Pass",
			"Interlaced", "Double Layered", "Final Radix", "Recursive Final Radix", 
            "Half Rotation", "Half Reversed", "BST Traversal", "Logarithmic Slopes",
            "Heapified", "Reversed Poplarified", "First Circle Pass", "Final Pairwise Pass",
			"Recursive Reversal", "Gray Code Fractal", "Sierpinski Triangle",
            "Triangular"
			}*/;
    private String[] distributionIDs/* = {
			"Linear", "Few Unique", "Random", 
			"Quadratic", "Square Root", "Centered Cubic", "Centered Quintic",
			"Perlin Noise", "Perlin Noise Curve", "Bell Curve", 
			"Ruler", "Blancmange Curve", "Cantor Function", 
			"Sum of Divisors", "Fly Straight, Damnit!", "Decreasing Random"
			}*/;
	
    private volatile boolean MUTABLE;

    private ArrayVisualizer ArrayVisualizer;
    private Delays Delays;
    private Highlights Highlights;
    private Shuffles Shuffles;
	private Distributions Distributions;
    private Writes Writes;
    
    public ArrayManager(ArrayVisualizer arrayVisualizer) {
        this.ArrayVisualizer = arrayVisualizer;
        this.presortedArray = new int[ArrayVisualizer.getMaximumLength()];
        
        this.Shuffles = utils.Shuffles.RANDOM;
		this.Distributions = utils.Distributions.LINEAR;
        this.shuffleTypes = utils.Shuffles.values();
		this.distributionTypes = utils.Distributions.values();
        
        this.Delays = ArrayVisualizer.getDelays();
        this.Highlights = ArrayVisualizer.getHighlights();
        this.Writes = ArrayVisualizer.getWrites();

        this.shuffleIDs = new String[this.shuffleTypes.length];
        for (int i = 0; i < this.shuffleTypes.length; i++)
            this.shuffleIDs[i] = this.shuffleTypes[i].getName();

        this.distributionIDs = new String[this.distributionTypes.length];
        for (int i = 0; i < this.distributionTypes.length; i++)
            this.distributionIDs[i] = this.distributionTypes[i].getName();
        
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
        int currentLen = ArrayVisualizer.getCurrentLength();
		
		int[] temp = new int[currentLen];
		Distributions.initializeArray(temp, this.ArrayVisualizer);
		
        double uniqueFactor = (double)currentLen/ArrayVisualizer.getUniqueItems();
		for(int i = 0; i < currentLen; i++)
			temp[i] = (int)(uniqueFactor*(int)(temp[i]/uniqueFactor))+(int)uniqueFactor/2;
		
		System.arraycopy(temp, 0, array, 0, currentLen);
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
	
	public String[] getDistributionIDs() {
        return this.distributionIDs;
    }
    public Distributions[] getDistributions() {
        return this.distributionTypes;
    }
    public Distributions getDistribution() {
        return this.Distributions;
    }
    public void setDistribution(Distributions choice) {
        this.Distributions = choice;
        this.Distributions.selectDistribution(ArrayVisualizer.getArray(), ArrayVisualizer);
        this.initializeArray(ArrayVisualizer.getArray());
    }
    
    public void shuffleArray(int[] array, int currentLen, ArrayVisualizer ArrayVisualizer) {
        this.initializeArray(array);

        String tmp = ArrayVisualizer.getHeading();
        ArrayVisualizer.setHeading("Shuffling...");
        
        double speed = Delays.getSleepRatio();
        
        if(ArrayVisualizer.isActive()) {
            double sleepRatio = ArrayVisualizer.getCurrentLength()/1024d;
            Delays.setSleepRatio(sleepRatio);
        }
        
		Shuffles tempShuffle = this.Shuffles;
		if(Distributions == Distributions.RANDOM || Distributions == Distributions.EQUAL)
			this.Shuffles = Shuffles.ALREADY;
		Shuffles.shuffleArray(array, this.ArrayVisualizer, Delays, Highlights, Writes);
		this.Shuffles = tempShuffle;
		
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