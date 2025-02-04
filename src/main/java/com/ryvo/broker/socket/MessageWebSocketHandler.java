package com.ryvo.broker.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class MessageWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageWebSocketHandler.class);
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        logger.info("🔌 Nova conexão WebSocket: {}", session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("📩 Mensagem recebida no WebSocket: {}", message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        logger.info("🔴 Conexão WebSocket fechada: {}", session.getId());
    }

    public void sendMessageToAll(String message) throws Exception {
        logger.info("🚀 Enviando mensagem para {} consumidores: {}", sessions.size(), message);
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(message));
        }
    }
}
