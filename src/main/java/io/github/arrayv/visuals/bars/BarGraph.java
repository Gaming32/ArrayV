package io.github.arrayv.visuals.bars;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.Visual;

import java.awt.*;

public final class BarGraph extends Visual {

    public BarGraph(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
        for (int i = 0, j = 0; i < renderer.getArrayLength(); i++) {
            int width = (int) (renderer.getXScale() * (i + 1)) - j;
            if (width == 0) continue;

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(Color.GREEN);
            else if (arrayVisualizer.colorEnabled()) {
                int val = arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getIndexValue(array[i]): array[i];
                this.mainRender.setColor(getIntColor(val, arrayVisualizer.getCurrentLength()));
            } else this.mainRender.setColor(Color.WHITE);

            int val = arrayVisualizer.doingStabilityCheck() && arrayVisualizer.colorEnabled() ? arrayVisualizer.getStabilityValue(array[i]): array[i];
            int y = (int) (((renderer.getViewSize() - 20)) - (val + 1) * renderer.getYScale());

            this.mainRender.fillRect(j + 20, renderer.getYOffset() + y, width, (int) ((val + 1) * renderer.getYScale()));
            j += width;
        }
        this.mainRender.setColor(arrayVisualizer.getHighlightColor());
        int length = Math.min(renderer.getArrayLength(), arrayVisualizer.getCurrentLength());

        boolean mark = false;
        for (int i = 0, j = 0; i < length; i++) {
            mark = mark || Highlights.containsPosition(i);

            int width = (int) (renderer.getXScale() * (i + 1)) - j;
            if (width == 0) continue;

            if (mark) {
                int val = arrayVisualizer.doingStabilityCheck() && arrayVisualizer.colorEnabled() ? arrayVisualizer.getStabilityValue(array[i]): array[i];
                int y = (int) (((renderer.getViewSize() - 20)) - (val + 1) * renderer.getYScale());

                this.mainRender.fillRect(j + 20, renderer.getYOffset() + y, Math.max(width, 2), (int) ((val + 1) * renderer.getYScale()));
                mark = false;
            }
            j += width;
        }
        if (arrayVisualizer.externalArraysEnabled()) {
            this.mainRender.setColor(Color.BLUE);
            this.mainRender.fillRect(0, renderer.getYOffset() + renderer.getViewSize() - 20, arrayVisualizer.currentWidth(), 1);
        }
    }
}
