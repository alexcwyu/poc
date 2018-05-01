package net.alexyu.common;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LRUCache extends LinkedHashMap<Integer, Integer> {

    private int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);  // for access order
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Entry entry) {
        return (size() > this.capacity);
    }
}