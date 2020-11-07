package threads;

import java.lang.reflect.Constructor;

import javax.swing.JOptionPane;

import main.ArrayManager;
import main.ArrayVisualizer;
import panes.JEnhancedOptionPane;
import panes.JErrorPane;
import sorts.templates.Sort;
import utils.Delays;
import utils.Sounds;
import utils.Timer;

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

final public class RunDistributionSort {
    private ArrayManager arrayManager;
    private ArrayVisualizer arrayVisualizer;
    private Delays delayOps;
    private Sounds sounds;
    private Timer realTimer;
    
    private Object[] inputOptions;
    
    public RunDistributionSort(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;
        this.arrayManager = arrayVisualizer.getArrayManager();
        this.delayOps = arrayVisualizer.getDelays();
        this.sounds = arrayVisualizer.getSounds();
        this.realTimer = arrayVisualizer.getTimer();
        
        this.inputOptions = new Object[]{"Enter", "Use default"};
    }
    
    private String getTimeSortEstimate(int bucketCount) {
        String timeString = "";
        String timeUnit;
        
        int seconds = Math.max(((arrayVisualizer.getCurrentLength() * bucketCount) / 1000), 1);
        int minutes;
        int hours;
        long days;
        
        if(seconds >= 60) {
            minutes = Math.round(seconds / 60);
            
            if(minutes >= 60) {
                hours = Math.round(minutes / 60);
                
                if(hours >= 24) {
                    days = Math.round(hours / 24);
                    
                    if(days < 2)  timeUnit = "day";
                    else          timeUnit = "days";
                    
                    timeString = "" + arrayVisualizer.getNumberFormat().format(days) + " " + timeUnit + " ";
                }
                else {
                    if(hours < 2) timeUnit = "hour";
                    else          timeUnit = "hours";
                    
                    timeString = "" + hours + " " + timeUnit + " ";
                }
            }
            else {
                if(minutes < 2) timeUnit = "minute";
                else            timeUnit = "minutes";
                
                timeString = "" + minutes + " " + timeUnit + " ";
            }
        }
        else {
            if(seconds < 2) timeUnit = "second";
            else            timeUnit = "seconds";
            
            timeString = "" + seconds + " " + timeUnit + " ";
        }
        
        return timeString;
    }
    
    private int getCustomInput(String text) throws Exception {
        String input = JEnhancedOptionPane.showInputDialog("Customize Sort", text, this.inputOptions);
        int integer = Integer.parseInt(input);
        return Math.abs(integer);
    }
    
    public void ReportDistributionSort(int[] array, int selection) {
        if(arrayVisualizer.getSortingThread() != null && arrayVisualizer.getSortingThread().isAlive())
            return;

        //TODO: This code is bugged! It causes the program to forget the sleep ratio specified by the user!
        if(delayOps.skipped()) {
            delayOps.setSleepRatio(1);
            delayOps.changeSkipped(false);
        }

        double storeVol = sounds.getVolume();
        
        arrayVisualizer.setCategory("Distribution Sorts");
        
        sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread() {
            @SuppressWarnings("unused")
            @Override
            public void run(){
                try {
                    Class<?> sortClass = Class.forName(arrayVisualizer.getDistributionSorts()[0][selection]);
                    Constructor<?> newSort = sortClass.getConstructor(new Class[] {ArrayVisualizer.class});
                    Sort sort = (Sort) newSort.newInstance(RunDistributionSort.this.arrayVisualizer);

                    int bucketCount;
                    
                    if(sort.getRunSortName().equals("Timesort")) {
                        try {
                            bucketCount = RunDistributionSort.this.getCustomInput("Enter delay per number in milliseconds:");
                        }
                        catch(Exception e) {
                            bucketCount = 10;
                        }
                    }
                    else {
                        if(sort.usesBuckets()) {
                            if(sort.isRadixSort()) {
                                try {
                                    bucketCount = RunDistributionSort.this.getCustomInput("Enter the base for this sort:");
                                }
                                catch(Exception e) {
                                    bucketCount = 4;
                                }   
                            }
                            else if(sort.getRunSortName().contains("Shatter")) {
                                try {
                                    bucketCount = RunDistributionSort.this.getCustomInput("Enter the size for each partition:");
                                }
                                catch(Exception e) {
                                    bucketCount = arrayVisualizer.getCurrentLength() / 16;
                                }
                            }
                            else {
                                try {
                                    bucketCount = RunDistributionSort.this.getCustomInput("How many buckets will this sort use?");
                                }
                                catch(Exception e) {
                                    bucketCount = 16;
                                }
                            }
                            if(bucketCount < 2) bucketCount = 2;
                        }
                        else {
                            bucketCount = 0;
                        }
                    }
                    
                    arrayManager.toggleMutableLength(false);
                    arrayManager.refreshArray(array, arrayVisualizer.getCurrentLength(), arrayVisualizer);
                
                    boolean goAhead;
                    
                    if(sort.isUnreasonablySlow() && arrayVisualizer.getCurrentLength() > sort.getUnreasonableLimit()) {
                        goAhead = false;
                       
                        if(sort.getRunSortName().equals("Timesort")) {
                            Object[] options = { "Continue", "Cancel" };

                            int warning = JOptionPane.showOptionDialog(arrayVisualizer.getMainWindow(), "Time Sort will take at least " + getTimeSortEstimate(bucketCount)
                                                                     + "to complete. Once it starts, you cannot skip this sort.", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                                                                     null, options, options[1]);

                            if(warning == 0) goAhead = true;
                            else goAhead = false;

                        }
                        else {
                            Object[] options = { "Let's see how bad " + sort.getRunSortName() + " is!", "Cancel" };

                            if(sort.isBogoSort()) {
                                int warning = JOptionPane.showOptionDialog(arrayVisualizer.getMainWindow(), "Even at a high speed, "
                                                                         + sort.getRunSortName() + "ing " + arrayVisualizer.getCurrentLength()
                                                                         + " numbers will almost certainly not finish in a reasonable amount of time. "
                                                                         + "Are you sure you want to continue?", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                                                                         null, options, options[1]);
                                if(warning == 0) goAhead = true;
                                else goAhead = false;
                            }
                            else {
                                //Currently, no distribution sort calls this message. It's here if you want to include a sort that might use it in the future.
                                int warning = JOptionPane.showOptionDialog(arrayVisualizer.getMainWindow(), "Even at a high speed, " 
                                                                         + sort.getRunSortName() + "ing " + arrayVisualizer.getCurrentLength()
                                                                         + " numbers will not finish in a reasonable amount of time. "
                                                                         + "Are you sure you want to continue?", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                                                                         null, options, options[1]);

                                if(warning == 0) goAhead = true;
                                else goAhead = false;
                            }
                        }
                    }
                    else {
                        goAhead = true;
                    }
                    
                    if(sort.getRunSortName().equals("In-Place LSD Radix")) {
                        sounds.changeVolume(0.01); // Here to protect your ears :)
                    }
                    
                    if(goAhead) {
                        arrayVisualizer.setHeading(sort.getRunSortName());
                        
                        realTimer.enableRealTimer();
                        sort.runSort(array, arrayVisualizer.getCurrentLength(), bucketCount);
                    }
                    else {
                        arrayManager.initializeArray(array);
                    }
                }
                catch(Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                arrayVisualizer.endSort();
                arrayManager.toggleMutableLength(true);
                sounds.changeVolume(storeVol);
                sounds.toggleSound(false);
            }
        });
        
        arrayVisualizer.runSortingThread();
    }
}