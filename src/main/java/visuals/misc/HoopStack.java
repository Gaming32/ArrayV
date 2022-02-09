package visuals.misc;

import java.awt.Color;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

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

final public class HoopStack extends Visual {
    public HoopStack(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    private void drawEllipseFromCenter(int x, int y, int rx, int ry) {
        this.mainRender.drawOval(x - rx, y - ry, 2*rx, 2*ry);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        if (Renderer.auxActive) return;

        int width = ArrayVisualizer.windowWidth();
        int height = ArrayVisualizer.windowHeight();
        int length = ArrayVisualizer.getCurrentLength();

        int radiusX = height / 3;
        int radiusY = height / 9;

        this.mainRender.setStroke(ArrayVisualizer.getThinStroke());

        for (int i = length - 1; i >= 0; i--) {
            double scale = (array[i] + 1) / (double) (length + 1);

            int y = (int) ((height - radiusY * 4) * i / (double) (length - 1));

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(Color.GREEN);

            else if (Highlights.containsPosition(i)) {
                if (ArrayVisualizer.analysisEnabled()) this.mainRender.setColor(Color.LIGHT_GRAY);
                else                                   this.mainRender.setColor(Color.WHITE);

                this.mainRender.setStroke(ArrayVisualizer.getDefaultStroke());
            }
            else this.mainRender.setColor(getIntColor(array[i], length));

            this.drawEllipseFromCenter(width / 2, y + radiusY * 2, (int) (scale * radiusX + 0.5), (int) (scale * radiusY + 0.5));
            this.mainRender.setStroke(ArrayVisualizer.getThinStroke());
        }
        this.mainRender.setStroke(ArrayVisualizer.getDefaultStroke());
    }
}