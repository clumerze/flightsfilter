package com.gridnine.testing.data;

import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FlightImplTest extends TestCase {
    Flight flight;
    LocalDateTime startTravel;
    LocalDateTime timeArrivingOnFirstStation;
    LocalDateTime departureTimeOnSecondStation;
    LocalDateTime finishTravel;

    public void setUp() {
        startTravel = LocalDateTime.now();
        timeArrivingOnFirstStation = startTravel.plusDays(2);
        departureTimeOnSecondStation = timeArrivingOnFirstStation.plusDays(2);
        finishTravel = departureTimeOnSecondStation.plusDays(2);
        flight = new FlightImpl(
                List.of(
                        new FlightImpl.Station(startTravel, timeArrivingOnFirstStation),
                        new FlightImpl.Station(departureTimeOnSecondStation, finishTravel)
                )
        );
    }

    public void testStartTravel() {
        var actual = flight.startTravel();

        assertThat(actual)
                .isEqualTo(startTravel);
    }

    public void testFinishTravel() {
        var actual = flight.finishTravel();

        assertThat(actual)
                .isEqualTo(finishTravel);
    }

    public void testTravelDuration() {
        var chronoUnit = ChronoUnit.DAYS;
        var actual = flight.travelDuration(chronoUnit);

        assertThat(actual)
                .isEqualTo(
                        chronoUnit.between(startTravel, finishTravel)
                );
    }

    public void testAirStayDuration() {
        var chronoUnit = ChronoUnit.DAYS;
        var actual = flight.airStayDuration(chronoUnit);

        assertThat(actual)
                .isEqualTo(
                        chronoUnit.between(startTravel, timeArrivingOnFirstStation) +
                                chronoUnit.between(departureTimeOnSecondStation, finishTravel)
                );
    }

    public void testEarthStayDuration() {
        var chronoUnit = ChronoUnit.DAYS;
        var actual = flight.earthStayDuration(chronoUnit);

        assertThat(actual)
                .isEqualTo(
                        chronoUnit.between(
                                timeArrivingOnFirstStation,
                                departureTimeOnSecondStation
                        )
                );
    }
}