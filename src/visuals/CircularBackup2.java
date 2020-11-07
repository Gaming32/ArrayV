package visuals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2020 aphitorite

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

final public class CircularBackup2 extends Visual {
    final private static double CIRC_HEIGHT_RATIO = 45 / 18d;
    final private static double CIRC_WIDTH_RATIO = 80 / 18d;
    
    final private static double PNT_HEIGHT_RATIO = 45 / 19d;
    final private static double PNT_WIDTH_RATIO = 80 / 19d;
    
    final private static Color fancyGray = new Color(0, 0, 0, .5f);
    final private static Color fancyGreen = Color.getHSBColor((1f/3f), 1f, 0.8f);
    
    final private static Color analysisBlue = new Color(0, 0, 1, .5f);
    
    private int pointerSize = 10;
    private int storedLength = 2048;
    private int storedLengthLog = 11;
    
    private int storedWindowHeight;
    private int storedWindowWidth;
    
    private double circleHeightFactor;
    private double circleWidthFactor;
    
    private double pointerHeightFactor;
    private double pointerWidthFactor;
    
    public CircularBackup2(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.processNewArraySize(ArrayVisualizer);
        this.processNewWindowHeight(ArrayVisualizer);
        this.processNewWindowWidth(ArrayVisualizer);
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
    
    private void processNewArraySize(ArrayVisualizer ArrayVisualizer) {
        this.storedLengthLog = ArrayVisualizer.getLogBaseTwoOfLength();
        this.storedLength = ArrayVisualizer.getCurrentLength();
        
        this.pointerSize = 10;
        if(this.storedLength < 2048) {
            int multiple = this.storedLength;
            do {
                this.pointerSize += 5;
            } while((multiple *= 2) < 2048);
        }
    }
    
    private void processNewWindowHeight(ArrayVisualizer ArrayVisualizer) {
        int modifiedHeight = ArrayVisualizer.windowHeight() - 96;
        this.circleHeightFactor = modifiedHeight / CIRC_HEIGHT_RATIO;
        this.pointerHeightFactor = modifiedHeight / PNT_HEIGHT_RATIO;
        this.storedWindowHeight = ArrayVisualizer.windowHeight();
    }
    
    private void processNewWindowWidth(ArrayVisualizer ArrayVisualizer) {
        int modifiedWidth = ArrayVisualizer.windowWidth() - 64;
        this.circleWidthFactor = modifiedWidth / CIRC_WIDTH_RATIO;
        this.pointerWidthFactor = modifiedWidth / PNT_WIDTH_RATIO;
        this.storedWindowWidth = ArrayVisualizer.windowWidth();
    }
    
    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        if(ArrayVisualizer.getCurrentLength() != this.storedLength) {
            this.processNewArraySize(ArrayVisualizer);
        }
        
        if(this.storedWindowHeight != ArrayVisualizer.windowHeight()) {
            this.processNewWindowHeight(ArrayVisualizer);
        }

        if(this.storedWindowWidth != ArrayVisualizer.windowWidth()) {
            this.processNewWindowWidth(ArrayVisualizer);
        }
        
        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++) {
            if(!Highlights.fancyFinishActive()) {
                if(Highlights.containsPosition(i)) {
                    this.extraRender.setColor(Color.WHITE);
                    if(ArrayVisualizer.analysisEnabled()) {
                        this.extraRender.setColor(analysisBlue);
                    }

                    double pointerSin = CircularBackup2.getSinOfDegrees(((i + (i + 1)) / 2d), ArrayVisualizer.halfCircle());
                    double pointerCos = CircularBackup2.getCosOfDegrees(((i + (i + 1)) / 2d), ArrayVisualizer.halfCircle());

                    //Create new Polygon for the pointer
                    Polygon pointer = new Polygon();

                    int pointerWidthRatio  = ArrayVisualizer.windowHalfWidth()  + (int) (pointerSin * this.pointerWidthFactor);
                    int pointerHeightRatio = ArrayVisualizer.windowHalfHeight() - (int) (pointerCos * this.pointerHeightFactor);

                    //First step: draw a triangle
                    int[] pointerXValues = {pointerWidthRatio - pointerSize,
                                            pointerWidthRatio,
                                            pointerWidthRatio + pointerSize};

                    int[] pointerYValues = {pointerHeightRatio - (10 + (pointerSize - 10)),
                                            pointerHeightRatio + 10,
                                            pointerHeightRatio - (10 + (pointerSize - 10))};

                    //Second step: rotate triangle (https://en.wikipedia.org/wiki/Rotation_matrix)
                    for(int j = 0; j < pointerXValues.length; j++) {
                        double x = pointerXValues[j] - pointerWidthRatio;
                        double y = pointerYValues[j] - pointerHeightRatio;

                        pointerXValues[j] = (int) (pointerWidthRatio
                                + x * pointerCos
                                - y * pointerSin);
                        pointerYValues[j] = (int) (pointerHeightRatio
                                + x * pointerSin
                                + y * pointerCos);
                    }

                    //Third step: redraw triangle coordinates on (x, y) plane
                    for(int j = 0; j < pointerXValues.length; j++) {
                        pointer.addPoint(pointerXValues[j], pointerYValues[j]);
                    }

                    //Fourth step: Fill it in!
                    this.extraRender.fillPolygon(pointer);                        
                }
            }

            this.extraRender.setColor(Color.WHITE);
            this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
            
            if(Highlights.fancyFinishActive()) {
                int position = Highlights.getFancyFinishPosition();
                if(i < position) {
                    /*
                     * GOOD replacement for
                     * switch(this.storedLengthLog) {
                        case 14: if(i == position - 13) this.extraRender.setColor(fancyRed);
                        case 13: if(i == position - 12) this.extraRender.setColor(fancyRed);
                        case 12: if(i == position - 11) this.extraRender.setColor(fancyRed);
                        case 11: if(i == position - 10) this.extraRender.setColor(fancyRed);
                        case 10: if(i == position - 9)  this.extraRender.setColor(fancyRed);
                        case 9:  if(i == position - 8)  this.extraRender.setColor(fancyRed);
                        case 8:  if(i == position - 7)  this.extraRender.setColor(fancyRed);
                        case 7:  if(i == position - 6)  this.extraRender.setColor(fancyRed);
                        case 6:  if(i == position - 5)  this.extraRender.setColor(fancyRed);
                        case 5:  if(i == position - 4)  this.extraRender.setColor(fancyRed);
                        case 4:  if(i == position - 3)  this.extraRender.setColor(fancyRed);
                        case 3:  if(i == position - 2)  this.extraRender.setColor(fancyRed);
                        case 2:  if(i == position - 1)  this.extraRender.setColor(fancyRed);
                        default: if(i == position)      this.extraRender.setColor(fancyRed);
                       }
                     */
                    if(i > (position - this.storedLengthLog)) {
                        this.extraRender.setColor(fancyGray);
                    }
                    else {
                        this.mainRender.setColor(fancyGreen);
                    }
                }
            }
                
            Polygon p = new Polygon();

            p.addPoint(ArrayVisualizer.windowHalfWidth(),
                       ArrayVisualizer.windowHalfHeight());

            p.addPoint(ArrayVisualizer.windowHalfWidth()  + (int) (CircularBackup2.getSinOfDegrees(i, ArrayVisualizer.halfCircle()) * this.circleWidthFactor),
                       ArrayVisualizer.windowHalfHeight() - (int) (CircularBackup2.getCosOfDegrees(i, ArrayVisualizer.halfCircle()) * this.circleHeightFactor));

            p.addPoint(ArrayVisualizer.windowHalfWidth()  + (int) (CircularBackup2.getSinOfDegrees(i + 1, ArrayVisualizer.halfCircle()) * this.circleWidthFactor),
                       ArrayVisualizer.windowHalfHeight() - (int) (CircularBackup2.getCosOfDegrees(i + 1, ArrayVisualizer.halfCircle()) * this.circleHeightFactor));

            this.mainRender.fillPolygon(p);

            if(this.extraRender.getColor().equals(fancyGray)) {
                Polygon e = p;
                this.extraRender.fillPolygon(e);
            }
        }
    }
}