package jiesu.websocket;

public interface WebSocketService {
    void send(String topic, Object payload);
}
