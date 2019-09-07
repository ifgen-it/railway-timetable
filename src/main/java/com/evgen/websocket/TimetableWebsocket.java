package com.evgen.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/timetable")
public class TimetableWebsocket {

    @OnOpen
    public void onOpen(Session session){
        System.out.println("---> Websocket - open");
        System.out.println("Session = "  + session.getId());
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("---> Websocket - close");


    }

    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println("---> Websocket - error");


    }

}
