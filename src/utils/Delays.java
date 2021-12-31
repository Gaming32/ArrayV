package utils;

import java.text.DecimalFormat;

import main.ArrayVisualizer;
import panes.JErrorPane;

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

final public class Delays {
    private ArrayVisualizer arrayVisualizer;

    private volatile double SLEEPRATIO;
    private volatile boolean SKIPPED;

    private double addamt;
    private double delay;
    private double nanos;

    private volatile double currentDelay;
    private volatile boolean paused;

    private DecimalFormat formatter;

    private Sounds Sounds;

    public Delays(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;

        this.SLEEPRATIO = 1.0;
        this.SKIPPED = false;
        this.addamt = 0;

        this.formatter = arrayVisualizer.getNumberFormat();
        this.Sounds = arrayVisualizer.getSounds();
    }

    public String displayCurrentDelay() {
        if (this.SKIPPED)
            return "Canceled";
        if (this.paused)
            return "Paused";

        String currDelay = "";
        if (this.currentDelay == 0) {
            currDelay = "0";
        } else if (this.currentDelay < 0.001) {
            currDelay = "< 0.001";
        } else {
            currDelay = formatter.format(this.currentDelay);
        }
        return currDelay + "ms";
    }
    //TODO: This is a mess.
    public double getDisplayedDelay() {
        return this.currentDelay;
    }
    public void setDisplayedDelay(double value) {
        this.currentDelay = value;
    }
    public void setCurrentDelay(double value) {
        this.delay = value;
    }
    public void updateCurrentDelay(double oldRatio, double newRatio) {
        this.delay = (this.delay * oldRatio) / newRatio;
        this.currentDelay = this.delay;
        this.Sounds.changeNoteDelayAndFilter((int) this.currentDelay);
        this.addamt = 0;

        if (this.currentDelay < 0) {
            this.delay = this.currentDelay = 0;
        }
    }
    //TODO: Remove when sorts receive ArrayVisualizer
    public void updateDelayForTimeSort(double value) {
        this.setDisplayedDelay(value);
        this.Sounds.changeNoteDelayAndFilter((int) value);
    }

    public double getSleepRatio() {
        return this.SLEEPRATIO;
    }
    public void setSleepRatio(double sleepRatio) {
        this.SLEEPRATIO = sleepRatio;
    }

    public boolean skipped() {
        return this.SKIPPED;
    }
    public void changeSkipped(boolean Bool) {
        this.SKIPPED = Bool;
        if (this.SKIPPED) this.Sounds.changeNoteDelayAndFilter(1);
    }

    public boolean paused() {
        return this.paused;
    }
    public void changePaused(boolean Bool) {
        this.paused = Bool;
        this.Sounds.toggleSound(!Bool);
    }
    public void togglePaused() {
        this.changePaused(!this.paused);;
    }

    public void sleep(double millis) {
        if (millis <= 0) {
            return;
        }

        this.delay += (millis * (1 / this.SLEEPRATIO));
        this.currentDelay = (millis * (1 / this.SLEEPRATIO));

        this.Sounds.changeNoteDelayAndFilter((int) this.currentDelay);

        try {
            // With this for loop, you can change the speed of sorts without waiting for the current delay to finish.
            if (!this.SKIPPED) {
                while (this.paused || this.delay >= 1) {
                    Thread.sleep(1);
                    if (!this.paused)
                        this.delay--;
                }
            } else {
                this.delay = 0;
            }
        } catch (Exception ex) {
            JErrorPane.invokeErrorMessage(ex);
        }

        this.currentDelay = 0;
    }
}