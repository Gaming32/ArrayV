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

public final class DisparityChords extends Visual {

    public DisparityChords(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
        if (renderer.isAuxActive()) return;

        int width  = arrayVisualizer.windowWidth();
        int height = arrayVisualizer.windowHeight();

        int n = arrayVisualizer.getCurrentLength();
        double r = Math.min(width, height)/2.5;

        this.mainRender.setStroke(arrayVisualizer.getThinStroke());

        for (int i = n-1; i >= 0; i--) {
            this.mainRender.setColor(getIntColor(array[i], arrayVisualizer.getCurrentLength()));

            int ax =  width/2 + (int)(r * Math.cos(Math.PI * (2d*i / n - 0.5)));
            int ay = height/2 + (int)(r * Math.sin(Math.PI * (2d*i / n - 0.5)));
            int bx =  width/2 + (int)(r * Math.cos(Math.PI * (2d*array[i] / n - 0.5)));
            int by = height/2 + (int)(r * Math.sin(Math.PI * (2d*array[i] / n - 0.5)));

            this.mainRender.drawLine(ax, ay, bx, by);
        }
        this.mainRender.setStroke(arrayVisualizer.getDefaultStroke());

        for (int i = 0; i < n; i++) {
            double x = r * Math.cos(Math.PI * (2d * i / n - 0.5));
            double y = r * Math.sin(Math.PI * (2d * i / n - 0.5));
            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition()) {
                this.mainRender.setColor(Color.GREEN);

                int ax =  width/2 + (int)x;
                int ay = height/2 + (int)y;
                int bx =  width/2 + (int)(r * Math.cos(Math.PI * (2d*array[i] / n - 0.5)));
                int by = height/2 + (int)(r * Math.sin(Math.PI * (2d*array[i] / n - 0.5)));

                this.mainRender.drawLine(ax, ay, bx, by);
            } else if (Highlights.containsPosition(i)) {
                if (arrayVisualizer.analysisEnabled()) this.mainRender.setColor(Color.LIGHT_GRAY);
                else                                   this.mainRender.setColor(Color.WHITE);

                int ax =  width/2 + (int)x;
                int ay = height/2 + (int)y;
                int bx =  width/2 + (int)(r * Math.cos(Math.PI * (2d*array[i] / n - 0.5)));
                int by = height/2 + (int)(r * Math.sin(Math.PI * (2d*array[i] / n - 0.5)));

                this.mainRender.drawLine(ax, ay, bx, by);
            }
        }
    }
}
