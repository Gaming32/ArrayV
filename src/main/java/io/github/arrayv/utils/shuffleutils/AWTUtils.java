package io.github.arrayv.utils.shuffleutils;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

public final class AWTUtils {
    private AWTUtils() {
    }

    public static void drawCircle(Graphics2D g, int x, int y, int r) {
        x = x - r;
        y = y - r;
        g.fillOval(x, y, r * 2, r * 2);
    }

    public static void drawBorderRect(Graphics2D g, int x, int y, int width, int height, int thickness) {
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(thickness));
        g.drawRect(x, y, width, height);
        g.setStroke(oldStroke);
    }

    public static Rectangle2D getStringBounds(Graphics2D g, String str, float x, float y) {
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = g.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }

    public static Rectangle2D getStringBounds(Graphics2D g, String str) {
        return getStringBounds(g, str, 0, 0);
    }
}
