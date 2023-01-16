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

public final class ColorCircle extends Visual {

    public ColorCircle(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
        if (renderer.isAuxActive()) return;

        int width  = arrayVisualizer.windowWidth();
        int height = arrayVisualizer.windowHeight();

        int n = arrayVisualizer.getCurrentLength();

        double r = Math.min(width, height)/2.75;
        int p = (int)(r/12);

        int[] x  = new int[3];
        int[] y  = new int[3];

        int[] px = new int[3];
        int[] py = new int[3];

        this.extraRender.setColor(Color.WHITE);

        x[0] =  width/2;
        y[0] = height/2;

        double angle = Math.PI * (2d * (n - 1) / n - 0.5);
        x[2] =  width/2 + (int)(r * Math.cos(angle));
        y[2] = height/2 + (int)(r * Math.sin(angle));

        for (int i = 0; i < n; i++) {
            x[1] = x[2];
            y[1] = y[2];

            x[2] =  width/2 + (int)(r * Math.cos(Math.PI * (2d*i / n - 0.5)));
            y[2] = height/2 + (int)(r * Math.sin(Math.PI * (2d*i / n - 0.5)));

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(Color.GREEN);

            else {
                this.mainRender.setColor(getIntColor(array[i], arrayVisualizer.getCurrentLength()));

                if (Highlights.containsPosition(i)) {
                    if (arrayVisualizer.analysisEnabled()) this.extraRender.setColor(Color.LIGHT_GRAY);
                    else                                  this.extraRender.setColor(Color.WHITE);

                    double angle1 = Math.PI * ((2d * i - 1) / n - 0.5);
                    px[0] =  width/2 + (int)((r + p/4) * Math.cos(angle1));
                    py[0] = height/2 + (int)((r + p/4) * Math.sin(angle1));

                    double angle2 = Math.PI * ((2d * i - 1) / n - 0.67);
                    px[1] = px[0] + (int)(p * Math.cos(angle2));
                    py[1] = py[0] + (int)(p * Math.sin(angle2));

                    double angle3 = Math.PI * ((2d * i - 1) / n - 0.33);
                    px[2] = px[0] + (int)(p * Math.cos(angle3));
                    py[2] = py[0] + (int)(p * Math.sin(angle3));

                    this.extraRender.fillPolygon(px, py, 3);
                }
            }

            if (x[1] != x[2] || y[1] != y[2]) this.mainRender.fillPolygon(x, y, 3);
        }
    }
}
