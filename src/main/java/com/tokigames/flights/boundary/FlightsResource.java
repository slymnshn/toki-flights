package com.tokigames.flights.boundary;

import com.tokigames.flights.control.FlightsService;
import com.tokigames.flights.model.Flight;
import io.smallrye.mutiny.Multi;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
public class FlightsResource {

    @Inject
    FlightsService flightsService;

    @GET
    @Path("/search")
    public Multi<Flight> search() {
        return flightsService.searchFlights();
    }

    @GET
    @Path("/cheap")
    public Multi<Flight> cheapFlights() {
        return flightsService.getCheapFlights();
    }

    @GET
    @Path("/business")
    public Multi<Flight> businessFlights() {
        return flightsService.getBusinessFlights();
    }

}
