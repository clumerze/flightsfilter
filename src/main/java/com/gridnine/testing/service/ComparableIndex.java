package com.gridnine.testing.service;

import java.util.List;

public interface ComparableIndex<P extends Comparable<? extends P>, T> {
    List<T> more(P key);

    List<T> less(P key);
}
