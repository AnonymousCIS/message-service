package org.anonymous.global.configs;

import org.anonymous.message.websockets.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MessageHandler messageHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        String profile = System.getenv("spring.profiles.active");

        registry.addHandler(messageHandler, "msg")
                .setAllowedOrigins(profile.contains("prod") ? "" : "http://cis-message-service.koreait.xyz");

    }
}
