package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

import java.text.DecimalFormat;
import java.util.Hashtable;

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

public final class Timer {
    private final DecimalFormat formatter;

    private volatile String minuteFormat;
    private volatile String secondFormat;

    private volatile int elapsedTime;
    private volatile double realTimer;
    private volatile boolean useRealTimer;
    private volatile double sortRunTime;
    private volatile boolean timerEnabled;

    private String operation;
    private final Hashtable<String, Double> categoricalTimes;

    private long timeStart;
    private long timeStop;

    public Timer(ArrayVisualizer arrayVisualizer) {
        this.useRealTimer = true;

        this.timeStart = 0;
        this.timeStop = 0;

        this.formatter = arrayVisualizer.getNumberFormat();

        this.categoricalTimes = new Hashtable<>();
    }

    public String getVisualTime() {
        if (this.timerEnabled) {
            this.elapsedTime = (int) ((System.nanoTime() - this.sortRunTime) / 1e+9);

            secondFormat = "" + ((this.elapsedTime % 60) / 10) + (this.elapsedTime % 10);
            minuteFormat = (this.elapsedTime / 60) + ":" + secondFormat;
        }

        if (!this.timerEnabled && this.elapsedTime == 0) return "-:--";
        else if (this.elapsedTime >= 60)                 return minuteFormat;
        else if (this.elapsedTime >= 1)                  return "0:" + secondFormat;
        else                                             return "0:00";
    }

    public String prettifyTime(double time) {
        double realTime = time * 1e-6d;

        if (!this.useRealTimer) {
            return "Disabled";
        } else if (realTime == 0) {
            if (this.timerEnabled) return "0.000ms";
            else                   return "---ms";
        } else if (realTime < 0.001)    return "< 0.001ms";
        else if (realTime >= 60000.000) return "~" + this.formatter.format((int) (realTime / 60000)) + "m" + (int) ((realTime % 60000) / 1000) + "s";
        else if (realTime >= 1000.000)  return "~" + this.formatter.format(realTime / 1000) + "s";
        else                            return "~" + this.formatter.format(realTime) + "ms";
    }

    public String getRealTime() {
        return prettifyTime(this.realTimer);
    }

    public void toggleRealTimer(boolean useRealTimer) {
        this.useRealTimer = useRealTimer;
    }

    public void enableRealTimer() {
        if (useRealTimer) this.timerEnabled = true;
        this.sortRunTime = System.nanoTime();
        this.realTimer = 0;
    }

    public void disableRealTimer() {
        this.timerEnabled = false;
    }

    public boolean timerEnabled() {
        return this.timerEnabled;
    }

    public void startLap(String message) {
        this.operation = message;
        this.categoricalTimes.putIfAbsent(message, 0d);
        if (this.timerEnabled) this.timeStart = System.nanoTime();
    }

    public void startLap() {
        startLap("");
    }

    public void stopLap() {
        this.timeStop = System.nanoTime();
        if (this.timerEnabled) {
            double timeDiff = timeStop - timeStart;
            this.realTimer += timeDiff;
            this.categoricalTimes.put(this.operation, categoricalTimes.get(this.operation) + timeDiff);
        }
    }

    public Hashtable<String, Double> getCategoricalTimes() {
        return this.categoricalTimes;
    }

    void manualAddTime(long milliseconds) {
        this.realTimer += milliseconds;
    }

    public void manualSetTime(long milliseconds) {
        this.realTimer = milliseconds;
    }
}
