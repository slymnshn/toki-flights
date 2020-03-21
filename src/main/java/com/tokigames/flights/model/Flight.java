package com.tokigames.flights.model;

import java.time.Instant;

public class Flight {

    public enum Type {
        CHEAP, BUSINESS
    }

    public Type type;
    public String departure;
    public String arrival;
    public Instant departureTime;
    public Instant arrivalTime;

    public Flight() {
    }

    public Flight(Type type, String departure, String arrival, Instant departureTime, Instant arrivalTime) {
        this.type = type;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
