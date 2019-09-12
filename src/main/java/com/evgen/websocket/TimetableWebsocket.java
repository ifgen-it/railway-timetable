package com.evgen.websocket;

import org.apache.log4j.Logger;

import javax.ejb.Singleton;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Singleton
@ServerEndpoint(value = "/websocket")
public class TimetableWebsocket {

    private static final Logger logger = Logger.getLogger(TimetableWebsocket.class);

    private static Set<Session> clients =
            Collections.synchronizedSet(new HashSet<Session>());


    public TimetableWebsocket() {
    }

    public void sendMessageToBrowser(String message) {
        try {
            synchronized (clients){
                for (Session client : clients){
                    client.getBasicRemote().sendText(message);
                }
            }
            logger.info("send message to browser :" + message);

        } catch (IOException e) {
            logger.warn("Exception : " + e);
            e.printStackTrace();
        }
    }


    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        logger.info("new session opened, session = " + session + ", session id = " + session.getId());

        clients.add(session);
        logger.info("Count of clients = " + clients.size());
    }

    // WAS USED FOR TESTING
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("message received : " + message);

        try {
            session.getBasicRemote().sendText("Hello, Browser!");
            for (int i = 1; i < 4; i++) {
                Thread.sleep(5000);
                session.getBasicRemote().sendText(i + " message from server");
            }
            session.getBasicRemote().sendText("Good bye, Browser!");

        } catch (IOException e) {
            logger.warn("Exception : " + e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.warn("Exception : " + e);
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.warn("Error occurred: " + throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.warn("Session : " + session + " - closed: " + closeReason);
        clients.remove(session);
    }


}
