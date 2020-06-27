package com.gridnine.testing.service;

import com.gridnine.testing.data.Flight;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlightFilterImpl implements FlightFilter {
    private final NavigableMap<LocalDateTime, List<Flight>> data;

    public FlightFilterImpl(List<Flight> data) {
        this.data = Collections.unmodifiableNavigableMap(
                new TreeMap<>(
                        data.parallelStream()
                                .collect(
                                        Collectors.groupingBy(
                                                Flight::startTravel
                                        )
                                )
                )
        );
    }

    public static FlightFilterImpl of(List<Flight> data) {
        return new FlightFilterImpl(data);
    }

    @Override
    public List<Flight> byCondition(Predicate<? super Flight> condition) {
        return select(condition);
    }

    @Override
    public <P> List<Flight> byProperty(Function<? super Flight, P> property, Predicate<? super P> condition) {
        return select(
                element -> condition.test(
                        property.apply(element)
                )
        );
    }

    @Override
    public <P> List<Flight> byCollectionProperty(Function<? super Flight, Collection<P>> property, Predicate<? super P> condition) {
        return select(
                element -> property.apply(element)
                        .parallelStream()
                        .anyMatch(condition)
        );
    }

    @Override
    public List<Flight> earlyFlight(LocalDateTime time) {
        return Optional.ofNullable(data.lowerEntry(time))
                .orElse(new AbstractMap.SimpleEntry<LocalDateTime, List<Flight>>(null, Collections.EMPTY_LIST))
                .getValue();

    }

    @Override
    public List<Flight> lateFlight(LocalDateTime time) {
        return Optional.ofNullable(data.higherEntry(time))
                .orElse(new AbstractMap.SimpleEntry<LocalDateTime, List<Flight>>(null, Collections.EMPTY_LIST))
                .getValue();
    }

    private List<Flight> select(Predicate<? super Flight> condition) {
        return data.values()
                .parallelStream()
                .flatMap(List::parallelStream)
                .filter(condition)
                .collect(Collectors.toList());
    }
}
