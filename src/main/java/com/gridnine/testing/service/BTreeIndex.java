package com.gridnine.testing.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BTreeIndex<K extends Comparable<? super K>, T> {
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
        return condition(data -> data.higherEntry(key));
    }

    public List<T> less(K key) {
        return condition(data -> data.lowerEntry(key));
    }

    private List<T> condition(Function<NavigableMap<K, List<T>>, Map.Entry<K, List<T>>> operation) {
        return Optional.ofNullable(operation.apply(data))
                .orElse(new AbstractMap.SimpleEntry<K, List<T>>(null, Collections.EMPTY_LIST))
                .getValue();
    }

}
