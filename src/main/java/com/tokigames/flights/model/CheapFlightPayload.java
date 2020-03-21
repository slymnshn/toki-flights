package com.tokigames.flights.model;

import java.time.Instant;
import java.util.Set;

public class CheapFlightPayload {

    public Set<CheapFlight> data;

    static class CheapFlight {
        public String route;
        public Instant departure;
        public Instant arrival;
    }

}
