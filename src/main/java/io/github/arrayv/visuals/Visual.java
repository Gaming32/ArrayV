package io.github.arrayv.visuals;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;

import java.awt.*;

public abstract class Visual {
    protected Graphics2D mainRender;
    protected Graphics2D extraRender;

    public Visual(ArrayVisualizer arrayVisualizer) {
        this.updateRender(arrayVisualizer);
    }

    public void updateRender(ArrayVisualizer arrayVisualizer) {
        this.mainRender = arrayVisualizer.getMainRender();
        this.extraRender = arrayVisualizer.getExtraRender();
    }

    public static Color getIntColor(int i, int length) {
        return Color.getHSBColor(((float) i / length), 0.8F, 0.8F);
    }

    public static void markBar(Graphics2D bar, boolean color, boolean rainbow, boolean analysis) {
        if (color || rainbow) {
            if (analysis) bar.setColor(Color.LIGHT_GRAY);
            else          bar.setColor(Color.WHITE);
        } else if (analysis) bar.setColor(Color.BLUE);
        else                 bar.setColor(Color.RED);
    }
    private static void markBarFancy(Graphics2D bar, boolean color, boolean rainbow) {
        if (!color && !rainbow) bar.setColor(Color.RED);
        else                    bar.setColor(Color.BLACK);
    }

    public static void lineMark(Graphics2D line, double width, boolean color, boolean analysis) {
        line.setStroke(new BasicStroke((float) (9f * (width / 1280f))));
        if (color) line.setColor(Color.BLACK);
        else if (analysis) line.setColor(Color.BLUE);
        else line.setColor(Color.RED);
    }

    public static void markLineFancy(Graphics2D line, double width) {
        line.setColor(Color.GREEN);
        line.setStroke(new BasicStroke((float) (9f * (width / 1280f))));
    }

    public static void clearLine(Graphics2D line, boolean color, int[] array, int i, int length, double width) {
        if (color) line.setColor(getIntColor(array[i], length));
        else line.setColor(Color.WHITE);
        line.setStroke(new BasicStroke((float) (3f * (width / 1280f))));
    }

    public static void setRectColor(Graphics2D rect, boolean color, boolean analysis) {
        if (color) rect.setColor(Color.WHITE);
        else if (analysis) rect.setColor(Color.BLUE);
        else rect.setColor(Color.RED);
    }

    //The longer the array length, the more bars marked. Makes the visual easier to see when bars are thinner.
    public static void colorMarkedBars(int logOfLen, int index, Highlights Highlights, Graphics2D mainRender, boolean colorEnabled, boolean rainbowEnabled, boolean analysis) {
        switch(logOfLen) {
            // @checkstyle:off LeftCurlyCheck|IndentationCheck
            case 15: if (Highlights.containsPosition(index - 15)) { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
                     if (Highlights.containsPosition(index - 14)) { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
                     if (Highlights.containsPosition(index - 13)) { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
                     if (Highlights.containsPosition(index - 12)) { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
                     if (Highlights.containsPosition(index - 11)) { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
            case 14: if (Highlights.containsPosition(index - 10)) { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
                     if (Highlights.containsPosition(index - 9))  { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
                     if (Highlights.containsPosition(index - 8))  { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
            case 13: if (Highlights.containsPosition(index - 7))  { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
                     if (Highlights.containsPosition(index - 6))  { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
                     if (Highlights.containsPosition(index - 5))  { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
            case 12: if (Highlights.containsPosition(index - 4))  { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
                     if (Highlights.containsPosition(index - 3))  { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
            case 11: if (Highlights.containsPosition(index - 2))  { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
            case 10: if (Highlights.containsPosition(index - 1))  { markBar(mainRender, colorEnabled, rainbowEnabled, analysis); break; }
            default: if (Highlights.containsPosition(index))        markBar(mainRender, colorEnabled, rainbowEnabled, analysis);
            // @checkstyle:on LeftCurlyCheck|IndentationCheck
        }
    }

    public static void drawFancyFinish(int logOfLen, int index, int position, Graphics2D mainRender, boolean colorEnabled, boolean rainbowEnabled) {
        switch(logOfLen) {
            // @checkstyle:off LeftCurlyCheck
            case 15: if (index == position - 14) { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 14: if (index == position - 13) { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 13: if (index == position - 12) { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 12: if (index == position - 11) { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 11: if (index == position - 10) { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 10: if (index == position - 9)  { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 9:  if (index == position - 8)  { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 8:  if (index == position - 7)  { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 7:  if (index == position - 6)  { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 6:  if (index == position - 5)  { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 5:  if (index == position - 4)  { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 4:  if (index == position - 3)  { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 3:  if (index == position - 2)  { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            case 2:  if (index == position - 1)  { markBarFancy(mainRender, colorEnabled, rainbowEnabled); break; }
            default: if (index == position)        markBarFancy(mainRender, colorEnabled, rainbowEnabled);
            // @checkstyle:on LeftCurlyCheck
        }
    }

    public static void drawFancyFinishLine(int logOfLen, int index, int position, Graphics2D mainRender, double width, boolean colorEnabled) {
        switch(logOfLen) {
            // @checkstyle:off LeftCurlyCheck
            case 15: if (index == position - 14) { lineMark(mainRender, width, colorEnabled, false); break; }
            case 14: if (index == position - 13) { lineMark(mainRender, width, colorEnabled, false); break; }
            case 13: if (index == position - 12) { lineMark(mainRender, width, colorEnabled, false); break; }
            case 12: if (index == position - 11) { lineMark(mainRender, width, colorEnabled, false); break; }
            case 11: if (index == position - 10) { lineMark(mainRender, width, colorEnabled, false); break; }
            case 10: if (index == position - 9)  { lineMark(mainRender, width, colorEnabled, false); break; }
            case 9:  if (index == position - 8)  { lineMark(mainRender, width, colorEnabled, false); break; }
            case 8:  if (index == position - 7)  { lineMark(mainRender, width, colorEnabled, false); break; }
            case 7:  if (index == position - 6)  { lineMark(mainRender, width, colorEnabled, false); break; }
            case 6:  if (index == position - 5)  { lineMark(mainRender, width, colorEnabled, false); break; }
            case 5:  if (index == position - 4)  { lineMark(mainRender, width, colorEnabled, false); break; }
            case 4:  if (index == position - 3)  { lineMark(mainRender, width, colorEnabled, false); break; }
            case 3:  if (index == position - 2)  { lineMark(mainRender, width, colorEnabled, false); break; }
            case 2:  if (index == position - 1)  { lineMark(mainRender, width, colorEnabled, false); break; }
            default: if (index == position)        lineMark(mainRender, width, colorEnabled, false);
            // @checkstyle:on LeftCurlyCheck
        }
    }

    public abstract void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights);
}
