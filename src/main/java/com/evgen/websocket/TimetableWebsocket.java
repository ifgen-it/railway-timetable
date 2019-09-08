package com.evgen.websocket;

import javax.ejb.Singleton;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Singleton
@ServerEndpoint(value = "/websocket")
public class TimetableWebsocket {

    private Session session;


    public TimetableWebsocket() {
    }

    public void sendMessageToBrowser(String message) {
        try {
            session.getBasicRemote().sendText(message);
            System.out.println("--> WS : send message to browser : " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("---> WS : new session opened");
        this.session = session;
    }

    // WAS USED FOR TESTING
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("---> WS : message received : " + message);

        try {
            session.getBasicRemote().sendText("Hello, Browser!");
            for (int i = 1; i < 4; i++) {
                Thread.sleep(5000);
                session.getBasicRemote().sendText(i + " message from server");
            }
            session.getBasicRemote().sendText("Good bye, Browser!");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("---> WS : Error occurred: " + throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("---> WS : Session closed: " + closeReason);
    }


}