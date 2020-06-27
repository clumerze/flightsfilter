package com.gridnine.testing.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@EqualsAndHashCode
public class FlightImpl implements Flight,
        Comparable<Flight> {
    private final NavigableSet<Station> stations;

    public FlightImpl(List<Station> stations) {
        this.stations = Collections.unmodifiableNavigableSet(
                new TreeSet<>(stations)
        );
    }

    @Override
    public LocalDateTime startTravel() {
        return getStations()
                .first()
                .getDepartureDate();
    }

    @Override
    public LocalDateTime finishTravel() {
        return getStations()
                .last()
                .getArrivalDate();
    }

    @Override
    public long travelDuration(ChronoUnit chronoUnit) {
        return chronoUnit.between(
                startTravel(),
                finishTravel()
        );
    }

    @Override
    public long airStayDuration(ChronoUnit chronoUnit) {
        return getStations()
                .parallelStream()
                .mapToLong(
                        station -> chronoUnit.between(
                                station.getDepartureDate(),
                                station.getArrivalDate()
                        )
                )
                .sum();
    }

    @Override
    public long earthStayDuration(ChronoUnit chronoUnit) {
        return travelDuration(chronoUnit) -
                airStayDuration(chronoUnit);
    }

    @Override
    public String toString() {
        return stations.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
    }

    @Override
    public int compareTo(Flight o) {
        return startTravel()
                .compareTo(
                        o.startTravel()
                );
    }

    @Getter
    @SuperBuilder
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class Station
            implements Comparable<Station> {
        private final LocalDateTime departureDate;
        private final LocalDateTime arrivalDate;

        @Override
        public int compareTo(Station o) {
            return getDepartureDate()
                    .compareTo(
                            o.getDepartureDate()
                    );
        }

        @Override
        public String toString() {
            DateTimeFormatter fmt =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            return String.format(
                    "[%s|%s]",
                    departureDate.format(fmt),
                    arrivalDate.format(fmt)
            );
        }
    }
}
