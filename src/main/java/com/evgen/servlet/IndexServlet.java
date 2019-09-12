package com.evgen.servlet;

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

@WebServlet("/index")
public class IndexServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(IndexServlet.class);

    @Inject
    private StationRESTService stationRESTService;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // INPUT PARAMETERS
        String strStationId = request.getParameter("stationId");
        logger.info("String stationId = " + strStationId);

        // NEED TO SHOW TIMETABLE
        if (strStationId != null) {
            int stationId = Integer.parseInt(strStationId);
            response.sendRedirect("time-table?stationId=" + stationId);
        }

        // GET LIST OF STATIONS BY REST
        try {
            List<StationSimpleDTO> stations = stationRESTService.getAllStations();
            request.setAttribute("stations", stations);
        } catch (Exception e) {
            logger.warn("ERROR = " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("connectionError", true);
        }

        // REDIRECT

        RequestDispatcher requestDispatcher = request
                .getRequestDispatcher("/jsp/index.jsp");
        requestDispatcher.forward(request, response);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("IndexServlet");

        RequestDispatcher requestDispatcher = request
                .getRequestDispatcher("/jsp/index.jsp");
        requestDispatcher.forward(request, response);
    }
}
