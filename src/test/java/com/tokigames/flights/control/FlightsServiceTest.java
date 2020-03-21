package com.tokigames.flights.control;

import com.tokigames.flights.model.Flight;
import com.tokigames.flights.model.SearchParams;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Duration;
import java.util.List;

import static com.tokigames.flights.model.Flight.Type.CHEAP;

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

    @Test
    public void searchFlightsTestFilter() {
        SearchParams params = new SearchParams();
        params.arrival = "Antalya";
        params.type = CHEAP;

        List<Flight> flights = flightsService.searchFlights(params)
                .collectItems()
                .asList()
                .await()
                .atMost(Duration.ofSeconds(10));

        Assertions.assertTrue(
                flights.stream().allMatch(f -> f.arrival.equalsIgnoreCase(params.arrival) && f.type.equals(params.type))
        );
        LOG.info(Json.encodePrettily(flights));
    }

    @Test
    public void searchFlightsTestPagination() {
        SearchParams params = new SearchParams();
        params.setPageSize(2);

        List<Flight> flights = flightsService.searchFlights(params)
                .collectItems()
                .asList()
                .await()
                .atMost(Duration.ofSeconds(10));

        Assertions.assertEquals(params.getPageSize(), flights.size());
        LOG.info(Json.encodePrettily(flights));
    }

}
