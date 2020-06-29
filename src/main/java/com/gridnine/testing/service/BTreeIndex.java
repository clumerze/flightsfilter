package com.gridnine.testing.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BTreeIndex<P extends Comparable<? extends P>, T> implements ComparableIndex<P, T> {
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
        return condition(data -> data.higherEntry(key));
    }

    public List<T> less(P key) {
        return condition(data -> data.lowerEntry(key));
    }

    private List<T> condition(Function<NavigableMap<P, List<T>>, Map.Entry<P, List<T>>> operation) {
        Map.Entry<P, List<T>> entry = operation.apply(DATA);
        return (entry == null)
                ? Collections.EMPTY_LIST
                : entry.getValue();
    }
}
