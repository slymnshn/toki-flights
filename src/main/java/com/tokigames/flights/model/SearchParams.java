package com.tokigames.flights.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class SearchParams {

    public enum SORT {
        ASC, DESC
    }

    public Flight.Type type;
    public String departure;
    public String arrival;
    public Instant minDepartureTime;
    public Instant maxDepartureTime;
    public Instant minArrivalTime;
    public Instant maxArrivalTime;

    public Map<String, SORT> sorting = new HashMap<>();

    private int page;

    private int pageSize = 50;

    public int start() {
        return (page * pageSize) - pageSize;
    }

    public int getOffset() {
        return (getPage() - 1) * getPageSize();
    }

    public int getPage() {
        return Math.max(page, 1);
    }

    public int getPageSize() {
        return Math.max(pageSize, 1);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}