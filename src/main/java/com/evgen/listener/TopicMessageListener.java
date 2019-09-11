package com.evgen.listener;

import com.evgen.bean.TimetableBean;
import com.evgen.config.JMSConfig;
import com.evgen.dto.RoutePathSimpleDTO;
import com.evgen.dto.StationSimpleDTO;
import com.evgen.dto.TimetableDTO;
import com.evgen.service.StationRESTService;
import com.evgen.websocket.TimetableWebsocket;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.List;

public class TopicMessageListener implements MessageListener {

    private JMSConfig jmsConfig;


    public TopicMessageListener(JMSConfig jmsConfig) {
        this.jmsConfig = jmsConfig;
    }

    @Override
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("---> onMessage method:");
            System.out.println("Text from message: " + textMessage.getText());
            String city = textMessage.getStringProperty("city");
            System.out.println("Properties: city = " + city);

            System.out.print("Message: ");
            System.out.println(message);

            // HANDLERS
            System.out.println("---> REST handlers:");
            try {
                StationRESTService stationRESTService = jmsConfig.getStationRESTService();
                StationSimpleDTO station = stationRESTService.getStationByName(city);
                List<RoutePathSimpleDTO> arrivals = stationRESTService.getArrivals(station.getStationId());
                List<RoutePathSimpleDTO> departures = stationRESTService.getDepartures(station.getStationId());

                TimetableDTO timetableDTO = new TimetableDTO();
                timetableDTO.setStation(station);
                timetableDTO.setArrivals(arrivals);
                timetableDTO.setDepartures(departures);

                // UPDATE GUI
                System.out.println("--> UPDATE GUI");
                TimetableWebsocket tws = jmsConfig.getTimetableWebsocket();

                ObjectMapper mapper = new ObjectMapper();
                String strTimetable = mapper.writeValueAsString(timetableDTO);
                System.out.println("---> Mapped object :" + strTimetable + "$");

                tws.sendMessageToBrowser(strTimetable);

                // UPDATE MANAGED BEAN
                TimetableBean timetableBean = jmsConfig.getTimetableBean();
                System.out.println("UPDATE timetableBean = " + timetableBean);
                timetableBean.setArrivals(arrivals);
                timetableBean.setDepartures(departures);


            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (JMSException e) {
            System.out.println("--> Error in onMessage:");
            e.printStackTrace();
        }
    }
}
