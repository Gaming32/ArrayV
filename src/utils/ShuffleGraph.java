package utils;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import utils.shuffle_utils.Connection;
import utils.shuffle_utils.Node;

public class ShuffleGraph {
    public List<Node> nodes;
    public List<Connection> connections;
    public Node selected;
    public Connection dragging;
    public Node dragCandidate;

    public ShuffleGraph() {
        this(new Shuffles[] {});
    }

    public ShuffleGraph(Shuffles[] shuffles) {
        this.nodes = new ArrayList<>();
        this.nodes.add(new Node(null, this, -Node.WIDTH, 15));
        for (Shuffles shuffle : shuffles) {
            this.nodes.add(new Node(shuffle, this));
        }
        this.connections = new ArrayList<>();
        this.selected = null;
        this.dragging = null;
        this.dragCandidate = null;
    }

    public void draw(Graphics2D g) {
        for (Connection connection : this.connections) {
            connection.draw(g);
        }
        for (Node node : this.nodes) {
            node.draw(g);
        }
    }

    public void drag(Point rel) {
        if (this.selected != null) {
            this.selected.drag(rel);
        } else if (this.dragging != null) {
            Point pos = this.dragging.currentDragPos;
            pos.translate(rel.x, rel.y);
            boolean foundCandidate = false;
            ListIterator<Node> it = this.nodes.listIterator(this.nodes.size());
            while (it.hasPrevious()) {
                Node node = it.previous();
                if (node.inArea(pos)) {
                    this.dragCandidate = node;
                    foundCandidate = true;
                    break;
                }
            }
            if (!foundCandidate) {
                this.dragCandidate = null;
            }
        }
    }

    public void select(Point pos) {
        ListIterator<Node> it = this.nodes.listIterator(this.nodes.size());
        while (it.hasPrevious()) {
            Node node = it.previous();
            if (node.inArea(pos)) {
                this.selected = node;
                return;
            } else if (node.inStartDrag(pos)) {
                Connection newConnection = new Connection(node, pos);
                int removed = 0;
                for (int i = 0; i < this.connections.size(); i++) {
                    Connection conn = this.connections.get(i - removed);
                    if (conn.from == node) {
                        this.connections.remove(i - removed);
                        removed++;
                    }
                }
                this.connections.add(newConnection);
                this.dragging = newConnection;
                node.postConnection = newConnection;
                this.selected = null;
                return;
            }
        }
        this.selected = null;
    }

    public void endConnection() {
        if (this.dragging == null) {
            return;
        }
        if (this.dragCandidate != null) {
            this.dragging.finishDragging(this.dragCandidate);
        } else {
            this.connections.remove(this.dragging);
        }
        this.dragging = null;
        this.dragCandidate = null;
    }

    public void deleteNode() {
        if (this.selected != null) {
            this.selected.delete();
        }
    }
}
