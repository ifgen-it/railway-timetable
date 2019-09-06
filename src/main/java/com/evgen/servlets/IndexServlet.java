package com.evgen.servlets;

import com.evgen.dto.RoutePathSimpleDTO;
import com.evgen.dto.StationSimpleDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import javax.enterprise.inject.Model;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("----> IndexServlet doPost");

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

        if (restResponse.getStatus() != 200){
            throw new RuntimeException("Failed : HTTP error code : " +
                    restResponse.getStatus());
        }

        String output = restResponse.getEntity(String.class);
        System.out.println("\nString output = " + output);

        ObjectMapper objectMapper = new ObjectMapper();
        List<StationSimpleDTO> result = objectMapper.readValue(output,
                new TypeReference<ArrayList<StationSimpleDTO>>(){});

        System.out.println("\nResult = " + result);

        // ADD PROPERTIES

        request.setAttribute("stations", result);


        // REDIRECT

        RequestDispatcher requestDispatcher = request
                .getRequestDispatcher("/jsp/index.jsp");
        requestDispatcher.forward(request, response);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("----> IndexServlet doGet");

        request.setAttribute("server", "Trains-and-Rails Server");

        RequestDispatcher requestDispatcher = request
                .getRequestDispatcher("/jsp/index.jsp");
        requestDispatcher.forward(request, response);
    }
}
