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
    final private static double CIRC_HEIGHT_RATIO = (9/6.0843731432) * (16/9d);
    final private static double CIRC_WIDTH_RATIO = (16/6.0843731432) * (16/9d);
    
    public ColorCircle(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }
    
    // The reason we use cosine with height (expressed in terms of y) and sine with width (expressed in terms of x) is because our circles are rotated 90 degrees.
    // After that rotation, sine is on the x-axis and cosine is on the y-axis.
    
    // If we we use sine with height and cosine with width, the sorts would start from the right side of the circle,
    // just like the unit circle from trigonometry.
    
    private static double getSinOfDegrees(double d, int halfCirc) {
        return Math.sin((d * Math.PI) / halfCirc);
    }
    
    private static double getCosOfDegrees(double d, int halfCirc) {
        return Math.cos((d * Math.PI) / halfCirc);
    }
    
    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++){
            if(Highlights.fancyFinishActive()) {
                if(i < Highlights.getFancyFinishPosition()) {
                    this.mainRender.setColor(Color.getHSBColor((1f/3f), 1f, 0.8f));
                }
                else this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));

                drawFancyFinish(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), this.mainRender, ArrayVisualizer.rainbowEnabled(), ArrayVisualizer.colorEnabled());
            }
            if(Highlights.fancyFinishActive()) {
                drawFancyFinish(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), this.mainRender, ArrayVisualizer.rainbowEnabled(), ArrayVisualizer.colorEnabled());
            }
            else {
                this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
                colorMarkedBars(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights, this.mainRender, ArrayVisualizer.rainbowEnabled(), ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                
                /*
                if(ArrayVisualizer.pointerActive()) {
                    if(Highlights.containsPosition(i)) {
                        if(ArrayVisualizer.analysisEnabled()) {
                            this.extraRender.setColor(Color.GRAY);
                        }
                        else {
                            this.extraRender.setColor(Color.WHITE);
                        }

                        //Create new Polygon for the pointer
                        Polygon pointer = new Polygon();

                        //Calculate radians
                        double degrees = 360 * ((double) i / ArrayVisualizer.getCurrentLength());
                        double radians = Math.toRadians(degrees);

                        int pointerWidthRatio  = (int) (ArrayVisualizer.windowHalfWidth()  / CIRC_WIDTH_RATIO);
                        int pointerHeightRatio = (int) (ArrayVisualizer.windowHalfHeight() / CIRC_HEIGHT_RATIO);

                        //First step: draw a triangle
                        int[] pointerXValues = {pointerWidthRatio - 10,
                                                pointerWidthRatio,
                                                pointerWidthRatio + 10};

                        int[] pointerYValues = {pointerHeightRatio - 10,
                                                pointerHeightRatio + 10,
                                                pointerHeightRatio - 10};

                        //Second step: rotate triangle (https://en.wikipedia.org/wiki/Rotation_matrix)
                        for(int j = 0; j < pointerXValues.length; j++) {
                            double x = pointerXValues[j] - pointerWidthRatio;
                            double y = pointerYValues[j] - pointerHeightRatio;

                            pointerXValues[j] = (int) (pointerWidthRatio
                                                    + x*Math.cos(radians)
                                                    - y*Math.sin(radians));
                            pointerYValues[j] = (int) (pointerHeightRatio
                                                    + x*Math.sin(radians)
                                                    + y*Math.cos(radians));
                        }

                        for(int j = 0; j < pointerXValues.length; j++) {
                            pointer.addPoint(pointerXValues[j], pointerYValues[j]);
                        }

                        this.extraRender.fillPolygon(pointer);                        
                    }
                }
                else */ 
            }

            Polygon p = new Polygon();

            p.addPoint(ArrayVisualizer.windowHalfWidth(),
                    ArrayVisualizer.windowHalfHeight());

            p.addPoint(ArrayVisualizer.windowHalfWidth()  + (int) (ColorCircle.getSinOfDegrees(i, ArrayVisualizer.halfCircle()) * ((ArrayVisualizer.windowWidth()  - 64) / CIRC_WIDTH_RATIO)),
                    ArrayVisualizer.windowHalfHeight() - (int) (ColorCircle.getCosOfDegrees(i, ArrayVisualizer.halfCircle()) * ((ArrayVisualizer.windowHeight() - 96) / CIRC_HEIGHT_RATIO)));

            p.addPoint(ArrayVisualizer.windowHalfWidth()  + (int) (ColorCircle.getSinOfDegrees(i + 1, ArrayVisualizer.halfCircle()) * ((ArrayVisualizer.windowWidth()  - 64) / CIRC_WIDTH_RATIO)),
                    ArrayVisualizer.windowHalfHeight() - (int) (ColorCircle.getCosOfDegrees(i + 1, ArrayVisualizer.halfCircle()) * ((ArrayVisualizer.windowHeight() - 96) / CIRC_HEIGHT_RATIO)));

            this.mainRender.fillPolygon(p);
        }
    }
}   