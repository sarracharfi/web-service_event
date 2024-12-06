package com.event.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class NotificationHandler implements WebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(NotificationHandler.class);

     private final ConcurrentHashMap<String, CopyOnWriteArrayList<WebSocketSession>> activeSessions = new ConcurrentHashMap<>();
    private static WebSocketSession tsession;



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (session != null && session.getId() != null) {
            logger.info("Connection established for session: {}", session.getId());

            String userId = getUserIdFromSession(session);

            if (userId != null) {
                activeSessions.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(session);
                logger.info("User with ID {} connected.", userId);

                tsession = session;
                logger.info("Global session updated to: {}", tsession.getId());

                session.sendMessage(new TextMessage("Welcome!"));
            } else {
                logger.error("Failed to extract userId from session: {}", session.getId());
            }
        } else {
            logger.error("Failed to establish connection: session or session ID is null.");
        }
    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Received message: {} from session: {}", message.getPayload(), session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Transport error on session: {}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
             activeSessions.computeIfPresent(userId, (k, sessions) -> {
                sessions.remove(session);
                return sessions.isEmpty() ? null : sessions;
            });
            log.info("Connection closed for user: {} with session: {}", userId, session.getId());
        } else {
            log.error("Unable to identify userId for session: {}", session.getId());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private String getUserIdFromSession(WebSocketSession session) {
        String uri = session.getUri().toString();
        String userId = null;

         if (uri != null && uri.contains("userId=")) {
            try {
                String[] parts = uri.split("\\?");
                for (String part : parts[1].split("&")) {
                    String[] keyValue = part.split("=");
                    if ("userId".equals(keyValue[0])) {
                        userId = keyValue[1];
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error("Error extracting userId from session URI: {}", uri, e);
            }
        }

        return userId;
    }
    public WebSocketSession getSessionByUserId(String userId) {
         List<WebSocketSession> sessions = activeSessions.get(userId);

        if (sessions != null && !sessions.isEmpty()) {
             return sessions.get(0);
        } else {
            logger.error("No active sessions found for userId: {}", userId);
            return null;
        }
    }

      public void sendNotification(String message, String userId) {
        try {
            if (tsession != null) {
                System.out.println("Global session ID: " + tsession.getId());
                if (tsession.isOpen()) {
                    tsession.sendMessage(new TextMessage(message));
                    logger.info("Notification sent to global session: {}", tsession.getId());
                } else {
                    logger.warn("Global session is not open. Cannot send notification.");
                }
            } else {
                logger.error("Global session is null. Cannot send notification.");
            }

        } catch (Exception e) {
            logger.error("Error sending notification to user: {}", userId, e);
        }
    }




}
