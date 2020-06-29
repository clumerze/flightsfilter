package com.gridnine.testing.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BTreeIndex<P extends Comparable<P>, T> implements ComparableIndex<P, T> {
    private final NavigableMap<P, List<T>> DATA;

    public BTreeIndex(Function<T, P> property, List<T> DATA) {
        this.DATA = Collections.unmodifiableNavigableMap(
                new TreeMap<>(
                        DATA.parallelStream()
                                .collect(Collectors.groupingBy(property))
                )
        );
    }

    public List<T> more(P key) {
        return iterateTree(DATA::higherEntry, key);
    }

    public List<T> less(P key) {
        return iterateTree(DATA::lowerEntry, key);
    }

    private List<T> iterateTree(Function<P, Map.Entry<P, List<T>>> operation, P value) {
        Map.Entry<P, List<T>> entry = operation.apply(value);
        List<T> result = new ArrayList<>(10);
        while (entry != null) {
            result.addAll(entry.getValue());
            entry = operation.apply(entry.getKey());
        }
        return result;
    }
}
