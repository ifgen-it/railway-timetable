package com.evgen.bean;

import com.evgen.config.JMSConfig;
import com.evgen.dto.RoutePathSimpleDTO;
import com.evgen.dto.StationSimpleDTO;
import com.evgen.dto.TimetableDTO;
import com.evgen.service.StationRESTService;
import com.evgen.websocket.TimetableWebsocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class TimetableBean implements Serializable {

    private static final Logger logger = Logger.getLogger(TimetableBean.class);

    private int stationId;

    private String stationName;

    private List<RoutePathSimpleDTO> arrivals;

    private List<RoutePathSimpleDTO> departures;

    private boolean connectionError;

    @Inject
    private StationRESTService stationRESTService;

    @Inject
    private JMSConfig jmsConfig;


    public TimetableBean() {
        logger.info("TimetableBean constructor");
    }


    public void init(){
        logger.info("Timetable INIT started");
        try {
            if (stationId == 0){
                logger.warn("StationId = 0, need to select station");
                return;
            }
            StationSimpleDTO station = stationRESTService.getStation(stationId);

            this.stationName = station.getStationName();
            this.arrivals = stationRESTService.getArrivals(stationId);
            this.departures = stationRESTService.getDepartures(stationId);
            this.connectionError = false;

            // RUN TOPIC LISTENER
            jmsConfig.startListen(stationName);
            logger.info("jmsConfig - startListen station : " + stationName);

            // UPDATE GUI FOR RUNNING CLIENTS WITH OPENED TIMETABLE-PAGE
            logger.info("UPDATE GUI FOR RUNNING CLIENTS WITH OPENED TIMETABLE");
            TimetableWebsocket tws = jmsConfig.getTimetableWebsocket();

            TimetableDTO timetableDTO = new TimetableDTO();
            timetableDTO.setStation(stationRESTService.getStation(stationId));
            timetableDTO.setArrivals(arrivals);
            timetableDTO.setDepartures(departures);

            ObjectMapper mapper = new ObjectMapper();
            String strTimetable = mapper.writeValueAsString(timetableDTO);
            tws.sendMessageToBrowser(strTimetable);

            // AFTER END THIS METHOD WILL BE RENDER THIS CLIENT TIMETABLE-PAGE : TIMETABLE.XHTML

        } catch (Exception e) {
            logger.warn("Connection error, message = " + e.getMessage());
            connectionError = true;
        }
        if (connectionError == false){
            logger.info("Timetable was loaded");
        } else {
            logger.warn("Timetable was not loaded");
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

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public boolean isConnectionError() {
        return connectionError;
    }

    public void setConnectionError(boolean connectionError) {
        this.connectionError = connectionError;
    }
}
