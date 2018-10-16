/*
 * Copyright 2014-2017 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cellsgame.game.util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * A {@link List} implementation that stores int values with the ability to not have them boxed.
 */
public class IntArrayList extends AbstractList<Integer> implements List<Integer>, RandomAccess {
    /**
     * The default value that will be used in place of null for an element.
     */
    public static final int DEFAULT_NULL_VALUE = Integer.MIN_VALUE;

    /**
     * Initial capacity to which the array will be sized.
     */
    public static final int INITIAL_CAPACITY = 10;

    /**
     * Maximum capacity to which the array can grow.
     */
    public static final int MAX_CAPACITY = Integer.MAX_VALUE - 8;

    private final int nullValue;
    private int size = 0;
    private int[] elements;

    public IntArrayList() {
        this(INITIAL_CAPACITY, DEFAULT_NULL_VALUE);
    }

    /**
     * Construct a new list.
     *
     * @param initialCapacity for the backing array.
     * @param nullValue       to be used to represent a null element.
     */
    public IntArrayList(
            final int initialCapacity,
            final int nullValue) {
        this.nullValue = nullValue;
        elements = new int[Math.max(initialCapacity, INITIAL_CAPACITY)];
    }

    /**
     * Create a new list that wraps an existing arrays without copying it.
     *
     * @param initialElements to be wrapped.
     * @param initialSize     of the array to wrap.
     * @param nullValue       to be used to represent a null element.
     */
    public IntArrayList(
            final int[] initialElements,
            final int initialSize,
            final int nullValue) {
        wrap(initialElements, initialSize);
        this.nullValue = nullValue;
    }

    /**
     * Wrap an existing array without copying it.
     * <p>
     * The array length must be greater than or equal to {@link #INITIAL_CAPACITY}.
     *
     * @param initialElements to be wrapped.
     * @param initialSize     of the array to wrap.
     * @throws IllegalArgumentException if the initialSize is is less than {@link #INITIAL_CAPACITY} or greater than
     *                                  the length of the initial array.
     */
    public void wrap(
            final int[] initialElements,
            final int initialSize) {
        if (initialSize < 0 || initialSize > initialElements.length) {
            throw new IllegalArgumentException(
                    "Illegal initial size " + initialSize + " for array length of " + initialElements.length);
        }

        if (initialElements.length < INITIAL_CAPACITY) {
            throw new IllegalArgumentException(
                    "Illegal initial array length " + initialElements.length + ", minimum required is " + INITIAL_CAPACITY);
        }

        elements = initialElements;
        size = initialSize;
    }

    /**
     * The value representing a null element.
     *
     * @return value representing a null element.
     */
    public int nullValue() {
        return nullValue;
    }

    public int size() {
        return size;
    }

    public void clear() {
        size = 0;
    }

    /**
     * Trim the underlying array to be the same capacity as the current size.
     */
    public void trimToSize() {
        if (elements.length != size) {
            elements = Arrays.copyOf(elements, Math.max(INITIAL_CAPACITY, size));
        }
    }

    public Integer get(
            final int index) {
        final int value = getInt(index);

        return value == nullValue ? null : value;
    }

    /**
     * Get the element at a given index without boxing.
     *
     * @param index to get.
     * @return the unboxed element.
     */
    public int getInt(
            final int index) {
        checkIndex(index);

        return elements[index];
    }

    public boolean add(final Integer element) {
        return addInt(null == element ? nullValue : element);
    }

    /**
     * Add an element without boxing.
     *
     * @param element to be added.
     * @return true
     */
    public boolean addInt(final int element) {
        ensureCapacityPrivate(size + 1);

        elements[size] = element;
        size++;

        return true;
    }

    public void add(
            final int index,
            final Integer element) {
        addInt(index, null == element ? nullValue : element);
    }

    /**
     * Add a element without boxing at a given index.
     *
     * @param index   at which the element should be added.
     * @param element to be added.
     */
    public void addInt(
            final int index,
            final int element) {
        checkIndexForAdd(index);

        final int requiredSize = size + 1;
        ensureCapacityPrivate(requiredSize);

        if (index < size) {
            System.arraycopy(elements, index, elements, index + 1, requiredSize - index);
        }

        elements[index] = element;
        size++;
    }

    public Integer set(
            final int index,
            final Integer element) {
        final int previous = setInt(index, null == element ? nullValue : element);

        return nullValue == previous ? null : previous;
    }

    /**
     * Set an element at a given index without boxing.
     *
     * @param index   at which to set the element.
     * @param element to be added.
     * @return the previous element at the index.
     */
    public int setInt(
            final int index,
            final int element) {
        checkIndex(index);

        final int previous = elements[index];
        elements[index] = element;

        return previous;
    }

    /**
     * Does the list contain this element value.
     *
     * @param value of the element.
     * @return true if present otherwise false.
     */
    public boolean containsInt(final int value) {
        return -1 != indexOf(value);
    }

