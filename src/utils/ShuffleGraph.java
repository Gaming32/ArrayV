package utils;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import main.ArrayVisualizer;
import utils.shuffle_utils.Connection;
import utils.shuffle_utils.Node;

public class ShuffleGraph implements Collection<ShuffleInfo> {
    public List<Node> nodes;
    public List<Connection> connections;
    public Node selected;
    public Connection dragging;
    public Node dragCandidate;

    public ShuffleGraph() {
        this(new ShuffleInfo[0]);
    }

    public ShuffleGraph(ShuffleInfo[] shuffles) {
        this.nodes = new ArrayList<>();
        this.nodes.add(new Node(null, this, -Node.WIDTH, 15));
        for (ShuffleInfo shuffle : shuffles) {
            this.nodes.add(new Node(shuffle, this));
        }
        this.connections = new ArrayList<>();
        this.selected = null;
        this.dragging = null;
        this.dragCandidate = null;
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

    public void addDisconnected(ShuffleInfo shuffle) {
        this.nodes.add(new Node(shuffle, this));
    }

    public void addDisconnected(ShuffleInfo shuffle, int x, int y) {
        this.nodes.add(new Node(shuffle, this, x, y));
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
                        conn.remove();
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
            this.dragging.remove();
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


    // Collection<ShuffleInfo> code
    public int size() {
        int size = 0;
        Node node = this.nodes.get(0);
        while (node != null) {
            size++;
            Connection connect = node.postConnection;
            if (connect == null) {
                break;
            }
            node = connect.to;
        }
        return size - 1;
    }

    public void clear() {
        this.nodes.subList(1, this.nodes.size()).clear();
        this.nodes.get(0).postConnection = null;
    }

    public Node findLast() {
        Node previous = null;
        Node node = this.nodes.get(0);
        while (node != null) {
            Connection connect = node.postConnection;
            if (connect == null) {
                previous = node;
                node = null;
                break;
            }
            previous = node;
            node = connect.to;
        }
        return previous;
    }

    public boolean add(ShuffleInfo shuffle) {
        add(shuffle, findLast());
        return true;
    }

    public boolean addAll(Collection<? extends ShuffleInfo> c) {
        Node after = findLast();
        for (ShuffleInfo shuffle : c) {
            after = add(shuffle, after);
        }
        return true;
    }

    Node add(ShuffleInfo shuffle, Node after) {
        Node newNode = new Node(shuffle, this, after.x + Node.WIDTH + 15, after.y);
        if (after.postConnection == null) {
            after.postConnection = new Connection(after, newNode);
            this.connections.add(after.postConnection);
        } else {
            after.postConnection.to = newNode;
        }
        newNode.preConnection = after.postConnection;
        this.nodes.add(newNode);
        return newNode;
    }

    Node find(Object o) {
        if (o == null) {
            return null;
        }
        Node node = this.nodes.get(0);
        while (node != null) {
            Connection connect = node.postConnection;
            if (connect == null) {
                break;
            }
            node = connect.to;
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
        Node found = find(o);
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
            Node node = this.nodes.get(0);
            while (node != null) {
                Connection connect = node.postConnection;
                if (connect == null) {
                    break;
                }
                node = connect.to;
                result[i++] = node.getValue();
            }
            if (i < a.length - 1) {
                a[i] = null;
            }
            return a;
        } else if (type == Node.class) {
            return this.nodes.toArray(a);
        } else {
            throw new ArrayStoreException();
        }
    }

    public Iterator<ShuffleInfo> iterator() {
        return new GraphIterator(this);
    }

    protected class GraphIterator implements Iterator<ShuffleInfo> {
        Node currentNode, nextNode;
        
        GraphIterator(ShuffleGraph graph) {
            this.currentNode = graph.nodes.get(0);
            this.nextNode = findNext();
        }

        Node findNext() {
            Connection connect = this.currentNode.postConnection;
            if (connect != null) {
                Node next = connect.to;
                if (next != null) {
                    return next;
                }
            }
            return null;
        }

        public boolean hasNext() {
            return this.nextNode != null;
        }

        public ShuffleInfo next() {
            if (this.nextNode == null) {
                throw new NoSuchElementException();
            }
            this.currentNode = this.nextNode;
            this.nextNode = findNext();
            return this.currentNode.getValue();
        }

        public void remove() {
            this.nextNode.delete();
            this.nextNode = findNext();
        }
    }
}
