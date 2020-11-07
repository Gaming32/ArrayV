package visuals.bars;

import java.awt.Color;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

final public class WhiteBarGraph extends Visual {

    public WhiteBarGraph(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++){
            if(Highlights.fancyFinishActive()) {
                if(i < Highlights.getFancyFinishPosition()) {
                    this.mainRender.setColor(Color.GREEN);
                }
                else this.mainRender.setColor(Color.WHITE);

                drawFancyFinish(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), this.mainRender, ArrayVisualizer.colorEnabled(), ArrayVisualizer.rainbowEnabled());
            }
            else {
                this.mainRender.setColor(Color.WHITE);

                if(ArrayVisualizer.getCurrentLength() != 2) {
                    colorMarkedBars(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights, this.mainRender, ArrayVisualizer.colorEnabled(), ArrayVisualizer.rainbowEnabled(), ArrayVisualizer.analysisEnabled());
                }
            }
            /*
            int markHeight = 0;
            Color currentColor = mainRender.getColor();
            if(currentColor == Color.BLACK || currentColor == Color.RED || currentColor == Color.BLUE) {
                markHeight = 5;
            }
            */

            int y = 0;
            int width = (int) (Renderer.getXScale() * (i + 1)) - Renderer.getOffset();

            if(width > 0) {
                /*
                    int gap = 0;
                    if(width > 5) {
                        gap = 5;
                    }
                 */

                y = (int) (((ArrayVisualizer.windowHeight() - 20)) - (array[i] + 1) * Renderer.getYScale());
                mainRender.fillRect(Renderer.getOffset() + 20, y, width, (int) ((array[i] + 1) * Renderer.getYScale()));

                //mainRender.fillRect(Renderer.getOffset() + 20, y /*- markHeight*/, width /*- gap*/, (int) ((array[i] + 1) * Renderer.getYScale()) /*+ markHeight*/);

                /*
                    double thickness = 1;
                    Stroke oldStroke = mainRender.getStroke();
                    mainRender.setStroke(new BasicStroke((float) thickness));
                    mainRender.setColor(Color.BLACK);
                    mainRender.drawLine(Renderer.getOffset() + 20, y, Renderer.getOffset() + 20, (int) Math.max(array[i] * Renderer.getYScale()-1, 1) + y);
                    mainRender.setStroke(oldStroke);
                 */
            }
            Renderer.setOffset(Renderer.getOffset() + width);
        }
    }
}