package utils.shuffle_utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Point;
import java.util.List;

public class Connection {
    public Node from, to;
    public Point currentDragPos;

    public Connection(Node from, Node to) {
        this.from = from;
        this.to = to;
        this.currentDragPos = new Point();
    }

    public Connection(Node from, Point to) {
        this.from = from;
        this.to = null;
        this.currentDragPos = to;
    }

    public void draw(Graphics2D g) {
        Point fromPos = this.from.getPos();
        fromPos = new Point(fromPos.x + Node.WIDTH + 10, fromPos.y + Node.HEIGHT / 2);
        Point endPos;
        if (this.to == null) {
            endPos = new Point(this.currentDragPos.x - 10, this.currentDragPos.y);
        } else {
            Point toPos = this.to.getPos();
            endPos = new Point(toPos.x, toPos.y + Node.HEIGHT / 2);
        }
        Point midStart = new Point(fromPos.x + 15, fromPos.y);
        Point midEnd = new Point(endPos.x - 15, endPos.y);
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(5));
        g.setColor(Color.BLACK);
        g.drawLine(fromPos.x, fromPos.y, midStart.x, midStart.y);
        g.drawLine(midStart.x, midStart.y, midEnd.x, midEnd.y);
        g.drawLine(midEnd.x, midEnd.y, endPos.x, endPos.y);
        g.setStroke(oldStroke);
        if (this.to == null) {
            AWTUtils.drawCircle(g, endPos.x + 10, endPos.y, 10);
        }
    }

    public void finishDragging(Node other) {
        this.to = other;
        other.preConnection = this;
        int removed = 0;
        List<Connection> connections = other.graph.connections;
        for (int i = 0; i < connections.size(); i++) {
            Connection conn = connections.get(i - removed);
            if (conn == this) {
                continue;
            }
            if (conn.to == other) {
                connections.remove(i - removed);
                removed++;
            }
        }
    }
}
