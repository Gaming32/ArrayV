package visuals.bars;

import java.awt.Color;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

final public class DisparityBarGraph extends Visual {

    public DisparityBarGraph(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
		for(int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
            int width = (int) (Renderer.getXScale() * (i + 1)) - j;
			
			if(Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
				this.mainRender.setColor(Color.GREEN);
			
			else if(ArrayVisualizer.colorEnabled()) 
				this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
				
			else this.mainRender.setColor(Color.WHITE);
			
			if(width > 0) {
				double disp = (1 + Math.cos((Math.PI * (array[i] - i)) / (ArrayVisualizer.getCurrentLength() * 0.5))) * 0.5;
				int y = (int) (((Renderer.getViewSize() - 20)) - disp * (array[i] + 1) * Renderer.getYScale());
				
				this.mainRender.fillRect(j + 20, Renderer.getYOffset() + y, width, (int) (disp * (array[i] + 1) * Renderer.getYScale()));
			}
			j += width;
        }
		for(int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
			int width = (int) (Renderer.getXScale() * (i + 1)) - j;
			
			if(Highlights.containsPosition(i)) {
				if(ArrayVisualizer.colorEnabled()) this.mainRender.setColor(Color.WHITE);
				else                               this.mainRender.setColor(Color.RED);
				
				double disp = (1 + Math.cos((Math.PI * (array[i] - i)) / (ArrayVisualizer.getCurrentLength() * 0.5))) * 0.5;
				int y = (int) (((Renderer.getViewSize() - 20)) - disp * (array[i] + 1) * Renderer.getYScale());
				
				this.mainRender.fillRect(j + 20, Renderer.getYOffset() + y, Math.max(width, 2), (int) (disp * (array[i] + 1) * Renderer.getYScale()));
			}
			j += width;
		}
		if(ArrayVisualizer.externalArraysEnabled()) {
			this.mainRender.setColor(Color.BLUE);
			this.mainRender.fillRect(0, Renderer.getYOffset() + Renderer.getViewSize() - 20, ArrayVisualizer.currentWidth(), 1);
		}
    }
}