package visuals.bars;

import java.awt.Color;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

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

final public class Rainbow extends Visual {
    public Rainbow(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        for (int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
            int width = (int) (Renderer.getXScale() * (i + 1)) - j;
            if (width == 0) continue;

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(Color.GREEN);
            else this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));

            this.mainRender.fillRect(j + 20, Renderer.getYOffset() - 20, width, (int) (Renderer.getViewSize()));

            j += width;
        }
        if (ArrayVisualizer.analysisEnabled()) this.mainRender.setColor(Color.LIGHT_GRAY);
        else                                   this.mainRender.setColor(Color.WHITE);

        for (int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
            int width = (int) (Renderer.getXScale() * (i + 1)) - j;

            if (Highlights.containsPosition(i)) {

                this.mainRender.fillRect(j + 20, Renderer.getYOffset() - 20, Math.max(width, 2), (int) (Renderer.getViewSize()));
            }
            j += width;
        }
        if (ArrayVisualizer.externalArraysEnabled()) {
            this.mainRender.setColor(Color.BLUE);
            this.mainRender.fillRect(0, Renderer.getYOffset() + Renderer.getViewSize() - 20, ArrayVisualizer.currentWidth(), 1);
        }
    }
}