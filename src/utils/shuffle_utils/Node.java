package utils.shuffle_utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import utils.ShuffleGraph;
import utils.Shuffles;

public class Node {
    public static final int WIDTH = 250;
    public static final int HEIGHT = 50;

    public Shuffles shuffle;
    public int x, y;
    public ShuffleGraph graph;
    public Connection preConnection, postConnection;

    public Node(Shuffles shuffle, ShuffleGraph graph, int x, int y) {
        this.shuffle = shuffle;
        this.graph = graph;
        this.x = x;
        this.y = y;
        this.preConnection = null;
        this.postConnection = null;
    }

    public Node(Shuffles shuffle, ShuffleGraph graph) {
        this(shuffle, graph, 0, 0);
    }

    protected String getShuffleName() {
        if (this.shuffle == null) {
            return "";
        }
        return this.shuffle.getName();
    }

    public void draw(Graphics2D g) {
        Color borderColor = this.graph.selected == this ? new Color(128, 128, 255) : new Color(0, 0, 0);
        Color leftColor = this.graph.dragCandidate == this ? new Color(128, 128, 255) : borderColor;
        g.setColor(leftColor);
        AWTUtils.drawCircle(g, x, y + HEIGHT / 2, 10);
        g.setColor(borderColor);
        AWTUtils.drawCircle(g, x + WIDTH, y + HEIGHT / 2, 10);
        g.setColor(Color.WHITE);
        g.fillRect(x, y, WIDTH, HEIGHT);
        g.setColor(borderColor);
        AWTUtils.drawBorderRect(g, x, y, WIDTH, HEIGHT, 2);
        g.setColor(Color.BLACK);
        String text = getShuffleName();
        Rectangle2D rect = AWTUtils.getStringBounds(g, text);
        g.drawString(
            text,
            (int)(x + WIDTH / 2 - rect.getWidth() / 2),
            (int)(y + HEIGHT / 2 + rect.getHeight() / 2)
        );
    }

    public void drag(Point rel) {
        this.x += rel.getX();
        this.y += rel.getY();
    }

    public boolean inArea(Point pos) {
        return (new Rectangle2D.Double(x, y, WIDTH, HEIGHT)).contains(pos);
    }

    public boolean inStartDrag(Point pos) {
        return (new Rectangle2D.Double(x + WIDTH, y + HEIGHT / 2 - 10, 10, 20)).contains(pos);
    }

    public void delete() {
        if (this == this.graph.selected) {
            this.graph.selected = null;
        }
        this.graph.nodes.remove(this);
        if (this.preConnection != null) {
            this.preConnection.to = this.postConnection.to;
        }
        if (this.postConnection != null) {
            this.postConnection.from = this.preConnection.from;
        }
    }

    public Point getPos() {
        return new Point(this.x, this.y);
    }
}
