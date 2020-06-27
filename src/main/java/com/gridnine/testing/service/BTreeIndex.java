package com.gridnine.testing.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BTreeIndex<K extends Comparable<K>, T> {
    private final NavigableMap<K, List<T>> data;

    public BTreeIndex(Function<T, K> property, List<T> data) {
        this.data = Collections.unmodifiableNavigableMap(
                new TreeMap<>(
                        data.parallelStream()
                                .collect(Collectors.groupingBy(property))
                )
        );
    }

    public List<T> more(K key) {
        return Optional.ofNullable(
                data.higherEntry(key)
        )
                .orElse(new AbstractMap.SimpleEntry<K, List<T>>(null, Collections.EMPTY_LIST))
                .getValue();
    }

    public List<T> less(K key) {
        return Optional.ofNullable(data.lowerEntry(key))
                .orElse(new AbstractMap.SimpleEntry<K, List<T>>(null, Collections.EMPTY_LIST))
                .getValue();
    }

}
