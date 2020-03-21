package com.tokigames.flights.control;

import com.tokigames.flights.model.BusinessFlightsPayload;
import com.tokigames.flights.model.CheapFlightPayload;
import com.tokigames.flights.model.Flight;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static com.tokigames.flights.model.Flight.Type.BUSINESS;
import static com.tokigames.flights.model.Flight.Type.CHEAP;

@ApplicationScoped
public class FlightsService {

    @Inject
    @RestClient
    FlightsRestClient client;

    public Multi<Flight> searchFlights() {
        return Multi.createBy().merging().streams(getCheapFlights(), getBusinessFlights());
    }

    public Multi<Flight> getBusinessFlights() {
        CompletionStage<BusinessFlightsPayload> businessFlights = client.getBusinessFlights();
        return Multi.createFrom()
                .completionStage(businessFlights)
                .flatMap(payload -> Multi.createFrom().items(payload.data.stream()))
                .map(cf -> new Flight(BUSINESS, cf.departure, cf.arrival, cf.departureTime, cf.arrivalTime));
    }

    public Multi<Flight> getCheapFlights() {
        CompletionStage<CheapFlightPayload> cheapFlights = client.getCheapFlights();
        return Multi.createFrom()
                .completionStage(cheapFlights)
                .flatMap(payload -> Multi.createFrom().items(payload.data.stream()))
                .map(cf -> {
                    String[] route = cf.route.split("-");
                    return new Flight(CHEAP, route[0], route[1], cf.departure, cf.arrival);
                });
    }

}
