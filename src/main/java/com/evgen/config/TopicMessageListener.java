package com.evgen.config;

import com.evgen.dto.StationSimpleDTO;
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
                System.out.println("stationRestService: " + jmsConfig.getStationRESTService());
                System.out.println("stationRestService hash: " + jmsConfig.getStationRESTService().hashCode());

                List<StationSimpleDTO> stations = jmsConfig.getStationRESTService().getAllStations();
                System.out.println("Stations by Rest: " + stations);




            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (JMSException e) {
            System.out.println("--> Error in onMessage:");
            e.printStackTrace();
        }
    }
}
