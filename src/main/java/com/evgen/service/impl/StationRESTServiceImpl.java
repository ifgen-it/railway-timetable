package com.evgen.service.impl;

import com.evgen.bean.StationsBean;
import com.evgen.dto.RoutePathSimpleDTO;
import com.evgen.dto.StationSimpleDTO;
import com.evgen.service.StationRESTService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class StationRESTServiceImpl implements StationRESTService {

    private static final Logger logger = Logger.getLogger(StationRESTServiceImpl.class);


    @Override
    public List<StationSimpleDTO> getAllStations() throws IOException {

        // GET LIST OF STATIONS BY REST

        logger.info("Rest-client started..");

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource("http://localhost:8080/api/stations");

        ClientResponse restResponse = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        logger.info("restResponse = " + restResponse);

        if (restResponse.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " +
                    restResponse.getStatus());
        }

        String output = restResponse.getEntity(String.class);
        logger.info("String output = " + output);

        ObjectMapper objectMapper = new ObjectMapper();
        List<StationSimpleDTO> result = objectMapper.readValue(output,
                new TypeReference<ArrayList<StationSimpleDTO>>() {
                });

        logger.info("Result = " + result);

        return result;
    }

    @Override
    public List<RoutePathSimpleDTO> getArrivals(int stationId) throws IOException {

        logger.info("Rest-client started..");

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource("http://localhost:8080/api/arrivals/" + stationId);

        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        logger.info("response : Arrivals = " + response);

        if (response.getStatus() != 200){
            throw new RuntimeException("Failed : HTTP error code : " +
                    response.getStatus());
        }

        String output = response.getEntity(String.class);
        logger.info("String output = " + output);
        if (output.equals("")){
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<RoutePathSimpleDTO> result = objectMapper.readValue(output,
                new TypeReference<ArrayList<RoutePathSimpleDTO>>(){});

        logger.info("Result = " + result);

        return result;
    }

    @Override
    public List<RoutePathSimpleDTO> getDepartures(int stationId) throws IOException {

        logger.info("Rest-client started..");

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource("http://localhost:8080/api/departures/" + stationId);

        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        logger.info("response : Departures = " + response);

        if (response.getStatus() != 200){
            throw new RuntimeException("Failed : HTTP error code : " +
                    response.getStatus());
        }

        String output = response.getEntity(String.class);
        logger.info("String output = " + output);
        if (output.equals("")){
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<RoutePathSimpleDTO> result = objectMapper.readValue(output,
                new TypeReference<ArrayList<RoutePathSimpleDTO>>(){});

        logger.info("Result = " + result);

        return result;
    }

    @Override
    public StationSimpleDTO getStation(int stationId) throws IOException {

        List<StationSimpleDTO> stations = getAllStations();

        for ( StationSimpleDTO station : stations ) {
            if (station.getStationId() == stationId){
                return station;
            }
        }
        return null;
    }

    @Override
    public StationSimpleDTO getStationByName(String stationName) throws IOException {

        List<StationSimpleDTO> stations = getAllStations();

        for ( StationSimpleDTO station : stations ) {
            if (station.getStationName().equals(stationName)){
                return station;
            }
        }
        return null;
    }
}
