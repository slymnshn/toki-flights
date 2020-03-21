package com.tokigames.flights.boundary;

import com.tokigames.flights.control.FlightsService;
import com.tokigames.flights.model.Flight;
import com.tokigames.flights.model.SearchParams;
import io.smallrye.mutiny.Multi;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/flights")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FlightsResource {

    @Inject
    FlightsService flightsService;

    @POST
    @Path("/search")
    public Multi<Flight> search(@NotNull SearchParams searchParams) {
        return flightsService.searchFlights(searchParams);
    }

}
