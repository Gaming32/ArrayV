package visuals;

import java.awt.BasicStroke;
import java.awt.Color;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;

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

final public class Pixels extends Visual {
    public Pixels(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        if(ArrayVisualizer.linesEnabled()) {
            //TODO: Wave visual needs to be *heavily* refactored
            if(ArrayVisualizer.waveEnabled()) {
                Renderer.setLineY((int) ((Renderer.getViewSize() / 4) * Math.sin((2 * Math.PI * ((double) array[1] / Renderer.getArrayLength()))) + Renderer.halfViewSize()));
            }
            else {
                Renderer.setLineY((int) ((Renderer.getViewSize() - 20) - array[0] * Renderer.getYScale()));
            }
            for(int i = 0; i < Renderer.getArrayLength(); i++) {
                int width = (int) (Renderer.getXScale() * (i + 1)) - Renderer.getOffset();
                if (width == 0) continue;

                int y;
                if(ArrayVisualizer.waveEnabled()) {
                    y = (int) ((Renderer.getViewSize() / 4) * Math.sin((2 * Math.PI * ((double) array[i] / Renderer.getArrayLength()))) + Renderer.halfViewSize());
                }
                else {
                    y = (int) ((Renderer.getViewSize() - 20) - (Math.max(array[i], 1) * Renderer.getYScale()));

                    // Quick patch to fix the first line being horizontal for some reason
                    if(i == 0) y += ((Renderer.getViewSize() - 20) - array[1] * Renderer.getYScale())
                                  - ((Renderer.getViewSize() - 20) - array[2] * Renderer.getYScale());
                }

                if(width > 0) {
                    if(i > 0) {
                        if(Highlights.fancyFinishActive()) {
                            if(i < Highlights.getFancyFinishPosition()) {
                                lineFancy(this.mainRender, ArrayVisualizer.currentWidth());
                            }
                            else lineClear(this.mainRender, ArrayVisualizer.colorEnabled(), array, i, Renderer.getArrayLength(), ArrayVisualizer.currentWidth());

                            drawFancyFinishLine(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), this.mainRender, ArrayVisualizer.currentWidth(), ArrayVisualizer.colorEnabled());
                        }
                        else if(Highlights.containsPosition(i) && Renderer.getArrayLength() != 2) {
                            lineMark(this.mainRender, ArrayVisualizer.currentWidth(), ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                        }
                        else lineClear(this.mainRender, ArrayVisualizer.colorEnabled(), array, i, Renderer.getArrayLength(), ArrayVisualizer.currentWidth());
                        
                        this.mainRender.drawLine(Renderer.getOffset() + 20, Renderer.getYOffset() + y, Renderer.getLineX() + 20, Renderer.getLineY());
                    }
                    Renderer.setLineX(Renderer.getOffset());
                    Renderer.setLineY(y);
                }
                Renderer.setOffset(Renderer.getOffset() + width);
            }
        }
        else {
            for(int i = 0; i < Renderer.getArrayLength(); i++) {
                int width = (int) (Renderer.getXScale() * (i + 1)) - Renderer.getOffset();
                if (width == 0) continue;

                if(i < Highlights.getFancyFinishPosition()) {
                    this.mainRender.setColor(Color.GREEN);
                }
                else if(i == Highlights.getFancyFinishPosition() && Highlights.fancyFinishActive()) {
                    if(ArrayVisualizer.colorEnabled()) {
                        this.mainRender.setColor(Color.WHITE);
                    }
                    else this.mainRender.setColor(Color.RED);
                }
                else if(ArrayVisualizer.colorEnabled()) {
                    this.mainRender.setColor(getIntColor(array[i], Renderer.getArrayLength()));
                }
                else this.mainRender.setColor(Color.WHITE);

                int y = 0;

                boolean drawRect = false;
                if(Highlights.containsPosition(i) && Renderer.getArrayLength() != 2) {
                    setRectColor(this.extraRender, ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                    drawRect = true;
                }

                if(width > 0) {
                    if(ArrayVisualizer.waveEnabled()) {
                        y = (int) ((Renderer.getViewSize() / 4) * Math.sin((2 * Math.PI * ((double) array[i] / Renderer.getArrayLength()))) + Renderer.halfViewSize());
                    }
                    else {
                        y = (int) ((Renderer.getViewSize() - 20) - (array[i] * Renderer.getYScale()));
                    }
                    this.mainRender.fillRect(Renderer.getOffset() + 20, Renderer.getYOffset() + y, Renderer.getDotDimensions(), Renderer.getDotDimensions());
                    
                    if(drawRect) {
                        this.extraRender.setStroke(ArrayVisualizer.getThickStroke());
                        
                        if(Highlights.fancyFinishActive()) {
                            this.extraRender.fillRect(Renderer.getOffset() + 10, Renderer.getYOffset() + y - 10, Renderer.getDotDimensions() + 20, Renderer.getDotDimensions() + 20);
                        }
                        else {
                            this.extraRender.drawRect(Renderer.getOffset() + 10, Renderer.getYOffset() + y - 10, Renderer.getDotDimensions() + 20, Renderer.getDotDimensions() + 20);
                        }
                        
                        this.extraRender.setStroke(new BasicStroke(3f * (ArrayVisualizer.currentWidth() / 1280f))); //TODO: This BasicStroke should have a getDefaultStroke() method
                    }
                }
                Renderer.setOffset(Renderer.getOffset() + width);
            }
        }
        if (ArrayVisualizer.externalArraysEnabled() && !ArrayVisualizer.rainbowEnabled()) {
            this.mainRender.setColor(Color.BLUE);
            this.mainRender.fillRect(0, Renderer.getYOffset() + Renderer.getViewSize() - 20, ArrayVisualizer.currentWidth(), 1);
        }
    }
}