package com.evgen.bean;

import com.evgen.dto.RoutePathSimpleDTO;
import com.evgen.dto.StationSimpleDTO;
import com.evgen.service.StationRESTService;

import javax.annotation.ManagedBean;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class TimetableBean implements Serializable {

    private String stationName;

    private List<RoutePathSimpleDTO> arrivals;

    private List<RoutePathSimpleDTO> departures;

    @Inject
    private StationRESTService stationRESTService;


    public TimetableBean() {
    }

    public void init(int stationId){
        try {
            StationSimpleDTO station = stationRESTService.getStation(stationId);
            this.stationName = station.getStationName();
            this.arrivals = stationRESTService.getArrivals(stationId);
            this.departures = stationRESTService.getDepartures(stationId);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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
}
