package jiesu.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

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
