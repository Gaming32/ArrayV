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
Copyright (c) 2020 MusicTheorist
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

final public class WaveDots extends Visual {
    public WaveDots(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
		int offset = 20 + (int) (Renderer.getXScale()/2);
		
        if(ArrayVisualizer.linesEnabled()) {
			int lastX = 0;
			int lastY = (int) ((Renderer.getViewSize() / 2.5) * Math.sin((2 * Math.PI * ((double) array[0] / Renderer.getArrayLength()))) + Renderer.halfViewSize());
			this.mainRender.setStroke(ArrayVisualizer.getThinStroke());
			
			for(int i = 1, j = (int) Renderer.getXScale(); i < Renderer.getArrayLength(); i++) {
				if(Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition()) {
					this.mainRender.setColor(Color.GREEN);
					
					this.mainRender.setStroke(ArrayVisualizer.getDefaultStroke());
				}
				else if(Highlights.containsPosition(i)) {
					if(ArrayVisualizer.colorEnabled()) this.mainRender.setColor(Color.WHITE);
					else                               this.mainRender.setColor(Color.RED);
					
					this.mainRender.setStroke(ArrayVisualizer.getDefaultStroke());
				}
				else if(ArrayVisualizer.colorEnabled())
					this.mainRender.setColor(getIntColor(array[i-1], ArrayVisualizer.getCurrentLength()));
				
				else this.mainRender.setColor(Color.WHITE);
				
				int y = (int) ((Renderer.getViewSize() / 2.5) * Math.sin((2 * Math.PI * ((double) array[i] / Renderer.getArrayLength()))) + Renderer.halfViewSize());
				
				this.mainRender.drawLine(lastX + offset, Renderer.getYOffset() + lastY, j + offset, Renderer.getYOffset() + y);
				
				lastX = j;
				lastY = y;
				
				this.mainRender.setStroke(ArrayVisualizer.getThinStroke());
				
				int width = (int) (Renderer.getXScale() * (i + 1)) - j;
				j += width;
			}
			this.mainRender.setStroke(ArrayVisualizer.getDefaultStroke());
		}
		else {
			int dotS = Renderer.getDotDimensions();
		
			for(int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
				if(Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
					this.mainRender.setColor(Color.GREEN);
				
				else if(ArrayVisualizer.colorEnabled()) 
					this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
					
				else this.mainRender.setColor(Color.WHITE);
				
				int y = (int) ((Renderer.getViewSize() / 2.5) * Math.sin((2 * Math.PI * ((double) array[i] / Renderer.getArrayLength()))) + Renderer.halfViewSize());
				
				this.mainRender.fillRect(j + offset, Renderer.getYOffset() + y, dotS, dotS);
				
				int width = (int) (Renderer.getXScale() * (i + 1)) - j;
				j += width;
			}
			for(int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
				if(Highlights.containsPosition(i)) {
					if(ArrayVisualizer.colorEnabled()) this.mainRender.setColor(Color.WHITE);
					else                               this.mainRender.setColor(Color.RED);
					
					int y = (int) ((Renderer.getViewSize() / 2.5) * Math.sin((2 * Math.PI * ((double) array[i] / Renderer.getArrayLength()))) + Renderer.halfViewSize());
					this.mainRender.fillRect(j + offset - (int)(1.5*dotS), Renderer.getYOffset() + y - (int)(1.5*dotS), 4*dotS, 4*dotS);
				}
				int width = (int) (Renderer.getXScale() * (i + 1)) - j;
				j += width;
			}
		}
    }
}