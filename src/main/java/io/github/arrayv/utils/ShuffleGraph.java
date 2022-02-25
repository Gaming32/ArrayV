package io.github.arrayv.utils;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.shuffleutils.GraphConnection;
import io.github.arrayv.utils.shuffleutils.GraphNode;

public class ShuffleGraph implements Collection<ShuffleInfo> {
    private List<GraphNode> nodes;
    private List<GraphConnection> connections;
    private GraphNode selected;
    private GraphConnection dragging;
    private GraphNode dragCandidate;
    private double sleepRatio;

    static final int DEFAULT_TEXT_SIZE = 24;
    Map<String, Integer> textSizes = new HashMap<>();

    public ShuffleGraph() {
        this(new ShuffleInfo[0]);
    }

    public ShuffleGraph(ShuffleInfo[] shuffles) {
        this.nodes = new ArrayList<>();
        this.nodes.add(new GraphNode(null, this, -GraphNode.WIDTH, 15));
        for (ShuffleInfo shuffle : shuffles) {
            this.nodes.add(new GraphNode(shuffle, this));
        }
        this.connections = new ArrayList<>();
        this.selected = null;
        this.dragging = null;
        this.dragCandidate = null;
        this.sleepRatio = 1;
    }

    public static ShuffleGraph single(ShuffleInfo shuffle) {
        ShuffleGraph graph = new ShuffleGraph();
        graph.add(shuffle);
        return graph;
    }

    public static ShuffleGraph single(Shuffles shuffle) {
        return single(new ShuffleInfo(shuffle));
    }

    public static ShuffleGraph single(Distributions distribution) {
        return single(new ShuffleInfo(distribution));
    }

    public static ShuffleGraph single(Distributions distribution, boolean warped) {
        return single(new ShuffleInfo(distribution, warped));
    }

