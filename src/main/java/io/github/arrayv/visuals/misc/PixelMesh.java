package io.github.arrayv.visuals.misc;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.Visual;

import java.awt.*;

/*
 *
MIT License

Copyright (c) 2020-2021 ArrayV 4.0 Team

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

public final class PixelMesh extends Visual {
    public PixelMesh(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
        if (renderer.isAuxActive()) return;

        int width = arrayVisualizer.windowWidth()-40;
        int height = arrayVisualizer.windowHeight()-50;
        int length = arrayVisualizer.getCurrentLength();

        int sqrt = (int)Math.ceil(Math.sqrt(length));
        int square = sqrt*sqrt;
        double scale = (double)length / square;

        int x = 0;
        int y = 0;
        double xStep = (double)width / sqrt;
        double yStep = (double)height / sqrt;

        for (int i = 0; i < square; i++) {
            int idx = (int)(i * scale);

            if (Highlights.fancyFinishActive() && idx < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(Color.GREEN);

            else if (Highlights.containsPosition(idx)) {
                if (arrayVisualizer.analysisEnabled()) this.mainRender.setColor(Color.LIGHT_GRAY);
                else                                   this.mainRender.setColor(Color.WHITE);
            } else this.mainRender.setColor(getIntColor(array[idx], length));

            this.mainRender.fillRect(20 + (int)(x * xStep), 40 + (int)(y * yStep),
                                     (int)((x+1)*xStep - x*xStep)+1, (int)((y+1)*yStep - y*yStep)+1);

            if (++x == sqrt) {
                x = 0;
                y++;
            }
        }
    }
}
