package io.github.arrayv.utils.shuffleutils;

import io.github.arrayv.utils.ShuffleGraph;
import io.github.arrayv.utils.ShuffleInfo;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GraphNode {
    public static final int WIDTH = 250;
    public static final int HEIGHT = 50;

    private final ShuffleInfo shuffle;
    private int x, y;
    private ShuffleGraph graph;
    private GraphConnection preConnection, postConnection;

    public GraphNode(ShuffleInfo shuffle, ShuffleGraph graph, int x, int y) {
        this.shuffle = shuffle;
        this.graph = graph;
        this.x = x;
        this.y = y;
        this.preConnection = null;
        this.postConnection = null;
    }

    public GraphNode(ShuffleInfo shuffle, ShuffleGraph graph) {
        this(shuffle, graph, 0, 0);
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof GraphNode) {
            GraphNode other = (GraphNode)o;
            return this.x     == other.x
                && this.y     == other.y
                && this.graph == other.graph
                && this.shuffle.equals(other.shuffle);
        } else if (o instanceof ShuffleInfo) {
            return this.shuffle.equals(o);
        } else {
            return false;
        }
    }

    protected String getShuffleName() {
        if (this.shuffle == null) {
            return "";
        }
        return this.shuffle.getName();
    }

    public void draw(Graphics2D g) {
        Color borderColor = this.graph.getSelected() == this ? new Color(128, 128, 255) : new Color(0, 0, 0);
        Color leftColor = this.graph.getDragCandidate() == this ? new Color(128, 128, 255) : borderColor;
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
        graph.calcTextSize(text, WIDTH, g);
        Rectangle2D rect = AWTUtils.getStringBounds(g, text);
        g.drawString(
            text,
            (int)(x + WIDTH / 2 - rect.getWidth() / 2),
            (int)(y + HEIGHT / 2 + rect.getHeight() / 2)
        );
    }

    public void drag(Point rel) {
        this.x += rel.getX();
        if (this.x < -WIDTH / 2) {
            this.x = -WIDTH / 2;
        }
        this.y += rel.getY();
    }

    public boolean inArea(Point pos) {
        return (new Rectangle2D.Double(x - 25, y, WIDTH + 15, HEIGHT)).contains(pos);
    }

    public boolean inStartDrag(Point pos) {
        return (new Rectangle2D.Double(x + WIDTH - 15, y + HEIGHT / 2.0 - 10, 30, 20)).contains(pos);
    }

    public void delete() {
        if (this == this.graph.getSelected()) {
            this.graph.setSelected(null);
        }
        this.graph.getNodes().remove(this);
        if (this.preConnection != null) {
            if (this.postConnection == null) {
                this.graph.getConnections().remove(this.preConnection);
                this.preConnection.remove();
            } else {
                this.preConnection.setTo(this.postConnection.getTo());
            }
        }
        if (this.postConnection != null) {
            if (this.preConnection == null) {
                this.graph.getConnections().remove(this.postConnection);
                this.postConnection.remove();
            } else {
                this.postConnection.setFrom(this.preConnection.getFrom());
            }
        }
    }

    public Point getPos() {
        return new Point(this.x, this.y);
    }

    public ShuffleInfo getValue() {
        return this.shuffle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ShuffleGraph getGraph() {
        return graph;
    }

    public GraphConnection getPreConnection() {
        return preConnection;
    }

    public GraphConnection getPostConnection() {
        return postConnection;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setGraph(ShuffleGraph graph) {
        this.graph = graph;
    }

    public void setPreConnection(GraphConnection connection) {
        this.preConnection = connection;
    }

    public void setPostConnection(GraphConnection connection) {
        this.postConnection = connection;
    }
}
