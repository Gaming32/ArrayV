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

final public class ScatterPlot extends Visual {
    public ScatterPlot(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        if(ArrayVisualizer.linesEnabled()) {
			int lastI = 0;
			int lastX = (int) (Renderer.getXScale()/2);
			int lastY = (int) (((Renderer.getViewSize() - 20)) - (array[0] + 1) * Renderer.getYScale());
			
			for(int i = 1, j = lastX + (int) Renderer.getXScale(); i < Renderer.getArrayLength(); i++) {
				if(Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
					this.mainRender.setColor(Color.GREEN);
				
				else if(Highlights.containsPosition(i)) {
					if(ArrayVisualizer.colorEnabled()) this.mainRender.setColor(Color.WHITE);
					else                               this.mainRender.setColor(Color.RED);
					
					this.mainRender.setStroke(ArrayVisualizer.getThickStroke());
				}
				else if(ArrayVisualizer.colorEnabled())
					this.mainRender.setColor(getIntColor(array[lastI], ArrayVisualizer.getCurrentLength()));
				
				else this.mainRender.setColor(Color.WHITE);
				
				int width = (int) (Renderer.getXScale() * (i + 1)) - j;
				int y = (int) (((Renderer.getViewSize() - 20)) - (array[i] + 1) * Renderer.getYScale());
				
				if(j > lastX) {
					this.mainRender.drawLine(lastX + 20, Renderer.getYOffset() + lastY, j + 20, Renderer.getYOffset() + y);
					
					lastI = i;
					lastX = j;
					lastY = y;
				}
				this.mainRender.setStroke(ArrayVisualizer.getDefaultStroke());
				j += width;
			}
		}
		else {
			int dotS = Renderer.getDotDimensions();
		
			for(int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
				if(Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
					this.mainRender.setColor(Color.GREEN);
				
				else if(ArrayVisualizer.colorEnabled()) 
					this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
					
				else this.mainRender.setColor(Color.WHITE);
				
				int width = (int) (Renderer.getXScale() * (i + 1)) - j;
				int y = (int) (((Renderer.getViewSize() - 20)) - (array[i] + 1) * Renderer.getYScale());
				
				this.mainRender.fillRect(j + 20 + (int) (Renderer.getXScale()/2), Renderer.getYOffset() + y, dotS, dotS);
				
				j += width;
			}
			for(int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
				int width = (int) (Renderer.getXScale() * (i + 1)) - j;
				
				if(Highlights.containsPosition(i)) {
					if(ArrayVisualizer.colorEnabled()) this.mainRender.setColor(Color.WHITE);
					else                               this.mainRender.setColor(Color.RED);
					
					int y = (int) (((Renderer.getViewSize() - 20)) - (array[i] + 1) * Renderer.getYScale());
					this.mainRender.fillRect(j + 20 + (int) (Renderer.getXScale()/2) - (int)(1.5*dotS), Renderer.getYOffset() + y - (int)(1.5*dotS), 4*dotS, 4*dotS);
				}
				j += width;
			}
		}
    }
}