package com.tokigames.flights.control;

import com.tokigames.flights.model.BusinessFlightsPayload;
import com.tokigames.flights.model.CheapFlightPayload;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.concurrent.CompletionStage;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/flights")
@Produces(APPLICATION_JSON)
@RegisterRestClient(configKey = "flights-api")
public interface FlightsRestClient {

    @GET
    @Path("/cheap")
    CompletionStage<CheapFlightPayload> getCheapFlights();

    @GET
    @Path("/business")
    CompletionStage<BusinessFlightsPayload> getBusinessFlights();

}
