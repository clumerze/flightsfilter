package com.gridnine.testing.main;

import com.gridnine.testing.data.Flight;
import com.gridnine.testing.data.FlightFactory;
import com.gridnine.testing.service.FlightFilter;
import com.gridnine.testing.service.FlightFilterImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightFactory.createFlights();

        System.out.printf(
                "in:\n%s\n\n",
                listToStr(flights)
        );

        FlightFilter filter = FlightFilterImpl.of(flights);

        System.out.printf(
                "Departure then the current moment of time:\n%s\n\n",
                listToStr(
                        filter.travelStartTimeIsBefore(LocalDateTime.now())
                )
        );

        System.out.printf(
                "Are segments with the date of arrival before departure:\n%s\n\n",
                listToStr(
                        filter.byCollectionProperty(
                                Flight::getStations,
                                station -> station.getArrivalDate()
                                        .isBefore(
                                                station.getDepartureDate()
                                        )
                        )
                )
        );

        System.out.printf(
                "The total duration of nailing on the ground exceeds 2 hours:\n%s\n\n",
                listToStr(
                        filter.earthStayDurationIsMore(2L)
                )
        );
    }

    private static String listToStr(List<?> list) {
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }
}
