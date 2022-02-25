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
Copyright (c) 2021 ArrayV 4.0 Team

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

public final class DisparityDots extends Visual {

    public DisparityDots(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
        if (renderer.isAuxActive()) return;

        int width  = arrayVisualizer.windowWidth();
        int height = arrayVisualizer.windowHeight();

        int n = arrayVisualizer.getCurrentLength();
        double r = Math.min(width, height)/2.5;

        if (arrayVisualizer.linesEnabled()) {
            double disp = (1 + Math.cos((Math.PI * (array[n-1] - (n-1))) / (arrayVisualizer.getCurrentLength() * 0.5))) * 0.5;
            int lastX =  width/2 + (int)(disp * r * Math.cos(Math.PI * (2d*(n-1) / n - 0.5)));
            int lastY = height/2 + (int)(disp * r * Math.sin(Math.PI * (2d*(n-1) / n - 0.5)));
            this.mainRender.setStroke(arrayVisualizer.getCustomStroke(2));

            for (int i = 0; i < n; i++) {
                if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition()) {
                    this.mainRender.setColor(Color.GREEN);
                    this.mainRender.setStroke(arrayVisualizer.getCustomStroke(4));
                } else if (Highlights.containsPosition(i)) {
                    this.mainRender.setColor(arrayVisualizer.getHighlightColor());
                    this.mainRender.setStroke(arrayVisualizer.getCustomStroke(4));
                } else if (arrayVisualizer.colorEnabled())
                    this.mainRender.setColor(getIntColor(array[i], arrayVisualizer.getCurrentLength()));

                else this.mainRender.setColor(Color.WHITE);

                disp = (1 + Math.cos((Math.PI * (array[i] - i)) / (arrayVisualizer.getCurrentLength() * 0.5))) * 0.5;
                int x =  width/2 + (int)(disp * r * Math.cos(Math.PI * (2d*i / n - 0.5)));
                int y = height/2 + (int)(disp * r * Math.sin(Math.PI * (2d*i / n - 0.5)));

                this.mainRender.drawLine(lastX, lastY, x, y);
                this.mainRender.setStroke(arrayVisualizer.getCustomStroke(2));

                lastX = x;
                lastY = y;
            }
            this.mainRender.setStroke(arrayVisualizer.getDefaultStroke());
        } else {
            int dotS = renderer.getDotDimensions();

            for (int i = 0; i < n; i++) {
                if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                    this.mainRender.setColor(Color.GREEN);

                else if (arrayVisualizer.colorEnabled())
                    this.mainRender.setColor(getIntColor(array[i], arrayVisualizer.getCurrentLength()));

                else this.mainRender.setColor(Color.WHITE);

                double disp = (1 + Math.cos((Math.PI * (array[i] - i)) / (arrayVisualizer.getCurrentLength() * 0.5))) * 0.5;
                int x =  width/2 + (int)(disp * r * Math.cos(Math.PI * (2d*i / n - 0.5)));
                int y = height/2 + (int)(disp * r * Math.sin(Math.PI * (2d*i / n - 0.5)));

                this.mainRender.fillRect(x, y, dotS, dotS);
            }
            this.mainRender.setColor(arrayVisualizer.getHighlightColor());

            for (int i = 0; i < n; i++) {
                if (Highlights.containsPosition(i)) {
                    double disp = (1 + Math.cos((Math.PI * (array[i] - i)) / (arrayVisualizer.getCurrentLength() * 0.5))) * 0.5;
                    int x =  width/2 + (int)(disp * r * Math.cos(Math.PI * (2d*i / n - 0.5)));
                    int y = height/2 + (int)(disp * r * Math.sin(Math.PI * (2d*i / n - 0.5)));

                    this.mainRender.fillRect(x - 2*dotS, y - 2*dotS, 4*dotS, 4*dotS);
                }
            }
        }
    }
}
