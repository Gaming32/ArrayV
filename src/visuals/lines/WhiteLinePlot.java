package visuals.lines;

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

final public class WhiteLinePlot extends Visual {
    public WhiteLinePlot(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        Renderer.setLineY((int) ((ArrayVisualizer.windowHeight() - 20) - array[0] * Renderer.getYScale()));
        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++) {
            int y = (int) ((ArrayVisualizer.windowHeight() - 20) - (Math.max(array[i], 1) * Renderer.getYScale()));

            // Quick patch to fix the first line being horizontal for some reason
            if(i == 0) y += ((ArrayVisualizer.windowHeight() - 20) - array[1] * Renderer.getYScale())
                          - ((ArrayVisualizer.windowHeight() - 20) - array[2] * Renderer.getYScale());


            int width = (int) (Renderer.getXScale() * (i + 1)) - Renderer.getOffset();

            if(width > 0) {
                if(Highlights.fancyFinishActive()) {
                    if(i < Highlights.getFancyFinishPosition()) {
                        lineFancy(this.mainRender, ArrayVisualizer.currentWidth());
                    }
                    else lineClear(this.mainRender, ArrayVisualizer.colorEnabled(), array, i, ArrayVisualizer.getCurrentLength(), ArrayVisualizer.currentWidth());

                    drawFancyFinishLine(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), this.mainRender, ArrayVisualizer.currentWidth(), ArrayVisualizer.colorEnabled());
                }
                else if(Highlights.containsPosition(i) && ArrayVisualizer.getCurrentLength() != 2) {
                    lineMark(this.mainRender, ArrayVisualizer.currentWidth(), ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                }
                else lineClear(this.mainRender, ArrayVisualizer.colorEnabled(), array, i, ArrayVisualizer.getCurrentLength(), ArrayVisualizer.currentWidth());

                this.mainRender.drawLine(Renderer.getOffset() + 20, y, Renderer.getLineX() + 20, Renderer.getLineY());

                Renderer.setLineX(Renderer.getOffset());
                Renderer.setLineY(y);
            }
            Renderer.setOffset(Renderer.getOffset() + width);
        }
    }
}