package com.gridnine.testing.service;

import com.gridnine.testing.data.Flight;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FlightFilter {
    List<Flight> byCondition(Predicate<? super Flight> condition);
    <P> List<Flight> byProperty(Function<? super Flight, P> property, Predicate<? super P> condition);
    <P> List<Flight> byCollectionProperty(Function<? super Flight, Collection<P>> property, Predicate<? super P> condition);
    List<Flight> earlyFlight(LocalDateTime time);
    List<Flight> lateFlight(LocalDateTime time);
}
