package com.gridnine.testing.data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public interface Flight {
    Set<FlightImpl.Station> getStations();

    LocalDateTime startTravel();

    LocalDateTime finishTravel();

    long travelDuration(ChronoUnit chronoUnit);

    long airStayDuration(ChronoUnit chronoUnit);

    long earthStayDuration(ChronoUnit chronoUnit);
}
