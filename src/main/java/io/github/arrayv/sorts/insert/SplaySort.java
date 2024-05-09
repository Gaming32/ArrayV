package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Splay")
public final class SplaySort extends Sort {

    // Splay sort is an adaptive algorithm based on splay tree data structure
    // It is O(n log n) in the average/worst case, and O(n) in the best case when
    // the data is mostly sorted

    public SplaySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    // Splay tree code retrieved from
    // https://www.geeksforgeeks.org/splay-tree-set-2-insert-delete/

    final private class Node {
        int key;
        Node left, right;

        public Node(int item) {
            key = item;
            left = right = null;
        }

    }

    private Node root;
    private int index, length;

    private Node treeWrite(Node element, int at) {
        Node node = new Node(0);

        if (at > 0 && at < this.length)
            Highlights.markArray(1, at - 1);
        Writes.changeAuxWrites(1);
        Writes.startLap();
        node = element;
        Writes.stopLap();

        Delays.sleep(0.25);

        return node;
    }

    private Node leftRotate(Node x, int depth) {
        Node y = x.right;
        x.right = treeWrite(y.left, depth * 2 + 1);
        y.left = treeWrite(x, depth * 4 + 2);
        return y;
    }

    private Node rightRotate(Node x, int depth) {
        Node y = x.left;
        x.left = treeWrite(y.right, depth * 2);
        y.right = treeWrite(x, depth * 4 + 1);
        return y;
    }

    private void insert(int key) {
        this.root = this.treeWrite(insertRec(this.root, key, 1), 1);
        Writes.changeAllocAmount(1);
    }

    private Node splay(Node root, int key, int depth) {
        if (root == null) {
            return root;
        }

        if (Reads.compareValues(root.key, key) == 1) {
            if (root.left == null) {
                return root;
            }

            if (Reads.compareValues(root.left.key, key) == 1) {
                root.left.left = treeWrite(splay(root.left.left, key, depth * 4), depth * 4);
                root = treeWrite(rightRotate(root, depth), depth);
            } else {
                root.left.right = treeWrite(splay(root.left.right, key, depth * 4 + 1), depth * 4 + 1);
                if (root.left.right != null) {
                    root.left = treeWrite(leftRotate(root.left, depth * 2), depth * 2);
                }
            }
            return (root.left == null) ? root : rightRotate(root, depth);
        } else {
            if (root.right == null) {
                return root;
            }

            if (Reads.compareValues(root.right.key, key) == 1) {
                root.right.left = treeWrite(splay(root.right.left, key, depth * 4 + 2), depth * 4 + 2);
                if (root.right.left != null) {
                    root.right = treeWrite(rightRotate(root.right, depth * 2), depth * 2);
                }
            } else {
                root.right.right = treeWrite(splay(root.right.right, key, depth * 4 + 3), depth * 4 + 3);
                root = treeWrite(leftRotate(root, depth), depth);
            }
            return (root.right == null) ? root : leftRotate(root, depth);
        }
    }

    private Node insertRec(Node root, int key, int depth) {
        if (root == null) {
            root = treeWrite(new Node(key), 1);
            return root;
        }

        root = splay(root, key, depth);
        Node n = new Node(key);

        if (Reads.compareValues(root.key, key) == 1) {
            n.right = treeWrite(root, 3);
            n.left = treeWrite(root.left, 2);
            root.left = treeWrite(null, depth * 2);
        } else {
            n.left = treeWrite(root, 2);
            n.right = treeWrite(root.right, 3);
            root.right = treeWrite(null, depth * 2 + 1);
        }
        return n;
    }

    private void traverseRec(Node root, int[] array) {
        if (root != null) {
            this.traverseRec(root.left, array);
            Writes.write(array, this.index++, root.key, 1, true, false);
            this.traverseRec(root.right, array);
        }
    }

    private void treeIns(int arr[]) {
        for (int i = 0; i < this.length; i++) {
            Highlights.markArray(2, i);
            this.insert(arr[i]);
        }
        Highlights.clearMark(2);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.length = currentLength;
        this.treeIns(array);
        this.index = 0;
        this.traverseRec(this.root, array);
        Writes.changeAllocAmount(-length);
    }
}
