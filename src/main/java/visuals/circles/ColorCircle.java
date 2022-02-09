package visuals.circles;

import java.awt.Color;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

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

final public class ColorCircle extends Visual {

    public ColorCircle(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        if (Renderer.auxActive) return;

        int width  = ArrayVisualizer.windowWidth();
        int height = ArrayVisualizer.windowHeight();

        int n = ArrayVisualizer.getCurrentLength();

        double r = Math.min(width, height)/2.75;
        int p = (int)(r/12);

        int[] x  = new int[3];
        int[] y  = new int[3];

        int[] px = new int[3];
        int[] py = new int[3];

        this.extraRender.setColor(Color.WHITE);

        x[0] =  width/2;
        y[0] = height/2;

        x[2] =  width/2 + (int)(r * Math.cos(Math.PI * (2d*(n-1) / n - 0.5)));
        y[2] = height/2 + (int)(r * Math.sin(Math.PI * (2d*(n-1) / n - 0.5)));

        for (int i = 0; i < n; i++) {
            x[1] = x[2];
            y[1] = y[2];

            x[2] =  width/2 + (int)(r * Math.cos(Math.PI * (2d*i / n - 0.5)));
            y[2] = height/2 + (int)(r * Math.sin(Math.PI * (2d*i / n - 0.5)));

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(Color.GREEN);

            else {
                this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));

                if (Highlights.containsPosition(i)) {
                    if (ArrayVisualizer.analysisEnabled()) this.extraRender.setColor(Color.LIGHT_GRAY);
                    else                                  this.extraRender.setColor(Color.WHITE);

                    px[0] =  width/2 + (int)((r + p/4) * Math.cos(Math.PI * ((2d*i - 1) / n - 0.5)));
                    py[0] = height/2 + (int)((r + p/4) * Math.sin(Math.PI * ((2d*i - 1) / n - 0.5)));

                    px[1] = px[0] + (int)(p * Math.cos(Math.PI * ((2d*i - 1) / n - 0.67)));
                    py[1] = py[0] + (int)(p * Math.sin(Math.PI * ((2d*i - 1) / n - 0.67)));

                    px[2] = px[0] + (int)(p * Math.cos(Math.PI * ((2d*i - 1) / n - 0.33)));
                    py[2] = py[0] + (int)(p * Math.sin(Math.PI * ((2d*i - 1) / n - 0.33)));

                    this.extraRender.fillPolygon(px, py, 3);
                }
            }

            if (x[1] != x[2] || y[1] != y[2]) this.mainRender.fillPolygon(x, y, 3);
        }
    }
}