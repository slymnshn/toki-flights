package com.tokigames.flights.control;

import com.tokigames.flights.model.Flight;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Duration;
import java.util.List;

@QuarkusTest
public class FlightsServiceTest {

    @Inject
    FlightsService flightsService;

    @Inject
    Logger LOG;

    @Test
    public void cheapFlightsTest() {
        List<Flight> cheapFlights = flightsService.getCheapFlights()
                .collectItems()
                .asList()
                .await()
                .atMost(Duration.ofSeconds(10));

        Assertions.assertNotNull(cheapFlights);
        Assertions.assertFalse(cheapFlights.isEmpty());
        LOG.info(Json.encodePrettily(cheapFlights));
    }

    @Test
    public void businessFlightsTest() {
        List<Flight> businessFlights = flightsService.getBusinessFlights()
                .collectItems()
                .asList()
                .await()
                .atMost(Duration.ofSeconds(10));

        Assertions.assertNotNull(businessFlights);
        Assertions.assertFalse(businessFlights.isEmpty());
        LOG.info(Json.encodePrettily(businessFlights));
    }

}
