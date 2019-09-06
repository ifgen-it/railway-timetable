package com.evgen.service.impl;

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

import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class StationRESTServiceImpl implements StationRESTService {


    @Override
    public List<StationSimpleDTO> getAllStations() throws IOException {

        // GET LIST OF STATIONS BY REST -- NEED TO MOVE THIS CODE INTO BEAN

        System.out.println("Rest-client started..");

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource("http://localhost:8080/api/stations");

        ClientResponse restResponse = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        System.out.println("restResponse = " + restResponse);

        if (restResponse.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " +
                    restResponse.getStatus());
        }

        String output = restResponse.getEntity(String.class);
        System.out.println("\nString output = " + output);

        ObjectMapper objectMapper = new ObjectMapper();
        List<StationSimpleDTO> result = objectMapper.readValue(output,
                new TypeReference<ArrayList<StationSimpleDTO>>() {
                });

        System.out.println("\nResult = " + result);

        return result;
    }

    @Override
    public List<RoutePathSimpleDTO> getArrivals(int stationId) throws IOException {

        System.out.println("Rest-client started..");

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource("http://localhost:8080/api/arrivals/" + stationId);

        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        System.out.println("response : Arrivals = " + response);

        if (response.getStatus() != 200){
            throw new RuntimeException("Failed : HTTP error code : " +
                    response.getStatus());
        }

        String output = response.getEntity(String.class);
        System.out.println("\nString output = " + output);
        if (output.equals("")){
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<RoutePathSimpleDTO> result = objectMapper.readValue(output,
                new TypeReference<ArrayList<RoutePathSimpleDTO>>(){});

        System.out.println("\nResult = " + result);

        return result;
    }

    @Override
    public List<RoutePathSimpleDTO> getDepartures(int stationId) throws IOException {

        System.out.println("Rest-client started..");

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource("http://localhost:8080/api/departures/" + stationId);

        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        System.out.println("response : Departures = " + response);

        if (response.getStatus() != 200){
            throw new RuntimeException("Failed : HTTP error code : " +
                    response.getStatus());
        }

        String output = response.getEntity(String.class);
        System.out.println("\nString output = " + output);
        if (output.equals("")){
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<RoutePathSimpleDTO> result = objectMapper.readValue(output,
                new TypeReference<ArrayList<RoutePathSimpleDTO>>(){});

        System.out.println("\nResult = " + result);

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
}
