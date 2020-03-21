package com.tokigames.flights.control;

import com.tokigames.flights.model.BusinessFlightsPayload;
import com.tokigames.flights.model.CheapFlightPayload;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@QuarkusTest
public class FlightsRestClientTest {

    @Inject
    @RestClient
    FlightsRestClient client;

    @Inject
    Logger LOG;

    @Test
    @Order(1)
    public void testCheapFlights() {
        CompletableFuture<CheapFlightPayload> future = client.getCheapFlights().toCompletableFuture();
        CheapFlightPayload payload = future.join();
        Assertions.assertTrue(future.isDone());
        Assertions.assertNotNull(payload);
        LOG.info(Json.encodePrettily(payload));
    }

    @Test
    @Order(2)
    public void testBusinessFlights() {
        CompletableFuture<BusinessFlightsPayload> future = client.getBusinessFlights().toCompletableFuture();
        BusinessFlightsPayload payload = future.join();
        Assertions.assertTrue(future.isDone());
        Assertions.assertNotNull(payload);
        LOG.info(Json.encodePrettily(payload));
    }

}
