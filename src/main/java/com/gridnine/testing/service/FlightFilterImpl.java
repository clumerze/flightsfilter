package com.gridnine.testing.service;

import com.gridnine.testing.data.Flight;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlightFilterImpl implements FlightFilter {
    private final List<Flight> FLIGHTS;
    private final ComparableIndex<ChronoLocalDateTime<?>, Flight> START_TRAVEL_IDX;
    private final ComparableIndex<Long, Flight> EARTH_STAY_HOURS_DURATION_IDX;

    public FlightFilterImpl(List<Flight> data) {
        FLIGHTS = Collections.unmodifiableList(data);
        START_TRAVEL_IDX = new BTreeIndex<>(Flight::startTravel, data);
        EARTH_STAY_HOURS_DURATION_IDX = new BTreeIndex<>(flight -> flight.earthStayDuration(ChronoUnit.HOURS), data);
    }

    @Override
    public List<Flight> byCondition(Predicate<? super Flight> condition) {
        return selectWhere(condition);
    }

    @Override
    public <P> List<Flight> byProperty(Function<? super Flight, P> property, Predicate<? super P> condition) {
        return selectWhere(
                element -> condition.test(
                        property.apply(element)
                )
        );
    }

    @Override
    public <P> List<Flight> byCollectionProperty(Function<? super Flight, Collection<P>> property, Predicate<? super P> condition) {
        return selectWhere(
                element -> property.apply(element)
                        .parallelStream()
                        .anyMatch(condition)
        );
    }

    @Override
    public List<Flight> travelStartTimeIsBefore(LocalDateTime time) {
        return START_TRAVEL_IDX.less(time);
    }

    @Override
    public List<Flight> travelStartTimeIsAfter(LocalDateTime time) {
        return START_TRAVEL_IDX.more(time);
    }

    @Override
    public List<Flight> earthStayDurationIsMore(long hours) {
        return EARTH_STAY_HOURS_DURATION_IDX.more(hours);
    }

    @Override
    public List<Flight> earthStayDurationIsLess(long hours) {
        return EARTH_STAY_HOURS_DURATION_IDX.less(hours);
    }

    private List<Flight> selectWhere(Predicate<? super Flight> condition) {
        return FLIGHTS.parallelStream()
                .filter(condition)
                .collect(Collectors.toList());
    }
}
