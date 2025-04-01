package com.github.maksimprivalov.datastructures;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LRUCache extends LinkedHashMap<String, Boolean> {
    private final int maxSize;
    private final Consumer<String> onEvict;

    public LRUCache(int maxSize, Consumer<String> onEvict) {
        super(maxSize, 0.75f, true); // accessOrder for LRU
        this.maxSize = maxSize;
        this.onEvict = onEvict;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Boolean> eldest) {
        boolean shouldRemove = size() > maxSize;
        if (shouldRemove && onEvict != null) {
            onEvict.accept(eldest.getKey()); // retrieve the Trie's delete method
        }
        return shouldRemove;
    }
}
