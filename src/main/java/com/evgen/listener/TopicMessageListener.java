package com.evgen.listener;

import com.evgen.bean.StationsBean;
import com.evgen.bean.TimetableBean;
import com.evgen.config.JMSConfig;
import com.evgen.dto.RoutePathSimpleDTO;
import com.evgen.dto.StationSimpleDTO;
import com.evgen.dto.TimetableDTO;
import com.evgen.service.StationRESTService;
import com.evgen.websocket.TimetableWebsocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.List;

public class TopicMessageListener implements MessageListener {

    private static final Logger logger = Logger.getLogger(TopicMessageListener.class);

    private JMSConfig jmsConfig;


    public TopicMessageListener(JMSConfig jmsConfig) {
        this.jmsConfig = jmsConfig;
    }

    @Override
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage) message;
        try {

            logger.info("message: text: " + textMessage.getText());
            String city = textMessage.getStringProperty("city");
            logger.info("Properties: city = " + city);

            logger.info("Message: " + message);

            // HANDLERS
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
                logger.info("UPDATE GUI");
                TimetableWebsocket tws = jmsConfig.getTimetableWebsocket();

                ObjectMapper mapper = new ObjectMapper();
                String strTimetable = mapper.writeValueAsString(timetableDTO);
                logger.info("Mapped object :" + strTimetable);

                tws.sendMessageToBrowser(strTimetable);

                // UPDATE MANAGED BEAN
                TimetableBean timetableBean = jmsConfig.getTimetableBean();
                logger.info("UPDATE timetableBean = " + timetableBean);
                timetableBean.setArrivals(arrivals);
                timetableBean.setDepartures(departures);


            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (JMSException e) {
            logger.warn("Error in onMessage:");
            e.printStackTrace();
        }
    }
}
