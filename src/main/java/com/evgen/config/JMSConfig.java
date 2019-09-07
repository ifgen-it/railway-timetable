package com.evgen.config;

import com.evgen.service.StationRESTService;
import com.evgen.servlet.TimetableServlet;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.*;


@Singleton
public class JMSConfig {

    private TopicSubscriber subscriber;
    private TopicSession session;
    private TopicConnection connection;

    private boolean isAlive;

    @Inject
    private StationRESTService stationRESTService;

    public JMSConfig() {
    }

    public void startListen(String stationName) throws JMSException {

        System.out.println("---> JMSConfig started");

        if (isAlive == true){
            clean();
        }

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        connection = connectionFactory.createTopicConnection();

        session = connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("topic1");

        subscriber = session.createSubscriber(topic,
                "city LIKE '" + stationName + "'", false);

        try {
            TopicMessageListener topicMessageListener = new TopicMessageListener(this);

            subscriber.setMessageListener(topicMessageListener);
            connection.start();
            System.out.println("Asynchronous Subscriber created, waiting for message...");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("---> finally : JMSConfig");
            isAlive = true;
        }
    }

    @PreDestroy
    public void clean() throws JMSException {
        System.out.println("---> JMSConfig Cleaning");

        subscriber.close();
        session.close();
        connection.close();
        isAlive = false;
        System.out.println("---> clean : Resources closed");
    }

    public StationRESTService getStationRESTService(){
        return stationRESTService;
    }



}