    /**
     * Index of the first element with this value.
     *
     * @param value for the element.
     * @return the index if found otherwise -1.
     */
    public int indexOf(
            final int value) {
        for (int i = 0; i < size; i++) {
            if (value == elements[i]) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Index of the last element with this value.
     *
     * @param value for the element.
     * @return the index if found otherwise -1.
     */
    public int lastIndexOf(
            final int value) {
        for (int i = size - 1; i >= 0; i--) {
            if (value == elements[i]) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Remove at a given index.
     *
     * @param index of the element to be removed.
     * @return the existing value at this index.
     */
    public Integer remove(
            final int index) {
        checkIndex(index);

        final int value = elements[index];

        final int moveCount = size - index - 1;
        if (moveCount > 0) {
            System.arraycopy(elements, index + 1, elements, index, moveCount);
        }

        size--;

        return value;
    }

    /**
     * Remove the first instance of a value if found in the list.
     *
     * @param value to be removed.
     * @return true if successful otherwise false.
     */
    public boolean removeInt(final int value) {
        final int index = indexOf(value);
        if (-1 != index) {
            remove(index);

            return true;
        }

        return false;
    }

    /**
     * Push an element onto the end of the array like a stack.
     *
     * @param element to be pushed onto the end of the array.
     */
    public void pushInt(final int element) {
        ensureCapacityPrivate(size + 1);

        elements[size] = element;
        size++;
    }

    /**
     * Pop a value off the end of the array as a stack operation.
     *
     * @return the value at the end of the array.
     * @throws NoSuchElementException if the array is empty.
     */
    public int popInt() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return elements[--size];
    }

    /**
     * For each element in order provide the int value to a {@link IntConsumer}.
     *
     * @param consumer for each element.
     */
    public void forEachOrderedInt(final IntConsumer consumer) {
        for (int i = 0; i < size; i++) {
            consumer.accept(elements[i]);
        }
    }

    /**
     * Create a {@link IntStream} over the elements of underlying array.
     *
     * @return a {@link IntStream} over the elements of underlying array.
     */
    public IntStream intStream() {
        return Arrays.stream(elements, 0, size);
    }

    /**
     * Create a new array that is a copy of the elements.
     *
     * @return a copy of the elements.
     */
    public int[] toIntArray() {
        return Arrays.copyOf(elements, size);
    }

    /**
     * Create a new array that is a copy of the elements.
     *
     * @param dst destination array for the copy if it is the correct size.
     * @return a copy of the elements.
     */
    public int[] toIntArray(final int[] dst) {
        if (dst.length == size) {
            System.arraycopy(elements, 0, dst, 0, dst.length);
            return dst;
        } else {
            return Arrays.copyOf(elements, size);
        }
    }

    /**
     * Ensure the backing array has a required capacity.
     *
     * @param requiredCapacity for the backing array.
     */
    public void ensureCapacity(final int requiredCapacity) {
        ensureCapacityPrivate(Math.max(requiredCapacity, INITIAL_CAPACITY));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IntArrayList integers = (IntArrayList) o;

        if (nullValue != integers.nullValue) return false;
        if (size != integers.size) return false;
        return Arrays.equals(elements, integers.elements);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + nullValue;
        result = 31 * result + size;
        result = 31 * result + Arrays.hashCode(elements);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            final int value = elements[i];
            if (value != nullValue) {
                sb.append(value);
                sb.append(", ");
            }
        }

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }

        sb.append(']');

        return sb.toString();
    }

    private void ensureCapacityPrivate(final int requiredCapacity) {
        final int currentCapacity = elements.length;
        if (requiredCapacity > currentCapacity) {
            int newCapacity = currentCapacity + (currentCapacity >> 1);

            if (newCapacity < 0 || newCapacity > MAX_CAPACITY) {
                if (currentCapacity == MAX_CAPACITY) {
                    throw new IllegalStateException("Max capacity reached: " + MAX_CAPACITY);
                }

                newCapacity = MAX_CAPACITY;
            }

            final int[] newElements = new int[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, currentCapacity);
            elements = newElements;
        }
    }

    private void checkIndex(final int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("index=" + index + " size=" + size);
        }
    }

    private void checkIndexForAdd(final int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("index=" + index + " size=" + size);
        }
    }

    public void shuffle() {
        Random rnd = ThreadLocalRandom.current();
        shuffle(rnd);
    }

    private void shuffle(Random rnd) {
        for (int i = size; i > 1; i--)
            swap(i - 1, rnd.nextInt(i));
    }

    private void swap(int i, int j) {
        // instead of using a raw type here, it's possible to capture
        // the wildcard but it will require a call to a supplementary
        // private method
        setInt(i, setInt(j, getInt(i)));
    }

    public IntIterator intIterator() {
        return new IntIterator();
    }

    public IntArrayList copy() {
        final int[] elements = this.elements;
        int size = this.size;
        int[] e = new int[elements.length];
        System.arraycopy(elements, 0, e, 0, e.length);
        return new IntArrayList(e, size, nullValue);
    }

    public class IntIterator {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;

        /**
         * Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;

        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        public int next() {
            checkForComodification();
            try {
                int i = cursor;
                int next = getInt(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                IntArrayList.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
}
