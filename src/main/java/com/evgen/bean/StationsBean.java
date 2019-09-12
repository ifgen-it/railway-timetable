package com.evgen.bean;

import com.evgen.dto.StationSimpleDTO;
import com.evgen.service.StationRESTService;
import org.apache.log4j.Logger;

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

    private static final Logger logger = Logger.getLogger(StationsBean.class);

    @Inject
    private StationRESTService stationRESTService;

    private List<StationSimpleDTO> stations;

    private boolean connectionError;

    public StationsBean() {

        logger.info("StationsBean constructor");
    }

    @PostConstruct
    public void init(){
        logger.info("StationsBean init");
        try {
            this.stations = stationRESTService.getAllStations();
        } catch (Exception e) {
            logger.warn("Connection error, message = " + e.getMessage());
            connectionError = true;
        }
        if (connectionError == false){
            logger.info("Stations was initialized");
        } else {
            logger.warn("Stations was not initialized");
        }

    }

    @PreDestroy
    public void destroy(){
        logger.info("StationsBean destroy");
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
