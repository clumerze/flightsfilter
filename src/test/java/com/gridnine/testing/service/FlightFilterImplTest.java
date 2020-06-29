package com.gridnine.testing.service;

import com.gridnine.testing.data.Flight;
import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.gridnine.testing.data.FlightFactory.createFlight;
import static org.assertj.core.api.Assertions.assertThat;

public class FlightFilterImplTest extends TestCase {
    FlightFilter filter;
    LocalDateTime now;
    Flight flightWithStartTravelBeforeNow;
    Flight flightWithStartTravelTwoHoursFromNow;
    Flight flightWithEarthDurationMoreTwoHours;

    @Override
    protected void setUp() {
        now = LocalDateTime.now();
        var twoHoursBeforeNow = now.minusHours(1);
        var twoHoursFromNow = now.plusHours(1);
        var twoDaysFromNow = now.plusDays(1);
        var fourDaysFromNow = now.plusDays(4);

        flightWithStartTravelTwoHoursFromNow = createFlight(
                twoHoursFromNow,
                twoDaysFromNow
        );
        flightWithStartTravelBeforeNow = createFlight(
                twoHoursBeforeNow,
                now
        );
        flightWithEarthDurationMoreTwoHours = createFlight(
                now,
                twoHoursFromNow,
                twoDaysFromNow,
                fourDaysFromNow
        );
        filter = new FlightFilterImpl(
                List.of(
                        flightWithEarthDurationMoreTwoHours,
                        flightWithStartTravelBeforeNow,
                        flightWithStartTravelTwoHoursFromNow
                )
        );

    }

    public void testByCondition() {
        var actual = filter.byCondition(
                flight -> flight.earthStayDuration(ChronoUnit.HOURS) > 2
        );

        assertThat(actual)
                .containsOnly(flightWithEarthDurationMoreTwoHours);
    }

    public void testByProperty() {
        var actual = filter.byProperty(
                Flight::finishTravel,
                time -> time.isAfter(now)
        );

        assertThat(actual)
                .containsOnly(flightWithStartTravelTwoHoursFromNow, flightWithEarthDurationMoreTwoHours);
    }

    public void testByCollectionProperty() {
        var actual = filter.byCollectionProperty(
                Flight::getStations,
                station -> station.getDepartureDate()
                        .equals(now)
        );

        assertThat(actual)
                .containsOnly(flightWithEarthDurationMoreTwoHours);
    }

    public void testTravelStartTimeIsBefore() {
        var actual = filter.travelStartTimeIsBefore(now);

        assertThat(actual)
                .containsOnly(flightWithStartTravelBeforeNow);
    }

    public void testTravelStartTimeIsAfter() {
        var actual = filter.travelStartTimeIsAfter(now);

        assertThat(actual)
                .containsOnly(flightWithStartTravelTwoHoursFromNow);
    }

    public void testEarthStayDurationIsMore() {
        var actual = filter.earthStayDurationIsMore(2);

        assertThat(actual)
                .containsOnly(flightWithEarthDurationMoreTwoHours);
    }

    public void testEarthStayDurationIsLess() {
        var actual = filter.earthStayDurationIsLess(2);

        assertThat(actual)
                .containsExactly(flightWithStartTravelBeforeNow, flightWithStartTravelTwoHoursFromNow);
    }
}