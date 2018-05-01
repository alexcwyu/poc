package net.alexyu.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by alexc on 5/4/2017.
 */
public class LRUCache2<T, R> {

    private int capacity;
    private Map<T, R> lookUp;
    private LinkedList<T> nodes;

    public LRUCache2(int capacity) {
        this.capacity = capacity;
        lookUp = new HashMap<>();
        nodes = new LinkedList<>();
    }

    public R get(T key) {
        R val = lookUp.get(key);
        if (val == null) {
            return null;
        } else {
            nodes.removeFirstOccurrence(key);
            nodes.addFirst(key);
            return val;
        }
    }

    public void set(T key, R value) {
        if (lookUp.get(key) == null) {
            if (nodes.size() == capacity) {
                T last = nodes.removeLast();
                lookUp.remove(last);
            }
            nodes.addFirst(key);
        } else {
            get(key);
        }
        lookUp.put(key, value);

    }
}
