package com.evgen.bean;

import com.evgen.dto.StationSimpleDTO;
import com.evgen.service.StationRESTService;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
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
public class StationsBean implements Serializable {

    @Inject
    private StationRESTService stationRESTService;

    private List<StationSimpleDTO> stations;

    public StationsBean() {
        System.out.println("---> StationsBean constructor");
    }

    @PostConstruct
    public void init(){
        System.out.println("---> StationsBean init");
        try {
            this.stations = stationRESTService.getAllStations();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Stations was initialized");
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
}
