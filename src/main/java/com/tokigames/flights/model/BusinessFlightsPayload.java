package com.tokigames.flights.model;

import java.time.Instant;
import java.util.Set;

public class BusinessFlightsPayload {

    public Set<BusinessFlight> data;

    public static class BusinessFlight {
        public String departure;
        public String arrival;
        public Instant departureTime;
        public Instant arrivalTime;
    }

}
