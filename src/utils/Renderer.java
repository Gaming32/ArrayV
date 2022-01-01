package utils;

import main.ArrayVisualizer;
import visuals.VisualStyles;

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

// TODO: Many of these methods should exist solely in visual classes

final class WindowState {
    private boolean windowUpdated;
    private boolean windowResized;

    public WindowState(boolean windowUpdate, boolean windowResize) {
        this.windowUpdated = windowUpdate;
        this.windowResized = windowResize;
    }

    public boolean updated() {
        return this.windowUpdated;
    }

    public boolean resized() {
        return this.windowResized;
    }
}

final public class Renderer {
    private volatile double xScale;
    private volatile double yScale;

    private volatile int yoffset;
    private volatile int vsize;
    public volatile boolean auxActive;

    private volatile int length;

    private volatile int amt;

    private int linkedpixdrawx; //TODO: Change names
    private int linkedpixdrawy;

    private int doth; //TODO: Change names
    private int dotw;
    private int dots; //TODO: Change name to dotDims/dotDimensions

    public Renderer(ArrayVisualizer ArrayVisualizer) {
        ArrayVisualizer.setWindowHeight();
        ArrayVisualizer.setWindowWidth();
    }

    public double getXScale() {
        return this.xScale;
    }
    public double getYScale() {
        return this.yScale;
    }
    public int getOffset() {
        return this.amt;
    }
    public int getYOffset() {
        return this.yoffset;
    }
    public int getViewSize() {
        return this.vsize;
    }
    public int halfViewSize() {
        return this.vsize / 2;
    }
    public int getArrayLength() {
        return this.length;
    }
    public int getDotWidth() {
        return this.dotw;
    }
    public int getDotHeight() {
        return this.doth;
    }
    public int getDotDimensions() {
        return this.dots;
    }
    public int getLineX() {
        return this.linkedpixdrawx;
    }
    public int getLineY() {
        return this.linkedpixdrawy;
    }

    public void setOffset(int amount) {
        this.amt = amount;
    }
    public void setLineX(int x) {
        this.linkedpixdrawx = x;
    }
    public void setLineY(int y) {
        this.linkedpixdrawy = y;
    }

    public static void createRenders(ArrayVisualizer ArrayVisualizer) {
        ArrayVisualizer.createVolatileImage();
        ArrayVisualizer.setMainRender();
        ArrayVisualizer.setExtraRender();
    }

    public static void initializeVisuals(ArrayVisualizer ArrayVisualizer) {
        Renderer.createRenders(ArrayVisualizer);
        ArrayVisualizer.repositionFrames();
    }

    public static void updateGraphics(ArrayVisualizer ArrayVisualizer) {
        Renderer.createRenders(ArrayVisualizer);
        ArrayVisualizer.updateVisuals();
    }

    private static WindowState checkWindowResizeAndReposition(ArrayVisualizer ArrayVisualizer) {
        boolean windowUpdate = false;
        boolean windowResize = false;

        if (ArrayVisualizer.currentHeight() != ArrayVisualizer.windowHeight()) {
            windowUpdate = true;
            windowResize = true;
        }
        if (ArrayVisualizer.currentWidth() != ArrayVisualizer.windowWidth()) {
            windowUpdate = true;
            windowResize = true;
        }
        if (ArrayVisualizer.currentX() != ArrayVisualizer.windowXCoordinate()) {
            windowUpdate = true;
        }
        if (ArrayVisualizer.currentY() != ArrayVisualizer.windowYCoordinate()) {
            windowUpdate = true;
        }

        return new WindowState(windowUpdate, windowResize);
    }

    public void updateVisualsStart(ArrayVisualizer ArrayVisualizer) {
        WindowState WindowState = checkWindowResizeAndReposition(ArrayVisualizer);

        if (WindowState.updated()) {
            ArrayVisualizer.repositionFrames();
            ArrayVisualizer.updateCoordinates();

            /*
            if (v != null && v.isVisible())
                v.reposition();
            */

            if (WindowState.resized()) {
                ArrayVisualizer.updateDimensions();
                updateGraphics(ArrayVisualizer);
            }
        }

        ArrayVisualizer.renderBackground();

        //CURRENT = WINDOW
        //WINDOW = C VARIABLES

        this.yScale = (double) (this.vsize) / ArrayVisualizer.getCurrentLength();

        this.dotw = (int) (2 * (ArrayVisualizer.currentWidth()  / 640.0));

        this.vsize = (ArrayVisualizer.currentHeight() - 96) / (ArrayVisualizer.externalArraysEnabled() ? Math.min(ArrayVisualizer.getArrays().size(), 7) : 1);
        this.yoffset = 96;
    }

    private void updateVisualsPerArray(ArrayVisualizer ArrayVisualizer, int[] array, int length) {

        //CURRENT = WINDOW
        //WINDOW = C VARIABLES

        this.xScale = (double) (ArrayVisualizer.currentWidth() - 40) / length;

        this.amt = 0; //TODO: rename to barCount

        this.linkedpixdrawx = 0;
        this.linkedpixdrawy = 0;

        this.doth = (int) (2 * (this.vsize / 480.0));
        this.dots = (this.dotw + this.doth) / 2; //TODO: Does multiply/divide by 2 like this cancel out??

        this.length = length;

        ArrayVisualizer.resetMainStroke();
    }

    public void drawVisual(VisualStyles VisualStyles, int[][] arrays, ArrayVisualizer ArrayVisualizer, Highlights Highlights) {
        if (ArrayVisualizer.externalArraysEnabled()) {
            this.auxActive = true;
            for (int i = Math.min(arrays.length - 1, 6); i > 0; i--) {
                if (arrays[i] != null) {
                    this.updateVisualsPerArray(ArrayVisualizer, arrays[i], arrays[i].length);
                    VisualStyles.drawVisual(arrays[i], ArrayVisualizer, this, Highlights);
                    this.yoffset += this.vsize;
                }
            }
            this.auxActive = false;
        }
        this.updateVisualsPerArray(ArrayVisualizer, arrays[0], ArrayVisualizer.getCurrentLength());
        VisualStyles.drawVisual(arrays[0], ArrayVisualizer, this, Highlights);
    }
}