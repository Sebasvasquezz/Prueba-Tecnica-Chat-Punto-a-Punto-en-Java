package prueba.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocketConfig configures WebSocket message handling with STOMP protocol.
 * It enables a simple in-memory message broker for real-time communication
 * and sets up the necessary endpoints for WebSocket connections.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker. A simple in-memory broker is enabled
     * that listens on the "/topic" destination. The application will use the
     * "/app" prefix for routing messages to message-handling methods.
     * 
     * @param config The MessageBrokerRegistry used to configure the broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers STOMP endpoints for WebSocket communication. These are the points 
     * where clients can connect to the WebSocket server. SockJS is enabled to provide 
     * fallback options for browsers that do not support native WebSockets.
     * 
     * @param registry The StompEndpointRegistry to register WebSocket endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket")
                .setAllowedOriginPatterns("https://localhost:8443", "https://your-domain.com") 
                .withSockJS();
    }
}

