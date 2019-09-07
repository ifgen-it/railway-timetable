package com.evgen.service;

import com.evgen.dto.RoutePathSimpleDTO;
import com.evgen.dto.StationSimpleDTO;

import java.io.IOException;
import java.util.List;

public interface StationRESTService {

    List<StationSimpleDTO> getAllStations() throws IOException;

    List<RoutePathSimpleDTO> getArrivals(int stationId) throws IOException;

    List<RoutePathSimpleDTO> getDepartures(int stationId) throws IOException;

    StationSimpleDTO getStation(int stationId) throws IOException;

    StationSimpleDTO getStationByName(String stationName) throws IOException;
}
