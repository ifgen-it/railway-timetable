package com.evgen.servlet;

import com.evgen.dto.StationSimpleDTO;
import com.evgen.service.StationRESTService;

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

    @Inject
    private StationRESTService stationRESTService;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("----> IndexServlet doPost");

        // INPUT PARAMETERS
        String strStationId = request.getParameter("stationId");
        System.out.println("String stationId = " + strStationId);

        // NEED TO SHOW TIMETABLE
        if (strStationId != null) {
            int stationId = Integer.parseInt(strStationId);
            response.sendRedirect("timetable?stationId=" + stationId);
        }

        // GET LIST OF STATIONS BY REST
        try {
            List<StationSimpleDTO> stations = stationRESTService.getAllStations();
            request.setAttribute("stations", stations);
        } catch (Exception e) {
            System.out.println("----> ERROR = " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("connectionError", true);
        }

        // REDIRECT

        RequestDispatcher requestDispatcher = request
                .getRequestDispatcher("/jsp/index.jsp");
        requestDispatcher.forward(request, response);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("----> IndexServlet doGet");

        RequestDispatcher requestDispatcher = request
                .getRequestDispatcher("/jsp/index.jsp");
        requestDispatcher.forward(request, response);
    }
}
