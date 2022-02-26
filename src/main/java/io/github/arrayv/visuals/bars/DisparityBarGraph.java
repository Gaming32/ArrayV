package io.github.arrayv.visuals.bars;

import java.awt.Color;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.Visual;

public final class DisparityBarGraph extends Visual {

    public DisparityBarGraph(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
        for (int i = 0, j = 0; i < renderer.getArrayLength(); i++) {
            int width = (int) (renderer.getXScale() * (i + 1)) - j;
            if (width == 0) continue;

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(Color.GREEN);

            else if (arrayVisualizer.colorEnabled())
                this.mainRender.setColor(getIntColor(array[i], arrayVisualizer.getCurrentLength()));

            else this.mainRender.setColor(Color.WHITE);

            double disp = (1 + Math.sin((Math.PI * (array[i] - i)) / arrayVisualizer.getCurrentLength())) * 0.5;
            int y = (int) (((renderer.getViewSize() - 20)) - disp *  arrayVisualizer.getCurrentLength() * renderer.getYScale());

            this.mainRender.fillRect(j + 20, renderer.getYOffset() + y, width, (int) (disp *  arrayVisualizer.getCurrentLength() * renderer.getYScale()));
            j += width;
        }
        this.mainRender.setColor(arrayVisualizer.getHighlightColor());

        for (int i = 0, j = 0; i < renderer.getArrayLength(); i++) {
            int width = (int) (renderer.getXScale() * (i + 1)) - j;

            if (Highlights.containsPosition(i)) {
                double disp = (1 + Math.sin((Math.PI * (array[i] - i)) / arrayVisualizer.getCurrentLength())) * 0.5;
                int y = (int) (((renderer.getViewSize() - 20)) - disp * arrayVisualizer.getCurrentLength() * renderer.getYScale());

                this.mainRender.fillRect(j + 20, renderer.getYOffset() + y, Math.max(width, 2), (int) (disp *  arrayVisualizer.getCurrentLength() * renderer.getYScale()));
            }
            j += width;
        }
        if (arrayVisualizer.externalArraysEnabled()) {
            this.mainRender.setColor(Color.BLUE);
            this.mainRender.fillRect(0, renderer.getYOffset() + renderer.getViewSize() - 20, arrayVisualizer.currentWidth(), 1);
        }
    }
}
