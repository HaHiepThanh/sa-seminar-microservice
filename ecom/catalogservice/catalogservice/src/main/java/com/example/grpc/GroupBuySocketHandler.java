package com.example.grpc;

import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class GroupBuySocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            session.sendSync(message);
        }
    }
}