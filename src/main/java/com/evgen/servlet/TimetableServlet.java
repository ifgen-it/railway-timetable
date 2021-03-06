package com.evgen.servlet;

import com.evgen.config.JMSConfig;
import com.evgen.dto.RoutePathSimpleDTO;
import com.evgen.dto.StationSimpleDTO;
import com.evgen.service.StationRESTService;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/time-table")
public class TimetableServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(TimetableServlet.class);

    @Inject
    private StationRESTService stationRESTService;

    @Inject
    private JMSConfig jmsConfig;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        super.doPost(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // INPUT PARAMETERS
        String strStationId = request.getParameter("stationId");
        logger.info("String stationId = " + strStationId);

        boolean stationIdError = false;

        if (strStationId == null) {
            stationIdError = true;
        } else {
            int stationId = 0;
            try {
                stationId = Integer.parseInt(strStationId);
            } catch (NumberFormatException e) {
                logger.warn("stationId parse Error");
                stationIdError = true;
            }

            // FETCH TIMETABLE FROM REMOTE SERVER
            if (stationIdError == false) {

                try {

                    List<RoutePathSimpleDTO> arrivals = stationRESTService.getArrivals(stationId);
                    request.setAttribute("arrivals", arrivals);

                    List<RoutePathSimpleDTO> departures = stationRESTService.getDepartures(stationId);
                    request.setAttribute("departures", departures);

                    if (arrivals == null || departures == null) {
                        stationIdError = false;
                        request.setAttribute("notFoundStationName", true);
                    }

                    StationSimpleDTO station = stationRESTService.getStation(stationId);
                    if (station != null) {
                        String stationName = station.getStationName();
                        request.setAttribute("stationName", stationName);

                        // RUN TOPIC LISTENER
                        jmsConfig.startListen(stationName);
                        logger.info("jmsConfig - startListen station : " + stationName);

                    } else {
                        request.setAttribute("stationName", "Unknown station");
                    }
                } catch (Exception e){
                    logger.warn("ERROR = " + e.getMessage());
                    e.printStackTrace();

                    request.setAttribute("connectionError", true);
                }
            }
        }

        request.setAttribute("stationIdError", stationIdError);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/jsp/time_table.jsp");
        requestDispatcher.forward(request, response);
    }
}
