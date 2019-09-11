package com.evgen.dto;

import java.io.Serializable;
import java.util.List;

public class TimetableDTO implements Serializable {

    private StationSimpleDTO station;

    private List<RoutePathSimpleDTO> arrivals;

    private List<RoutePathSimpleDTO> departures;

    public TimetableDTO() {
    }


    public List<RoutePathSimpleDTO> getArrivals() {
        return arrivals;
    }

    public void setArrivals(List<RoutePathSimpleDTO> arrivals) {
        this.arrivals = arrivals;
    }

    public List<RoutePathSimpleDTO> getDepartures() {
        return departures;
    }

    public void setDepartures(List<RoutePathSimpleDTO> departures) {
        this.departures = departures;
    }

    public StationSimpleDTO getStation() {
        return station;
    }

    public void setStation(StationSimpleDTO station) {
        this.station = station;
    }

    @Override
    public String toString() {
        return "TimetableDTO{" +
                "station=" + station +
                ", arrivals=" + arrivals +
                ", departures=" + departures +
                '}';
    }
}
