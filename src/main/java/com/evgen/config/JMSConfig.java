package com.evgen.config;

import com.evgen.bean.StationsBean;
import com.evgen.bean.TimetableBean;
import com.evgen.listener.TopicMessageListener;
import com.evgen.service.StationRESTService;
import com.evgen.websocket.TimetableWebsocket;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.*;


@Singleton
public class JMSConfig {

    private static final Logger logger = Logger.getLogger(JMSConfig.class);

    private TopicSubscriber subscriber;
    private TopicSession session;
    private TopicConnection connection;

    private boolean isAlive;

    @Inject
    private StationRESTService stationRESTService;

    @Inject
    private TimetableWebsocket timetableWebsocket;

    @Inject
    private TimetableBean timetableBean;

    public JMSConfig() {
    }

    public void startListen(String stationName) throws JMSException {

        logger.info("JMSConfig started");

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
            logger.info("Asynchronous Subscriber created, waiting for message...");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("JMSConfig - work done");
            isAlive = true;
        }
    }

    @PreDestroy
    public void clean() throws JMSException {
        logger.info("JMSConfig Cleaning");

        subscriber.close();
        session.close();
        connection.close();
        isAlive = false;
        logger.info("Resources closed");
    }

    public StationRESTService getStationRESTService(){
        return stationRESTService;
    }

    public TimetableWebsocket getTimetableWebsocket() {
        return timetableWebsocket;
    }

    public TimetableBean getTimetableBean() {
        return timetableBean;
    }

    public void setTimetableBean(TimetableBean timetableBean) {
        this.timetableBean = timetableBean;
    }
}
