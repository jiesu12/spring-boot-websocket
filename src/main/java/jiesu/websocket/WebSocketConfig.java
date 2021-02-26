package jiesu.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket is handled by SockJS on both server side (Spring boot) and client side (browser).
 * SockJS client side create a special URL pattern to call WebSocket endpoint on server.
 * The URL looks like http://example.com/websocket/123/aaaaa/websocket. This pattern is used to create SockJS sessions.
 * SockJS server side sends heartbeats to client side periodically to decide whether to keep the session alive.
 * <p>
 * When SockJS client side initializing the connection, it will try multiple protocols.
 * First it will try WebSocket. If WebSocket failed, SockJS will try other protocols, such as xhr streaming.
 * What is why you may sometime see browser trying to connect to different URLs ending with xhr_streaming,
 * xdr_streaming, etc.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final String allowedOrigins;
    private final String endPoint;
    private final String topicPrefix;

    public WebSocketConfig(
            @Value("${jiesu.websocket.allowed.origins:*}") String allowedOrigins,
            @Value("${jiesu.websocket.endpoint:/websocket}") String endPoint,
            @Value("${jiesu.websocket.topic.prefix:/topic}") String topicPrefix) {
        this.allowedOrigins = allowedOrigins;
        this.endPoint = endPoint;
        this.topicPrefix = topicPrefix;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(topicPrefix);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(endPoint).setAllowedOrigins(allowedOrigins).withSockJS();
    }

    @Bean
    public WebSocketService webSocketService(MessageSendingOperations<String> operations) {
        return new WebSocketServiceImpl(operations);
    }
}
