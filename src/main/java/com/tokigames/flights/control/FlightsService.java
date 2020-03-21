package com.tokigames.flights.control;

import com.tokigames.flights.model.BusinessFlightsPayload;
import com.tokigames.flights.model.CheapFlightPayload;
import com.tokigames.flights.model.Flight;
import com.tokigames.flights.model.SearchParams;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.function.Predicate;

import static com.tokigames.flights.model.Flight.Type.BUSINESS;
import static com.tokigames.flights.model.Flight.Type.CHEAP;
import static com.tokigames.flights.model.SearchParams.SORT.DESC;
import static io.smallrye.mutiny.Multi.createBy;
import static io.smallrye.mutiny.Multi.createFrom;

@ApplicationScoped
public class FlightsService {

    @Inject
    @RestClient
    FlightsRestClient client;

    public Multi<Flight> searchFlights(SearchParams searchParams) {
        return createFrom().items(
                createBy()
                        .merging()
                        .streams(getCheapFlights(), getBusinessFlights())
                        .transform()
                        .byFilteringItemsWith(filterFlights(searchParams))
                        .subscribe().asStream()
                        .sorted(compareFlights(searchParams))
                        .skip(searchParams.getOffset())
                        .limit(searchParams.getPageSize()));
    }

    public Multi<Flight> getBusinessFlights() {
        CompletionStage<BusinessFlightsPayload> businessFlights = client.getBusinessFlights();
        return createFrom()
                .completionStage(businessFlights)
                .flatMap(payload -> createFrom().items(payload.data.stream()))
                .map(cf -> new Flight(BUSINESS, cf.departure, cf.arrival, cf.departureTime, cf.arrivalTime));
    }

    public Multi<Flight> getCheapFlights() {
        CompletionStage<CheapFlightPayload> cheapFlights = client.getCheapFlights();
        return createFrom()
                .completionStage(cheapFlights)
                .flatMap(payload -> createFrom().items(payload.data.stream()))
                .map(cf -> {
                    String[] route = cf.route.split("-");
                    return new Flight(CHEAP, route[0], route[1], cf.departure, cf.arrival);
                });
    }

    private Predicate<Flight> filterFlights(SearchParams searchParams) {
        return flight -> (searchParams.type == null || flight.type.equals(searchParams.type))
                && (searchParams.departure == null || flight.departure.equalsIgnoreCase(searchParams.departure))
                && (searchParams.arrival == null || flight.arrival.equalsIgnoreCase(searchParams.arrival))
                && (searchParams.minArrivalTime == null || flight.arrivalTime.isAfter(searchParams.minArrivalTime))
                && (searchParams.maxArrivalTime == null || flight.arrivalTime.isBefore(searchParams.maxArrivalTime))
                && (searchParams.minDepartureTime == null || flight.departureTime.isAfter(searchParams.minDepartureTime))
                && (searchParams.maxDepartureTime == null || flight.departureTime.isBefore(searchParams.maxDepartureTime));
    }

    private Comparator<Flight> compareFlights(SearchParams searchParams) {
        return searchParams.sorting.entrySet().stream().map(entry -> {
            Comparator<Flight> comparator;
            switch (entry.getKey()) {
                case "arrival":
                    comparator = Comparator.comparing(f -> f.arrival);
                    break;
                case "departure":
                    comparator = Comparator.comparing(f -> f.departure);
                    break;
                case "arrivalTime":
                    comparator = Comparator.comparing(f -> f.arrivalTime);
                    break;
                case "departureTime":
                    comparator = Comparator.comparing(f -> f.departureTime);
                    break;
                default:
                    return null;
            }
            return DESC.equals(entry.getValue()) ? comparator.reversed() : comparator;
        }).filter(Objects::nonNull).reduce(Comparator::thenComparing).orElse((o1, o2) -> 0);
    }

}
