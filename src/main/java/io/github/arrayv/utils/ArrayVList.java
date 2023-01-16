package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

import java.util.*;
import java.util.function.Consumer;

public class ArrayVList extends AbstractList<Integer> implements RandomAccess, java.io.Serializable {
    private static final int DEFAULT_CAPACITY = 128;
    private static final double DEFAULT_GROW_FACTOR = 2;

    private static ArrayVisualizer arrayVisualizer;
    // @checkstyle:off StaticVariableNameCheck
    private static Reads Reads;
    private static Writes Writes;
    // @checkstyle:on StaticVariableNameCheck

    int[] internal;
    double growFactor;
    int count, capacity;

    public ArrayVList() {
        this(DEFAULT_CAPACITY, DEFAULT_GROW_FACTOR);
    }

    public ArrayVList(int capacity) {
        this(capacity, DEFAULT_GROW_FACTOR);
    }

    public ArrayVList(int capacity, double growFactor) {
        if (arrayVisualizer == null) {
            arrayVisualizer = ArrayVisualizer.getInstance();
            Reads = arrayVisualizer.getReads();
            Writes = arrayVisualizer.getWrites();
        }
        this.internal = new int[capacity];
        arrayVisualizer.getArrays().add(internal);
        this.count = 0;
        this.capacity = capacity;
        this.growFactor = growFactor;
    }

    public void delete() {
        Writes.changeAllocAmount(-count);
        arrayVisualizer.getArrays().remove(internal);
        this.internal = null;
        this.count = 0;
        this.capacity = 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) > -1;
    }

    @Override
    public Object[] toArray() {
        Integer[] result = new Integer[count];
        for (int i = 0; i < count; i++) {
            result[i] = internal[i];
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < count) {
            for (int i = 0; i < count; i++) {
                a[i] = (T)Integer.valueOf(internal[i]);
            }
            return a;
        }
        return (T[])toArray();
    }

    protected void grow() {
        int newCapacity = (int)Math.ceil(capacity * growFactor);
        int[] newInternal = new int[newCapacity];
        System.arraycopy(internal, 0, newInternal, 0, count);
        ArrayList<int[]> arrays = arrayVisualizer.getArrays();
        arrays.set(arrays.indexOf(internal), newInternal);
        this.capacity = newCapacity;
        this.internal = newInternal;
    }

    public boolean add(int e, double sleep, boolean mark) {
        if (count == capacity) {
            grow();
        }
        Writes.write(internal, count++, e, sleep, mark, true);
        Writes.changeAllocAmount(1);
        return true;
    }

    @Override
    public boolean add(Integer e) {
        return add(e, 0, false);
    }

    private void fastRemove(int index) {
        int numMoved = count - index - 1;
        if (numMoved > 0)
            Writes.arraycopy(internal, index + 1, internal, index, numMoved, 0, false, true);
        internal[--count] = 0;
        Writes.changeAllocAmount(-1);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        } else {
            for (int i = 0; i < count; i++) {
                if (o.equals(internal[i])) {
                    fastRemove(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Integer> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        Arrays.fill(internal, 0, count, 0);
        Writes.changeAllocAmount(-count);
        count = 0;
    }

    @Override
    public Integer get(int index) {
        return internal[index];
    }

    public Integer set(int index, int element, double sleep, boolean mark) {
        rangeCheck(index);
        int old = internal[index];
        Writes.write(internal, index, element, sleep, mark, true);
        return old;
    }

    @Override
    public Integer set(int index, Integer element) {
        return set(index, element, 0, false);
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + this.count;
    }

    protected void removeRange(int fromIndex, int toIndex) {
        int numMoved = count - toIndex;
        System.arraycopy(internal, toIndex, internal, fromIndex,
                         numMoved);

        int sizeOffset = toIndex - fromIndex;
        int newSize = count - sizeOffset;
        Arrays.fill(internal, newSize, count, 0);
        count = newSize;
        Writes.changeAllocAmount(-sizeOffset);
    }

    private void rangeCheck(int index) {
        if (index >= count)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    @Override
    public void add(int index, Integer element) {

    }

    @Override
    public Integer remove(int index) {
        rangeCheck(index);
        int old = internal[index];
        fastRemove(index);
        return old;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            return -1;
        }
        if (!(o instanceof Integer)) {
            return -1;
        }
        int value = (Integer)o;
        for (int i = 0; i < count; i++) {
            if (Reads.compareValues(internal[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            return -1;
        }
        if (!(o instanceof Integer)) {
            return -1;
        }
        int value = (Integer)o;
        for (int i = count - 1; i >= 0; i--) {
            if (Reads.compareValues(internal[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<Integer> listIterator() {
        return new ListItr(0);
    }

    @Override
    public ListIterator<Integer> listIterator(int index) {
        return new ListItr(index);
    }

    private class Itr implements Iterator<Integer> {
        int cursor; // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        public boolean hasNext() {
            return cursor != count;
        }

        public Integer next() {
            int i = cursor;
            if (i >= count)
                throw new NoSuchElementException();
            cursor = i + 1;
            return ArrayVList.this.internal[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                ArrayVList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super Integer> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayVList.this.capacity;
            int i = cursor;
            if (i >= size) {
                return;
            }
            final int[] internal = ArrayVList.this.internal;
            if (i >= internal.length) {
                throw new ConcurrentModificationException();
            }
            while (i != size) {
                consumer.accept(internal[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
        }
    }

    private class ListItr extends Itr implements ListIterator<Integer> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public Integer previous() {
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            int[] internal = ArrayVList.this.internal;
            if (i >= internal.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return internal[lastRet = i];
        }

        public void set(Integer e) {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                ArrayVList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(Integer e) {
            try {
                int i = cursor;
                ArrayVList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public List<Integer> subList(int fromIndex, int toIndex) {
        return new SubList(this, 0, fromIndex, toIndex);
    }

    private class SubList extends AbstractList<Integer> implements RandomAccess {
        private final ArrayVList parent;
        private final int parentOffset;
        private final int offset;
        int size;

        SubList(ArrayVList parent,
                int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
        }

        @SuppressWarnings("unused")
        public Integer set(int index, int e) {
            rangeCheck(index);
            int oldValue = ArrayVList.this.internal[offset + index];
            ArrayVList.this.internal[offset + index] = e;
            return oldValue;
        }

        public Integer get(int index) {
            rangeCheck(index);
            return ArrayVList.this.internal[offset + index];
        }

        public int size() {
            return this.size;
        }

        @SuppressWarnings("unused")
        public void add(int index, int e) {
            rangeCheckForAdd(index);
            parent.add(parentOffset + index, e);
            size++;
        }

        public Integer remove(int index) {
            rangeCheck(index);
            int result = parent.remove(parentOffset + index);
            this.size--;
            return result;
        }

        protected void removeRange(int fromIndex, int toIndex) {
            parent.removeRange(parentOffset + fromIndex,
                               parentOffset + toIndex);
            this.size -= toIndex - fromIndex;
        }

        public Iterator<Integer> iterator() {
            return listIterator();
        }

        public ListIterator<Integer> listIterator(final int index) {
            return null;
        }

        public List<Integer> subList(int fromIndex, int toIndex) {
            return null;
            // subListRangeCheck(fromIndex, toIndex, size);
            // return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+this.size;
        }

        public Spliterator<Integer> spliterator() {
            return null;
        }
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }
}
