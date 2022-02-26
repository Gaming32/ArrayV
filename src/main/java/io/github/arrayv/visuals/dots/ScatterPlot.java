package io.github.arrayv.visuals.dots;

import java.awt.Color;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.Visual;

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

public final class ScatterPlot extends Visual {
    public ScatterPlot(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
        int offset = 20 + (int) (renderer.getXScale()/2);

        if (arrayVisualizer.linesEnabled()) {
            int lastX = 0;
            int lastY = (int) (((renderer.getViewSize() - 20)) - (array[0] + 1) * renderer.getYScale());
            this.mainRender.setStroke(arrayVisualizer.getCustomStroke(2));

            for (int i = 1, j = (int) renderer.getXScale(); i < renderer.getArrayLength(); i++) {
                if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition()) {
                    this.mainRender.setColor(Color.GREEN);
                    this.mainRender.setStroke(arrayVisualizer.getCustomStroke(4));
                } else if (Highlights.containsPosition(i)) {
                    this.mainRender.setColor(arrayVisualizer.getHighlightColor());
                    this.mainRender.setStroke(arrayVisualizer.getCustomStroke(4));
                } else if (arrayVisualizer.colorEnabled()) {
                    int val = arrayVisualizer.doingStabilityCheck() && arrayVisualizer.colorEnabled() ? arrayVisualizer.getIndexValue(array[i]): array[i];
                    this.mainRender.setColor(getIntColor(val, arrayVisualizer.getCurrentLength()));
                } else this.mainRender.setColor(Color.WHITE);

                int val = arrayVisualizer.doingStabilityCheck() && arrayVisualizer.colorEnabled() ? arrayVisualizer.getStabilityValue(array[i]): array[i];
                int y = (int) (((renderer.getViewSize() - 20)) - (val + 1) * renderer.getYScale());

                this.mainRender.drawLine(lastX + offset, renderer.getYOffset() + lastY, j + offset, renderer.getYOffset() + y);

                lastX = j;
                lastY = y;

                this.mainRender.setStroke(arrayVisualizer.getCustomStroke(2));

                int width = (int) (renderer.getXScale() * (i + 1)) - j;
                j += width;
            }
            this.mainRender.setStroke(arrayVisualizer.getDefaultStroke());
        } else {
            int dotS = renderer.getDotDimensions();

            for (int i = 0, j = 0; i < renderer.getArrayLength(); i++) {
                if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                    this.mainRender.setColor(Color.GREEN);
                else if (arrayVisualizer.colorEnabled()) {
                    int val = arrayVisualizer.doingStabilityCheck() && arrayVisualizer.colorEnabled() ? arrayVisualizer.getIndexValue(array[i]): array[i];
                    this.mainRender.setColor(getIntColor(val, arrayVisualizer.getCurrentLength()));
                } else this.mainRender.setColor(Color.WHITE);

                int val = arrayVisualizer.doingStabilityCheck() && arrayVisualizer.colorEnabled() ? arrayVisualizer.getStabilityValue(array[i]): array[i];
                int y = (int) (((renderer.getViewSize() - 20)) - (val + 1) * renderer.getYScale());

                this.mainRender.fillRect(j + offset, renderer.getYOffset() + y, dotS, dotS);

                int width = (int) (renderer.getXScale() * (i + 1)) - j;
                j += width;
            }
            this.mainRender.setColor(arrayVisualizer.getHighlightColor());

            for (int i = 0, j = 0; i < renderer.getArrayLength(); i++) {
                if (Highlights.containsPosition(i)) {
                    int val = arrayVisualizer.doingStabilityCheck() && arrayVisualizer.colorEnabled() ? arrayVisualizer.getStabilityValue(array[i]): array[i];
                    int y = (int) (((renderer.getViewSize() - 20)) - (val + 1) * renderer.getYScale());

                    this.mainRender.fillRect(j + offset - (int)(1.5*dotS), renderer.getYOffset() + y - (int)(1.5*dotS), 4*dotS, 4*dotS);
                }
                int width = (int) (renderer.getXScale() * (i + 1)) - j;
                j += width;
            }
        }
        if (arrayVisualizer.externalArraysEnabled()) {
            this.mainRender.setColor(Color.BLUE);
            this.mainRender.fillRect(0, renderer.getYOffset() + renderer.getViewSize() - 20, arrayVisualizer.currentWidth(), 1);
        }
    }
}
