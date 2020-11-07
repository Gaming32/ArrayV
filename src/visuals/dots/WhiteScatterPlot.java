package visuals.dots;

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

final public class WhiteScatterPlot extends Visual {
    public WhiteScatterPlot(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++) {
            if(Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition()) {
                this.mainRender.setColor(Color.GREEN);
            }
            else this.mainRender.setColor(Color.WHITE);

            int y = 0;
            int width = (int) (Renderer.getXScale() * (i + 1)) - Renderer.getOffset();

            boolean drawRect = false;
            if(Highlights.containsPosition(i) && ArrayVisualizer.getCurrentLength() != 2) {
                setRectColor(this.extraRender, ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                drawRect = true;
            }

            if(width > 0) {
                y = (int) ((ArrayVisualizer.windowHeight() - 20) - (array[i] * Renderer.getYScale()));
                this.mainRender.fillRect(Renderer.getOffset() + 20, y, Renderer.getDotDimensions(), Renderer.getDotDimensions());

                if(drawRect) {
                    this.extraRender.setStroke(ArrayVisualizer.getThickStroke());

                    if(Highlights.fancyFinishActive()) {
                        this.extraRender.fillRect(Renderer.getOffset() + 10, y - 10, Renderer.getDotDimensions() + 20, Renderer.getDotDimensions() + 20);
                    }
                    else {
                        this.extraRender.drawRect(Renderer.getOffset() + 10, y - 10, Renderer.getDotDimensions() + 20, Renderer.getDotDimensions() + 20);
                    }

                    this.extraRender.setStroke(ArrayVisualizer.getDefaultStroke());
                }
            }
            Renderer.setOffset(Renderer.getOffset() + width);
        }
    }
}