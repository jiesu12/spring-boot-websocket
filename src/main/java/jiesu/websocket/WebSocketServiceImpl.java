package jiesu.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.core.MessageSendingOperations;

public class WebSocketServiceImpl implements WebSocketService {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServiceImpl.class);
    private final MessageSendingOperations<String> operations;

    public WebSocketServiceImpl(MessageSendingOperations<String> operations) {
        this.operations = operations;
    }

    @Override
    public void send(String topic, Object payload) {
        try {
            operations.convertAndSend(topic, payload);
        } catch (Exception e) {
            LOG.warn("Failed to send WebSocket message with topic {}, payload {}", topic, payload);
        }
    }
}
