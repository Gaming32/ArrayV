package io.github.arrayv.visuals.circles;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.Visual;

import java.awt.*;

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

public final class Spiral extends Visual {

    public Spiral(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
        if (renderer.isAuxActive()) return;

        int width  = arrayVisualizer.windowWidth();
        int height = arrayVisualizer.windowHeight();

        int n = arrayVisualizer.getCurrentLength();
        double r = Math.min(width, height)/2.5;

        this.extraRender.setStroke(arrayVisualizer.getThickStroke());
        this.extraRender.setColor(arrayVisualizer.getHighlightColor());

        int[] x =  {width/2, 0, 0};
        int[] y = {height/2, 0, 0};

        double mult = (double) array[n-1] / arrayVisualizer.getCurrentLength() - 1;
        mult = 1 - mult*mult;

        double angle = Math.PI * (2d * (n - 1) / n - 0.5);
        x[2] =  width/2 + (int)(mult * r * Math.cos(angle));
        y[2] = height/2 + (int)(mult * r * Math.sin(angle));

        for (int i = 0; i < n; i++) {
            x[1] = x[2];
            y[1] = y[2];

            mult = (double) array[i] / arrayVisualizer.getCurrentLength() - 1;
            mult = 1 - mult*mult;

            x[2] =  width/2 + (int)(mult * r * Math.cos(Math.PI * (2d*i / n - 0.5)));
            y[2] = height/2 + (int)(mult * r * Math.sin(Math.PI * (2d*i / n - 0.5)));

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(Color.GREEN);

            else if (Highlights.containsPosition(i)) {
                this.mainRender.setColor(arrayVisualizer.getHighlightColor());
                this.extraRender.drawPolygon(x, y, 3);
            } else if (arrayVisualizer.colorEnabled())
                this.mainRender.setColor(getIntColor(array[i], arrayVisualizer.getCurrentLength()));

            else this.mainRender.setColor(Color.WHITE);

            this.mainRender.fillPolygon(x, y, 3);
        }
        this.extraRender.setStroke(arrayVisualizer.getDefaultStroke());
    }
}
