package io.github.arrayv.utils;

import java.text.DecimalFormat;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;

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

public final class Delays {
    private volatile double sleepRatio;
    private volatile boolean skipped;

    private double delay;

    private volatile double currentDelay;
    private volatile boolean paused;

    private DecimalFormat formatter;

    private Sounds Sounds;

    public Delays(ArrayVisualizer arrayVisualizer) {
        this.sleepRatio = 1.0;
        this.skipped = false;

        this.formatter = arrayVisualizer.getNumberFormat();
        this.Sounds = arrayVisualizer.getSounds();
    }

    public String displayCurrentDelay() {
        if (this.skipped)
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
        return this.sleepRatio;
    }
    public void setSleepRatio(double sleepRatio) {
        this.sleepRatio = sleepRatio;
    }

    public boolean skipped() {
        return this.skipped;
    }
    public void changeSkipped(boolean skipped) {
        this.skipped = skipped;
        if (this.skipped) this.Sounds.changeNoteDelayAndFilter(1);
    }

    public boolean paused() {
        return this.paused;
    }
    public void changePaused(boolean paused) {
        this.paused = paused;
        this.Sounds.toggleSound(!paused);
    }
    public void togglePaused() {
        this.changePaused(!this.paused);;
    }

    public void sleep(double millis) {
        if (millis <= 0) {
            return;
        }

        this.delay += (millis * (1 / this.sleepRatio));
        this.currentDelay = (millis * (1 / this.sleepRatio));

        this.Sounds.changeNoteDelayAndFilter((int) this.currentDelay);

        try {
            // With this for loop, you can change the speed of sorts without waiting for the current delay to finish.
            if (!this.skipped) {
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
