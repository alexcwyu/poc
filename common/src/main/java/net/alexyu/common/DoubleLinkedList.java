package net.alexyu.common;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alexc on 5/4/2017.
 */
public class DoubleLinkedList<T> implements Iterable<T> {
    private class Node<T> {
        private T item;
        private Node<T> next;
        private Node<T> prev;

        public Node(T item, Node<T> next, Node<T> prev) {
            this.item = item;
            this.next = next;
            if (next != null) {
                next.prev = this;
            }
            this.prev = prev;
            if (prev != null) {
                prev.next = this;
            }
        }

        @Override
        public String toString() {
            return "item = " + item;
        }
    }

    private class DoubleLinkedListIterator<T> implements ListIterator<T> {
        private int index;
        private Node<T> current;
        private Node<T> lastAccessed;

        public DoubleLinkedListIterator(Node<T> current) {
            this.current = current;
            this.lastAccessed = null;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            lastAccessed = current;
            T item = current.item;
            current = current.next;
            index++;
            return item;
        }

        @Override
        public T previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            current = current.prev;
            index--;
            lastAccessed = current;
            return current.item;
        }

        @Override
        public void remove() {
            if (lastAccessed == null)
                throw new IllegalStateException();
            Node<T> prev = lastAccessed.prev;
            Node<T> next = lastAccessed.next;

            next.prev = prev;
            prev.next = next;
            size--;
            if (current == lastAccessed)
                current = next;
            else
                index--;
            lastAccessed = null;
        }

        @Override
        public void set(T item) {
            if (lastAccessed == null)
                throw new IllegalStateException();
            lastAccessed.item = item;
        }

        @Override
        public void add(T item) {
            Node<T> prev = current.prev;
            Node<T> newNext = current;
            Node<T> newCurrent = new Node<>(item, newNext, prev);
            index++;
            size++;
            lastAccessed = null;
        }
    }

    private int size;
    private Node<T> head;
    private Node<T> tail;

    public DoubleLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void addFirst(T item) {
        Node<T> newHead = new Node<>(item, head, null);
        if (head != null) {
            head.prev = newHead;
        }
        head = newHead;

        if (tail == null) {
            tail = newHead;
        }
        size++;
    }

    public void addLast(T item) {
        Node<T> newTail = new Node<>(item, null, tail);
        if (tail != null) {
            tail.next = newTail;
        }
        tail = newTail;

        if (head == null) {
            head = newTail;
        }
        size++;
    }

    public T removeFirst() {
        if (head != null) {
            Node<T> origHead = head;
            head = head.next;
            if (head != null) {
                head.prev = null;
            }
            size--;
            return origHead.item;
        }
        return null;
    }

    public T removeLast() {
        if (tail != null) {
            Node<T> origTail = tail;
            tail = tail.prev;
            if (tail != null) {
                tail.next = null;
            }
            size--;
            return origTail.item;
        }
        return null;
    }

    public T peekFirst() {
        if (head != null) {
            return head.item;
        }
        return null;
    }

    public T peekLast() {
        if (tail != null) {
            return tail.item;
        }
        return null;
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        Node<T> tmp = head;
        while (tmp != null) {
            list.add(tmp.item.toString());
            tmp = tmp.next;
        }
        return list.stream().collect(Collectors.joining(", "));
    }

    @Override
    public Iterator<T> iterator() {
        return new DoubleLinkedListIterator<>(head);
    }
}
