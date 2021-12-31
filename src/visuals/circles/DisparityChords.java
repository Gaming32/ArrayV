package visuals.circles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;

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

final public class DisparityChords extends Visual {

    public DisparityChords(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        if (Renderer.auxActive) return;

        int width  = ArrayVisualizer.windowWidth();
        int height = ArrayVisualizer.windowHeight();

        int n = ArrayVisualizer.getCurrentLength();
        double r = Math.min(width, height)/2.5;

        this.mainRender.setStroke(ArrayVisualizer.getThinStroke());

        for (int i = n-1; i >= 0; i--) {
            this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));

            int ax =  width/2 + (int)(r * Math.cos(Math.PI * (2d*i / n - 0.5)));
            int ay = height/2 + (int)(r * Math.sin(Math.PI * (2d*i / n - 0.5)));
            int bx =  width/2 + (int)(r * Math.cos(Math.PI * (2d*array[i] / n - 0.5)));
            int by = height/2 + (int)(r * Math.sin(Math.PI * (2d*array[i] / n - 0.5)));

            this.mainRender.drawLine(ax, ay, bx, by);
        }
        this.mainRender.setStroke(ArrayVisualizer.getDefaultStroke());

        for (int i = 0; i < n; i++) {
            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition()) {
                this.mainRender.setColor(Color.GREEN);

                int ax =  width/2 + (int)(r * Math.cos(Math.PI * (2d*i / n - 0.5)));
                int ay = height/2 + (int)(r * Math.sin(Math.PI * (2d*i / n - 0.5)));
                int bx =  width/2 + (int)(r * Math.cos(Math.PI * (2d*array[i] / n - 0.5)));
                int by = height/2 + (int)(r * Math.sin(Math.PI * (2d*array[i] / n - 0.5)));

                this.mainRender.drawLine(ax, ay, bx, by);
            } else if (Highlights.containsPosition(i)) {
                if (ArrayVisualizer.analysisEnabled()) this.mainRender.setColor(Color.LIGHT_GRAY);
                else                                   this.mainRender.setColor(Color.WHITE);

                int ax =  width/2 + (int)(r * Math.cos(Math.PI * (2d*i / n - 0.5)));
                int ay = height/2 + (int)(r * Math.sin(Math.PI * (2d*i / n - 0.5)));
                int bx =  width/2 + (int)(r * Math.cos(Math.PI * (2d*array[i] / n - 0.5)));
                int by = height/2 + (int)(r * Math.sin(Math.PI * (2d*array[i] / n - 0.5)));

                this.mainRender.drawLine(ax, ay, bx, by);
            }
        }
    }
}