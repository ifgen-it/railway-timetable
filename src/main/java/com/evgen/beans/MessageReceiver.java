package com.evgen.beans;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.jms.*;

//@Startup
@Stateless
public class MessageReceiver {

    private TopicSubscriber subscriber;
    private TopicSession session;
    private TopicConnection connection;

    //private String stationName;


    public MessageReceiver() {
    }

    //@PostConstruct
    public void startListen(String stationName) throws JMSException {
        System.out.println("---> MessageReceiver init Listen method:");

        System.out.println("JMS TOPIC MessageReceiver started");

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        connection = connectionFactory.createTopicConnection();

        session = connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("topic1");

        //String stationName = "Тула";
        subscriber = session.createSubscriber(topic, "city LIKE '" + stationName + "'", false);


//        try {
//            subscriber.setMessageListener(new MessageReceiver());
//            connection.start();
//            System.out.println("Asynchronous Subscriber created, waiting for message...");
//
////            while (true) {
////                Thread.sleep(1000);
////            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            System.out.println("---> finally in Init MessageReceiver bean");
////            subscriber.close();
////            session.close();
////            connection.close();
////            System.out.println("Resources closed");
//        }

    }

    @PreDestroy
    public void destroy() throws JMSException {
        System.out.println("---> MessageReceiver PreDestroy");

        subscriber.close();
        session.close();
        connection.close();
        System.out.println("Resources closed");


    }

//    @Override
//    public void onMessage(Message message) {
//
//        TextMessage textMessage = (TextMessage) message;
//
//        try {
//
//            System.out.println("Text from message: " + textMessage.getText());
//            String city = textMessage.getStringProperty("city");
//            System.out.println("Properties: city = " + city);
//
//            System.out.print("Message: ");
//            System.out.println(message);
//
//            //message.acknowledge();
//
//        } catch (JMSException e) {
//            System.out.println("--> Error in onMessage:");
//            e.printStackTrace();
//        }
//
//    }

//    public String getStationName() {
//        return stationName;
//    }
//
//    public void setStationName(String stationName) {
//        this.stationName = stationName;
//    }
}