    public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer) {
        for (ShuffleInfo shuffle : this) {
            shuffle.shuffle(array, arrayVisualizer);
        }
    }

    public ShuffleGraph setSleepRatio(double ratio) {
        this.sleepRatio = ratio;
        return this;
    }

    public ShuffleGraph addDisconnected(ShuffleInfo shuffle) {
        this.nodes.add(new GraphNode(shuffle, this));
        return this;
    }

    public ShuffleGraph addDisconnected(ShuffleInfo shuffle, int x, int y) {
        this.nodes.add(new GraphNode(shuffle, this, x, y));
        return this;
    }

    public ShuffleGraph addSingle(ShuffleInfo shuffle) {
        add(shuffle);
        return this;
    }

    public ShuffleGraph addSingle(Shuffles shuffle) {
        return addSingle(new ShuffleInfo(shuffle));
    }

    public ShuffleGraph addSingle(Distributions distribution) {
        return addSingle(new ShuffleInfo(distribution));
    }

    public ShuffleGraph addSingle(Distributions distribution, boolean warped) {
        return addSingle(new ShuffleInfo(distribution, warped));
    }

    public Point findSafeCoordinates(int baseX, int baseY, int offsetX, int offsetY) {
        Point p = new Point(baseX, baseY);
        while (this.nodes.stream().anyMatch(node -> node.getX() == p.getX() && node.getY() == p.getY())) {
            p.x += offsetX;
            p.y += offsetY;
        }
        return p;
    }

    public int removeAllDisconnected() {
        Set<GraphNode> toKeep = new HashSet<>();
        toKeep.add(this.nodes.get(0));
        for (GraphNode connected : connectedNodesIterable()) {
            toKeep.add(connected);
        }
        int removed = this.nodes.size() - toKeep.size();
        this.nodes.retainAll(toKeep);
        return removed;
    }

    public Iterator<GraphNode> iterateConnectedNodes() {
        return new NodeIterator(this);
    }

    public Iterable<GraphNode> connectedNodesIterable() {
        return new Iterable<GraphNode>() {
            public Iterator<GraphNode> iterator() {
                return ShuffleGraph.this.iterateConnectedNodes();
            }
        };
    }

    public void draw(Graphics2D g) {
        for (GraphConnection connection : this.connections) {
            connection.draw(g);
        }
        for (GraphNode node : this.nodes) {
            node.draw(g);
        }
    }

    public void drag(Point rel) {
        if (this.selected != null) {
            this.selected.drag(rel);
        } else if (this.dragging != null) {
            Point pos = this.dragging.getCurrentDragPos();
            pos.translate(rel.x, rel.y);
            boolean foundCandidate = false;
            ListIterator<GraphNode> it = this.nodes.listIterator(this.nodes.size());
            while (it.hasPrevious()) {
                GraphNode node = it.previous();
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
        ListIterator<GraphNode> it = this.nodes.listIterator(this.nodes.size());
        while (it.hasPrevious()) {
            GraphNode node = it.previous();
            if (node.inArea(pos)) {
                this.selected = node;
                return;
            } else if (node.inStartDrag(pos)) {
                GraphConnection newConnection = new GraphConnection(node, pos);
                int removed = 0;
                for (int i = 0; i < this.connections.size(); i++) {
                    GraphConnection conn = this.connections.get(i - removed);
                    if (conn.getFrom() == node) {
                        this.connections.remove(i - removed);
                        conn.remove();
                        removed++;
                    }
                }
                this.connections.add(newConnection);
                this.dragging = newConnection;
                node.setPostConnection(newConnection);
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
            this.dragging.remove();
        }
        this.dragging = null;
        this.dragCandidate = null;
    }

    public void deleteNode() {
        if (this.selected != null) {
            this.selected.delete();
        }
    }

    public void calcTextSize(String text, int fit, Graphics2D g) {
        if (textSizes.containsKey(text)) {
            g.setFont(g.getFont().deriveFont((float)textSizes.get(text)));
            return;
        }
        int size = DEFAULT_TEXT_SIZE;
        g.setFont(g.getFont().deriveFont((float)size));
        while (g.getFontMetrics().stringWidth(text) >= fit) {
            size--;
            g.setFont(g.getFont().deriveFont((float)size));
        }
        textSizes.put(text, size);
    }


    // Collection<ShuffleInfo> code
    public int size() {
        int size = 0;
        GraphNode node = this.nodes.get(0);
        while (node != null) {
            size++;
            GraphConnection connect = node.getPostConnection();
            if (connect == null) {
                break;
            }
            node = connect.getTo();
        }
        return size - 1;
    }

    public void clear() {
        this.nodes.subList(1, this.nodes.size()).clear();
        this.nodes.get(0).setPostConnection(null);
    }

    public GraphNode findLast() {
        GraphNode previous = null;
        GraphNode node = this.nodes.get(0);
        while (node != null) {
            GraphConnection connect = node.getPostConnection();
            if (connect == null) {
                previous = node;
                node = null;
                break;
            }
            previous = node;
            node = connect.getTo();
        }
        return previous;
    }

    public boolean add(ShuffleInfo shuffle) {
        add(shuffle, findLast());
        return true;
    }

    public boolean addAll(Collection<? extends ShuffleInfo> c) {
        GraphNode after = findLast();
        for (ShuffleInfo shuffle : c) {
            after = add(shuffle, after);
        }
        return true;
    }

    GraphNode add(ShuffleInfo shuffle, GraphNode after) {
        GraphNode newNode = new GraphNode(shuffle, this, after.getX() + GraphNode.WIDTH + 15, after.getY());
        if (after.getPostConnection() == null) {
            after.setPostConnection(new GraphConnection(after, newNode));
            this.connections.add(after.getPostConnection());
        } else {
            if (after.getPostConnection().getTo() != null) {
                after.getPostConnection().getTo().setPreConnection(null);
            }
            after.getPostConnection().setTo(newNode);
        }
        newNode.setPreConnection(after.getPostConnection());
        this.nodes.add(newNode);
        return newNode;
    }

    GraphNode find(Object o) {
        if (o == null) {
            return null;
        }
        GraphNode node = this.nodes.get(0);
        while (node != null) {
            GraphConnection connect = node.getPostConnection();
            if (connect == null) {
                break;
            }
            node = connect.getTo();
            if (node == null) {
                break;
            }
            if (node.equals(o)) {
                return node;
            }
        }
        return null;
    }

    public boolean contains(Object o) {
        return find(o) != null;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return iterator().hasNext();
    }

    public boolean remove(Object o) {
        GraphNode found = find(o);
        if (found == null) {
            return false;
        }
        found.delete();
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        boolean affected = false;
        for (Object o : c) {
            affected = remove(o) || affected;
        }
        return affected;
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        List<ShuffleInfo> result = new ArrayList<>();
        for (ShuffleInfo shuffle : this) {
            result.add(shuffle);
        }
        return result.toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Class<?> type = a.getClass().getComponentType();
        if (type == ShuffleInfo.class || type == Object.class) {
            int mySize = size();
            if (mySize > a.length) {
                return (T[])toArray();
            }
            ShuffleInfo[] result = (ShuffleInfo[])a;
            int i = 0;
            GraphNode node = this.nodes.get(0);
            while (node != null) {
                GraphConnection connect = node.getPostConnection();
                if (connect == null) {
                    break;
                }
                node = connect.getTo();
                result[i++] = node.getValue();
            }
            if (i < a.length - 1) {
                a[i] = null;
            }
            return a;
        } else if (type == GraphNode.class) {
            return this.nodes.toArray(a);
        } else {
            throw new ArrayStoreException();
        }
    }

    public Iterator<ShuffleInfo> iterator() {
        return new GraphIterator(this);
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public List<GraphConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<GraphConnection> connections) {
        this.connections = connections;
    }

    public GraphNode getSelected() {
        return selected;
    }

    public void setSelected(GraphNode selected) {
        this.selected = selected;
    }

    public GraphConnection getDragging() {
        return dragging;
    }

    public void setDragging(GraphConnection dragging) {
        this.dragging = dragging;
    }

    public GraphNode getDragCandidate() {
        return dragCandidate;
    }

    public void setDragCandidate(GraphNode dragCandidate) {
        this.dragCandidate = dragCandidate;
    }

    public double getSleepRatio() {
        return sleepRatio;
    }

    public Map<String, Integer> getTextSizes() {
        return textSizes;
    }

    public void setTextSizes(Map<String, Integer> textSizes) {
        this.textSizes = textSizes;
    }

    protected class GraphIterator implements Iterator<ShuffleInfo> {
        NodeIterator it;

        GraphIterator(ShuffleGraph graph) {
            this.it = new NodeIterator(graph);
        }

        public boolean hasNext() {
            return this.it.hasNext();
        }

        public ShuffleInfo next() {
            return this.it.next().getValue();
        }

        public void remove() {
            this.it.remove();;
        }
    }

    protected class NodeIterator implements Iterator<GraphNode> {
        GraphNode currentNode, nextNode;

        NodeIterator(ShuffleGraph graph) {
            this.currentNode = graph.nodes.get(0);
            this.nextNode = findNext();
        }

        GraphNode findNext() {
            GraphConnection connect = this.currentNode.getPostConnection();
            if (connect != null) {
                GraphNode next = connect.getTo();
                if (next != null) {
                    return next;
                }
            }
            return null;
        }

        public boolean hasNext() {
            return this.nextNode != null;
        }

        public GraphNode next() {
            if (this.nextNode == null) {
                throw new NoSuchElementException();
            }
            this.currentNode = this.nextNode;
            this.nextNode = findNext();
            return this.currentNode;
        }

        public void remove() {
            this.nextNode.delete();
            this.nextNode = findNext();
        }
    }
}
