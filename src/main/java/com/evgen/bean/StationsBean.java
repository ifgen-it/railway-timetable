package com.evgen.bean;

import com.evgen.dto.StationSimpleDTO;
import com.evgen.service.StationRESTService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;


@Named
@ViewScoped
public class StationsBean implements Serializable {

    @Inject
    private StationRESTService stationRESTService;

    private List<StationSimpleDTO> stations;

    private boolean connectionError;

    public StationsBean() {
        System.out.println("---> StationsBean constructor");
    }

    @PostConstruct
    public void init(){
        System.out.println("---> StationsBean init");
        try {
            this.stations = stationRESTService.getAllStations();
        } catch (Exception e) {
            System.out.println("---> Connection error, message = " + e.getMessage());
            connectionError = true;
        }
        if (connectionError == false){
            System.out.println("Stations was initialized");
        } else {
            System.out.println("Stations was not initialized");
        }

    }

    @PreDestroy
    public void destroy(){
        System.out.println("---> StationsBean destroy");
    }


    public List<StationSimpleDTO> getStations() {
        return stations;
    }

    public void setStations(List<StationSimpleDTO> stations) {
        this.stations = stations;
    }

    public boolean isConnectionError() {
        return connectionError;
    }

    public void setConnectionError(boolean connectionError) {
        this.connectionError = connectionError;
    }
}
